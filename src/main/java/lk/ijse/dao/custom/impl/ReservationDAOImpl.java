package lk.ijse.dao.custom.impl;

import lk.ijse.dao.CrudUtil;
import lk.ijse.dao.custom.ReservationDAO;
import lk.ijse.entity.Reservation;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class ReservationDAOImpl implements ReservationDAO {

    @Override
    public boolean save(Reservation r) throws Exception {
        String sql = "INSERT INTO reservations " +
                "(customer_id, equipment_id, reserved_from, reserved_to, total_price, status) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        return CrudUtil.executeUpdate(sql,
                r.getCustomerId(),
                r.getEquipmentId(),
                r.getReservedFrom(),
                r.getReservedTo(),
                r.getTotalPrice(),
                r.getStatus()
        );
    }

    @Override
    public boolean update(Reservation r) throws Exception {
        String sql = "UPDATE reservations SET " +
                "reserved_from = ?, " +
                "reserved_to = ?, " +
                "total_price = ?, " +
                "status = ? " +
                "WHERE reservation_id = ?";

        return CrudUtil.executeUpdate(sql,
                r.getReservedFrom(),
                r.getReservedTo(),
                r.getTotalPrice(),
                r.getStatus(),
                r.getReservationId()
        );
    }

    @Override
    public boolean delete(Long id) throws Exception {
        String sql = "DELETE FROM reservations WHERE reservation_id = ?";
        return CrudUtil.executeUpdate(sql, id);
    }

    @Override
    public Reservation find(Long id) throws Exception {
        String sql = "SELECT * FROM reservations WHERE reservation_id = ?";
        ResultSet rs = CrudUtil.executeQuery(sql, id);
        return rs.next() ? mapRow(rs) : null;
    }

    @Override
    public List<Reservation> findAll() throws Exception {
        String sql = "SELECT * FROM reservations ORDER BY reservation_id";
        ResultSet rs = CrudUtil.executeQuery(sql);
        List<Reservation> list = new ArrayList<>();
        while (rs.next()) {
            list.add(mapRow(rs));
        }
        return list;
    }

    @Override
    public boolean isEquipmentAvailable(long equipmentId, Date from, Date to, Long excludeReservationId) throws Exception {
        String sql = "SELECT COUNT(*) FROM reservations " +
                "WHERE equipment_id = ? " +
                "AND status IN ('Pending', 'Confirmed') " +
                "AND NOT (? > reserved_to OR ? < reserved_from)";

        Object[] params;
        if (excludeReservationId != null) {
            sql += " AND reservation_id <> ?";
            params = new Object[]{equipmentId, to, from, excludeReservationId};
        } else {
            params = new Object[]{equipmentId, to, from};
        }

        ResultSet rs = CrudUtil.executeQuery(sql, params);
        rs.next();
        return rs.getInt(1) == 0;
    }

    @Override
    public List<Reservation> findByFilter(String filter, Date startDate, Date endDate) throws Exception {
        String sql = "SELECT r.* FROM reservations r " +
                "JOIN customers c ON r.customer_id = c.customer_id " +
                "JOIN equipment e ON r.equipment_id = e.equipment_id " +
                "WHERE (c.name LIKE ? OR e.brand LIKE ? OR e.model LIKE ? OR e.equipment_code LIKE ?)";

        List<Object> params = new ArrayList<>();
        params.add("%" + filter + "%");
        params.add("%" + filter + "%");
        params.add("%" + filter + "%");
        params.add("%" + filter + "%");

        // Only add date filter if both dates are provided
        if (startDate != null && endDate != null) {
            sql += " AND r.created_at BETWEEN ? AND ?";
            params.add(startDate);
            params.add(endDate);
        }

        ResultSet rs = CrudUtil.executeQuery(sql, params.toArray());
        List<Reservation> list = new ArrayList<>();
        while (rs.next()) {
            list.add(mapRow(rs));
        }
        return list;
    }

    private Reservation mapRow(ResultSet rs) throws Exception {
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
}