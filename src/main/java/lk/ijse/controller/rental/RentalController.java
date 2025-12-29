package lk.ijse.controller.rental;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import lk.ijse.dto.RentalDTO;
import lk.ijse.service.ServiceFactory;
import lk.ijse.service.custom.RentalService;

import java.math.BigDecimal;
import java.time.LocalDate;

public class RentalController {

    /* ===================== TABLE ===================== */

    @FXML
    private TableView<RentalDTO> tblRental;

    @FXML
    private TableColumn<RentalDTO, Long> colId;

    @FXML
    private TableColumn<RentalDTO, Long> colCustomer;

    @FXML
    private TableColumn<RentalDTO, Long> colEquipment;

    @FXML
    private TableColumn<RentalDTO, LocalDate> colFrom;

    @FXML
    private TableColumn<RentalDTO, LocalDate> colTo;

    @FXML
    private TableColumn<RentalDTO, String> colStatus;

    /* ===================== FIELDS ===================== */

    @FXML
    private TextField txtRentalId;

    @FXML
    private TextField txtCustomerId;

    @FXML
    private TextField txtEquipmentId;

    @FXML
    private TextField txtBranchId;

    @FXML
    private DatePicker dpFrom;

    @FXML
    private DatePicker dpTo;

    @FXML
    private DatePicker dpReturn;

    @FXML
    private TextField txtDailyPrice;

    @FXML
    private TextField txtDeposit;

    @FXML
    private TextField txtDiscount;

    @FXML
    private TextField txtDamage;

    @FXML
    private ComboBox<String> cmbPayment;

    @FXML
    private ComboBox<String> cmbStatus;

    /* ===================== SERVICE ===================== */

    private RentalService rentalService;

    /* ===================== INITIALIZE ===================== */

    @FXML
    public void initialize() {

        // IMPORTANT: service initialized here (NOT at field level)
        rentalService =
                (RentalService) ServiceFactory.getInstance()
                        .getService(ServiceFactory.ServiceType.RENTAL);

        // Table columns
        colId.setCellValueFactory(new PropertyValueFactory<>("rentalId"));
        colCustomer.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        colEquipment.setCellValueFactory(new PropertyValueFactory<>("equipmentId"));
        colFrom.setCellValueFactory(new PropertyValueFactory<>("rentedFrom"));
        colTo.setCellValueFactory(new PropertyValueFactory<>("rentedTo"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));

        cmbPayment.setItems(FXCollections.observableArrayList(
                "Paid", "Partially Paid", "Unpaid"
        ));

        cmbStatus.setItems(FXCollections.observableArrayList(
                "Active", "Returned", "Overdue"
        ));

        loadAllRentals();
        tableListener();
    }

    /* ===================== LOAD ===================== */

    private void loadAllRentals() {
        try {
            ObservableList<RentalDTO> list =
                    FXCollections.observableArrayList(
                            rentalService.getAllRentals()
                    );
            tblRental.setItems(list);
        } catch (Exception e) {
            showError(e.getMessage());
        }
    }

    private void tableListener() {
        tblRental.getSelectionModel()
                .selectedItemProperty()
                .addListener((obs, old, rental) -> {
                    if (rental != null) {
                        fillForm(rental);
                    }
                });
    }

    private void fillForm(RentalDTO r) {
        txtRentalId.setText(String.valueOf(r.getRentalId()));
        txtCustomerId.setText(String.valueOf(r.getCustomerId()));
        txtEquipmentId.setText(String.valueOf(r.getEquipmentId()));
        txtBranchId.setText(String.valueOf(r.getBranchId()));
        dpFrom.setValue(r.getRentedFrom());
        dpTo.setValue(r.getRentedTo());
        txtDailyPrice.setText(r.getDailyPrice().toString());
        txtDeposit.setText(r.getSecurityDeposit().toString());
        txtDiscount.setText(
                r.getDiscount() != null ? r.getDiscount().toString() : "0"
        );
        cmbStatus.setValue(r.getStatus());
        cmbPayment.setValue(r.getPaymentStatus());
    }

    /* ===================== BUTTON ACTIONS ===================== */

    @FXML
    void btnSaveOnAction(ActionEvent event) {
        try {
            RentalDTO dto = new RentalDTO();
            dto.setCustomerId(Long.parseLong(txtCustomerId.getText()));
            dto.setEquipmentId(Long.parseLong(txtEquipmentId.getText()));
            dto.setBranchId(Long.parseLong(txtBranchId.getText()));
            dto.setRentedFrom(dpFrom.getValue());
            dto.setRentedTo(dpTo.getValue());
            dto.setDailyPrice(new BigDecimal(txtDailyPrice.getText()));
            dto.setSecurityDeposit(new BigDecimal(txtDeposit.getText()));

            boolean saved = rentalService.saveRental(dto);
            if (saved) {
                showInfo("Rental saved successfully");
                loadAllRentals();
                clearForm();
            }

        } catch (Exception e) {
            showError(e.getMessage());
        }
    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) {
        try {
            RentalDTO dto = new RentalDTO();
            dto.setRentalId(Long.parseLong(txtRentalId.getText()));
            dto.setCustomerId(Long.parseLong(txtCustomerId.getText()));
            dto.setEquipmentId(Long.parseLong(txtEquipmentId.getText()));
            dto.setBranchId(Long.parseLong(txtBranchId.getText()));
            dto.setRentedFrom(dpFrom.getValue());
            dto.setRentedTo(dpTo.getValue());
            dto.setDailyPrice(new BigDecimal(txtDailyPrice.getText()));
            dto.setSecurityDeposit(new BigDecimal(txtDeposit.getText()));
            dto.setStatus(cmbStatus.getValue());
            dto.setPaymentStatus(cmbPayment.getValue());

            boolean updated = rentalService.updateRental(dto);
            if (updated) {
                showInfo("Rental updated");
                loadAllRentals();
            }

        } catch (Exception e) {
            showError(e.getMessage());
        }
    }

    @FXML
    void btnReturnOnAction(ActionEvent event) {
        try {
            long rentalId = Long.parseLong(txtRentalId.getText());
            LocalDate returnDate = dpReturn.getValue();

            BigDecimal damage =
                    txtDamage.getText().isEmpty()
                            ? BigDecimal.ZERO
                            : new BigDecimal(txtDamage.getText());

            boolean returned =
                    rentalService.returnRental(
                            rentalId,
                            returnDate,
                            damage,
                            "Returned via UI"
                    );

            if (returned) {
                showInfo("Rental returned successfully");
                loadAllRentals();
                clearForm();
            }

        } catch (Exception e) {
            showError(e.getMessage());
        }
    }

    /* ===================== HELPERS ===================== */

    private void clearForm() {
        txtRentalId.clear();
        txtCustomerId.clear();
        txtEquipmentId.clear();
        txtBranchId.clear();
        dpFrom.setValue(null);
        dpTo.setValue(null);
        dpReturn.setValue(null);
        txtDailyPrice.clear();
        txtDeposit.clear();
        txtDiscount.clear();
        txtDamage.clear();
        cmbStatus.setValue(null);
        cmbPayment.setValue(null);
    }

    private void showError(String msg) {
        new Alert(Alert.AlertType.ERROR, msg).show();
    }

    private void showInfo(String msg) {
        new Alert(Alert.AlertType.INFORMATION, msg).show();
    }
}
