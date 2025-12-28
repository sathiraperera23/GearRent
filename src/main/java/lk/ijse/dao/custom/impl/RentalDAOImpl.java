package lk.ijse.dao.custom.impl;

import lk.ijse.dao.custom.RentalDAO;
import lk.ijse.db.DBConnection;
import lk.ijse.entity.Rental;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class RentalDAOImpl implements RentalDAO {

    @Override
    public boolean save(Rental rental) throws Exception {
        String sql = "INSERT INTO rentals(customer_id, equipment_id, rented_from, rented_to, actual_return, daily_price, security_deposit, reservation_id, status, total_amount, discount, final_amount, payment_status, damage_charge) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement pstm = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstm.setLong(1, rental.getCustomerId());
            pstm.setLong(2, rental.getEquipmentId());
            pstm.setDate(3, Date.valueOf(rental.getRentedFrom()));
            pstm.setDate(4, Date.valueOf(rental.getRentedTo()));
            if (rental.getActualReturn() != null)
                pstm.setDate(5, Date.valueOf(rental.getActualReturn()));
            else
                pstm.setNull(5, Types.DATE);
            pstm.setBigDecimal(6, rental.getDailyPrice());
            pstm.setBigDecimal(7, rental.getSecurityDeposit());
            if (rental.getReservationId() != null)
                pstm.setLong(8, rental.getReservationId());
            else
                pstm.setNull(8, Types.BIGINT);
            pstm.setString(9, rental.getStatus());
            pstm.setBigDecimal(10, rental.getTotalAmount());
            pstm.setBigDecimal(11, rental.getDiscount());
            pstm.setBigDecimal(12, rental.getFinalAmount());
            pstm.setString(13, rental.getPaymentStatus());
            pstm.setBigDecimal(14, rental.getDamageCharge() != null ? rental.getDamageCharge() : BigDecimal.ZERO);

            int affectedRows = pstm.executeUpdate();
            if (affectedRows > 0) {
                ResultSet keys = pstm.getGeneratedKeys();
                if (keys.next()) rental.setRentalId(keys.getLong(1));
                return true;
            }
            return false;
        }
    }

    @Override
    public boolean update(Rental rental) throws Exception {
        String sql = "UPDATE rentals SET customer_id=?, equipment_id=?, rented_from=?, rented_to=?, actual_return=?, daily_price=?, security_deposit=?, reservation_id=?, status=?, total_amount=?, discount=?, final_amount=?, payment_status=?, damage_charge=? WHERE rental_id=?";
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement pstm = conn.prepareStatement(sql)) {

            pstm.setLong(1, rental.getCustomerId());
            pstm.setLong(2, rental.getEquipmentId());
            pstm.setDate(3, Date.valueOf(rental.getRentedFrom()));
            pstm.setDate(4, Date.valueOf(rental.getRentedTo()));
            if (rental.getActualReturn() != null)
                pstm.setDate(5, Date.valueOf(rental.getActualReturn()));
            else
                pstm.setNull(5, Types.DATE);
            pstm.setBigDecimal(6, rental.getDailyPrice());
            pstm.setBigDecimal(7, rental.getSecurityDeposit());
            if (rental.getReservationId() != null)
                pstm.setLong(8, rental.getReservationId());
            else
                pstm.setNull(8, Types.BIGINT);
            pstm.setString(9, rental.getStatus());
            pstm.setBigDecimal(10, rental.getTotalAmount());
            pstm.setBigDecimal(11, rental.getDiscount());
            pstm.setBigDecimal(12, rental.getFinalAmount());
            pstm.setString(13, rental.getPaymentStatus());
            pstm.setBigDecimal(14, rental.getDamageCharge() != null ? rental.getDamageCharge() : BigDecimal.ZERO);
            pstm.setLong(15, rental.getRentalId());

            return pstm.executeUpdate() > 0;
        }
    }

    @Override
    public boolean delete(Long rentalId) throws Exception {
        String sql = "DELETE FROM rentals WHERE rental_id=?";
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement pstm = conn.prepareStatement(sql)) {
            pstm.setLong(1, rentalId);
            return pstm.executeUpdate() > 0;
        }
    }

    @Override
    public Rental find(Long rentalId) throws Exception {
        String sql = "SELECT * FROM rentals WHERE rental_id=?";
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement pstm = conn.prepareStatement(sql)) {

            pstm.setLong(1, rentalId);
            ResultSet rs = pstm.executeQuery();
            if (rs.next()) return extractRental(rs);
            return null;
        }
    }

    @Override
    public List<Rental> findAll() throws Exception {
        String sql = "SELECT * FROM rentals";
        List<Rental> list = new ArrayList<>();
        try (Connection conn = DBConnection.getInstance().getConnection();
             Statement stm = conn.createStatement();
             ResultSet rs = stm.executeQuery(sql)) {

            while (rs.next()) {
                list.add(extractRental(rs));
            }
        }
        return list;
    }

    @Override
    public List<Rental> findOverdueRentals(LocalDate today) throws Exception {
        String sql = "SELECT * FROM rentals WHERE rented_to < ? AND status='Open'";
        List<Rental> list = new ArrayList<>();
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement pstm = conn.prepareStatement(sql)) {

            pstm.setDate(1, Date.valueOf(today));
            ResultSet rs = pstm.executeQuery();
            while (rs.next()) {
                list.add(extractRental(rs));
            }
        }
        return list;
    }

    private Rental extractRental(ResultSet rs) throws SQLException {
        Rental r = new Rental();
        r.setRentalId(rs.getLong("rental_id"));
        r.setCustomerId(rs.getLong("customer_id"));
        r.setEquipmentId(rs.getLong("equipment_id"));
        r.setRentedFrom(rs.getDate("rented_from").toLocalDate());
        r.setRentedTo(rs.getDate("rented_to").toLocalDate());
        Date actualReturn = rs.getDate("actual_return");
        r.setActualReturn(actualReturn != null ? actualReturn.toLocalDate() : null);
        r.setDailyPrice(rs.getBigDecimal("daily_price"));
        r.setSecurityDeposit(rs.getBigDecimal("security_deposit"));
        long resId = rs.getLong("reservation_id");
        r.setReservationId(rs.wasNull() ? null : resId);
        r.setStatus(rs.getString("status"));
        Timestamp ts = rs.getTimestamp("created_at");
        r.setCreatedAt(ts != null ? ts.toLocalDateTime() : null);
        r.setTotalAmount(rs.getBigDecimal("total_amount"));
        r.setDiscount(rs.getBigDecimal("discount"));
        r.setFinalAmount(rs.getBigDecimal("final_amount"));
        r.setPaymentStatus(rs.getString("payment_status"));
        r.setDamageCharge(rs.getBigDecimal("damage_charge"));
        return r;
    }
}
