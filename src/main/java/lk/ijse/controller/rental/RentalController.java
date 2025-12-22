package lk.ijse.controller.rental;

import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import lk.ijse.dto.RentalDTO;
import lk.ijse.service.ServiceFactory;
import lk.ijse.service.custom.RentalService;

import java.math.BigDecimal;
import java.time.LocalDate;

public class RentalController {

    /* ===================== FORM ===================== */

    @FXML private TextField txtRentalId;
    @FXML private TextField txtCustomerId;
    @FXML private TextField txtEquipmentId;
    @FXML private TextField txtReservationId;
    @FXML private TextField txtDailyPrice;
    @FXML private TextField txtDeposit;
    @FXML private ComboBox<String> cmbStatus;
    @FXML private DatePicker dpFrom;
    @FXML private DatePicker dpTo;

    /* ===================== TABLE ===================== */

    @FXML private TableView<RentalDTO> tblRental;
    @FXML private TableColumn<RentalDTO, Long> colId;
    @FXML private TableColumn<RentalDTO, Long> colCustomer;
    @FXML private TableColumn<RentalDTO, Long> colEquipment;
    @FXML private TableColumn<RentalDTO, LocalDate> colFrom;
    @FXML private TableColumn<RentalDTO, LocalDate> colTo;
    @FXML private TableColumn<RentalDTO, BigDecimal> colDaily;
    @FXML private TableColumn<RentalDTO, String> colStatus;

    private final RentalService rentalService =
            (RentalService) ServiceFactory.getInstance()
                    .getService(ServiceFactory.ServiceType.RENTAL);

    /* ===================== INIT ===================== */

    @FXML
    public void initialize() {

        cmbStatus.getItems().addAll("Open", "Closed");

        colId.setCellValueFactory(c -> new SimpleLongProperty(c.getValue().getRentalId()).asObject());
        colCustomer.setCellValueFactory(c -> new SimpleLongProperty(c.getValue().getCustomerId()).asObject());
        colEquipment.setCellValueFactory(c -> new SimpleLongProperty(c.getValue().getEquipmentId()).asObject());
        colFrom.setCellValueFactory(c -> new SimpleObjectProperty<>(c.getValue().getRentedFrom()));
        colTo.setCellValueFactory(c -> new SimpleObjectProperty<>(c.getValue().getRentedTo()));
        colDaily.setCellValueFactory(c -> new SimpleObjectProperty<>(c.getValue().getDailyPrice()));
        colStatus.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getStatus()));

        tblRental.getSelectionModel().selectedItemProperty()
                .addListener((obs, old, r) -> fillForm(r));

        loadRentals();
    }

    /* ===================== LOAD ===================== */

    private void loadRentals() {
        try {
            tblRental.setItems(
                    FXCollections.observableArrayList(
                            rentalService.getAllRentals()
                    )
            );
        } catch (Exception e) {
            showError(e.getMessage());
        }
    }

    /* ===================== FORM FILL ===================== */

    private void fillForm(RentalDTO r) {
        if (r == null) return;

        txtRentalId.setText(String.valueOf(r.getRentalId()));
        txtCustomerId.setText(String.valueOf(r.getCustomerId()));
        txtEquipmentId.setText(String.valueOf(r.getEquipmentId()));

        txtReservationId.setText(
                r.getReservationId() == 0 ? "" : String.valueOf(r.getReservationId())
        );

        dpFrom.setValue(r.getRentedFrom());
        dpTo.setValue(r.getRentedTo());
        txtDailyPrice.setText(r.getDailyPrice().toString());
        txtDeposit.setText(r.getSecurityDeposit().toString());
        cmbStatus.setValue(r.getStatus());
    }

    /* ===================== ADD ===================== */

    @FXML
    private void addRental() {
        try {
            RentalDTO dto = buildDTO(0);
            rentalService.saveRental(dto);
            loadRentals();
            clearForm();
        } catch (Exception e) {
            showError(e.getMessage());
        }
    }

    /* ===================== UPDATE ===================== */

    @FXML
    private void updateRental() {
        try {
            long id = Long.parseLong(txtRentalId.getText());
            RentalDTO dto = buildDTO(id);
            rentalService.updateRental(dto);
            loadRentals();
            clearForm();
        } catch (Exception e) {
            showError(e.getMessage());
        }
    }

    /* ===================== DELETE ===================== */

    @FXML
    private void deleteRental() {
        try {
            long id = Long.parseLong(txtRentalId.getText());
            rentalService.deleteRental(id);
            loadRentals();
            clearForm();
        } catch (Exception e) {
            showError(e.getMessage());
        }
    }

    /* ===================== CREATE FROM RESERVATION ===================== */

    @FXML
    private void createFromReservation() {
        try {
            long reservationId = Long.parseLong(txtReservationId.getText());
            rentalService.createRentalFromReservation(reservationId);
            loadRentals();
            clearForm();
        } catch (Exception e) {
            showError(e.getMessage());
        }
    }

    /* ===================== HELPERS ===================== */

    private RentalDTO buildDTO(long rentalId) {

        long reservationId = txtReservationId.getText().isEmpty()
                ? 0
                : Long.parseLong(txtReservationId.getText());

        return new RentalDTO(
                rentalId,
                Long.parseLong(txtCustomerId.getText()),
                Long.parseLong(txtEquipmentId.getText()),
                dpFrom.getValue(),
                dpTo.getValue(),
                new BigDecimal(txtDailyPrice.getText()),
                new BigDecimal(txtDeposit.getText()),
                reservationId,
                cmbStatus.getValue(),
                null
        );
    }

    private void clearForm() {
        txtRentalId.clear();
        txtCustomerId.clear();
        txtEquipmentId.clear();
        txtReservationId.clear();
        txtDailyPrice.clear();
        txtDeposit.clear();
        dpFrom.setValue(null);
        dpTo.setValue(null);
        cmbStatus.setValue(null);
    }

    private void showError(String msg) {
        new Alert(Alert.AlertType.ERROR, msg).showAndWait();
    }
}
