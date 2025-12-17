package lk.ijse.controller.dashboard;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import lk.ijse.dto.UserDTO;

public class DashboardController {

    @FXML
    private Label lblWelcome;

    private UserDTO loggedUser;

    public void setLoggedUser(UserDTO user) {
        this.loggedUser = user;
        lblWelcome.setText(
                "Welcome " + user.getUsername() + " (" + user.getRole() + ")"
        );
    }
}
