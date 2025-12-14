package lk.ijse.dao.custom.impl;



import lk.ijse.dao.CrudUtil;
import lk.ijse.dao.custom.RentalDAO;
import lk.ijse.entity.Rental;

import java.sql.ResultSet;
import java.util.ArrayList;

public class RentalDAOImpl implements RentalDAO {

    @Override
    public boolean save(Rental r) throws Exception {
        return CrudUtil.executeUpdate(
                "INSERT INTO rentals (customer_id, equipment_id, rented_from, rented_to, daily_price, security_deposit, reservation_id, status) " +
                        "VALUES (?,?,?,?,?,?,?,?)",
                r.getCustomerId(),
                r.getEquipmentId(),
                r.getRentedFrom(),
                r.getRentedTo(),
                r.getDailyPrice(),
                r.getSecurityDeposit(),
                r.getReservationId(),
                "Open"
        );
    }

    @Override
    public boolean update(Rental r) throws Exception {
        return CrudUtil.executeUpdate(
                "UPDATE rentals SET rented_to=?, status=? WHERE rental_id=?",
                r.getRentedTo(),
                r.getStatus(),
                r.getRentalId()
        );
    }

    @Override
    public boolean delete(Long id) throws Exception {
        return CrudUtil.executeUpdate(
                "DELETE FROM rentals WHERE rental_id=?", id
        );
    }

    @Override
    public Rental find(Long id) throws Exception {
        ResultSet rs = CrudUtil.executeQuery(
                "SELECT * FROM rentals WHERE rental_id=?", id
        );

        if (rs.next()) {
            return new Rental(
                    rs.getLong("rental_id"),
                    rs.getLong("customer_id"),
                    rs.getLong("equipment_id"),
                    rs.getDate("rented_from"),
                    rs.getDate("rented_to"),
                    rs.getDouble("daily_price"),
                    rs.getDouble("security_deposit"),
                    rs.getObject("reservation_id", Long.class),
                    rs.getString("status"),
                    rs.getTimestamp("created_at")
            );
        }
        return null;
    }

    @Override
    public ArrayList<Rental> findAll() throws Exception {
        ResultSet rs = CrudUtil.executeQuery("SELECT * FROM rentals");
        ArrayList<Rental> list = new ArrayList<>();

        while (rs.next()) {
            list.add(new Rental(
                    rs.getLong("rental_id"),
                    rs.getLong("customer_id"),
                    rs.getLong("equipment_id"),
                    rs.getDate("rented_from"),
                    rs.getDate("rented_to"),
                    rs.getDouble("daily_price"),
                    rs.getDouble("security_deposit"),
                    rs.getObject("reservation_id", Long.class),
                    rs.getString("status"),
                    rs.getTimestamp("created_at")
            ));
        }
        return list;
    }
}
