package lk.ijse.dao.custom.impl;

import lk.ijse.dao.CrudUtil;
import lk.ijse.dao.custom.RentalDAO;
import lk.ijse.entity.Rental;

import java.sql.ResultSet;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class RentalDAOImpl implements RentalDAO {

    @Override
    public boolean save(Rental r) throws Exception {
        String sql = "INSERT INTO rentals (customer_id, equipment_id, rented_from, rented_to, daily_price, security_deposit, reservation_id, status) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        return CrudUtil.executeUpdate(sql,
                r.getCustomerId(),
                r.getEquipmentId(),
                Date.valueOf(r.getRentedFrom()),
                Date.valueOf(r.getRentedTo()),
                r.getDailyPrice(),
                r.getSecurityDeposit(),
                r.getReservationId(),
                r.getStatus());
    }

    @Override
    public boolean update(Rental r) throws Exception {
        String sql = "UPDATE rentals SET rented_from=?, rented_to=?, daily_price=?, security_deposit=?, status=? WHERE rental_id=?";
        return CrudUtil.executeUpdate(sql,
                Date.valueOf(r.getRentedFrom()),
                Date.valueOf(r.getRentedTo()),
                r.getDailyPrice(),
                r.getSecurityDeposit(),
                r.getStatus(),
                r.getRentalId());
    }

    @Override
    public boolean delete(Long id) throws Exception {
        String sql = "DELETE FROM rentals WHERE rental_id=?";
        return CrudUtil.executeUpdate(sql, id);
    }

    @Override
    public Rental find(Long id) throws Exception {
        String sql = "SELECT * FROM rentals WHERE rental_id=?";
        ResultSet rs = CrudUtil.executeQuery(sql, id);
        if (rs.next()) {
            return new Rental(
                    rs.getLong("rental_id"),
                    rs.getLong("customer_id"),
                    rs.getLong("equipment_id"),
                    rs.getDate("rented_from").toLocalDate(),
                    rs.getDate("rented_to").toLocalDate(),
                    rs.getBigDecimal("daily_price"),
                    rs.getBigDecimal("security_deposit"),
                    rs.getLong("reservation_id"),
                    rs.getString("status"),
                    rs.getTimestamp("created_at")
            );
        }
        return null;
    }

    @Override
    public List<Rental> findAll() throws Exception {
        String sql = "SELECT * FROM rentals";
        ResultSet rs = CrudUtil.executeQuery(sql);
        List<Rental> list = new ArrayList<>();
        while (rs.next()) {
            list.add(new Rental(
                    rs.getLong("rental_id"),
                    rs.getLong("customer_id"),
                    rs.getLong("equipment_id"),
                    rs.getDate("rented_from").toLocalDate(),
                    rs.getDate("rented_to").toLocalDate(),
                    rs.getBigDecimal("daily_price"),
                    rs.getBigDecimal("security_deposit"),
                    rs.getLong("reservation_id"),
                    rs.getString("status"),
                    rs.getTimestamp("created_at")
            ));
        }
        return list;
    }
}
