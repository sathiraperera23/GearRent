package lk.ijse.dao.custom;

import lk.ijse.dao.CrudDAO;
import lk.ijse.entity.Customer;

import java.math.BigDecimal;
import java.util.List;

public interface CustomerDAO extends CrudDAO<Customer, Long> {

    Customer findByEmail(String email) throws Exception;

    // New methods for rental history and deposits
    BigDecimal getTotalActiveDeposits(long customerId) throws Exception;

    List<Customer> findCustomersWithActiveRentals() throws Exception;
}
