package lk.ijse.controller.branch;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import lk.ijse.dto.BranchDTO;
import lk.ijse.service.ServiceFactory;
import lk.ijse.service.custom.BranchService;

import java.util.List;

public class BranchController {

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

    @FXML
    private TextField txtCode, txtName, txtAddress, txtContact;

    private final BranchService branchService =
            (BranchService) ServiceFactory.getInstance()
                    .getService(ServiceFactory.ServiceType.BRANCH);

    @FXML
    public void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("branchId"));
        colCode.setCellValueFactory(new PropertyValueFactory<>("code"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        colContact.setCellValueFactory(new PropertyValueFactory<>("contact"));
        colCreatedAt.setCellValueFactory(new PropertyValueFactory<>("createdAt"));

        loadAllBranches();

        // Handle row selection
        tblBranches.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                txtCode.setText(newSelection.getCode());
                txtName.setText(newSelection.getName());
                txtAddress.setText(newSelection.getAddress());
                txtContact.setText(newSelection.getContact());
            }
        });
    }

    private void loadAllBranches() {
        try {
            List<BranchDTO> branches = branchService.getAllBranches();
            ObservableList<BranchDTO> obsList = FXCollections.observableArrayList(branches);
            tblBranches.setItems(obsList);
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Failed to load branches: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void addBranch() {
        try {
            BranchDTO branch = new BranchDTO();
            branch.setCode(txtCode.getText());
            branch.setName(txtName.getText());
            branch.setAddress(txtAddress.getText());
            branch.setContact(txtContact.getText());

            boolean result = branchService.addBranch(branch);
            if (result) {
                showAlert("Success", "Branch added successfully!", Alert.AlertType.INFORMATION);
                clearFields();
                loadAllBranches();
            } else {
                showAlert("Error", "Failed to add branch.", Alert.AlertType.ERROR);
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void updateBranch() {
        BranchDTO selected = tblBranches.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Warning", "Please select a branch to update.", Alert.AlertType.WARNING);
            return;
        }
        try {
            selected.setCode(txtCode.getText());
            selected.setName(txtName.getText());
            selected.setAddress(txtAddress.getText());
            selected.setContact(txtContact.getText());

            boolean result = branchService.updateBranch(selected);
            if (result) {
                showAlert("Success", "Branch updated successfully!", Alert.AlertType.INFORMATION);
                clearFields();
                loadAllBranches();
            } else {
                showAlert("Error", "Failed to update branch.", Alert.AlertType.ERROR);
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void deleteBranch() {
        BranchDTO selected = tblBranches.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Warning", "Please select a branch to delete.", Alert.AlertType.WARNING);
            return;
        }
        try {
            boolean result = branchService.deleteBranch(selected.getBranchId());
            if (result) {
                showAlert("Success", "Branch deleted successfully!", Alert.AlertType.INFORMATION);
                clearFields();
                loadAllBranches();
            } else {
                showAlert("Error", "Failed to delete branch.", Alert.AlertType.ERROR);
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void clearFields() {
        txtCode.clear();
        txtName.clear();
        txtAddress.clear();
        txtContact.clear();
        tblBranches.getSelectionModel().clearSelection();
    }

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
