package lk.ijse.service.custom.impl;

import lk.ijse.dao.DAOFactory;
import lk.ijse.dao.custom.ReservationDAO;
import lk.ijse.dto.ConfigDTO;
import lk.ijse.dto.ReservationDTO;
import lk.ijse.dto.RentalDTO;
import lk.ijse.entity.Reservation;
import lk.ijse.service.custom.ConfigService;
import lk.ijse.service.custom.RentalService;
import lk.ijse.service.custom.ReservationService;
import lk.ijse.db.DBConnection;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.Date;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class ReservationServiceImpl implements ReservationService {

    private final ReservationDAO reservationDAO = (ReservationDAO) DAOFactory.getInstance()
            .getDAO(DAOFactory.DAOTypes.RESERVATION);

    private RentalService rentalService;
    private ConfigService configService;

    public void setRentalService(RentalService rentalService) {
        this.rentalService = rentalService;
    }

    public void setConfigService(ConfigService configService) {
        this.configService = configService;
    }

    @Override
    public boolean saveReservation(ReservationDTO dto) throws Exception {
        validateDates(dto.getReservedFrom(), dto.getReservedTo());
        validatePrice(dto.getTotalPrice());

        boolean available = reservationDAO.isEquipmentAvailable(
                dto.getEquipmentId(),
                Date.valueOf(dto.getReservedFrom()),
                Date.valueOf(dto.getReservedTo()),
                null
        );

        if (!available) {
            throw new IllegalStateException("Equipment is not available for the selected dates");
        }

        Reservation reservation = new Reservation();
        reservation.setCustomerId(dto.getCustomerId());
        reservation.setEquipmentId(dto.getEquipmentId());
        reservation.setReservedFrom(Date.valueOf(dto.getReservedFrom()));
        reservation.setReservedTo(Date.valueOf(dto.getReservedTo()));
        reservation.setTotalPrice(dto.getTotalPrice().doubleValue());
        reservation.setStatus("Pending");
        // createdAt is auto-filled by DB → do not set

        return reservationDAO.save(reservation);
    }

    @Override
    public boolean updateReservation(ReservationDTO dto) throws Exception {
        Reservation existing = reservationDAO.find(dto.getReservationId());
        if (existing == null) return false;
        if ("Cancelled".equals(existing.getStatus())) return false;

        if (!isValidStatusTransition(existing.getStatus(), dto.getStatus())) return false;

        validateDates(dto.getReservedFrom(), dto.getReservedTo());

        boolean available = reservationDAO.isEquipmentAvailable(
                existing.getEquipmentId(),
                Date.valueOf(dto.getReservedFrom()),
                Date.valueOf(dto.getReservedTo()),
                existing.getReservationId()
        );

        if (!available) {
            throw new IllegalStateException("Equipment is not available for the new dates");
        }

        existing.setReservedFrom(Date.valueOf(dto.getReservedFrom()));
        existing.setReservedTo(Date.valueOf(dto.getReservedTo()));
        existing.setTotalPrice(dto.getTotalPrice().doubleValue());
        existing.setStatus(dto.getStatus());

        return reservationDAO.update(existing);
    }

    @Override
    public boolean deleteReservation(long id) throws Exception {
        return reservationDAO.delete(id);
    }

    @Override
    public ReservationDTO searchReservation(long id) throws Exception {
        Reservation r = reservationDAO.find(id);
        return r != null ? mapToDTO(r) : null;
    }

    @Override
    public List<ReservationDTO> getAllReservations() throws Exception {
        List<ReservationDTO> list = new ArrayList<>();
        for (Reservation r : reservationDAO.findAll()) {
            list.add(mapToDTO(r));
        }
        return list;
    }

    @Override
    public boolean confirmReservation(long reservationId) throws Exception {
        ReservationDTO reservation = searchReservation(reservationId);
        if (reservation == null || !"Pending".equals(reservation.getStatus())) {
            return false;
        }

        ConfigDTO config = configService.getConfig();
        if (config != null && reservation.getTotalPrice().compareTo(config.getMaxDeposit()) > 0) {
            return false;
        }

        reservation.setStatus("Confirmed");
        return updateReservation(reservation);
    }

    @Override
    public boolean cancelReservation(long reservationId) throws Exception {
        ReservationDTO reservation = searchReservation(reservationId);
        if (reservation == null) return false;

        reservation.setStatus("Cancelled");
        return updateReservation(reservation);
    }

    /**
     * Converts a Confirmed reservation into an Open rental (transactional)
     */
    @Override
    public boolean createRentalFromReservation(long reservationId) throws Exception {
        try (Connection conn = DBConnection.getInstance().getConnection()) {
            conn.setAutoCommit(false);

            ReservationDTO reservation = searchReservation(reservationId);
            if (reservation == null) {
                throw new IllegalStateException("Reservation not found");
            }
            if (!"Confirmed".equals(reservation.getStatus())) {
                throw new IllegalStateException("Only Confirmed reservations can be converted");
            }

            // Re-check availability
            boolean available = reservationDAO.isEquipmentAvailable(
                    reservation.getEquipmentId(),
                    Date.valueOf(reservation.getReservedFrom()),
                    Date.valueOf(reservation.getReservedTo()),
                    reservation.getReservationId()
            );
            if (!available) {
                throw new IllegalStateException("Equipment is no longer available");
            }

            // Calculate daily price
            long days = ChronoUnit.DAYS.between(reservation.getReservedFrom(), reservation.getReservedTo()) + 1;
            BigDecimal dailyPrice = reservation.getTotalPrice()
                    .divide(BigDecimal.valueOf(days), 2, RoundingMode.HALF_UP);

            // Create rental from reservation
            RentalDTO rental = new RentalDTO();
            rental.setCustomerId(reservation.getCustomerId());
            rental.setEquipmentId(reservation.getEquipmentId());
            rental.setRentedFrom(reservation.getReservedFrom());
            rental.setRentedTo(reservation.getReservedTo());
            rental.setDailyPrice(dailyPrice);
            rental.setSecurityDeposit(reservation.getTotalPrice());
            rental.setReservationId(reservationId);
            rental.setStatus("Open");
            rental.setPaymentStatus("Unpaid");

            boolean rentalSaved = rentalService.saveRental(rental);
            if (!rentalSaved) {
                throw new RuntimeException("Failed to save rental");
            }

            // Mark reservation as Completed
            reservation.setStatus("Completed");
            boolean reservationUpdated = updateReservation(reservation);
            if (!reservationUpdated) {
                throw new RuntimeException("Failed to update reservation status");
            }

            conn.commit();
            return true;

        } catch (Exception e) {
            throw e; // rollback handled automatically by try-with-resources
        }
    }

    private ReservationDTO mapToDTO(Reservation r) {
        return new ReservationDTO(
                r.getReservationId(),
                r.getCustomerId(),
                r.getEquipmentId(),
                r.getReservedFrom().toLocalDate(),
                r.getReservedTo().toLocalDate(),
                BigDecimal.valueOf(r.getTotalPrice()),
                r.getStatus(),
                r.getCreatedAt()  // now included
        );
    }

    private void validateDates(LocalDate from, LocalDate to) {
        if (from == null || to == null) {
            throw new IllegalArgumentException("Dates cannot be null");
        }
        if (from.isAfter(to)) {
            throw new IllegalArgumentException("Reserved-to date cannot be before reserved-from date");
        }
    }

    private void validatePrice(BigDecimal price) {
        if (price == null || price.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Total price must be greater than zero");
        }
    }

    private boolean isValidStatusTransition(String oldStatus, String newStatus) {
        return switch (oldStatus) {
            case "Pending" -> "Confirmed".equals(newStatus) || "Cancelled".equals(newStatus);
            case "Confirmed" -> "Completed".equals(newStatus) || "Cancelled".equals(newStatus);
            default -> false;
        };
    }

    @Override
    public List<ReservationDTO> getReservationsByFilter(String filter, LocalDate startDate, LocalDate endDate) throws Exception {
        Date sqlStart = startDate != null ? Date.valueOf(startDate) : null;
        Date sqlEnd = endDate != null ? Date.valueOf(endDate) : null;

        List<Reservation> reservations = reservationDAO.findByFilter(filter, sqlStart, sqlEnd);
        List<ReservationDTO> dtoList = new ArrayList<>();
        for (Reservation r : reservations) {
            dtoList.add(mapToDTO(r));
        }
        return dtoList;
    }
}