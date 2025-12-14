package lk.ijse.dao.custom.impl;

import lk.ijse.dao.CrudUtil;
import lk.ijse.dao.custom.ReservationDAO;
import lk.ijse.entity.Reservation;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ReservationDAOImpl implements ReservationDAO {

    @Override
    public boolean save(Reservation r) throws Exception {
        String sql = "INSERT INTO reservations (customer_id, equipment_id, reserved_from, reserved_to, total_price, status) VALUES (?, ?, ?, ?, ?, ?)";
        return CrudUtil.execute(sql, r.getCustomerId(), r.getEquipmentId(), r.getReservedFrom(),
                r.getReservedTo(), r.getTotalPrice(), r.getStatus());
    }

    @Override
    public boolean update(Reservation r) throws Exception {
        String sql = "UPDATE reservations SET reserved_to=?, total_price=?, status=? WHERE reservation_id=?";
        return CrudUtil.execute(sql, r.getReservedTo(), r.getTotalPrice(), r.getStatus(), r.getReservationId());
    }

    @Override
    public boolean delete(Long id) throws Exception {
        String sql = "DELETE FROM reservations WHERE reservation_id=?";
        return CrudUtil.execute(sql, id);
    }

    @Override
    public Reservation find(Long id) throws Exception {
        String sql = "SELECT * FROM reservations WHERE reservation_id=?";
        ResultSet rs = CrudUtil.executeQuery(sql, id);
        if (rs.next()) {
            return new Reservation(
                    rs.getLong("reservation_id"),
                    rs.getLong("customer_id"),
                    rs.getLong("equipment_id"),
                    rs.getDate("reserved_from"),
                    rs.getDate("reserved_to"),
                    rs.getDouble("total_price"),
                    rs.getString("status"),
                    rs.getTimestamp("created_at")
            );
        }
        return null;
    }

    @Override
    public List<Reservation> findAll() throws Exception {
        String sql = "SELECT * FROM reservations";
        ResultSet rs = CrudUtil.executeQuery(sql);
        List<Reservation> list = new ArrayList<>();
        while (rs.next()) {
            list.add(new Reservation(
                    rs.getLong("reservation_id"),
                    rs.getLong("customer_id"),
                    rs.getLong("equipment_id"),
                    rs.getDate("reserved_from"),
                    rs.getDate("reserved_to"),
                    rs.getDouble("total_price"),
                    rs.getString("status"),
                    rs.getTimestamp("created_at")
            ));
        }
        return list;
    }
}
