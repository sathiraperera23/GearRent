package lk.ijse.controller.report;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;

public class ReportSelectionController {

    @FXML
    private AnchorPane contentPane; // injected from FXML

    // ================= BACK TO DASHBOARD =================

    @FXML
    private void backToDashboard(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(
                    getClass().getResource("/view/dashboard.fxml")
            );

            Stage stage = (Stage) contentPane.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("GearRent | Dashboard");
            stage.centerOnScreen();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // ================= OPEN REPORTS =================

    @FXML
    private void openBranchReport() {
        loadReport("/view/report/branch_report.fxml");
    }

    @FXML
    private void openCustomerReport() {
        loadReport("/view/report/customer_report.fxml");
    }

    @FXML
    private void openReservationReport() {
        loadReport("/view/report/reservation_report.fxml");
    }

    // ================= COMMON LOADER =================

    private void loadReport(String path) {
        try {
            Pane pane = FXMLLoader.load(getClass().getResource(path));
            contentPane.getChildren().setAll(pane);

            AnchorPane.setTopAnchor(pane, 0.0);
            AnchorPane.setBottomAnchor(pane, 0.0);
            AnchorPane.setLeftAnchor(pane, 0.0);
            AnchorPane.setRightAnchor(pane, 0.0);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
