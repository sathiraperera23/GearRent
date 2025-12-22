package lk.ijse.service.custom.impl;

import lk.ijse.dao.DAOFactory;
import lk.ijse.dao.custom.RentalDAO;
import lk.ijse.dto.RentalDTO;
import lk.ijse.dto.ReservationDTO;
import lk.ijse.entity.Rental;
import lk.ijse.service.ServiceFactory;
import lk.ijse.service.custom.RentalService;
import lk.ijse.service.custom.ReservationService;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class RentalServiceImpl implements RentalService {

    private final RentalDAO rentalDAO =
            (RentalDAO) DAOFactory.getInstance().getDAO(DAOFactory.DAOTypes.RENTAL);

    private final ReservationService reservationService =
            (ReservationService) ServiceFactory.getInstance()
                    .getService(ServiceFactory.ServiceType.RESERVATION);

    /* ===================== CRUD ===================== */

    @Override
    public boolean saveRental(RentalDTO dto) throws Exception {

        validateRental(dto);

        // Check equipment availability
        if (!rentalDAO.isEquipmentAvailable(
                dto.getEquipmentId(),
                Date.valueOf(dto.getRentedFrom()),
                Date.valueOf(dto.getRentedTo())
        )) {
            throw new IllegalStateException("Equipment not available");
        }

        Rental rental = new Rental(
                0,
                dto.getCustomerId(),
                dto.getEquipmentId(),
                dto.getRentedFrom(),
                dto.getRentedTo(),
                dto.getDailyPrice(),
                dto.getSecurityDeposit(),
                dto.getReservationId(),
                "Open",
                null
        );

        return rentalDAO.save(rental);
    }

    @Override
    public boolean updateRental(RentalDTO dto) throws Exception {

        Rental existing = rentalDAO.find(dto.getRentalId());
        if (existing == null) return false;

        // Closed rentals cannot be changed
        if ("Closed".equals(existing.getStatus())) return false;

        validateRental(dto);

        // Re-check availability ONLY if dates changed
        if (!existing.getRentedFrom().equals(dto.getRentedFrom()) ||
                !existing.getRentedTo().equals(dto.getRentedTo())) {

            if (!rentalDAO.isEquipmentAvailable(
                    dto.getEquipmentId(),
                    Date.valueOf(dto.getRentedFrom()),
                    Date.valueOf(dto.getRentedTo())
            )) {
                throw new IllegalStateException("Equipment not available for new dates");
            }
        }

        if (!isValidStatusChange(existing.getStatus(), dto.getStatus()))
            return false;

        existing.setRentedFrom(dto.getRentedFrom());
        existing.setRentedTo(dto.getRentedTo());
        existing.setDailyPrice(dto.getDailyPrice());
        existing.setSecurityDeposit(dto.getSecurityDeposit());
        existing.setStatus(dto.getStatus());

        return rentalDAO.update(existing);
    }


    @Override
    public boolean deleteRental(long id) throws Exception {
        return rentalDAO.delete(id);
    }

    @Override
    public RentalDTO searchRental(long id) throws Exception {
        Rental r = rentalDAO.find(id);
        if (r == null) return null;
        return mapToDTO(r);
    }

    @Override
    public List<RentalDTO> getAllRentals() throws Exception {
        List<RentalDTO> list = new ArrayList<>();
        for (Rental r : rentalDAO.findAll()) {
            list.add(mapToDTO(r));
        }
        return list;
    }

    /* ===================== RESERVATION → RENTAL ===================== */

    @Override
    public boolean createRentalFromReservation(long reservationId) throws Exception {
        ReservationDTO reservation = reservationService.searchReservation(reservationId);
        if (reservation == null || !"Confirmed".equals(reservation.getStatus())) return false;

        RentalDTO rental = new RentalDTO(
                0,
                reservation.getCustomerId(),
                reservation.getEquipmentId(),
                reservation.getReservedFrom(),
                reservation.getReservedTo(),
                reservation.getTotalPrice(), // total price already calculated in reservation
                reservation.getTotalPrice(),
                reservation.getReservationId(),
                "Open",
                null
        );

        boolean saved = saveRental(rental);
        if (!saved) return false;

        reservation.setStatus("Completed");
        reservationService.updateReservation(reservation);

        return true;
    }

    /* ===================== HELPERS ===================== */

    private RentalDTO mapToDTO(Rental r) {
        return new RentalDTO(
                r.getRentalId(),
                r.getCustomerId(),
                r.getEquipmentId(),
                r.getRentedFrom(),
                r.getRentedTo(),
                r.getDailyPrice(),
                r.getSecurityDeposit(),
                r.getReservationId(),
                r.getStatus(),
                r.getCreatedAt()
        );
    }

    private void validateRental(RentalDTO dto) {
        if (dto.getRentedFrom() == null || dto.getRentedTo() == null)
            throw new IllegalArgumentException("Rental dates cannot be null");

        if (dto.getRentedFrom().isAfter(dto.getRentedTo()))
            throw new IllegalArgumentException("Invalid rental period");

        if (dto.getDailyPrice() == null || dto.getDailyPrice().compareTo(BigDecimal.ZERO) <= 0)
            throw new IllegalArgumentException("Invalid daily price");

        if (dto.getSecurityDeposit() == null || dto.getSecurityDeposit().compareTo(BigDecimal.ZERO) < 0)
            throw new IllegalArgumentException("Invalid security deposit");
    }

    private BigDecimal calculateTotalPrice(RentalDTO dto) {
        long days = ChronoUnit.DAYS.between(dto.getRentedFrom(), dto.getRentedTo()) + 1;
        return dto.getDailyPrice().multiply(BigDecimal.valueOf(days)).add(dto.getSecurityDeposit());
    }

    private boolean isValidStatusChange(String oldStatus, String newStatus) {
        if (oldStatus.equals(newStatus)) return true;
        return "Open".equals(oldStatus) && "Closed".equals(newStatus);
    }
}
