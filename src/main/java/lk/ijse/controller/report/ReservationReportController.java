package lk.ijse.controller.report;

import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import lk.ijse.dto.ReservationDTO;
import lk.ijse.service.ServiceFactory;
import lk.ijse.service.custom.ReservationService;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class ReservationReportController {

    @FXML
    private TextField txtFilter;
    @FXML
    private DatePicker dpStartDate;
    @FXML
    private DatePicker dpEndDate;
    @FXML
    private TableView<ReservationDTO> tblReservations;
    @FXML
    private TableColumn<ReservationDTO, Long> colId;
    @FXML
    private TableColumn<ReservationDTO, Long> colCustomerId;
    @FXML
    private TableColumn<ReservationDTO, Long> colEquipmentId;
    @FXML
    private TableColumn<ReservationDTO, String> colFrom;
    @FXML
    private TableColumn<ReservationDTO, String> colTo;
    @FXML
    private TableColumn<ReservationDTO, String> colPrice;
    @FXML
    private TableColumn<ReservationDTO, String> colStatus;
    @FXML
    private TableColumn<ReservationDTO, String> colCreatedAt;

    private final ReservationService reservationService =
            (ReservationService) ServiceFactory.getInstance()
                    .getService(ServiceFactory.ServiceType.RESERVATION);

    @FXML
    public void initialize() {
        colId.setCellValueFactory(cell -> new SimpleLongProperty(cell.getValue().getReservationId()).asObject());
        colCustomerId.setCellValueFactory(cell -> new SimpleLongProperty(cell.getValue().getCustomerId()).asObject());
        colEquipmentId.setCellValueFactory(cell -> new SimpleLongProperty(cell.getValue().getEquipmentId()).asObject());
        colFrom.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getReservedFrom().toString()));
        colTo.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getReservedTo().toString()));
        colPrice.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getTotalPrice().toString()));
        colStatus.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getStatus()));
        colCreatedAt.setCellValueFactory(cell -> {
            if (cell.getValue().getCreatedAt() != null) {
                String formatted = cell.getValue().getCreatedAt().toLocalDateTime()
                        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
                return new SimpleStringProperty(formatted);
            }
            return new SimpleStringProperty("");
        });

        loadReservations();
    }

    @FXML
    private void searchReservations() {
        loadReservations();
    }

    private void loadReservations() {
        try {
            String filter = txtFilter.getText();

            // Get LocalDate directly from DatePicker
            var startDate = dpStartDate.getValue();
            var endDate   = dpEndDate.getValue();

            List<ReservationDTO> reservations =
                    reservationService.getReservationsByFilter(
                            filter,
                            startDate,
                            endDate
                    );

            tblReservations.getItems().setAll(reservations);

        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to load reservations").show();
        }
    }

}
