package lk.ijse.service.custom.impl;

import lk.ijse.dao.DAOFactory;
import lk.ijse.dao.custom.ReservationDAO;
import lk.ijse.dto.ConfigDTO;
import lk.ijse.dto.ReservationDTO;
import lk.ijse.dto.RentalDTO;
import lk.ijse.entity.Reservation;
import lk.ijse.service.ServiceFactory;
import lk.ijse.service.custom.ConfigService;
import lk.ijse.service.custom.RentalService;
import lk.ijse.service.custom.ReservationService;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ReservationServiceImpl implements ReservationService {

    private final ReservationDAO reservationDAO =
            (ReservationDAO) DAOFactory.getInstance()
                    .getDAO(DAOFactory.DAOTypes.RESERVATION);

    private final RentalService rentalService =
            (RentalService) ServiceFactory.getInstance()
                    .getService(ServiceFactory.ServiceType.RENTAL);

    private final ConfigService configService =
            (ConfigService) ServiceFactory.getInstance()
                    .getService(ServiceFactory.ServiceType.CONFIG);

    /* ===================== CRUD ===================== */

    @Override
    public boolean saveReservation(ReservationDTO dto) throws Exception {

        // ---- Date validation ----
        if (dto.getReservedFrom() == null || dto.getReservedTo() == null)
            throw new IllegalArgumentException("Reservation dates cannot be null");

        if (dto.getReservedFrom().isAfter(dto.getReservedTo()))
            throw new IllegalArgumentException("Invalid reservation period");

        // ---- Price validation ----
        if (dto.getTotalPrice() == null || dto.getTotalPrice().compareTo(BigDecimal.ZERO) <= 0)
            throw new IllegalArgumentException("Invalid total price");

        // ---- Availability check ----
        if (!isEquipmentAvailable(
                dto.getEquipmentId(),
                dto.getReservedFrom(),
                dto.getReservedTo()
        )) {
            throw new IllegalStateException("Equipment not available");
        }

        Reservation reservation = new Reservation(
                0,
                dto.getCustomerId(),
                dto.getEquipmentId(),
                Date.valueOf(dto.getReservedFrom()),
                Date.valueOf(dto.getReservedTo()),
                dto.getTotalPrice().doubleValue(),
                "Pending",          // BUSINESS RULE
                null
        );

        return reservationDAO.save(reservation);
    }

    @Override
    public boolean updateReservation(ReservationDTO dto) throws Exception {

        Reservation existing = reservationDAO.find(dto.getReservationId());
        if (existing == null) return false;

        // Cancelled reservations cannot be modified
        if ("Cancelled".equals(existing.getStatus())) return false;

        // Status transition validation
        if (!isValidStatusTransition(existing.getStatus(), dto.getStatus()))
            return false;

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
        if (r == null) return null;

        return new ReservationDTO(
                r.getReservationId(),
                r.getCustomerId(),
                r.getEquipmentId(),
                r.getReservedFrom().toLocalDate(),
                r.getReservedTo().toLocalDate(),
                BigDecimal.valueOf(r.getTotalPrice()),
                r.getStatus(),
                r.getCreatedAt()
        );
    }

    @Override
    public List<ReservationDTO> getAllReservations() throws Exception {
        List<ReservationDTO> list = new ArrayList<>();
        for (Reservation r : reservationDAO.findAll()) {
            list.add(new ReservationDTO(
                    r.getReservationId(),
                    r.getCustomerId(),
                    r.getEquipmentId(),
                    r.getReservedFrom().toLocalDate(),
                    r.getReservedTo().toLocalDate(),
                    BigDecimal.valueOf(r.getTotalPrice()),
                    r.getStatus(),
                    r.getCreatedAt()
            ));
        }
        return list;
    }

    /* ================= BUSINESS LOGIC ================= */

    @Override
    public boolean confirmReservation(long reservationId) throws Exception {

        ReservationDTO reservation = searchReservation(reservationId);
        if (reservation == null) return false;

        if (!"Pending".equals(reservation.getStatus())) return false;

        if (!isEquipmentAvailable(
                reservation.getEquipmentId(),
                reservation.getReservedFrom(),
                reservation.getReservedTo()
        )) return false;

        ConfigDTO config = configService.getConfig();
        if (reservation.getTotalPrice().compareTo(config.getMaxDeposit()) > 0)
            return false;

        reservation.setStatus("Confirmed");
        return updateReservation(reservation);
    }

    @Override
    public boolean cancelReservation(long reservationId) throws Exception {

        ReservationDTO reservation = searchReservation(reservationId);
        if (reservation == null) return false;

        if ("Cancelled".equals(reservation.getStatus())) return false;

        reservation.setStatus("Cancelled");
        return updateReservation(reservation);
    }

    @Override
    public boolean createRentalFromReservation(long reservationId) throws Exception {

        ReservationDTO reservation = searchReservation(reservationId);
        if (reservation == null) return false;

        if (!"Confirmed".equals(reservation.getStatus())) return false;

        RentalDTO rental = new RentalDTO(
                0,
                reservation.getCustomerId(),
                reservation.getEquipmentId(),
                reservation.getReservedFrom(),
                reservation.getReservedTo(),
                reservation.getTotalPrice(),
                reservation.getTotalPrice(),
                reservationId,
                "Open",
                null
        );

        boolean rentalSaved = rentalService.saveRental(rental);
        if (!rentalSaved) return false;

        reservation.setStatus("Completed");
        return updateReservation(reservation);
    }

    /* ================= HELPERS ================= */

    private boolean isEquipmentAvailable(
            long equipmentId,
            LocalDate from,
            LocalDate to
    ) throws Exception {

        // Check rentals
        for (RentalDTO r : rentalService.getAllRentals()) {
            if (r.getEquipmentId() == equipmentId &&
                    "Open".equals(r.getStatus()) &&
                    !from.isAfter(r.getRentedTo()) &&
                    !r.getRentedFrom().isAfter(to)) {
                return false;
            }
        }

        // Check reservations
        for (ReservationDTO r : getAllReservations()) {
            if (r.getEquipmentId() == equipmentId &&
                    ("Pending".equals(r.getStatus()) || "Confirmed".equals(r.getStatus())) &&
                    !from.isAfter(r.getReservedTo()) &&
                    !r.getReservedFrom().isAfter(to)) {
                return false;
            }
        }

        return true;
    }

    private boolean isValidStatusTransition(String oldStatus, String newStatus) {

        if (oldStatus == null || newStatus == null) {
            return false;
        }

        switch (oldStatus) {
            case "Pending":
                return "Confirmed".equals(newStatus) || "Cancelled".equals(newStatus);

            case "Confirmed":
                return "Completed".equals(newStatus) || "Cancelled".equals(newStatus);

            default:
                return false;
        }
    }

}
