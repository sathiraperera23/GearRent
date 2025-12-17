package lk.ijse.controller.reservation;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import lk.ijse.dto.ReservationDTO;
import lk.ijse.service.ServiceFactory;
import lk.ijse.service.custom.ReservationService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class ReservationController {

    @FXML
    private TableView<ReservationDTO> tblReservation;

    @FXML
    private TableColumn<ReservationDTO, Long> colId;
    @FXML
    private TableColumn<ReservationDTO, Long> colCustomer;
    @FXML
    private TableColumn<ReservationDTO, Long> colEquipment;
    @FXML
    private TableColumn<ReservationDTO, LocalDate> colFrom;
    @FXML
    private TableColumn<ReservationDTO, LocalDate> colTo;
    @FXML
    private TableColumn<ReservationDTO, BigDecimal> colTotal;
    @FXML
    private TableColumn<ReservationDTO, String> colStatus;

    private final ReservationService reservationService =
            (ReservationService) ServiceFactory.getInstance()
                    .getService(ServiceFactory.ServiceType.RESERVATION);

    @FXML
    public void initialize() {
        colId.setCellValueFactory(c -> new javafx.beans.property.SimpleLongProperty(
                c.getValue().getReservationId()).asObject());

        colCustomer.setCellValueFactory(c -> new javafx.beans.property.SimpleLongProperty(
                c.getValue().getCustomerId()).asObject());

        colEquipment.setCellValueFactory(c -> new javafx.beans.property.SimpleLongProperty(
                c.getValue().getEquipmentId()).asObject());

        colFrom.setCellValueFactory(c ->
                new javafx.beans.property.SimpleObjectProperty<>(c.getValue().getReservedFrom()));

        colTo.setCellValueFactory(c ->
                new javafx.beans.property.SimpleObjectProperty<>(c.getValue().getReservedTo()));

        colTotal.setCellValueFactory(c ->
                new javafx.beans.property.SimpleObjectProperty<>(c.getValue().getTotalPrice()));

        colStatus.setCellValueFactory(c ->
                new javafx.beans.property.SimpleStringProperty(c.getValue().getStatus()));

        loadReservations();
    }

    private void loadReservations() {
        try {
            List<ReservationDTO> list = reservationService.getAllReservations();
            tblReservation.setItems(FXCollections.observableArrayList(list));
        } catch (Exception e) {
            showError("Failed to load reservations");
            e.printStackTrace();
        }
    }

    // ---------------- ADD ----------------
    @FXML
    private void addReservation() {
        Dialog<ReservationDTO> dialog = createReservationDialog(null);
        dialog.showAndWait().ifPresent(dto -> {
            try {
                reservationService.saveReservation(dto);
                loadReservations();
            } catch (Exception e) {
                showError("Failed to add reservation");
            }
        });
    }

    // ---------------- UPDATE ----------------
    @FXML
    private void updateReservation() {
        ReservationDTO selected = tblReservation.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showWarning("Select a reservation first");
            return;
        }

        Dialog<ReservationDTO> dialog = createReservationDialog(selected);
        dialog.showAndWait().ifPresent(dto -> {
            try {
                reservationService.updateReservation(dto);
                loadReservations();
            } catch (Exception e) {
                showError("Failed to update reservation");
            }
        });
    }

    // ---------------- DELETE ----------------
    @FXML
    private void deleteReservation() {
        ReservationDTO selected = tblReservation.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showWarning("Select a reservation first");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                "Delete this reservation?", ButtonType.YES, ButtonType.NO);

        if (alert.showAndWait().orElse(ButtonType.NO) == ButtonType.YES) {
            try {
                reservationService.deleteReservation(selected.getReservationId());
                loadReservations();
            } catch (Exception e) {
                showError("Failed to delete reservation");
            }
        }
    }

    // ---------------- DIALOG ----------------
    private Dialog<ReservationDTO> createReservationDialog(ReservationDTO data) {
        Dialog<ReservationDTO> dialog = new Dialog<>();
        dialog.setTitle(data == null ? "Add Reservation" : "Update Reservation");

        ButtonType saveBtn = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveBtn, ButtonType.CANCEL);

        TextField txtCustomerId = new TextField();
        TextField txtEquipmentId = new TextField();
        DatePicker dpFrom = new DatePicker();
        DatePicker dpTo = new DatePicker();
        TextField txtTotal = new TextField();
        ComboBox<String> cmbStatus = new ComboBox<>();

        cmbStatus.getItems().addAll("Pending", "Confirmed", "Cancelled", "Completed");

        if (data != null) {
            txtCustomerId.setText(String.valueOf(data.getCustomerId()));
            txtEquipmentId.setText(String.valueOf(data.getEquipmentId()));
            dpFrom.setValue(data.getReservedFrom());
            dpTo.setValue(data.getReservedTo());
            txtTotal.setText(data.getTotalPrice().toString());
            cmbStatus.setValue(data.getStatus());
        } else {
            cmbStatus.setValue("Pending");
        }

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        grid.addRow(0, new Label("Customer ID:"), txtCustomerId);
        grid.addRow(1, new Label("Equipment ID:"), txtEquipmentId);
        grid.addRow(2, new Label("From:"), dpFrom);
        grid.addRow(3, new Label("To:"), dpTo);
        grid.addRow(4, new Label("Total Price:"), txtTotal);
        grid.addRow(5, new Label("Status:"), cmbStatus);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(btn -> {
            if (btn == saveBtn) {
                ReservationDTO dto = new ReservationDTO();
                if (data != null) dto.setReservationId(data.getReservationId());

                dto.setCustomerId(Long.parseLong(txtCustomerId.getText()));
                dto.setEquipmentId(Long.parseLong(txtEquipmentId.getText()));
                dto.setReservedFrom(dpFrom.getValue());
                dto.setReservedTo(dpTo.getValue());
                dto.setTotalPrice(new BigDecimal(txtTotal.getText()));
                dto.setStatus(cmbStatus.getValue());

                return dto;
            }
            return null;
        });

        return dialog;
    }

    private void showError(String msg) {
        new Alert(Alert.AlertType.ERROR, msg).showAndWait();
    }

    private void showWarning(String msg) {
        new Alert(Alert.AlertType.WARNING, msg).showAndWait();
    }
}
