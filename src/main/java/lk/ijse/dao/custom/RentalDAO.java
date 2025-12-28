package lk.ijse.dao.custom;

import lk.ijse.dao.CrudDAO;
import lk.ijse.entity.Rental;

import java.time.LocalDate;
import java.util.List;

public interface RentalDAO extends CrudDAO<Rental, Long> {

    // Find overdue rentals
    List<Rental> findOverdueRentals(LocalDate today) throws Exception;

    // Optional: additional methods for reporting or filtering
}
