package lk.ijse.controller.customer;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.beans.property.SimpleObjectProperty;
import lk.ijse.dto.CustomerDTO;
import lk.ijse.service.ServiceFactory;
import lk.ijse.service.custom.CustomerService;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

public class CustomerController {

    /* ===================== TABLE ===================== */
    @FXML private TableView<CustomerDTO> tblCustomers;
    @FXML private TableColumn<CustomerDTO, Long> colCustomerId;
    @FXML private TableColumn<CustomerDTO, String> colName;
    @FXML private TableColumn<CustomerDTO, String> colMembership;
    @FXML private TableColumn<CustomerDTO, BigDecimal> colTotalDeposit;

    /* ===================== FORM ===================== */
    @FXML private TextField txtCustomerId;
    @FXML private TextField txtName;
    @FXML private TextField txtNIC;
    @FXML private TextField txtContact;
    @FXML private TextField txtEmail;
    @FXML private TextField txtAddress;
    @FXML private ComboBox<String> cmbMembership;
    @FXML private Label lblActiveDeposits;

    /* ===================== SERVICE ===================== */
    private final CustomerService customerService =
            (CustomerService) ServiceFactory.getInstance()
                    .getService(ServiceFactory.ServiceType.CUSTOMER);

    /* ===================== INITIALIZE ===================== */
    @FXML
    public void initialize() {
        colCustomerId.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colMembership.setCellValueFactory(new PropertyValueFactory<>("membership"));
        colTotalDeposit.setCellValueFactory(cell -> {
            try {
                BigDecimal total = customerService.getTotalActiveDeposits(cell.getValue().getCustomerId());
                return new SimpleObjectProperty<>(total);
            } catch (Exception e) {
                return new SimpleObjectProperty<>(BigDecimal.ZERO);
            }
        });

        cmbMembership.setItems(FXCollections.observableArrayList("Regular", "Silver", "Gold"));

        loadTable();
        tableListener();
    }

    /* ===================== TABLE ===================== */
    private void loadTable() {
        try {
            List<CustomerDTO> list = customerService.getAllCustomers();
            tblCustomers.setItems(FXCollections.observableArrayList(list));
        } catch (Exception e) {
            showError(e.getMessage());
        }
    }

    private void tableListener() {
        tblCustomers.getSelectionModel()
                .selectedItemProperty()
                .addListener((obs, old, c) -> {
                    if (c != null) fillForm(c);
                });
    }

    private void fillForm(CustomerDTO c) {
        txtCustomerId.setText(String.valueOf(c.getCustomerId()));
        txtName.setText(c.getName());
        txtNIC.setText(c.getNicPassport());
        txtContact.setText(c.getContactNo());
        txtEmail.setText(c.getEmail());
        txtAddress.setText(c.getAddress());
        cmbMembership.setValue(c.getMembership());

        try {
            BigDecimal total = customerService.getTotalActiveDeposits(c.getCustomerId());
            lblActiveDeposits.setText("Active Deposits: LKR " + total);
        } catch (Exception e) {
            lblActiveDeposits.setText("Active Deposits: Error");
        }
    }

    /* ===================== BUTTON ACTIONS ===================== */
    @FXML
    private void btnSaveOnAction() {
        try {
            customerService.saveCustomer(buildDTO());
            loadTable();
            clearForm();
        } catch (Exception e) {
            showError(e.getMessage());
        }
    }

    @FXML
    private void btnUpdateOnAction() {
        try {
            customerService.updateCustomer(buildDTO());
            loadTable();
        } catch (Exception e) {
            showError(e.getMessage());
        }
    }

    @FXML
    private void btnDeleteOnAction() {
        try {
            long id = Long.parseLong(txtCustomerId.getText());
            customerService.deleteCustomer(id);
            loadTable();
            clearForm();
        } catch (Exception e) {
            showError(e.getMessage());
        }
    }

    /* ===================== HELPERS ===================== */
    private CustomerDTO buildDTO() {
        long id = txtCustomerId.getText().isEmpty() ? 0 : Long.parseLong(txtCustomerId.getText());
        Timestamp createdAt = new Timestamp(System.currentTimeMillis());

        return new CustomerDTO(
                id,
                txtName.getText(),
                txtNIC.getText(),
                txtContact.getText(),
                txtEmail.getText(),
                txtAddress.getText(),
                cmbMembership.getValue(),
                createdAt
        );
    }

    private void clearForm() {
        txtCustomerId.clear();
        txtName.clear();
        txtNIC.clear();
        txtContact.clear();
        txtEmail.clear();
        txtAddress.clear();
        cmbMembership.getSelectionModel().clearSelection();
        lblActiveDeposits.setText("Active Deposits: LKR 0");
    }

    private void showError(String msg) {
        new Alert(Alert.AlertType.ERROR, msg).show();
    }
}
