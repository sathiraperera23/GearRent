package lk.ijse.controller.customer;

import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import lk.ijse.dto.CustomerDTO;
import lk.ijse.service.ServiceFactory;
import lk.ijse.service.custom.CustomerService;

public class CustomerController {

    @FXML
    private TextField txtCustomerId;
    @FXML
    private TextField txtName;
    @FXML
    private TextField txtNic;
    @FXML
    private TextField txtContact;
    @FXML
    private TextField txtEmail;
    @FXML
    private TextField txtAddress;
    @FXML
    private ComboBox<String> cmbMembership;
    @FXML
    private TableView<CustomerDTO> tblCustomers;
    @FXML
    private TableColumn<CustomerDTO, Long> colId;
    @FXML
    private TableColumn<CustomerDTO, String> colName;
    @FXML
    private TableColumn<CustomerDTO, String> colNic;
    @FXML
    private TableColumn<CustomerDTO, String> colContact;
    @FXML
    private TableColumn<CustomerDTO, String> colEmail;
    @FXML
    private TableColumn<CustomerDTO, String> colAddress;
    @FXML
    private TableColumn<CustomerDTO, String> colMembership;

    private final CustomerService customerService =
            (CustomerService) ServiceFactory.getInstance().getService(ServiceFactory.ServiceType.CUSTOMER);

    private final ObservableList<CustomerDTO> customerList = FXCollections.observableArrayList();

    // Make controller public so FXMLLoader can access it
    public CustomerController() {}

    @FXML
    public void initialize() {
        // Initialize membership options
        cmbMembership.getItems().addAll("Regular", "Silver", "Gold");

        // Initialize TableView columns correctly
        colId.setCellValueFactory(data -> new SimpleLongProperty(data.getValue().getCustomerId()).asObject());
        colName.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getName()));
        colNic.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getNicPassport()));
        colContact.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getContactNo()));
        colEmail.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getEmail()));
        colAddress.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getAddress()));
        colMembership.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getMembership()));

        tblCustomers.setItems(customerList);

        loadAllCustomers();
    }

    private void loadAllCustomers() {
        try {
            customerList.clear();
            customerList.addAll(customerService.getAllCustomers());
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to load customers.");
        }
    }

    @FXML
    private void addCustomer() {
        try {
            CustomerDTO dto = new CustomerDTO(
                    0,
                    txtName.getText(),
                    txtNic.getText(),
                    txtContact.getText(),
                    txtEmail.getText(),
                    txtAddress.getText(),
                    cmbMembership.getValue()
            );
            boolean success = customerService.addCustomer(dto);
            if (success) {
                showAlert(Alert.AlertType.INFORMATION, "Success", "Customer added successfully.");
                loadAllCustomers();
                clearFields();
            } else {
                showAlert(Alert.AlertType.WARNING, "Warning", "Failed to add customer.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "An error occurred while adding customer.");
        }
    }

    @FXML
    private void updateCustomer() {
        try {
            // Validate ID
            if (txtCustomerId.getText().isEmpty()) {
                showAlert(Alert.AlertType.WARNING, "Validation Error", "Please enter a Customer ID to update.");
                return;
            }

            long id;
            try {
                id = Long.parseLong(txtCustomerId.getText());
            } catch (NumberFormatException e) {
                showAlert(Alert.AlertType.WARNING, "Validation Error", "Customer ID must be a number.");
                return;
            }

            // Validate required fields
            if (txtName.getText().isEmpty() || txtNic.getText().isEmpty() ||
                    txtContact.getText().isEmpty() || cmbMembership.getValue() == null) {
                showAlert(Alert.AlertType.WARNING, "Validation Error", "Please fill all required fields.");
                return;
            }

            CustomerDTO dto = new CustomerDTO(
                    id,
                    txtName.getText(),
                    txtNic.getText(),
                    txtContact.getText(),
                    txtEmail.getText(),
                    txtAddress.getText(),
                    cmbMembership.getValue()
            );

            boolean success = customerService.updateCustomer(dto);
            if (success) {
                showAlert(Alert.AlertType.INFORMATION, "Success", "Customer updated successfully.");
                loadAllCustomers();
                clearFields();
            } else {
                showAlert(Alert.AlertType.WARNING, "Failed", "Customer not found or update failed.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "An error occurred while updating customer.");
        }
    }

    @FXML
    private void deleteCustomer() {
        try {
            // Validate ID
            if (txtCustomerId.getText().isEmpty()) {
                showAlert(Alert.AlertType.WARNING, "Validation Error", "Please enter a Customer ID to delete.");
                return;
            }

            long id;
            try {
                id = Long.parseLong(txtCustomerId.getText());
            } catch (NumberFormatException e) {
                showAlert(Alert.AlertType.WARNING, "Validation Error", "Customer ID must be a number.");
                return;
            }

            // Confirm deletion
            Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmAlert.setTitle("Confirm Deletion");
            confirmAlert.setHeaderText(null);
            confirmAlert.setContentText("Are you sure you want to delete this customer?");
            if (confirmAlert.showAndWait().orElse(ButtonType.CANCEL) != ButtonType.OK) {
                return;
            }

            boolean success = customerService.deleteCustomer(id);
            if (success) {
                showAlert(Alert.AlertType.INFORMATION, "Success", "Customer deleted successfully.");
                loadAllCustomers();
                clearFields();
            } else {
                showAlert(Alert.AlertType.WARNING, "Failed", "Customer not found or deletion failed.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "An error occurred while deleting customer.");
        }
    }


    @FXML
    private void searchCustomer() {
        try {
            long id = Long.parseLong(txtCustomerId.getText());
            CustomerDTO dto = customerService.findCustomer(id);
            if (dto != null) {
                txtName.setText(dto.getName());
                txtNic.setText(dto.getNicPassport());
                txtContact.setText(dto.getContactNo());
                txtEmail.setText(dto.getEmail());
                txtAddress.setText(dto.getAddress());
                cmbMembership.setValue(dto.getMembership());
            } else {
                showAlert(Alert.AlertType.INFORMATION, "Not Found", "Customer not found.");
                clearFields();
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "An error occurred while searching for customer.");
        }
    }

    private void clearFields() {
        txtCustomerId.clear();
        txtName.clear();
        txtNic.clear();
        txtContact.clear();
        txtEmail.clear();
        txtAddress.clear();
        cmbMembership.getSelectionModel().clearSelection();
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
