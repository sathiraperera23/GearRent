package lk.ijse.controller.reservation;

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
import lk.ijse.dto.ReservationDTO;
import lk.ijse.service.ServiceFactory;
import lk.ijse.service.custom.ReservationService;

import java.math.BigDecimal;
import java.time.LocalDate;

public class ReservationController {

    @FXML private TextField txtReservationId;
    @FXML private TextField txtCustomerId;
    @FXML private TextField txtEquipmentId;
    @FXML private TextField txtTotalPrice;

    @FXML private DatePicker dpFrom;
    @FXML private DatePicker dpTo;

    @FXML private TableView<ReservationDTO> tblReservation;

    @FXML private TableColumn<ReservationDTO, Long> colId;
    @FXML private TableColumn<ReservationDTO, Long> colCustomer;
    @FXML private TableColumn<ReservationDTO, Long> colEquipment;
    @FXML private TableColumn<ReservationDTO, LocalDate> colFrom;
    @FXML private TableColumn<ReservationDTO, LocalDate> colTo;
    @FXML private TableColumn<ReservationDTO, BigDecimal> colTotal;
    @FXML private TableColumn<ReservationDTO, String> colStatus;

    @FXML private Button btnConfirm;
    @FXML private Button btnCancel;
    @FXML private Button btnCreateRental;

    private final ReservationService reservationService = (ReservationService) ServiceFactory.getInstance()
            .getService(ServiceFactory.ServiceType.RESERVATION);

    @FXML
    private void backToDashboard(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/view/dashboard.fxml"));
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("GearRent | Dashboard");
            stage.centerOnScreen();
        } catch (Exception e) {
            showError("Failed to load dashboard: " + e.getMessage());
        }
    }

    @FXML
    public void initialize() {
        // Simple PropertyValueFactory — perfect for DTOs
        colId.setCellValueFactory(new PropertyValueFactory<>("reservationId"));
        colCustomer.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        colEquipment.setCellValueFactory(new PropertyValueFactory<>("equipmentId"));
        colFrom.setCellValueFactory(new PropertyValueFactory<>("reservedFrom"));
        colTo.setCellValueFactory(new PropertyValueFactory<>("reservedTo"));
        colTotal.setCellValueFactory(new PropertyValueFactory<>("totalPrice"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));

        loadReservations();
        setupTableSelectionListener();
    }

    private void loadReservations() {
        try {
            ObservableList<ReservationDTO> reservations = FXCollections.observableArrayList(
                    reservationService.getAllReservations()
            );
            tblReservation.setItems(reservations);
        } catch (Exception e) {
            showError("Failed to load reservations: " + e.getMessage());
        }
    }

    private void setupTableSelectionListener() {
        tblReservation.getSelectionModel().selectedItemProperty()
                .addListener((obs, oldSelection, newSelection) -> {
                    if (newSelection != null) {
                        populateForm(newSelection);
                    } else {
                        clearForm();
                    }
                });
    }

    private void populateForm(ReservationDTO dto) {
        txtReservationId.setText(String.valueOf(dto.getReservationId()));
        txtCustomerId.setText(String.valueOf(dto.getCustomerId()));
        txtEquipmentId.setText(String.valueOf(dto.getEquipmentId()));
        dpFrom.setValue(dto.getReservedFrom());
        dpTo.setValue(dto.getReservedTo());
        txtTotalPrice.setText(dto.getTotalPrice().toString());

        // Button logic based on status
        String status = dto.getStatus();
        btnConfirm.setDisable(!"Pending".equals(status));
        btnCancel.setDisable("Cancelled".equals(status) || "Completed".equals(status));
        btnCreateRental.setDisable(!"Confirmed".equals(status));
    }

    @FXML
    private void addReservation() {
        try {
            ReservationDTO dto = new ReservationDTO();
            dto.setCustomerId(Long.parseLong(txtCustomerId.getText()));
            dto.setEquipmentId(Long.parseLong(txtEquipmentId.getText()));
            dto.setReservedFrom(dpFrom.getValue());
            dto.setReservedTo(dpTo.getValue());
            dto.setTotalPrice(new BigDecimal(txtTotalPrice.getText()));

            reservationService.saveReservation(dto);
            showInfo("Reservation added successfully!");
            loadReservations();
            clearForm();
        } catch (Exception e) {
            showError("Failed to save reservation: " + e.getMessage());
        }
    }

    @FXML
    private void updateReservation() {
        ReservationDTO selected = tblReservation.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showError("Please select a reservation to update");
            return;
        }

        try {
            ReservationDTO dto = new ReservationDTO();
            dto.setReservationId(selected.getReservationId());
            dto.setCustomerId(Long.parseLong(txtCustomerId.getText()));
            dto.setEquipmentId(Long.parseLong(txtEquipmentId.getText()));
            dto.setReservedFrom(dpFrom.getValue());
            dto.setReservedTo(dpTo.getValue());
            dto.setTotalPrice(new BigDecimal(txtTotalPrice.getText()));
            dto.setStatus(selected.getStatus()); // keep current status unless changed

            reservationService.updateReservation(dto);
            showInfo("Reservation updated successfully!");
            loadReservations();
        } catch (Exception e) {
            showError("Update failed: " + e.getMessage());
        }
    }

    @FXML
    private void deleteReservation() {
        ReservationDTO selected = tblReservation.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showError("Please select a reservation to delete");
            return;
        }

        try {
            reservationService.deleteReservation(selected.getReservationId());
            showInfo("Reservation deleted successfully!");
            loadReservations();
            clearForm();
        } catch (Exception e) {
            showError("Delete failed: " + e.getMessage());
        }
    }

    @FXML
    private void confirmReservation() {
        ReservationDTO selected = tblReservation.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showError("Please select a reservation");
            return;
        }

        try {
            reservationService.confirmReservation(selected.getReservationId());
            showInfo("Reservation confirmed!");
            loadReservations();
        } catch (Exception e) {
            showError("Confirm failed: " + e.getMessage());
        }
    }

    @FXML
    private void cancelReservation() {
        ReservationDTO selected = tblReservation.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showError("Please select a reservation");
            return;
        }

        try {
            reservationService.cancelReservation(selected.getReservationId());
            showInfo("Reservation cancelled!");
            loadReservations();
        } catch (Exception e) {
            showError("Cancel failed: " + e.getMessage());
        }
    }

    @FXML
    private void createRental() {
        ReservationDTO selected = tblReservation.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showError("Please select a Confirmed reservation");
            return;
        }

        try {
            reservationService.createRentalFromReservation(selected.getReservationId());
            showInfo("Rental created successfully from reservation!");
            loadReservations();
        } catch (Exception e) {
            showError("Failed to create rental: " + e.getMessage());
        }
    }

    private void clearForm() {
        txtReservationId.clear();
        txtCustomerId.clear();
        txtEquipmentId.clear();
        txtTotalPrice.clear();
        dpFrom.setValue(null);
        dpTo.setValue(null);
        btnConfirm.setDisable(true);
        btnCancel.setDisable(true);
        btnCreateRental.setDisable(true);
    }

    private void showInfo(String message) {
        new Alert(Alert.AlertType.INFORMATION, message).show();
    }

    private void showError(String message) {
        new Alert(Alert.AlertType.ERROR, message).show();
    }
}