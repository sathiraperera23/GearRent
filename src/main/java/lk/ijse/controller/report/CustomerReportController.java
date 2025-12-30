package lk.ijse.controller.report;

import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import lk.ijse.dto.CustomerDTO;
import lk.ijse.service.ServiceFactory;
import lk.ijse.service.custom.CustomerService;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class CustomerReportController {

    @FXML
    private TextField txtFilter;
    @FXML
    private DatePicker dpStartDate;
    @FXML
    private DatePicker dpEndDate;
    @FXML
    private TableView<CustomerDTO> tblCustomers;
    @FXML
    private TableColumn<CustomerDTO, Long> colId;
    @FXML
    private TableColumn<CustomerDTO, String> colName;
    @FXML
    private TableColumn<CustomerDTO, String> colNic;
    @FXML
    private TableColumn<CustomerDTO, String> colContact;
    @FXML
    private TableColumn<CustomerDTO, String> colEmail;
    @FXML
    private TableColumn<CustomerDTO, String> colAddress;
    @FXML
    private TableColumn<CustomerDTO, String> colMembership;
    @FXML
    private TableColumn<CustomerDTO, String> colCreatedAt;

    private final CustomerService customerService =
            (CustomerService) ServiceFactory.getInstance()
                    .getService(ServiceFactory.ServiceType.CUSTOMER);

    @FXML
    private void backToReportSelection(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(
                    getClass().getResource("/view/report_selection.fxml")
            );

            Stage stage = (Stage) ((javafx.scene.Node) event.getSource())
                    .getScene().getWindow();

            stage.setScene(new Scene(root));
            stage.setTitle("GearRent | Reports");
            stage.centerOnScreen();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void initialize() {
        colId.setCellValueFactory(cell -> new SimpleLongProperty(cell.getValue().getCustomerId()).asObject());
        colName.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getName()));
        colNic.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getNicPassport()));
        colContact.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getContactNo()));
        colEmail.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getEmail()));
        colAddress.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getAddress()));
        colMembership.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getMembership()));
        colCreatedAt.setCellValueFactory(cell -> {
            if (cell.getValue().getCreatedAt() != null) {
                String formatted = cell.getValue().getCreatedAt().toLocalDateTime()
                        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
                return new SimpleStringProperty(formatted);
            }
            return new SimpleStringProperty("");
        });

        loadCustomers();
    }

    @FXML
    private void searchCustomers() {
        loadCustomers();
    }

    private void loadCustomers() {
        try {
            String filter = txtFilter.getText();
            String start = dpStartDate.getValue() != null ? dpStartDate.getValue().toString() : null;
            String end = dpEndDate.getValue() != null ? dpEndDate.getValue().toString() : null;

            List<CustomerDTO> customers = customerService.getCustomersByFilter(filter, start, end);
            tblCustomers.getItems().setAll(customers);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
