package lk.ijse.controller.dashboard;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import lk.ijse.dto.UserDTO;

import java.io.IOException;
import java.net.URL;

public class DashboardController {

    /* ===================== UI ===================== */
    @FXML
    private Label lblWelcome;

    @FXML
    private Button btnCustomers;
    @FXML
    private Button btnEquipment;
    @FXML
    private Button btnReservations;
    @FXML
    private Button btnRentals;
    @FXML
    private Button btnReports;
    @FXML
    private Button btnLogout;

    private UserDTO loggedUser;

    /* ===================== LOGIN DATA ===================== */
    public void setLoggedUser(UserDTO user) {
        this.loggedUser = user;
        lblWelcome.setText("Welcome, " + user.getUsername());

        // Apply role-based access
        applyRoleAccess();
    }

    /* ===================== ROLE-BASED ACCESS ===================== */
    private void applyRoleAccess() {
        if (loggedUser == null) return;

        // Enable all buttons first
        btnCustomers.setDisable(false);
        btnEquipment.setDisable(false);
        btnReservations.setDisable(false);
        btnRentals.setDisable(false);
        btnReports.setDisable(false);

        String role = loggedUser.getRole(); // ADMIN / BRANCH_MANAGER / STAFF
        System.out.println("Logged role = " + role);

        switch (role) {
            case "ADMIN":
                // Admin has full access
                break;

            case "BRANCH_MANAGER":
                // Branch Manager: limited access (cannot see system-wide reports)
                btnReports.setDisable(true);
                break;

            case "STAFF":
                // Staff: limited access
                btnReports.setDisable(true);    // cannot access reports
                btnCustomers.setDisable(true);  // cannot manage users
                break;

            default:
                System.out.println("Unknown role: " + role);
        }
    }

    /* ===================== BUTTON ACTIONS ===================== */
    @FXML
    public void openCustomers(ActionEvent event) {
        loadView("/view/customer.fxml", event);
    }

    @FXML
    public void openEquipment(ActionEvent event) {
        loadView("/view/equipment.fxml", event);
    }

    @FXML
    public void openReservations(ActionEvent event) {
        loadView("/view/reservation.fxml", event);
    }

    @FXML
    public void openRentals(ActionEvent event) {
        loadView("/view/rental.fxml", event);
    }

    @FXML
    public void openOverdueRentals(ActionEvent event) {
        loadView("/view/overdueRentals.fxml", event);
    }


    @FXML
    public void openReports(ActionEvent event) {
        loadView("/view/report/report_selection.fxml", event);
    }

    @FXML
    public void openBranches(ActionEvent event) {
        loadView("/view/branch.fxml", event);
    }

    @FXML
    public void logout(ActionEvent event) {
        loadView("/view/login.fxml", event);
    }

    /* ===================== VIEW LOADER ===================== */
    private void loadView(String path, ActionEvent event) {
        try {
            URL resource = getClass().getResource(path);
            if (resource == null) {
                throw new RuntimeException("FXML NOT FOUND: " + path);
            }

            Parent root = FXMLLoader.load(resource);

            Stage stage = (Stage) ((javafx.scene.Node) event.getSource())
                    .getScene().getWindow();

            stage.setScene(new Scene(root));
            stage.centerOnScreen();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
