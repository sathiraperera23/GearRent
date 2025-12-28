package lk.ijse.service.custom.impl;

import lk.ijse.dao.DAOFactory;
import lk.ijse.dao.custom.CustomerDAO;
import lk.ijse.dto.CustomerDTO;
import lk.ijse.entity.Customer;
import lk.ijse.service.custom.CustomerService;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class CustomerServiceImpl implements CustomerService {

    private final CustomerDAO customerDAO =
            (CustomerDAO) DAOFactory.getInstance().getDAO(DAOFactory.DAOTypes.CUSTOMER);

    /* ===================== HELPERS ===================== */
    private Customer toEntity(CustomerDTO dto) {
        return new Customer(
                dto.getCustomerId(),
                dto.getName(),
                dto.getNicPassport(),
                dto.getContactNo(),
                dto.getEmail(),
                dto.getAddress(),
                dto.getMembership(),
                dto.getCreatedAt()
        );
    }

    private CustomerDTO toDTO(Customer c) {
        return new CustomerDTO(
                c.getCustomerId(),
                c.getName(),
                c.getNicPassport(),
                c.getContactNo(),
                c.getEmail(),
                c.getAddress(),
                c.getMembership(),
                c.getCreatedAt()
        );
    }

    /* ===================== BUSINESS ===================== */

    @Override
    public boolean saveCustomer(CustomerDTO dto) throws Exception {
        if (dto.getCreatedAt() == null) dto.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        if (dto.getMembership() == null || dto.getMembership().isEmpty()) dto.setMembership("Regular");
        return customerDAO.save(toEntity(dto));
    }

    @Override
    public boolean updateCustomer(CustomerDTO dto) throws Exception {
        if (dto.getMembership() == null || dto.getMembership().isEmpty()) dto.setMembership("Regular");
        return customerDAO.update(toEntity(dto));
    }

    @Override
    public boolean deleteCustomer(long customerId) throws Exception {
        return customerDAO.delete(customerId);
    }

    @Override
    public CustomerDTO findCustomer(long customerId) throws Exception {
        Customer c = customerDAO.find(customerId);
        return c != null ? toDTO(c) : null;
    }

    @Override
    public List<CustomerDTO> getAllCustomers() throws Exception {
        List<CustomerDTO> list = new ArrayList<>();
        for (Customer c : customerDAO.findAll()) list.add(toDTO(c));
        return list;
    }

    @Override
    public BigDecimal getTotalActiveDeposits(long customerId) throws Exception {
        return customerDAO.getTotalActiveDeposits(customerId);
    }

    @Override
    public List<CustomerDTO> getCustomersWithActiveRentals() throws Exception {
        List<CustomerDTO> list = new ArrayList<>();
        for (Customer c : customerDAO.findCustomersWithActiveRentals()) list.add(toDTO(c));
        return list;
    }
}
