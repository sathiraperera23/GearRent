package lk.ijse.dao.custom.impl;

import lk.ijse.dao.custom.CustomerDAO;
import lk.ijse.db.DBConnection;
import lk.ijse.entity.Customer;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.math.BigDecimal;

public class CustomerDAOImpl implements CustomerDAO {

    @Override
    public boolean save(Customer customer) throws Exception {
        String sql = "INSERT INTO customers(name, nic_passport, contact_no, email, address, membership, created_at) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement pstm = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstm.setString(1, customer.getName());
            pstm.setString(2, customer.getNicPassport());
            pstm.setString(3, customer.getContactNo());
            pstm.setString(4, customer.getEmail());
            pstm.setString(5, customer.getAddress());
            pstm.setString(6, customer.getMembership());
            pstm.setTimestamp(7, customer.getCreatedAt());

            int affectedRows = pstm.executeUpdate();
            if (affectedRows > 0) {
                ResultSet keys = pstm.getGeneratedKeys();
                if (keys.next()) customer.setCustomerId(keys.getLong(1));
                return true;
            }
            return false;
        }
    }

    @Override
    public boolean update(Customer customer) throws Exception {
        String sql = "UPDATE customers SET name=?, nic_passport=?, contact_no=?, email=?, address=?, membership=? WHERE customer_id=?";
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement pstm = conn.prepareStatement(sql)) {

            pstm.setString(1, customer.getName());
            pstm.setString(2, customer.getNicPassport());
            pstm.setString(3, customer.getContactNo());
            pstm.setString(4, customer.getEmail());
            pstm.setString(5, customer.getAddress());
            pstm.setString(6, customer.getMembership());
            pstm.setLong(7, customer.getCustomerId());

            return pstm.executeUpdate() > 0;
        }
    }

    @Override
    public boolean delete(Long customerId) throws Exception {
        String sql = "DELETE FROM customers WHERE customer_id=?";
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement pstm = conn.prepareStatement(sql)) {
            pstm.setLong(1, customerId);
            return pstm.executeUpdate() > 0;
        }
    }

    @Override
    public Customer find(Long customerId) throws Exception {
        String sql = "SELECT * FROM customers WHERE customer_id=?";
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement pstm = conn.prepareStatement(sql)) {
            pstm.setLong(1, customerId);
            ResultSet rs = pstm.executeQuery();
            if (rs.next()) return extractCustomer(rs);
            return null;
        }
    }

    @Override
    public List<Customer> findAll() throws Exception {
        String sql = "SELECT * FROM customers";
        List<Customer> list = new ArrayList<>();
        try (Connection conn = DBConnection.getInstance().getConnection();
             Statement stm = conn.createStatement();
             ResultSet rs = stm.executeQuery(sql)) {

            while (rs.next()) list.add(extractCustomer(rs));
        }
        return list;
    }

    @Override
    public Customer findByEmail(String email) throws Exception {
        String sql = "SELECT * FROM customers WHERE email=?";
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement pstm = conn.prepareStatement(sql)) {
            pstm.setString(1, email);
            ResultSet rs = pstm.executeQuery();
            if (rs.next()) return extractCustomer(rs);
            return null;
        }
    }

    @Override
    public BigDecimal getTotalActiveDeposits(long customerId) throws Exception {
        String sql = "SELECT SUM(security_deposit) as total FROM rentals WHERE customer_id=? AND status='Open'";
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement pstm = conn.prepareStatement(sql)) {
            pstm.setLong(1, customerId);
            ResultSet rs = pstm.executeQuery();
            if (rs.next()) return rs.getBigDecimal("total") != null ? rs.getBigDecimal("total") : BigDecimal.ZERO;
            return BigDecimal.ZERO;
        }
    }

    @Override
    public List<Customer> findCustomersWithActiveRentals() throws Exception {
        String sql = "SELECT DISTINCT c.* FROM customers c JOIN rentals r ON c.customer_id=r.customer_id WHERE r.status='Open'";
        List<Customer> list = new ArrayList<>();
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement pstm = conn.prepareStatement(sql);
             ResultSet rs = pstm.executeQuery()) {

            while (rs.next()) list.add(extractCustomer(rs));
        }
        return list;
    }

    private Customer extractCustomer(ResultSet rs) throws SQLException {
        Customer c = new Customer();
        c.setCustomerId(rs.getLong("customer_id"));
        c.setName(rs.getString("name"));
        c.setNicPassport(rs.getString("nic_passport"));
        c.setContactNo(rs.getString("contact_no"));
        c.setEmail(rs.getString("email"));
        c.setAddress(rs.getString("address"));
        c.setMembership(rs.getString("membership"));
        c.setCreatedAt(rs.getTimestamp("created_at"));
        return c;
    }

    @Override
    public List<Customer> findByFilter(String filter, String startDate, String endDate) throws Exception {
        String sql = "SELECT * FROM customers WHERE (name LIKE ? OR email LIKE ?) AND created_at BETWEEN ? AND ?";
        List<Customer> list = new ArrayList<>();

        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement pstm = conn.prepareStatement(sql)) {

            pstm.setString(1, "%" + filter + "%");
            pstm.setString(2, "%" + filter + "%");
            pstm.setString(3, startDate);
            pstm.setString(4, endDate);

            try (ResultSet rs = pstm.executeQuery()) {
                while (rs.next()) {
                    list.add(extractCustomer(rs));
                }
            }
        }

        return list;
    }

}
