package lk.ijse.service.custom;

import lk.ijse.dto.ReservationDTO;
import lk.ijse.service.SuperService;

import java.util.List;

public interface ReservationService extends SuperService {

    /* ===================== CRUD ===================== */

    boolean saveReservation(ReservationDTO dto) throws Exception;

    boolean updateReservation(ReservationDTO dto) throws Exception;

    boolean deleteReservation(long reservationId) throws Exception;

    ReservationDTO searchReservation(long reservationId) throws Exception;

    List<ReservationDTO> getAllReservations() throws Exception;

    /* ================= BUSINESS OPERATIONS ================= */

    /**
     * Confirm a pending reservation
     * (validates deposit limit, availability, etc.)
     */
    boolean confirmReservation(long reservationId) throws Exception;

    /**
     * Cancel a reservation
     */
    boolean cancelReservation(long reservationId) throws Exception;

    /**
     * Convert a confirmed reservation into an active rental.
     * - Rechecks equipment availability
     * - Creates rental
     * - Marks reservation as completed
     */
    boolean createRentalFromReservation(long reservationId) throws Exception;
}
