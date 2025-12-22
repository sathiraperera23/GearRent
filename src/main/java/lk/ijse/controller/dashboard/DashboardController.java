package lk.ijse.controller.dashboard;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import lk.ijse.dto.UserDTO;

import java.io.IOException;
import java.net.URL;

public class DashboardController {

    @FXML
    private Label lblWelcome;

    private UserDTO loggedUser;

    /* ===================== LOGIN DATA ===================== */

    public void setLoggedUser(UserDTO user) {
        this.loggedUser = user;
        lblWelcome.setText("Welcome, " + user.getUsername());
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
    public void openReports(ActionEvent event) {
        loadView("/view/report.fxml", event);
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
