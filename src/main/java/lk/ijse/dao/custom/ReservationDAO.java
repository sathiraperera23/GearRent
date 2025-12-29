package lk.ijse.dao.custom;

import lk.ijse.dao.CrudDAO;
import lk.ijse.entity.Reservation;

import java.sql.Date;
import java.util.List;

public interface ReservationDAO extends CrudDAO<Reservation, Long> {

    boolean isEquipmentAvailable(
            long equipmentId,
            Date from,
            Date to,
            Long excludeReservationId
    ) throws Exception;

    List<Reservation> findByFilter(
            String filter,
            Date startDate,
            Date endDate
    ) throws Exception;
}
