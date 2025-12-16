package lk.ijse.dao.custom;

import lk.ijse.dao.SuperDAO;
import lk.ijse.entity.User;

import java.util.List;

public interface UserDAO extends SuperDAO {

    boolean save(User user) throws Exception;
    boolean update(User user) throws Exception;
    boolean delete(long id) throws Exception;

    User find(long id) throws Exception;
    User findByUsername(String username) throws Exception;

    List<User> findAll() throws Exception;
}
