package lk.ijse.controller.equipment;

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
import lk.ijse.dto.EquipmentDTO;
import lk.ijse.service.ServiceFactory;
import lk.ijse.service.custom.EquipmentService;

public class EquipmentController {

    /* ===================== TABLE ===================== */

    @FXML private TableView<EquipmentDTO> tblEquipment;
    @FXML private TableColumn<EquipmentDTO, Long> colId;
    @FXML private TableColumn<EquipmentDTO, String> colCode;
    @FXML private TableColumn<EquipmentDTO, String> colBrand;
    @FXML private TableColumn<EquipmentDTO, String> colModel;
    @FXML private TableColumn<EquipmentDTO, Double> colPrice;
    @FXML private TableColumn<EquipmentDTO, String> colStatus;

    /* ===================== FORM ===================== */

    @FXML private TextField txtEquipmentId;
    @FXML private TextField txtCategoryId;
    @FXML private TextField txtBranchId;
    @FXML private TextField txtCode;
    @FXML private TextField txtBrand;
    @FXML private TextField txtModel;
    @FXML private TextField txtYear;
    @FXML private TextField txtDailyPrice;
    @FXML private TextField txtDeposit;
    @FXML private ComboBox<String> cmbStatus;

    private EquipmentDTO selectedEquipment;


    private final EquipmentService equipmentService =
            (EquipmentService) ServiceFactory.getInstance()
                    .getService(ServiceFactory.ServiceType.EQUIPMENT);


    @FXML
    private void backToDashboard(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(
                    getClass().getResource("/view/dashboard.fxml")
            );

            Stage stage = (Stage) ((javafx.scene.Node) event.getSource())
                    .getScene().getWindow();

            stage.setScene(new Scene(root));
            stage.setTitle("GearRent | Dashboard");
            stage.centerOnScreen();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @FXML
    public void initialize() {

        colId.setCellValueFactory(new PropertyValueFactory<>("equipmentId"));
        colCode.setCellValueFactory(new PropertyValueFactory<>("equipmentCode"));
        colBrand.setCellValueFactory(new PropertyValueFactory<>("brand"));
        colModel.setCellValueFactory(new PropertyValueFactory<>("model"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("baseDailyPrice"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));

        cmbStatus.setItems(
                FXCollections.observableArrayList("Available", "Rented", "Maintenance")
        );

        tableListener();
        loadTable();
    }

    private void tableListener() {
        tblEquipment.getSelectionModel()
                .selectedItemProperty()
                .addListener((obs, old, e) -> {
                    if (e != null) {
                        selectedEquipment = e;
                        fillForm(e);
                    }
                });
    }

    private void fillForm(EquipmentDTO e) {
        txtEquipmentId.setText(String.valueOf(e.getEquipmentId()));
        txtCategoryId.setText(String.valueOf(e.getCategoryId()));
        txtBranchId.setText(String.valueOf(e.getBranchId()));
        txtCode.setText(e.getEquipmentCode());
        txtBrand.setText(e.getBrand());
        txtModel.setText(e.getModel());
        txtYear.setText(String.valueOf(e.getPurchaseYear()));
        txtDailyPrice.setText(String.valueOf(e.getBaseDailyPrice()));
        txtDeposit.setText(String.valueOf(e.getSecurityDeposit()));
        cmbStatus.setValue(e.getStatus());
    }

    private void loadTable() {
        try {
            ObservableList<EquipmentDTO> list =
                    FXCollections.observableArrayList(
                            equipmentService.getAllEquipment()
                    );
            tblEquipment.setItems(list);
        } catch (Exception e) {
            showError(e.getMessage());
        }
    }


    @FXML
    void btnAddOnAction() {
        try {
            equipmentService.saveEquipment(buildDTO());
            loadTable();
            clearForm();
        } catch (Exception e) {
            showError(e.getMessage());
        }
    }

    @FXML
    void btnUpdateOnAction() {
        try {
            if (selectedEquipment == null) {
                showError("Select equipment to update");
                return;
            }
            equipmentService.updateEquipment(buildDTO());
            loadTable();
        } catch (Exception e) {
            showError(e.getMessage());
        }
    }

    @FXML
    void btnDeleteOnAction() {
        try {
            long id = Long.parseLong(txtEquipmentId.getText());
            equipmentService.deleteEquipment(id);
            loadTable();
            clearForm();
        } catch (Exception e) {
            showError(e.getMessage());
        }
    }

    @FXML
    void btnClearOnAction() {
        clearForm();
    }

    /* ===================== HELPERS ===================== */

    private EquipmentDTO buildDTO() {
        return new EquipmentDTO(
                Long.parseLong(txtEquipmentId.getText()),
                Integer.parseInt(txtCategoryId.getText()),
                Integer.parseInt(txtBranchId.getText()),
                txtCode.getText(),
                txtBrand.getText(),
                txtModel.getText(),
                Integer.parseInt(txtYear.getText()),
                Double.parseDouble(txtDailyPrice.getText()),
                Double.parseDouble(txtDeposit.getText()),
                cmbStatus.getValue()
        );
    }

    private void clearForm() {
        txtEquipmentId.clear();
        txtCategoryId.clear();
        txtBranchId.clear();
        txtCode.clear();
        txtBrand.clear();
        txtModel.clear();
        txtYear.clear();
        txtDailyPrice.clear();
        txtDeposit.clear();
        cmbStatus.getSelectionModel().clearSelection();
        selectedEquipment = null;
    }

    private void showError(String msg) {
        new Alert(Alert.AlertType.ERROR, msg).show();
    }
}
