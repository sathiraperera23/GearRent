package lk.ijse.service.custom.Impl;

import lk.ijse.dao.DAOFactory;
import lk.ijse.dao.custom.RentalDAO;
import lk.ijse.dto.RentalDTO;
import lk.ijse.entity.Rental;
import lk.ijse.service.custom.RentalService;
import lk.ijse.service.custom.ReservationService;
import lk.ijse.dto.ReservationDTO;
import lk.ijse.service.ServiceFactory;

import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;
import java.math.BigDecimal;

public class RentalServiceImpl implements RentalService {

    private final RentalDAO rentalDAO =
            (RentalDAO) DAOFactory.getInstance().getDAO(DAOFactory.DAOTypes.RENTAL);

    private final ReservationService reservationService =
            (ReservationService) ServiceFactory.getInstance().getService(ServiceFactory.ServiceType.RESERVATION);

    // CRUD + BUSINESS LOGIC

    @Override
    public boolean saveRental(RentalDTO dto) throws Exception {
        // Check if equipment is available for this period
        if (!isEquipmentAvailable(dto.getEquipmentId(), dto.getRentedFrom(), dto.getRentedTo())) {
            System.out.println("Equipment is not available in the selected period.");
            return false;
        }
        return rentalDAO.save(new Rental(
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
        ));
    }

    @Override
    public boolean updateRental(RentalDTO dto) throws Exception {
        Rental existing = rentalDAO.find(dto.getRentalId());
        if (existing == null) return false;

        // Optional: check availability if dates changed
        if (!dto.getRentedTo().equals(existing.getRentedTo()) &&
                !isEquipmentAvailable(existing.getEquipmentId(), existing.getRentedFrom(), dto.getRentedTo())) {
            System.out.println("Cannot extend rental. Equipment not available.");
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

    // ========== BUSINESS LOGIC METHODS ==========

    // Check if equipment is available for a given period
    private boolean isEquipmentAvailable(long equipmentId, LocalDate from, LocalDate to) throws Exception {
        List<RentalDTO> rentals = getAllRentals();
        for (RentalDTO r : rentals) {
            if (r.getEquipmentId() == equipmentId && "Open".equals(r.getStatus())
                    && !(to.isBefore(r.getRentedFrom()) || from.isAfter(r.getRentedTo()))) {
                return false; // Overlap detected
            }
        }
        return true;
    }

    // Create Rental from a confirmed Reservation
    public boolean createRentalFromReservation(long reservationId) throws Exception {
        ReservationDTO reservation = reservationService.searchReservation(reservationId);
        if (reservation == null || !"Confirmed".equals(reservation.getStatus())) return false;

        // Build rental DTO
        RentalDTO rental = new RentalDTO();
        rental.setCustomerId(reservation.getCustomerId());
        rental.setEquipmentId(reservation.getEquipmentId());
        rental.setRentedFrom(reservation.getReservedFrom());
        rental.setRentedTo(reservation.getReservedTo());
        rental.setDailyPrice(reservation.getTotalPrice()); // can split by days if needed
        rental.setSecurityDeposit(reservation.getTotalPrice());
        rental.setReservationId(reservation.getReservationId());
        rental.setStatus("Open");

        return saveRental(rental);
    }
}
