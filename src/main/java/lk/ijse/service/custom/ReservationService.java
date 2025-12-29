package lk.ijse.service.custom;

import lk.ijse.dto.ReservationDTO;
import lk.ijse.service.SuperService;

import java.time.LocalDate;
import java.util.List;

public interface ReservationService extends SuperService {

    /* ===================== CRUD ===================== */

    boolean saveReservation(ReservationDTO dto) throws Exception;

    boolean updateReservation(ReservationDTO dto) throws Exception;

    boolean deleteReservation(long reservationId) throws Exception;

    ReservationDTO searchReservation(long reservationId) throws Exception;

    List<ReservationDTO> getAllReservations() throws Exception;

    /* ================= BUSINESS OPERATIONS ================= */

    boolean confirmReservation(long reservationId) throws Exception;

    boolean cancelReservation(long reservationId) throws Exception;

    boolean createRentalFromReservation(long reservationId) throws Exception;

    /* ================= REPORTING ================= */

    List<ReservationDTO> getReservationsByFilter(
            String filter,
            LocalDate startDate,
            LocalDate endDate
    ) throws Exception;
}
