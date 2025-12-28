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

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ReservationServiceImpl implements ReservationService {

    private final ReservationDAO reservationDAO =
            (ReservationDAO) DAOFactory.getInstance()
                    .getDAO(DAOFactory.DAOTypes.RESERVATION);

    // 🔥 injected later (NO ServiceFactory here)
    private RentalService rentalService;
    private ConfigService configService;

    /* ===================== DEPENDENCY INJECTION ===================== */

    public void setRentalService(RentalService rentalService) {
        this.rentalService = rentalService;
    }

    public void setConfigService(ConfigService configService) {
        this.configService = configService;
    }

    /* ===================== CRUD ===================== */

    @Override
    public boolean saveReservation(ReservationDTO dto) throws Exception {

        validateDates(dto.getReservedFrom(), dto.getReservedTo());
        validatePrice(dto.getTotalPrice());

        if (!reservationDAO.isEquipmentAvailable(
                dto.getEquipmentId(),
                Date.valueOf(dto.getReservedFrom()),
                Date.valueOf(dto.getReservedTo()),
                null
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
                "Pending",
                null
        );

        return reservationDAO.save(reservation);
    }

    @Override
    public boolean updateReservation(ReservationDTO dto) throws Exception {

        Reservation existing = reservationDAO.find(dto.getReservationId());
        if (existing == null) return false;

        if ("Cancelled".equals(existing.getStatus())) return false;

        if (!isValidStatusTransition(existing.getStatus(), dto.getStatus()))
            return false;

        validateDates(dto.getReservedFrom(), dto.getReservedTo());

        boolean available = reservationDAO.isEquipmentAvailable(
                existing.getEquipmentId(),
                Date.valueOf(dto.getReservedFrom()),
                Date.valueOf(dto.getReservedTo()),
                existing.getReservationId()
        );

        if (!available)
            throw new IllegalStateException("Equipment not available");

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
        if (r == null) return null;

        return mapToDTO(r);
    }

    @Override
    public List<ReservationDTO> getAllReservations() throws Exception {

        List<ReservationDTO> list = new ArrayList<>();
        for (Reservation r : reservationDAO.findAll()) {
            list.add(mapToDTO(r));
        }
        return list;
    }

    /* ================= BUSINESS LOGIC ================= */

    @Override
    public boolean confirmReservation(long reservationId) throws Exception {

        ReservationDTO reservation = searchReservation(reservationId);
        if (reservation == null) return false;

        if (!"Pending".equals(reservation.getStatus())) return false;

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

        reservation.setStatus("Cancelled");
        return updateReservation(reservation);
    }

//    @Override
//    public boolean createRentalFromReservation(long reservationId) throws Exception {
//
//        ReservationDTO reservation = searchReservation(reservationId);
//        if (reservation == null) return false;
//
//        if (!"Confirmed".equals(reservation.getStatus())) return false;
//
//        RentalDTO rental = new RentalDTO(
//                0,
//                reservation.getCustomerId(),
//                reservation.getEquipmentId(),
//                reservation.getReservedFrom(),
//                reservation.getReservedTo(),
//                reservation.getTotalPrice(),
//                reservation.getTotalPrice(),
//                reservationId,
//                "Open",
//                null
//        );
//
//        if (!rentalService.saveRental(rental)) return false;
//
//        reservation.setStatus("Completed");
//        return updateReservation(reservation);
//    }

    /* ===================== HELPERS ===================== */

    private ReservationDTO mapToDTO(Reservation r) {
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

    private void validateDates(LocalDate from, LocalDate to) {
        if (from == null || to == null)
            throw new IllegalArgumentException("Dates cannot be null");

        if (from.isAfter(to))
            throw new IllegalArgumentException("Invalid date range");
    }

    private void validatePrice(BigDecimal price) {
        if (price == null || price.compareTo(BigDecimal.ZERO) <= 0)
            throw new IllegalArgumentException("Invalid price");
    }

    private boolean isValidStatusTransition(String oldStatus, String newStatus) {

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
