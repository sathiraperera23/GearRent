package lk.ijse.service.custom;

import lk.ijse.dto.ReservationDTO;
import lk.ijse.service.SuperService;

import java.util.List;

public interface ReservationService extends SuperService {
    boolean saveReservation(ReservationDTO dto) throws Exception;
    boolean updateReservation(ReservationDTO dto) throws Exception;
    boolean deleteReservation(long id) throws Exception;
    ReservationDTO searchReservation(long id) throws Exception;
    List<ReservationDTO> getAllReservations() throws Exception;
}
