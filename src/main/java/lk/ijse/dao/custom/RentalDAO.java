package lk.ijse.dao.custom;

import lk.ijse.dao.CrudDAO;
import lk.ijse.entity.Rental;

import java.sql.Date;

public interface RentalDAO extends CrudDAO<Rental, Long> {
    boolean isEquipmentAvailable(long equipmentId, Date from, Date to) throws Exception;
}
