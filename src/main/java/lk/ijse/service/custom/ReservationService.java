package lk.ijse.service.custom;

import lk.ijse.dto.ReservationDTO;
import lk.ijse.service.SuperService;

import java.util.List;

public interface ReservationService extends SuperService {

    // --- CRUD Operations ---
    boolean saveReservation(ReservationDTO dto) throws Exception;
    boolean updateReservation(ReservationDTO dto) throws Exception;
    boolean deleteReservation(long id) throws Exception;
    ReservationDTO searchReservation(long id) throws Exception;
    List<ReservationDTO> getAllReservations() throws Exception;

    // --- Business Logic Operations ---
    boolean confirmReservation(long reservationId) throws Exception;
    boolean cancelReservation(long reservationId) throws Exception;
//    boolean createRentalFromReservation(long reservationId) throws Exception;
}
