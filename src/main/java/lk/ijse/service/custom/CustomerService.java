package lk.ijse.service.custom;


import lk.ijse.dto.CustomerDTO;
import lk.ijse.service.SuperService;
import java.util.List;

public interface CustomerService extends SuperService {

    boolean addCustomer(CustomerDTO dto) throws Exception;

    boolean updateCustomer(CustomerDTO dto) throws Exception;

    boolean deleteCustomer(long id) throws Exception;

    CustomerDTO findCustomer(long id) throws Exception;

    List<CustomerDTO> getAllCustomers() throws Exception;
}