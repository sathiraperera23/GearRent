package lk.ijse.dao.custom.impl;

import lk.ijse.dao.CrudUtil;
import lk.ijse.dao.custom.RentalDAO;
import lk.ijse.entity.Rental;

import java.sql.Date;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.math.BigDecimal;

public class RentalDAOImpl implements RentalDAO {

    @Override
    public boolean save(Rental r) throws Exception {
        String sql = "INSERT INTO rentals (customer_id, equipment_id, rented_from, rented_to, daily_price, security_deposit, reservation_id, status) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        return CrudUtil.executeUpdate(sql,
                r.getCustomerId(),
                r.getEquipmentId(),
                Date.valueOf(r.getRentedFrom()),
                Date.valueOf(r.getRentedTo()),
                r.getDailyPrice().doubleValue(),
                r.getSecurityDeposit().doubleValue(),
                r.getReservationId(),
                r.getStatus());
    }

    @Override
    public boolean update(Rental r) throws Exception {
        String sql = "UPDATE rentals SET rented_to=?, daily_price=?, security_deposit=?, status=? WHERE rental_id=?";
        return CrudUtil.executeUpdate(sql,
                Date.valueOf(r.getRentedTo()),
                r.getDailyPrice().doubleValue(),
                r.getSecurityDeposit().doubleValue(),
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
            return mapRow(rs);
        }
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
    public boolean isEquipmentAvailable(long equipmentId, Date from, Date to) throws Exception {
        // Check both rentals and reservations in a single query
        String sql = """
        SELECT COUNT(*) FROM (
            SELECT rented_from AS start_date, rented_to AS end_date FROM rentals 
                WHERE equipment_id=? AND status='Open'
            UNION ALL
            SELECT reserved_from AS start_date, reserved_to AS end_date FROM reservations
                WHERE equipment_id=? AND status IN ('Pending','Confirmed')
        ) AS periods
        WHERE NOT (? > end_date OR ? < start_date)
        """;

        ResultSet rs = CrudUtil.executeQuery(sql, equipmentId, equipmentId, from, to);
        rs.next();
        return rs.getInt(1) == 0;
    }

    private Rental mapRow(ResultSet rs) throws Exception {
        return new Rental(
                rs.getLong("rental_id"),
                rs.getLong("customer_id"),
                rs.getLong("equipment_id"),
                rs.getDate("rented_from").toLocalDate(),
                rs.getDate("rented_to").toLocalDate(),
                BigDecimal.valueOf(rs.getDouble("daily_price")),
                BigDecimal.valueOf(rs.getDouble("security_deposit")),
                rs.getLong("reservation_id"),
                rs.getString("status"),
                rs.getTimestamp("created_at")
        );
    }
}
