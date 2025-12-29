package lk.ijse.service.custom;

import lk.ijse.dto.CustomerDTO;
import lk.ijse.service.SuperService;

import java.math.BigDecimal;
import java.util.List;

public interface CustomerService extends SuperService {

    boolean saveCustomer(CustomerDTO dto) throws Exception;

    boolean updateCustomer(CustomerDTO dto) throws Exception;

    boolean deleteCustomer(long customerId) throws Exception;

    CustomerDTO findCustomer(long customerId) throws Exception;

    List<CustomerDTO> getAllCustomers() throws Exception;

    BigDecimal getTotalActiveDeposits(long customerId) throws Exception;

    List<CustomerDTO> getCustomersWithActiveRentals() throws Exception;

    List<CustomerDTO> getCustomersByFilter(String filter, String startDate, String endDate) throws Exception;

}
