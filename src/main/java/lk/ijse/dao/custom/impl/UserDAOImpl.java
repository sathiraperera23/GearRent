package lk.ijse.dao.custom.impl;

import lk.ijse.dao.CrudUtil;
import lk.ijse.dao.custom.UserDAO;
import lk.ijse.entity.User;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class UserDAOImpl implements UserDAO {

    @Override
    public boolean save(User u) throws Exception {
        String sql = "INSERT INTO users (username, password, role, status) VALUES (?, ?, ?, ?)";
        return CrudUtil.executeUpdate(
                sql,
                u.getUsername(),
                u.getPassword(),
                u.getRole(),
                u.getStatus()
        );
    }

    @Override
    public boolean update(User u) throws Exception {
        String sql = "UPDATE users SET username=?, password=?, role=?, status=? WHERE user_id=?";
        return CrudUtil.executeUpdate(
                sql,
                u.getUsername(),
                u.getPassword(),
                u.getRole(),
                u.getStatus(),
                u.getUserId()
        );
    }

    @Override
    public boolean delete(long id) throws Exception {
        return CrudUtil.executeUpdate(
                "DELETE FROM users WHERE user_id=?",
                id
        );
    }

    @Override
    public User find(long id) throws Exception {
        ResultSet rs = CrudUtil.executeQuery(
                "SELECT * FROM users WHERE user_id=?",
                id
        );
        if (rs.next()) {
            return new User(
                    rs.getLong("user_id"),
                    rs.getString("username"),
                    rs.getString("password"),
                    rs.getString("role"),
                    rs.getString("status")
            );
        }
        return null;
    }

    @Override
    public User findByUsername(String username) throws Exception {
        ResultSet rs = CrudUtil.executeQuery(
                "SELECT * FROM users WHERE username=?",
                username
        );
        if (rs.next()) {
            return new User(
                    rs.getLong("user_id"),
                    rs.getString("username"),
                    rs.getString("password"),
                    rs.getString("role"),
                    rs.getString("status")
            );
        }
        return null;
    }

    @Override
    public List<User> findAll() throws Exception {
        List<User> list = new ArrayList<>();
        ResultSet rs = CrudUtil.executeQuery("SELECT * FROM users");
        while (rs.next()) {
            list.add(new User(
                    rs.getLong("user_id"),
                    rs.getString("username"),
                    rs.getString("password"),
                    rs.getString("role"),
                    rs.getString("status")
            ));
        }
        return list;
    }
}
