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

    /* ===================== FORM ===================== */

    @FXML private TextField txtRentalId;
    @FXML private TextField txtCustomerId;
    @FXML private TextField txtEquipmentId;
    @FXML private DatePicker dpRentedFrom;
    @FXML private DatePicker dpExpectedReturn;
    @FXML private TextField txtDailyRate;
    @FXML private TextField txtDeposit;
    @FXML private ComboBox<String> cmbStatus;

    /* ===================== SERVICE ===================== */

    private final RentalService rentalService =
            (RentalService) ServiceFactory.getInstance()
                    .getService(ServiceFactory.ServiceType.RENTAL);

    /* ===================== INITIALIZE ===================== */

    @FXML
    public void initialize() {

        colRentalId.setCellValueFactory(new PropertyValueFactory<>("rentalId"));
        colCustomerId.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        colEquipmentId.setCellValueFactory(new PropertyValueFactory<>("equipmentId"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));

        cmbStatus.setItems(
                FXCollections.observableArrayList("Open", "Closed")
        );

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
        txtDailyRate.setText(r.getDailyPrice().toString());
        txtDeposit.setText(r.getSecurityDeposit().toString());
        cmbStatus.setValue(r.getStatus());
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
        return new RentalDTO(
                txtRentalId.getText().isEmpty() ? 0 : Long.parseLong(txtRentalId.getText()),
                Long.parseLong(txtCustomerId.getText()),
                Long.parseLong(txtEquipmentId.getText()),
                dpRentedFrom.getValue(),
                dpExpectedReturn.getValue(),
                new BigDecimal(txtDailyRate.getText()),
                new BigDecimal(txtDeposit.getText()),
                null,
                cmbStatus.getValue()
        );
    }

    private void clearForm() {
        txtRentalId.clear();
        txtCustomerId.clear();
        txtEquipmentId.clear();
        txtDailyRate.clear();
        txtDeposit.clear();
        dpRentedFrom.setValue(null);
        dpExpectedReturn.setValue(null);
        cmbStatus.getSelectionModel().clearSelection();
    }

    private void showError(String msg) {
        new Alert(Alert.AlertType.ERROR, msg).show();
    }
}
