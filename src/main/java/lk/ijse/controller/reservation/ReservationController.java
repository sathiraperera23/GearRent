package lk.ijse.controller.reservation;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import lk.ijse.dto.ReservationDTO;
import lk.ijse.service.ServiceFactory;
import lk.ijse.service.custom.ReservationService;

import java.math.BigDecimal;
import java.time.LocalDate;

public class ReservationController {

    // ===== Form =====
    @FXML private TextField txtReservationId;
    @FXML private TextField txtCustomerId;
    @FXML private TextField txtEquipmentId;
    @FXML private TextField txtTotalPrice;
    @FXML private DatePicker dpFrom;
    @FXML private DatePicker dpTo;

    // ===== Table =====
    @FXML private TableView<ReservationDTO> tblReservation;
    @FXML private TableColumn<ReservationDTO, Long> colId;
    @FXML private TableColumn<ReservationDTO, Long> colCustomer;
    @FXML private TableColumn<ReservationDTO, Long> colEquipment;
    @FXML private TableColumn<ReservationDTO, LocalDate> colFrom;
    @FXML private TableColumn<ReservationDTO, LocalDate> colTo;
    @FXML private TableColumn<ReservationDTO, BigDecimal> colTotal;
    @FXML private TableColumn<ReservationDTO, String> colStatus;

    // ===== Buttons =====
    @FXML private Button btnConfirm;
    @FXML private Button btnCancel;
    @FXML private Button btnCreateRental;

    private final ReservationService reservationService =
            (ReservationService) ServiceFactory.getInstance()
                    .getService(ServiceFactory.ServiceType.RESERVATION);

    @FXML
    public void initialize() {

        colId.setCellValueFactory(c -> new SimpleLongProperty(c.getValue().getReservationId()).asObject());
        colCustomer.setCellValueFactory(c -> new SimpleLongProperty(c.getValue().getCustomerId()).asObject());
        colEquipment.setCellValueFactory(c -> new SimpleLongProperty(c.getValue().getEquipmentId()).asObject());
        colFrom.setCellValueFactory(c -> new SimpleObjectProperty<>(c.getValue().getReservedFrom()));
        colTo.setCellValueFactory(c -> new SimpleObjectProperty<>(c.getValue().getReservedTo()));
        colTotal.setCellValueFactory(c -> new SimpleObjectProperty<>(c.getValue().getTotalPrice()));
        colStatus.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getStatus()));

        loadReservations();

        tblReservation.getSelectionModel()
                .selectedItemProperty()
                .addListener((obs, old, selected) -> populateForm(selected));
    }

    // ================= CRUD =================

    @FXML
    private void addReservation() {
        try {
            ReservationDTO dto = new ReservationDTO(
                    0,
                    Long.parseLong(txtCustomerId.getText()),
                    Long.parseLong(txtEquipmentId.getText()),
                    dpFrom.getValue(),
                    dpTo.getValue(),
                    new BigDecimal(txtTotalPrice.getText()),
                    "Pending",
                    null
            );

            reservationService.saveReservation(dto);
            loadReservations();
            clearFields();
            info("Reservation added");

        } catch (Exception e) {
            error(e.getMessage());
        }
    }

    @FXML
    private void updateReservation() {
        try {
            ReservationDTO selected = getSelected();
            if (selected == null) return;

            ReservationDTO dto = new ReservationDTO(
                    selected.getReservationId(),
                    selected.getCustomerId(),
                    selected.getEquipmentId(),
                    dpFrom.getValue(),
                    dpTo.getValue(),
                    new BigDecimal(txtTotalPrice.getText()),
                    selected.getStatus(),
                    null
            );

            reservationService.updateReservation(dto);
            loadReservations();
            clearFields();
            info("Reservation updated");

        } catch (Exception e) {
            error(e.getMessage());
        }
    }

    @FXML
    private void deleteReservation() {
        try {
            ReservationDTO selected = getSelected();
            if (selected == null) return;

            reservationService.deleteReservation(selected.getReservationId());
            loadReservations();
            clearFields();
            info("Reservation deleted");

        } catch (Exception e) {
            error(e.getMessage());
        }
    }

    // ================= BUSINESS LOGIC =================

    @FXML
    private void confirmReservation() {
        try {
            ReservationDTO r = getSelected();
            reservationService.confirmReservation(r.getReservationId());
            loadReservations();
            info("Reservation confirmed");
        } catch (Exception e) {
            error(e.getMessage());
        }
    }

    @FXML
    private void cancelReservation() {
        try {
            ReservationDTO r = getSelected();
            reservationService.cancelReservation(r.getReservationId());
            loadReservations();
            info("Reservation cancelled");
        } catch (Exception e) {
            error(e.getMessage());
        }
    }

    @FXML
    private void createRental() {
        try {
            ReservationDTO r = getSelected();
            reservationService.createRentalFromReservation(r.getReservationId());
            loadReservations();
            info("Rental created");
        } catch (Exception e) {
            error(e.getMessage());
        }
    }

    // ================= HELPERS =================

    private void populateForm(ReservationDTO r) {
        if (r == null) return;

        txtReservationId.setText(String.valueOf(r.getReservationId()));
        txtCustomerId.setText(String.valueOf(r.getCustomerId()));
        txtEquipmentId.setText(String.valueOf(r.getEquipmentId()));
        dpFrom.setValue(r.getReservedFrom());
        dpTo.setValue(r.getReservedTo());
        txtTotalPrice.setText(r.getTotalPrice().toString());

        btnConfirm.setDisable(!"Pending".equals(r.getStatus()));
        btnCancel.setDisable("Cancelled".equals(r.getStatus()) || "Completed".equals(r.getStatus()));
        btnCreateRental.setDisable(!"Confirmed".equals(r.getStatus()));
    }

    private ReservationDTO getSelected() {
        ReservationDTO r = tblReservation.getSelectionModel().getSelectedItem();
        if (r == null) {
            error("Select a reservation first");
        }
        return r;
    }

    private void loadReservations() {
        try {
            tblReservation.setItems(
                    FXCollections.observableArrayList(
                            reservationService.getAllReservations()
                    )
            );
        } catch (Exception e) {
            error(e.getMessage());
        }
    }

    private void clearFields() {
        txtReservationId.clear();
        txtCustomerId.clear();
        txtEquipmentId.clear();
        txtTotalPrice.clear();
        dpFrom.setValue(null);
        dpTo.setValue(null);
    }

    private void info(String msg) {
        new Alert(Alert.AlertType.INFORMATION, msg).show();
    }

    private void error(String msg) {
        new Alert(Alert.AlertType.ERROR, msg).show();
    }
}
