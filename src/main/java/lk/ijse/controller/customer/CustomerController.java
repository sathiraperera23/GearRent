package lk.ijse.controller.customer;



import lk.ijse.dto.CustomerDTO;
import lk.ijse.service.ServiceFactory;
import lk.ijse.service.custom.CustomerService;

import java.util.List;
import java.util.Scanner;

public class CustomerController {

    private final CustomerService customerService =
            (CustomerService) ServiceFactory.getInstance().getService(ServiceFactory.ServiceType.CUSTOMER);

    private final Scanner scanner = new Scanner(System.in);

    // -----------------------------
    // CREATE CUSTOMER
    // -----------------------------
    public void createCustomer() {
        try {
            System.out.print("Name: ");
            String name = scanner.nextLine();

            System.out.print("NIC / Passport: ");
            String nic = scanner.nextLine();

            System.out.print("Contact Number: ");
            String contact = scanner.nextLine();

            System.out.print("Email: ");
            String email = scanner.nextLine();

            System.out.print("Address: ");
            String address = scanner.nextLine();

            System.out.print("Membership (Regular/Silver/Gold): ");
            String membership = scanner.nextLine();

            CustomerDTO dto = new CustomerDTO(
                    0L, name, nic, contact, email, address, membership
            );

            boolean success = customerService.addCustomer(dto);
            System.out.println(success ? "Customer added!" : "Failed to add customer.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // -----------------------------
    // LIST CUSTOMERS
    // -----------------------------
    public void listCustomers() {
        try {
            List<CustomerDTO> list = customerService.getAllCustomers();
            list.forEach(c -> System.out.println(
                    c.getCustomerId() + " | " + c.getName() + " | " + c.getNicPassport() +
                            " | " + c.getContactNo() + " | " + c.getEmail() +
                            " | " + c.getAddress() + " | " + c.getMembership()
            ));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // -----------------------------
    // UPDATE CUSTOMER
    // -----------------------------
    public void updateCustomer() {
        try {
            System.out.print("Customer ID to update: ");
            long id = Long.parseLong(scanner.nextLine());

            System.out.print("New Name: ");
            String name = scanner.nextLine();

            System.out.print("New Contact Number: ");
            String contact = scanner.nextLine();

            System.out.print("New Email: ");
            String email = scanner.nextLine();

            System.out.print("New Address: ");
            String address = scanner.nextLine();

            System.out.print("New Membership (Regular/Silver/Gold): ");
            String membership = scanner.nextLine();

            CustomerDTO dto = new CustomerDTO(
                    id, name, null, contact, email, address, membership
            );

            boolean success = customerService.updateCustomer(dto);
            System.out.println(success ? "Customer updated!" : "Update failed.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // -----------------------------
    // DELETE CUSTOMER
    // -----------------------------
    public void deleteCustomer() {
        try {
            System.out.print("Enter Customer ID to delete: ");
            long id = Long.parseLong(scanner.nextLine());

            boolean success = customerService.deleteCustomer(id);
            System.out.println(success ? "Customer deleted!" : "Delete failed.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // -----------------------------
    // SEARCH CUSTOMER
    // -----------------------------
    public void searchCustomer() {
        try {
            System.out.print("Enter Customer ID to search: ");
            long id = Long.parseLong(scanner.nextLine());

            CustomerDTO dto = customerService.findCustomer(id);
            if (dto != null) {
                System.out.println(
                        "Customer Found: " + dto.getCustomerId() + " | " +
                                dto.getName() + " | " + dto.getNicPassport() +
                                " | " + dto.getContactNo() + " | " + dto.getEmail() +
                                " | " + dto.getAddress() + " | " + dto.getMembership()
                );
            } else {
                System.out.println("Customer not found.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}