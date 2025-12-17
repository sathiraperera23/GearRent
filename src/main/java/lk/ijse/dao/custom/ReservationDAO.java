package lk.ijse.dao.custom;

import lk.ijse.dao.CrudDAO;
import lk.ijse.entity.Reservation;

import java.sql.Date;


public interface ReservationDAO extends CrudDAO<Reservation, Long> {
    // Can add extra methods like findByCustomerId if needed
    boolean isEquipmentAvailable(
            long equipmentId,
            Date from,
            Date to,
            Long excludeReservationId
    ) throws Exception;
}
