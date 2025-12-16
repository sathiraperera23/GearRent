package lk.ijse.controller.auth;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import lk.ijse.dto.UserDTO;
import lk.ijse.service.ServiceFactory;
import lk.ijse.service.custom.UserService;

public class LoginController {

    @FXML
    private TextField txtUsername;

    @FXML
    private PasswordField txtPassword;

    private final UserService userService =
            (UserService) ServiceFactory.getInstance()
                    .getService(ServiceFactory.ServiceType.USER);

    @FXML
    private void handleLogin() {
        try {
            String username = txtUsername.getText().trim();
            String password = txtPassword.getText().trim();

            if (username.isEmpty() || password.isEmpty()) {
                showAlert("Error", "Username and password cannot be empty.");
                return;
            }

            UserDTO user = userService.login(username, password); // call backend login

            if (user != null) {
                // Successful login
                showAlert("Success", "Welcome " + user.getUsername() + "!");

                // TODO: load the main dashboard
                Stage stage = (Stage) txtUsername.getScene().getWindow();
                // Replace with code to switch scene to dashboard
                // e.g., DashboardUI.load(stage);
            } else {
                showAlert("Login Failed", "Invalid username or password.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "An error occurred during login.");
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(
                title.equals("Success")
                        ? Alert.AlertType.INFORMATION
                        : Alert.AlertType.ERROR
        );
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
