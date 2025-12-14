package lk.ijse.dao.custom.impl;



import lk.ijse.dao.CrudUtil;
import lk.ijse.dao.custom.CustomerDAO;
import lk.ijse.entity.Customer;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class CustomerDAOImpl implements CustomerDAO {

    @Override
    public boolean save(Customer customer) throws Exception {
        return CrudUtil.executeUpdate(
                "INSERT INTO Customers (name, nic_passport, contact_no, email, address, membership) VALUES (?,?,?,?,?,?)",
                customer.getName(), customer.getNicPassport(), customer.getContactNo(),
                customer.getEmail(), customer.getAddress(), customer.getMembership()
        );
    }

    @Override
    public boolean update(Customer customer) throws Exception {
        return CrudUtil.executeUpdate(
                "UPDATE Customers SET name=?, nic_passport=?, contact_no=?, email=?, address=?, membership=? WHERE customer_id=?",
                customer.getName(), customer.getNicPassport(), customer.getContactNo(),
                customer.getEmail(), customer.getAddress(), customer.getMembership(),
                customer.getCustomerId()
        );
    }

    @Override
    public boolean delete(Long customerId) throws Exception {
        return CrudUtil.executeUpdate("DELETE FROM Customers WHERE customer_id=?", customerId);
    }

    @Override
    public Customer find(Long customerId) throws Exception {
        ResultSet rst = CrudUtil.executeQuery("SELECT * FROM Customers WHERE customer_id=?", customerId);
        if (rst.next()) {
            return new Customer(
                    rst.getLong("customer_id"),
                    rst.getString("name"),
                    rst.getString("nic_passport"),
                    rst.getString("contact_no"),
                    rst.getString("email"),
                    rst.getString("address"),
                    rst.getString("membership"),
                    rst.getTimestamp("created_at")
            );
        }
        return null;
    }

    @Override
    public List<Customer> findAll() throws Exception {
        ResultSet rst = CrudUtil.executeQuery("SELECT * FROM Customers");
        List<Customer> list = new ArrayList<>();
        while (rst.next()) {
            list.add(new Customer(
                    rst.getLong("customer_id"),
                    rst.getString("name"),
                    rst.getString("nic_passport"),
                    rst.getString("contact_no"),
                    rst.getString("email"),
                    rst.getString("address"),
                    rst.getString("membership"),
                    rst.getTimestamp("created_at")
            ));
        }
        return list;
    }
}