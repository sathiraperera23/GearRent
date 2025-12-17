package lk.ijse.dao.custom.impl;

import lk.ijse.dao.CrudUtil;
import lk.ijse.dao.custom.ReservationDAO;
import lk.ijse.entity.Reservation;

import java.sql.Date;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ReservationDAOImpl implements ReservationDAO {

    /* ===================== SAVE ===================== */

    @Override
    public boolean save(Reservation r) throws Exception {

        String sql =
                "INSERT INTO reservations " +
                        "(customer_id, equipment_id, reserved_from, reserved_to, total_price, status) " +
                        "VALUES (?, ?, ?, ?, ?, ?)";

        return CrudUtil.executeUpdate(
                sql,
                r.getCustomerId(),
                r.getEquipmentId(),
                r.getReservedFrom(),
                r.getReservedTo(),
                r.getTotalPrice(),
                r.getStatus()
        );
    }

    /* ===================== UPDATE ===================== */

    @Override
    public boolean update(Reservation r) throws Exception {

        String sql =
                "UPDATE reservations SET " +
                        "reserved_from = ?, " +
                        "reserved_to = ?, " +
                        "total_price = ?, " +
                        "status = ? " +
                        "WHERE reservation_id = ?";

        return CrudUtil.executeUpdate(
                sql,
                r.getReservedFrom(),
                r.getReservedTo(),
                r.getTotalPrice(),
                r.getStatus(),
                r.getReservationId()
        );
    }

    /* ===================== DELETE ===================== */

    @Override
    public boolean delete(Long id) throws Exception {

        String sql = "DELETE FROM reservations WHERE reservation_id = ?";
        return CrudUtil.executeUpdate(sql, id);
    }

    /* ===================== FIND BY ID ===================== */

    @Override
    public Reservation find(Long id) throws Exception {

        String sql = "SELECT * FROM reservations WHERE reservation_id = ?";
        ResultSet rs = CrudUtil.executeQuery(sql, id);

        if (rs.next()) {
            return mapRow(rs);
        }
        return null;
    }

    /* ===================== FIND ALL ===================== */

    @Override
    public List<Reservation> findAll() throws Exception {

        String sql = "SELECT * FROM reservations";
        ResultSet rs = CrudUtil.executeQuery(sql);

        List<Reservation> list = new ArrayList<>();
        while (rs.next()) {
            list.add(mapRow(rs));
        }
        return list;
    }

    /* ===================== AVAILABILITY CHECK ===================== */

    @Override
    public boolean isEquipmentAvailable(
            long equipmentId,
            Date from,
            Date to,
            Long excludeReservationId
    ) throws Exception {

        String sql =
                "SELECT COUNT(*) " +
                        "FROM reservations " +
                        "WHERE equipment_id = ? " +
                        "AND status IN ('Pending','Confirmed') " +
                        "AND NOT (? > reserved_to OR ? < reserved_from)";

        if (excludeReservationId != null) {
            sql += " AND reservation_id <> ?";
        }

        ResultSet rs;

        if (excludeReservationId == null) {
            rs = CrudUtil.executeQuery(sql, equipmentId, from, to);
        } else {
            rs = CrudUtil.executeQuery(sql, equipmentId, from, to, excludeReservationId);
        }

        rs.next();
        return rs.getInt(1) == 0;
    }

    /* ===================== HELPER ===================== */

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
