package lk.ijse.dao.custom.impl;

import lk.ijse.dao.CrudUtil;
import lk.ijse.dao.custom.RentalDAO;
import lk.ijse.entity.Rental;

import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class RentalDAOImpl implements RentalDAO {

    @Override
    public boolean save(Rental r) throws Exception {
        String sql = "INSERT INTO rentals (" +
                "customer_id, equipment_id, rented_from, rented_to, " +
                "daily_price, security_deposit, reservation_id, status, " +
                "total_amount, discount, final_amount, payment_status" +
                ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        return CrudUtil.executeUpdate(sql,
                r.getCustomerId(),
                r.getEquipmentId(),
                r.getRentedFrom(),
                r.getRentedTo(),
                r.getDailyPrice(),
                r.getSecurityDeposit(),
                r.getReservationId(),
                r.getStatus(),
                r.getTotalAmount(),
                r.getDiscount(),
                r.getFinalAmount(),
                r.getPaymentStatus()
        );
    }

    @Override
    public boolean update(Rental r) throws Exception {
        String sql = "UPDATE rentals SET " +
                "customer_id = ?, equipment_id = ?, rented_from = ?, rented_to = ?, " +
                "daily_price = ?, security_deposit = ?, reservation_id = ?, status = ?, " +
                "total_amount = ?, discount = ?, final_amount = ?, payment_status = ? " +
                "WHERE rental_id = ?";

        return CrudUtil.executeUpdate(sql,
                r.getCustomerId(),
                r.getEquipmentId(),
                r.getRentedFrom(),
                r.getRentedTo(),
                r.getDailyPrice(),
                r.getSecurityDeposit(),
                r.getReservationId(),
                r.getStatus(),
                r.getTotalAmount(),
                r.getDiscount(),
                r.getFinalAmount(),
                r.getPaymentStatus(),
                r.getRentalId()
        );
    }

    @Override
    public boolean delete(Long id) throws Exception {
        String sql = "DELETE FROM rentals WHERE rental_id = ?";
        return CrudUtil.executeUpdate(sql, id);
    }

    @Override
    public Rental find(Long id) throws Exception {
        String sql = "SELECT * FROM rentals WHERE rental_id = ?";
        ResultSet rs = CrudUtil.executeQuery(sql, id);
        return rs.next() ? mapRow(rs) : null;
    }

    @Override
    public List<Rental> findAll() throws Exception {
        String sql = "SELECT * FROM rentals ORDER BY rental_id";
        ResultSet rs = CrudUtil.executeQuery(sql);
        List<Rental> list = new ArrayList<>();
        while (rs.next()) {
            list.add(mapRow(rs));
        }
        return list;
    }

    @Override
    public List<Rental> findOverdueRentals(LocalDate today) throws Exception {
        String sql = "SELECT * FROM rentals WHERE status = 'Open' AND rented_to < ? ORDER BY rented_to";
        ResultSet rs = CrudUtil.executeQuery(sql, today);
        List<Rental> list = new ArrayList<>();
        while (rs.next()) {
            list.add(mapRow(rs));
        }
        return list;
    }

    @Override
    public boolean isEquipmentAvailable(long equipmentId, LocalDate from, LocalDate to, Long excludeRentalId) throws Exception {
        String sql = "SELECT COUNT(*) FROM rentals " +
                "WHERE equipment_id = ? " +
                "AND status = 'Open' " +
                "AND NOT (? > rented_to OR ? < rented_from)";

        Object[] params;
        if (excludeRentalId != null) {
            sql += " AND rental_id <> ?";
            params = new Object[]{equipmentId, to, from, excludeRentalId};
        } else {
            params = new Object[]{equipmentId, to, from};
        }

        ResultSet rs = CrudUtil.executeQuery(sql, params);
        rs.next();
        return rs.getInt(1) == 0;
    }

    /**
     * Maps a ResultSet row to a Rental entity.
     * Uses safe null handling for reservation_id.
     * All removed fields are set to null.
     */
    private Rental mapRow(ResultSet rs) throws Exception {
        Long reservationId = null;
        Object resObj = rs.getObject("reservation_id");
        if (resObj != null) {
            reservationId = (Long) resObj;
        }

        return new Rental(
                rs.getLong("rental_id"),           // Long → safe even if theoretically null
                rs.getLong("customer_id"),
                rs.getLong("equipment_id"),
                rs.getDate("rented_from").toLocalDate(),
                rs.getDate("rented_to").toLocalDate(),
                rs.getBigDecimal("daily_price"),
                rs.getBigDecimal("security_deposit"),
                reservationId,
                rs.getString("status"),
                rs.getBigDecimal("total_amount"),
                rs.getBigDecimal("discount"),
                rs.getBigDecimal("final_amount"),
                rs.getString("payment_status")
        );
    }
}