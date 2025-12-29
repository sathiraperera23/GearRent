package lk.ijse.service.custom;

import lk.ijse.dto.RentalDTO;
import lk.ijse.service.ServiceFactory;
import lk.ijse.service.SuperService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface RentalService extends SuperService {

    boolean saveRental(RentalDTO dto) throws Exception;

    boolean updateRental(RentalDTO dto) throws Exception;

    boolean closeRental(long rentalId,
                        RentalDTO returnInfo) throws Exception;

    RentalDTO findRental(long rentalId) throws Exception;

    List<RentalDTO> getAllRentals() throws Exception;

    List<RentalDTO> getOverdueRentals() throws Exception;

    boolean returnRental(
            long rentalId,
            LocalDate actualReturn,
            BigDecimal damageCharge,
            String damageNote
    ) throws Exception;

}
