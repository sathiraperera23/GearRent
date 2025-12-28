package lk.ijse.service.custom;

import lk.ijse.dto.RentalDTO;
import lk.ijse.service.SuperService;

import java.util.List;

public interface RentalService extends SuperService {

    boolean saveRental(RentalDTO dto) throws Exception;

    boolean updateRental(RentalDTO dto) throws Exception;

    boolean closeRental(long rentalId) throws Exception;

    RentalDTO findRental(long rentalId) throws Exception;

    List<RentalDTO> getAllRentals() throws Exception;

    // ✅ NO PARAMETER
    List<RentalDTO> getOverdueRentals() throws Exception;
}
