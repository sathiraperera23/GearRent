package lk.ijse.service.custom;

import lk.ijse.dto.RentalDTO;
import lk.ijse.service.SuperService;

import java.util.List;

public interface RentalService extends SuperService {

    /**
     * Save a new rental
     */
    boolean saveRental(RentalDTO dto) throws Exception;

    /**
     * Update an existing rental
     */
    boolean updateRental(RentalDTO dto) throws Exception;

    /**
     * Find a rental by ID
     */
    RentalDTO findRental(long rentalId) throws Exception;

    /**
     * Get all rentals
     */
    List<RentalDTO> getAllRentals() throws Exception;

    /**
     * Get overdue rentals (rented_to < today and status = 'Open')
     */
    List<RentalDTO> getOverdueRentals() throws Exception;

    // Future methods (uncomment when you add the required columns to the rentals table)
    /*
    boolean returnRental(long rentalId, LocalDate actualReturn, BigDecimal damageCharge, String damageNote) throws Exception;

    boolean closeRental(long rentalId, RentalDTO returnInfo) throws Exception;
    */
}