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
import lk.ijse.service.custom.impl.CustomerServiceImpl;


public class CustomerController {

    /* ===================== UI ===================== */

    @FXML private TableView<CustomerDTO> tblCustomers;
    @FXML private TableColumn<CustomerDTO, Long> colId;
    @FXML private TableColumn<CustomerDTO, String> colName;
    @FXML private TableColumn<CustomerDTO, String> colNic;
    @FXML private TableColumn<CustomerDTO, String> colContact;
    @FXML private TableColumn<CustomerDTO, String> colEmail;
    @FXML private TableColumn<CustomerDTO, String> colMembership;

    @FXML private TextField txtCustomerId;
    @FXML private TextField txtName;
    @FXML private TextField txtNic;
    @FXML private TextField txtContact;
    @FXML private TextField txtEmail;
    @FXML private TextField txtAddress;
    @FXML private ComboBox<String> cmbMembership;

    /* ===================== SERVICE ===================== */
    private CustomerDTO selectedCustomer;

    private final CustomerService customerService =
            (CustomerService) ServiceFactory.getInstance()
                    .getService(ServiceFactory.ServiceType.CUSTOMER);

    /* ===================== INIT ===================== */

    @FXML
    public void initialize() {

        colId.setCellValueFactory(data -> new javafx.beans.property.SimpleLongProperty(
                data.getValue().getCustomerId()).asObject());
        colName.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(
                data.getValue().getName()));
        colNic.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(
                data.getValue().getNicPassport()));
        colContact.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(
                data.getValue().getContactNo()));
        colEmail.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(
                data.getValue().getEmail()));
        colMembership.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(
                data.getValue().getMembership()));

        cmbMembership.setItems(
                FXCollections.observableArrayList("Regular", "Silver", "Gold", "Platinum")
        );

        loadTable();
        tableListener();
    }

    /* ===================== LOAD ===================== */

    private void loadTable() {
        try {
            ObservableList<CustomerDTO> list =
                    FXCollections.observableArrayList(customerService.getAllCustomers());
            tblCustomers.setItems(list);
        } catch (Exception e) {
            showError(e.getMessage());
        }
    }

    private void tableListener() {
        tblCustomers.getSelectionModel().selectedItemProperty().addListener((obs, old, c) -> {
            if (c != null) {
                selectedCustomer = c;

                txtCustomerId.setText(String.valueOf(c.getCustomerId()));
                txtName.setText(c.getName());
                txtNic.setText(c.getNicPassport());
                txtContact.setText(c.getContactNo());
                txtEmail.setText(c.getEmail());
                txtAddress.setText(c.getAddress());
                cmbMembership.setValue(c.getMembership());
            }
        });
    }


    /* ===================== BUTTONS ===================== */

    @FXML
    void btnAddOnAction() {
        try {
            CustomerDTO dto = buildDTO();
            customerService.addCustomer(dto);
            loadTable();
            clearForm();
        } catch (Exception e) {
            showError(e.getMessage());
        }
    }

    @FXML
    void btnUpdateOnAction() {
        try {
            CustomerDTO dto = buildDTO();
            customerService.updateCustomer(dto);
            loadTable();
        } catch (Exception e) {
            showError(e.getMessage());
        }
    }

    @FXML
    void btnDeleteOnAction() {
        try {
            long id = Long.parseLong(txtCustomerId.getText());
            customerService.deleteCustomer(id);
            loadTable();
            clearForm();
        } catch (Exception e) {
            showError(e.getMessage());
        }
    }

    @FXML
    void btnClearOnAction() {
        clearForm();
    }

    /* ===================== HELPERS ===================== */

    private CustomerDTO buildDTO() {
        CustomerDTO dto = new CustomerDTO(
                Long.parseLong(txtCustomerId.getText()),
                txtName.getText(),
                txtNic.getText(),
                txtContact.getText(),
                txtEmail.getText(),
                txtAddress.getText(),
                cmbMembership.getValue()
        );

        if (selectedCustomer != null) {
            dto.setCreatedAt(selectedCustomer.getCreatedAt());
        }

        return dto;
    }


    private void clearForm() {
        txtCustomerId.clear();
        txtName.clear();
        txtNic.clear();
        txtContact.clear();
        txtEmail.clear();
        txtAddress.clear();
        cmbMembership.getSelectionModel().clearSelection();
    }

    private void showError(String msg) {
        new Alert(Alert.AlertType.ERROR, msg).show();
    }
}
