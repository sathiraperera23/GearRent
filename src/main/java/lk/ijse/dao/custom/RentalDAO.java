package lk.ijse.dao.custom;

import lk.ijse.dao.CrudDAO;
import lk.ijse.entity.Rental;

import java.time.LocalDate;
import java.util.List;

public interface RentalDAO extends CrudDAO<Rental, Long> {

    List<Rental> findOverdueRentals(LocalDate today) throws Exception;

    boolean isEquipmentAvailable(long equipmentId,
                                 LocalDate from,
                                 LocalDate to,
                                 Long excludeRentalId) throws Exception;
}
