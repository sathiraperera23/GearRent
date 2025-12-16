package lk.ijse.service.custom.impl;

import lk.ijse.dao.DAOFactory;
import lk.ijse.dao.custom.RentalDAO;
import lk.ijse.dto.RentalDTO;
import lk.ijse.dto.ReservationDTO;
import lk.ijse.entity.Rental;
import lk.ijse.service.ServiceFactory;
import lk.ijse.service.custom.RentalService;
import lk.ijse.service.custom.ReservationService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.math.BigDecimal;

public class RentalServiceImpl implements RentalService {

    private final RentalDAO rentalDAO =
            (RentalDAO) DAOFactory.getInstance().getDAO(DAOFactory.DAOTypes.RENTAL);

    private final ReservationService reservationService =
            (ReservationService) ServiceFactory.getInstance()
                    .getService(ServiceFactory.ServiceType.RESERVATION);

    // ===================== CRUD + BUSINESS LOGIC =====================

    @Override
    public boolean saveRental(RentalDTO dto) throws Exception {

        // 1. Validate dates
        if (dto.getRentedFrom() == null || dto.getRentedTo() == null) {
            throw new IllegalArgumentException("Rental dates cannot be null");
        }

        if (dto.getRentedFrom().isAfter(dto.getRentedTo())) {
            throw new IllegalArgumentException("Invalid rental date range");
        }

        // 2. Validate prices
        if (dto.getDailyPrice() == null || dto.getDailyPrice().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Invalid daily price");
        }

        if (dto.getSecurityDeposit() == null || dto.getSecurityDeposit().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Invalid security deposit");
        }

        // 3. Check equipment availability
        if (!isEquipmentAvailable(
                dto.getEquipmentId(),
                dto.getRentedFrom(),
                dto.getRentedTo())) {
            return false;
        }

        // 4. Save rental
        Rental rental = new Rental(
                0, // auto-generated
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

        // Prevent editing closed rentals
        if ("Closed".equals(existing.getStatus())) return false;

        // Validate date changes
        if (dto.getRentedFrom().isAfter(dto.getRentedTo())) return false;

        // Check availability only if dates changed
        if (!existing.getRentedTo().equals(dto.getRentedTo())) {
            if (!isEquipmentAvailable(
                    existing.getEquipmentId(),
                    existing.getRentedFrom(),
                    dto.getRentedTo())) {
                return false;
            }
        }

        // Validate status transition
        if (!isValidStatusChange(existing.getStatus(), dto.getStatus())) {
            return false;
        }

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

    @Override
    public List<RentalDTO> getAllRentals() throws Exception {
        List<RentalDTO> list = new ArrayList<>();
        for (Rental r : rentalDAO.findAll()) {
            list.add(new RentalDTO(
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
            ));
        }
        return list;
    }

    // ===================== BUSINESS RULES =====================

    private boolean isEquipmentAvailable(long equipmentId, LocalDate from, LocalDate to) throws Exception {

        // Check active rentals
        for (RentalDTO r : getAllRentals()) {
            if (r.getEquipmentId() == equipmentId &&
                    "Open".equals(r.getStatus()) &&
                    !from.isAfter(r.getRentedTo()) &&
                    !r.getRentedFrom().isAfter(to)) {
                return false;
            }
        }

        // Check active reservations
        for (ReservationDTO r : reservationService.getAllReservations()) {
            if (r.getEquipmentId() == equipmentId &&
                    ("Pending".equals(r.getStatus()) || "Confirmed".equals(r.getStatus())) &&
                    !from.isAfter(r.getReservedTo()) &&
                    !r.getReservedFrom().isAfter(to)) {
                return false;
            }
        }

        return true;
    }

    private boolean isValidStatusChange(String oldStatus, String newStatus) {
        if (oldStatus.equals(newStatus)) return true;
        return "Open".equals(oldStatus) && "Closed".equals(newStatus);
    }

    // ===================== RESERVATION → RENTAL =====================

    public boolean createRentalFromReservation(long reservationId) throws Exception {

        ReservationDTO reservation = reservationService.searchReservation(reservationId);
        if (reservation == null || !"Confirmed".equals(reservation.getStatus())) return false;

        RentalDTO rental = new RentalDTO(
                0,
                reservation.getCustomerId(),
                reservation.getEquipmentId(),
                reservation.getReservedFrom(),
                reservation.getReservedTo(),
                reservation.getTotalPrice(),
                reservation.getTotalPrice(),
                reservation.getReservationId(),
                "Open",
                null
        );

        boolean saved = saveRental(rental);
        if (!saved) return false;

        // Mark reservation as completed
        reservation.setStatus("Completed");
        reservationService.updateReservation(reservation);

        return true;
    }
}
