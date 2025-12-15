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
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;

public class ReservationServiceImpl implements ReservationService {

    private final ReservationDAO reservationDAO =
            (ReservationDAO) DAOFactory.getInstance().getDAO(DAOFactory.DAOTypes.RESERVATION);

    private final RentalService rentalService =
            (RentalService) ServiceFactory.getInstance().getService(ServiceFactory.ServiceType.RENTAL);

    private final ConfigService configService =
            (ConfigService) ServiceFactory.getInstance().getService(ServiceFactory.ServiceType.CONFIG);

    // --- CRUD Operations ---
    @Override
    public boolean saveReservation(ReservationDTO dto) throws Exception {
        return reservationDAO.save(new Reservation(
                0,
                dto.getCustomerId(),
                dto.getEquipmentId(),
                Date.valueOf(dto.getReservedFrom()),
                Date.valueOf(dto.getReservedTo()),
                dto.getTotalPrice().doubleValue(),
                dto.getStatus(),
                null
        ));
    }

    @Override
    public boolean updateReservation(ReservationDTO dto) throws Exception {
        return reservationDAO.update(new Reservation(
                dto.getReservationId(),
                0,
                0,
                null,
                Date.valueOf(dto.getReservedTo()),
                dto.getTotalPrice().doubleValue(),
                dto.getStatus(),
                null
        ));
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

    // --- Business Logic ---
    public boolean confirmReservation(long reservationId) throws Exception {
        ReservationDTO reservation = searchReservation(reservationId);
        if (reservation == null || "Cancelled".equals(reservation.getStatus())) return false;

        if (!isEquipmentAvailable(reservation.getEquipmentId(), reservation.getReservedFrom(), reservation.getReservedTo()))
            return false;

        ConfigDTO cfg = configService.getConfig();
        if (reservation.getTotalPrice().compareTo(cfg.getMaxDeposit()) > 0) return false;

        reservation.setStatus("Confirmed");
        return updateReservation(reservation);
    }

    public boolean cancelReservation(long reservationId) throws Exception {
        ReservationDTO reservation = searchReservation(reservationId);
        if (reservation == null) return false;

        reservation.setStatus("Cancelled");
        return updateReservation(reservation);
    }

    public boolean createRentalFromReservation(long reservationId) throws Exception {
        ReservationDTO reservation = searchReservation(reservationId);
        if (reservation == null || !"Confirmed".equals(reservation.getStatus())) return false;

        BigDecimal rentalPrice = reservation.getTotalPrice(); // or calculate logic

        RentalDTO rental = new RentalDTO(
                0,
                reservation.getCustomerId(),
                reservation.getEquipmentId(),
                reservation.getReservedFrom(),
                reservation.getReservedTo(),
                rentalPrice,
                reservation.getTotalPrice(),
                reservationId,
                "Open",
                null
        );

        return rentalService.saveRental(rental);
    }

    // --- Helper Methods ---
    private boolean isEquipmentAvailable(long equipmentId, LocalDate from, LocalDate to) throws Exception {
        List<RentalDTO> rentals = rentalService.getAllRentals();
        for (RentalDTO r : rentals) {
            LocalDate rentedFrom = r.getRentedFrom();
            LocalDate rentedTo = r.getRentedTo();
            if (r.getEquipmentId() == equipmentId && "Open".equals(r.getStatus())
                    && !from.isAfter(rentedTo) && !rentedFrom.isAfter(to)) {
                return false;
            }
        }
        return true;
    }


}
