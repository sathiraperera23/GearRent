package lk.ijse.controller.rental;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import lk.ijse.dto.RentalDTO;
import lk.ijse.service.ServiceFactory;
import lk.ijse.service.custom.RentalService;

import java.math.BigDecimal;
import java.util.List;

public class RentalController {

    /* ===================== TABLE ===================== */
    @FXML private TableView<RentalDTO> tblRentals;
    @FXML private TableColumn<RentalDTO, Long> colRentalId;
    @FXML private TableColumn<RentalDTO, Long> colCustomerId;
    @FXML private TableColumn<RentalDTO, Long> colEquipmentId;
    @FXML private TableColumn<RentalDTO, String> colStatus;
    @FXML private TableColumn<RentalDTO, String> colPaymentStatus;
    @FXML private TableColumn<RentalDTO, BigDecimal> colFinalAmount;

    /* ===================== FORM ===================== */
    @FXML private TextField txtRentalId;
    @FXML private TextField txtCustomerId;
    @FXML private TextField txtEquipmentId;
    @FXML private DatePicker dpRentedFrom;
    @FXML private DatePicker dpExpectedReturn;
    @FXML private DatePicker dpActualReturn;
    @FXML private TextField txtDailyRate;
    @FXML private TextField txtDeposit;
    @FXML private TextField txtDamageCharge;
    @FXML private ComboBox<String> cmbStatus;
    @FXML private ComboBox<String> cmbPaymentStatus;

    /* ===================== SERVICE ===================== */
    private final RentalService rentalService =
            (RentalService) ServiceFactory.getInstance()
                    .getService(ServiceFactory.ServiceType.RENTAL);

    /* ===================== INITIALIZE ===================== */
    @FXML
    public void initialize() {

        // Table columns
        colRentalId.setCellValueFactory(new PropertyValueFactory<>("rentalId"));
        colCustomerId.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        colEquipmentId.setCellValueFactory(new PropertyValueFactory<>("equipmentId"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        colPaymentStatus.setCellValueFactory(new PropertyValueFactory<>("paymentStatus"));
        colFinalAmount.setCellValueFactory(new PropertyValueFactory<>("finalAmount"));

        // ComboBoxes
        cmbStatus.setItems(FXCollections.observableArrayList("Open", "Closed"));
        cmbPaymentStatus.setItems(FXCollections.observableArrayList("Paid", "Partially Paid", "Unpaid"));

        loadTable();
        tableListener();
    }

    /* ===================== TABLE ===================== */
    private void loadTable() {
        try {
            List<RentalDTO> list = rentalService.getAllRentals();
            tblRentals.setItems(FXCollections.observableArrayList(list));
        } catch (Exception e) {
            showError(e.getMessage());
        }
    }

    private void tableListener() {
        tblRentals.getSelectionModel()
                .selectedItemProperty()
                .addListener((obs, old, r) -> {
                    if (r != null) fillForm(r);
                });
    }

    private void fillForm(RentalDTO r) {
        txtRentalId.setText(String.valueOf(r.getRentalId()));
        txtCustomerId.setText(String.valueOf(r.getCustomerId()));
        txtEquipmentId.setText(String.valueOf(r.getEquipmentId()));
        dpRentedFrom.setValue(r.getRentedFrom());
        dpExpectedReturn.setValue(r.getRentedTo());
        dpActualReturn.setValue(r.getActualReturn());
        txtDailyRate.setText(r.getDailyPrice().toString());
        txtDeposit.setText(r.getSecurityDeposit().toString());
        txtDamageCharge.setText(r.getDamageCharge() != null ? r.getDamageCharge().toString() : "0.00");
        cmbStatus.setValue(r.getStatus());
        cmbPaymentStatus.setValue(r.getPaymentStatus());
    }

    /* ===================== BUTTON ACTIONS ===================== */
    @FXML
    private void btnSaveOnAction() {
        try {
            rentalService.saveRental(buildDTO());
            loadTable();
            clearForm();
        } catch (Exception e) {
            showError(e.getMessage());
        }
    }

    @FXML
    private void btnUpdateOnAction() {
        try {
            rentalService.updateRental(buildDTO());
            loadTable();
        } catch (Exception e) {
            showError(e.getMessage());
        }
    }

    @FXML
    private void btnReturnOnAction() {
        try {
            long rentalId = Long.parseLong(txtRentalId.getText());
            rentalService.closeRental(rentalId);
            loadTable();
            clearForm();
        } catch (Exception e) {
            showError(e.getMessage());
        }
    }

    /* ===================== HELPERS ===================== */
    private RentalDTO buildDTO() {
        RentalDTO dto = new RentalDTO(
                txtRentalId.getText().isEmpty() ? 0 : Long.parseLong(txtRentalId.getText()),
                Long.parseLong(txtCustomerId.getText()),
                Long.parseLong(txtEquipmentId.getText()),
                dpRentedFrom.getValue(),
                dpExpectedReturn.getValue(),
                new BigDecimal(txtDailyRate.getText()),
                new BigDecimal(txtDeposit.getText()),
                null, // reservationId
                cmbStatus.getValue()
        );

        // Optional fields
        dto.setActualReturn(dpActualReturn.getValue());
        dto.setPaymentStatus(cmbPaymentStatus.getValue());
        dto.setDamageCharge(!txtDamageCharge.getText().isEmpty() ? new BigDecimal(txtDamageCharge.getText()) : BigDecimal.ZERO);

        // Compute final amount: dailyPrice * days + damageCharge
        long days = dto.getRentedFrom() != null && dto.getRentedTo() != null ?
                dto.getRentedTo().toEpochDay() - dto.getRentedFrom().toEpochDay() + 1 : 1;
        BigDecimal total = dto.getDailyPrice().multiply(BigDecimal.valueOf(days));
        BigDecimal finalAmount = total.add(dto.getDamageCharge());
        dto.setTotalAmount(total);
        dto.setFinalAmount(finalAmount);

        return dto;
    }

    private void clearForm() {
        txtRentalId.clear();
        txtCustomerId.clear();
        txtEquipmentId.clear();
        txtDailyRate.clear();
        txtDeposit.clear();
        txtDamageCharge.clear();
        dpRentedFrom.setValue(null);
        dpExpectedReturn.setValue(null);
        dpActualReturn.setValue(null);
        cmbStatus.getSelectionModel().clearSelection();
        cmbPaymentStatus.getSelectionModel().clearSelection();
    }

    private void showError(String msg) {
        new Alert(Alert.AlertType.ERROR, msg).show();
    }
}
