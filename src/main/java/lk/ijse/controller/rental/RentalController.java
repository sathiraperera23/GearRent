package lk.ijse.controller.rental;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import lk.ijse.dto.RentalDTO;
import lk.ijse.service.ServiceFactory;
import lk.ijse.service.custom.RentalService;

import java.math.BigDecimal;
import java.time.LocalDate;

public class RentalController {

    /* ===================== TABLE ===================== */
    @FXML private TableView<RentalDTO> tblRental;

    @FXML private TableColumn<RentalDTO, Long> colId;
    @FXML private TableColumn<RentalDTO, Long> colCustomer;
    @FXML private TableColumn<RentalDTO, Long> colEquipment;
    @FXML private TableColumn<RentalDTO, LocalDate> colFrom;
    @FXML private TableColumn<RentalDTO, LocalDate> colTo;
    @FXML private TableColumn<RentalDTO, String> colStatus;
    @FXML private TableColumn<RentalDTO, BigDecimal> colFinalAmount;
    @FXML private TableColumn<RentalDTO, String> colPaymentStatus;

    /* ===================== FORM ===================== */
    @FXML private TextField txtRentalId;
    @FXML private TextField txtCustomerId;
    @FXML private TextField txtEquipmentId;
    @FXML private DatePicker dpFrom;
    @FXML private DatePicker dpTo;
    @FXML private TextField txtDailyPrice;
    @FXML private TextField txtDeposit;
    @FXML private ComboBox<String> cmbStatus;
    @FXML private ComboBox<String> cmbPayment;

    private RentalDTO selectedRental;

    private final RentalService rentalService = (RentalService) ServiceFactory.getInstance()
            .getService(ServiceFactory.ServiceType.RENTAL);

    @FXML
    private void backToDashboard(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/view/dashboard.fxml"));
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("GearRent | Dashboard");
            stage.centerOnScreen();
        } catch (Exception e) {
            e.printStackTrace();
            showError("Failed to load dashboard");
        }
    }

    @FXML
    public void initialize() {
        // Table columns
        colId.setCellValueFactory(new PropertyValueFactory<>("rentalId"));
        colCustomer.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        colEquipment.setCellValueFactory(new PropertyValueFactory<>("equipmentId"));
        colFrom.setCellValueFactory(new PropertyValueFactory<>("rentedFrom"));
        colTo.setCellValueFactory(new PropertyValueFactory<>("rentedTo"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        colFinalAmount.setCellValueFactory(new PropertyValueFactory<>("finalAmount"));
        colPaymentStatus.setCellValueFactory(new PropertyValueFactory<>("paymentStatus"));

        // ComboBoxes
        cmbStatus.setItems(FXCollections.observableArrayList("Open", "Closed"));
        cmbPayment.setItems(FXCollections.observableArrayList("Paid", "Partially Paid", "Unpaid"));

        tableListener();
        loadTable();
    }

    private void tableListener() {
        tblRental.getSelectionModel().selectedItemProperty()
                .addListener((obs, old, newSelection) -> {
                    if (newSelection != null) {
                        selectedRental = newSelection;
                        fillForm(newSelection);
                    }
                });
    }

    private void fillForm(RentalDTO r) {
        txtRentalId.setText(String.valueOf(r.getRentalId()));
        txtCustomerId.setText(String.valueOf(r.getCustomerId()));
        txtEquipmentId.setText(String.valueOf(r.getEquipmentId()));
        dpFrom.setValue(r.getRentedFrom());
        dpTo.setValue(r.getRentedTo());
        txtDailyPrice.setText(r.getDailyPrice().toString());
        txtDeposit.setText(r.getSecurityDeposit().toString());
        cmbStatus.setValue(r.getStatus());
        cmbPayment.setValue(r.getPaymentStatus());
    }

    private void loadTable() {
        try {
            ObservableList<RentalDTO> list = FXCollections.observableArrayList(
                    rentalService.getAllRentals()
            );
            tblRental.setItems(list);
        } catch (Exception e) {
            showError("Failed to load rentals: " + e.getMessage());
        }
    }

    @FXML
    void btnAddOnAction() {
        try {
            rentalService.saveRental(buildDTO());
            showInfo("Rental added successfully!");
            loadTable();
            clearForm();
        } catch (Exception e) {
            showError("Add failed: " + e.getMessage());
        }
    }

    @FXML
    void btnUpdateOnAction() {
        try {
            if (selectedRental == null) {
                showError("Please select a rental to update");
                return;
            }
            rentalService.updateRental(buildDTO());
            showInfo("Rental updated successfully!");
            loadTable();
        } catch (Exception e) {
            showError("Update failed: " + e.getMessage());
        }
    }

    @FXML
    void btnClearOnAction() {
        clearForm();
    }

    /* ===================== HELPERS ===================== */

    private RentalDTO buildDTO() {
        RentalDTO dto = new RentalDTO();

        // If updating, include rentalId
        if (!txtRentalId.getText().isEmpty()) {
            dto.setRentalId(Long.parseLong(txtRentalId.getText()));
        }

        dto.setCustomerId(Long.parseLong(txtCustomerId.getText()));
        dto.setEquipmentId(Long.parseLong(txtEquipmentId.getText()));
        dto.setRentedFrom(dpFrom.getValue());
        dto.setRentedTo(dpTo.getValue());
        dto.setDailyPrice(new BigDecimal(txtDailyPrice.getText()));
        dto.setSecurityDeposit(new BigDecimal(txtDeposit.getText()));
        dto.setStatus(cmbStatus.getValue());
        dto.setPaymentStatus(cmbPayment.getValue());

        return dto;
    }

    private void clearForm() {
        txtRentalId.clear();
        txtCustomerId.clear();
        txtEquipmentId.clear();
        dpFrom.setValue(null);
        dpTo.setValue(null);
        txtDailyPrice.clear();
        txtDeposit.clear();
        cmbStatus.setValue(null);
        cmbPayment.setValue(null);
        selectedRental = null;
    }

    private void showInfo(String msg) {
        new Alert(Alert.AlertType.INFORMATION, msg).show();
    }

    private void showError(String msg) {
        new Alert(Alert.AlertType.ERROR, msg).show();
    }
}