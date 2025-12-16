package lk.ijse.dao;


import lk.ijse.db.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class CrudUtil {

    private static PreparedStatement getStatement(String sql, Object... args) throws Exception {
        Connection connection = DBConnection.getInstance().getConnection();
        PreparedStatement statement = connection.prepareStatement(sql);

        for (int i = 0; i < args.length; i++) {
            statement.setObject(i + 1, args[i]);
        }
        return statement;
    }

    public static boolean executeUpdate(String sql, Object... args) throws Exception {
        return getStatement(sql, args).executeUpdate() > 0;
    }

    public static ResultSet executeQuery(String sql, Object... args) throws Exception {
        return getStatement(sql, args).executeQuery();
    }
}