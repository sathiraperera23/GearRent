//module lk.ijse {
//    requires javafx.controls;
//    requires javafx.fxml;
//    requires java.sql;
//
//    opens lk.ijse to javafx.fxml;
//    exports lk.ijse;
//}

module lk.ijse {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    // FXML needs reflection access
    opens lk.ijse.controller to javafx.fxml;
    opens lk.ijse.controller.auth to javafx.fxml;
    opens lk.ijse.controller.reservation to javafx.fxml;
    opens lk.ijse.controller.rental to javafx.fxml;

    exports lk.ijse;
}
