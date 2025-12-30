package lk.ijse.controller.auth;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import lk.ijse.controller.dashboard.DashboardController;
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
                showAlert(Alert.AlertType.ERROR, "Validation Error", "Username and password cannot be empty.");
                return;
            }

            UserDTO user = userService.login(username, password);

            if (user == null) {
                showAlert(Alert.AlertType.ERROR, "Login Failed", "Invalid username or password or inactive user.");
                return;
            }

            // ✅ Load dashboard with role-based access
            Stage stage = (Stage) txtUsername.getScene().getWindow();
            loadDashboard(stage, user);

        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "System Error", "An error occurred during login.");
        }
    }

    private void loadDashboard(Stage stage, UserDTO user) throws Exception {
        // Load FXML
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/dashboard.fxml"));
        Parent root = loader.load();

        // Get controller and pass the logged-in user
        DashboardController controller = loader.getController();
        controller.setLoggedUser(user); // <-- enables/disables buttons here

        // Set scene
        stage.setScene(new Scene(root));
        stage.setTitle("GearRent | Dashboard");
        stage.centerOnScreen();
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
