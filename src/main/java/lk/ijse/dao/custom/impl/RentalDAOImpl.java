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
        return CrudUtil.executeUpdate(
                """
                INSERT INTO rentals
                (customer_id, equipment_id, rented_from, rented_to,
                 daily_price, security_deposit, reservation_id, status)
                VALUES (?,?,?,?,?,?,?,?)
                """,
                r.getCustomerId(),
                r.getEquipmentId(),
                Date.valueOf(r.getRentedFrom()),
                Date.valueOf(r.getRentedTo()),
                r.getDailyPrice(),
                r.getSecurityDeposit(),
                r.getReservationId(),
                r.getStatus()
        );
    }

    @Override
    public boolean update(Rental r) throws Exception {
        return CrudUtil.executeUpdate(
                """
                UPDATE rentals SET
                customer_id=?,
                equipment_id=?,
                rented_from=?,
                rented_to=?,
                daily_price=?,
                security_deposit=?,
                reservation_id=?,
                status=?
                WHERE rental_id=?
                """,
                r.getCustomerId(),
                r.getEquipmentId(),
                Date.valueOf(r.getRentedFrom()),
                Date.valueOf(r.getRentedTo()),
                r.getDailyPrice(),
                r.getSecurityDeposit(),
                r.getReservationId(),
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

        return rs.next() ? map(rs) : null;
    }

    @Override
    public List<Rental> findAll() throws Exception {
        ResultSet rs = CrudUtil.executeQuery("SELECT * FROM rentals");
        List<Rental> list = new ArrayList<>();

        while (rs.next()) {
            list.add(map(rs));
        }
        return list;
    }

    @Override
    public List<Rental> findOverdueRentals(LocalDate today) throws Exception {
        ResultSet rs = CrudUtil.executeQuery(
                """
                SELECT * FROM rentals
                WHERE rented_to < ?
                AND status = 'Open'
                """,
                Date.valueOf(today)
        );

        List<Rental> list = new ArrayList<>();
        while (rs.next()) {
            list.add(map(rs));
        }
        return list;
    }

    /* ===================== MAPPER ===================== */

    private Rental map(ResultSet rs) throws Exception {
        return new Rental(
                rs.getLong("rental_id"),
                rs.getLong("customer_id"),
                rs.getLong("equipment_id"),
                rs.getDate("rented_from").toLocalDate(),
                rs.getDate("rented_to").toLocalDate(),
                rs.getBigDecimal("daily_price"),
                rs.getBigDecimal("security_deposit"),
                rs.getObject("reservation_id", Long.class),
                rs.getString("status"),
                rs.getTimestamp("created_at")
        );
    }
}
