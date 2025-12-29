package lk.ijse.controller.report;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import java.io.IOException;

public class ReportSelectionController {

    @FXML
    private AnchorPane contentPane; // Injected from FXML

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

    private void loadReport(String path) {
        try {
            Pane pane = FXMLLoader.load(getClass().getResource(path));
            if (contentPane != null) {
                contentPane.getChildren().setAll(pane);
                AnchorPane.setTopAnchor(pane, 0.0);
                AnchorPane.setBottomAnchor(pane, 0.0);
                AnchorPane.setLeftAnchor(pane, 0.0);
                AnchorPane.setRightAnchor(pane, 0.0);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
