package lk.ijse.dao.custom.impl;

import lk.ijse.dao.CrudUtil;
import lk.ijse.dao.custom.RentalDAO;
import lk.ijse.entity.Rental;

import java.sql.Date;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class RentalDAOImpl implements RentalDAO {

    @Override
    public boolean save(Rental r) throws Exception {
        String sql = "INSERT INTO rentals (" +
                "customer_id, equipment_id, branch_id, rented_from, rented_to, " +
                "daily_price, security_deposit, reservation_id, status, " +
                "total_amount, discount, final_amount, payment_status, damage_charge, damage_description" +
                ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        return CrudUtil.executeUpdate(
                sql,
                r.getCustomerId(),
                r.getEquipmentId(),
                r.getBranchId(),
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
                r.getDamageCharge(),
                r.getDamageDescription()
        );
    }

    @Override
    public boolean update(Rental r) throws Exception {
        String sql = "UPDATE rentals SET " +
                "customer_id=?, equipment_id=?, branch_id=?, rented_from=?, rented_to=?, " +
                "actual_return=?, daily_price=?, security_deposit=?, reservation_id=?, status=?, " +
                "total_amount=?, discount=?, final_amount=?, payment_status=?, damage_charge=?, damage_description=? " +
                "WHERE rental_id=?";

        return CrudUtil.executeUpdate(
                sql,
                r.getCustomerId(),
                r.getEquipmentId(),
                r.getBranchId(),
                r.getRentedFrom(),
                r.getRentedTo(),
                r.getActualReturn(),
                r.getDailyPrice(),
                r.getSecurityDeposit(),
                r.getReservationId(),
                r.getStatus(),
                r.getTotalAmount(),
                r.getDiscount(),
                r.getFinalAmount(),
                r.getPaymentStatus(),
                r.getDamageCharge(),
                r.getDamageDescription(),
                r.getRentalId()
        );
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

        if (rs.next()) return mapRow(rs);
        return null;
    }

    @Override
    public List<Rental> findAll() throws Exception {
        String sql = "SELECT * FROM rentals";
        ResultSet rs = CrudUtil.executeQuery(sql);

        List<Rental> list = new ArrayList<>();
        while (rs.next()) {
            list.add(mapRow(rs));
        }
        return list;
    }

    @Override
    public List<Rental> findOverdueRentals(LocalDate today) throws Exception {
        String sql = "SELECT * FROM rentals WHERE status='Active' AND rented_to < ?";
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
                "WHERE equipment_id=? AND status IN ('Active','Overdue') " +
                "AND NOT (? > rented_to OR ? < rented_from)";

        if (excludeRentalId != null) {
            sql += " AND rental_id <> ?";
            ResultSet rs = CrudUtil.executeQuery(sql, equipmentId, from, to, excludeRentalId);
            rs.next();
            return rs.getInt(1) == 0;
        } else {
            ResultSet rs = CrudUtil.executeQuery(sql, equipmentId, from, to);
            rs.next();
            return rs.getInt(1) == 0;
        }
    }

    private Rental mapRow(ResultSet rs) throws Exception {
        return new Rental(
                rs.getLong("rental_id"),
                rs.getLong("customer_id"),
                rs.getLong("equipment_id"),
                rs.getLong("branch_id"),
                rs.getDate("rented_from").toLocalDate(),
                rs.getDate("rented_to").toLocalDate(),
                rs.getDate("actual_return") != null ? rs.getDate("actual_return").toLocalDate() : null,
                rs.getBigDecimal("daily_price"),
                rs.getBigDecimal("security_deposit"),
                rs.getObject("reservation_id") != null ? rs.getLong("reservation_id") : null,
                rs.getString("status"),
                null,
                rs.getBigDecimal("total_amount"),
                rs.getBigDecimal("discount"),
                rs.getBigDecimal("final_amount"),
                rs.getString("payment_status"),
                rs.getBigDecimal("damage_charge"),
                rs.getString("damage_description")
        );
    }
}
