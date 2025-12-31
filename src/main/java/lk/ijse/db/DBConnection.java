package lk.ijse.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    private static DBConnection instance;
    private Connection connection;

    private static final String URL =
//            "jdbc:mysql://localhost:3306/gearrent_pro?useSSL=false&serverTimezone=UTC";
                "jdbc:mysql://localhost:3306/GearRent?useSSL=false&serverTimezone=UTC";

    private static final String USER = "root";
    private static final String PASSWORD = "Sathira@123"; // change if needed

    private DBConnection() throws SQLException {
        connection = DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static DBConnection getInstance() throws SQLException {
        if (instance == null || instance.connection.isClosed()) {
            instance = new DBConnection();
        }
        return instance;
    }

    public Connection getConnection() {
        return connection;
    }
}
