package lk.ijse.service.custom;

import lk.ijse.dto.RentalDTO;
import lk.ijse.service.SuperService;

import java.util.List;

public interface RentalService extends SuperService {

    boolean saveRental(RentalDTO dto) throws Exception;
    boolean updateRental(RentalDTO dto) throws Exception;
    boolean deleteRental(long id) throws Exception;
    RentalDTO searchRental(long id) throws Exception;
    List<RentalDTO> getAllRentals() throws Exception;

    // New business logic
    boolean createRentalFromReservation(long reservationId) throws Exception;
}
