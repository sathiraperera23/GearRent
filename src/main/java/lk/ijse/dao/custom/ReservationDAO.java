package lk.ijse.dao.custom;

import lk.ijse.dao.CrudDAO;
import lk.ijse.entity.Reservation;

public interface ReservationDAO extends CrudDAO<Reservation, Long> {
    // Can add extra methods like findByCustomerId if needed
}
