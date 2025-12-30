package lk.ijse.controller.report;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import lk.ijse.dto.BranchDTO;
import lk.ijse.service.ServiceFactory;
import lk.ijse.service.custom.BranchService;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class BranchReportController {

    @FXML
    private TextField txtFilter;
    @FXML
    private DatePicker dpStartDate;
    @FXML
    private DatePicker dpEndDate;
    @FXML
    private TableView<BranchDTO> tblBranches;
    @FXML
    private TableColumn<BranchDTO, Integer> colId;
    @FXML
    private TableColumn<BranchDTO, String> colCode;
    @FXML
    private TableColumn<BranchDTO, String> colName;
    @FXML
    private TableColumn<BranchDTO, String> colAddress;
    @FXML
    private TableColumn<BranchDTO, String> colContact;
    @FXML
    private TableColumn<BranchDTO, String> colCreatedAt;

    private final BranchService branchService =
            (BranchService) ServiceFactory.getInstance().getService(ServiceFactory.ServiceType.BRANCH);

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
        // Use lambda expressions instead of PropertyValueFactory
        colId.setCellValueFactory(cell -> new SimpleIntegerProperty(cell.getValue().getBranchId()).asObject());
        colCode.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getCode()));
        colName.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getName()));
        colAddress.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getAddress()));
        colContact.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getContact()));
        colCreatedAt.setCellValueFactory(cell -> {
            if (cell.getValue().getCreatedAt() != null) {
                String formatted = cell.getValue().getCreatedAt().toLocalDateTime()
                        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
                return new SimpleStringProperty(formatted);
            }
            return new SimpleStringProperty("");
        });

        loadBranches();
    }

    @FXML
    private void searchBranches() {
        loadBranches();
    }

    private void loadBranches() {
        try {
            String filter = txtFilter.getText();
            String start = dpStartDate.getValue() != null ? dpStartDate.getValue().toString() : null;
            String end = dpEndDate.getValue() != null ? dpEndDate.getValue().toString() : null;

            List<BranchDTO> branches = branchService.getBranchesByFilter(filter, start, end);
            tblBranches.getItems().setAll(branches);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
