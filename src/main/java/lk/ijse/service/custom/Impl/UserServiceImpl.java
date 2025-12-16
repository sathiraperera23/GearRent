package lk.ijse.service.custom.Impl;

import lk.ijse.dao.DAOFactory;
import lk.ijse.dao.custom.UserDAO;
import lk.ijse.dto.UserDTO;
import lk.ijse.entity.User;
import lk.ijse.service.custom.UserService;

import java.util.ArrayList;
import java.util.List;

public class UserServiceImpl implements UserService {

    private final UserDAO userDAO =
            (UserDAO) DAOFactory.getInstance().getDAO(DAOFactory.DAOTypes.USER);

    @Override
    public boolean registerUser(UserDTO dto) throws Exception {
        // Prevent duplicate usernames
        if (userDAO.findByUsername(dto.getUsername()) != null) {
            System.out.println("Username already exists.");
            return false;
        }

        return userDAO.save(new User(
                0,
                dto.getUsername(),
                dto.getPassword(),
                dto.getRole(),
                "ACTIVE"
        ));
    }

    @Override
    public UserDTO login(String username, String password) throws Exception {
        User user = userDAO.findByUsername(username);
        if (user == null) return null;

        if (!user.getPassword().equals(password)) return null;
        if (!"ACTIVE".equals(user.getStatus())) return null;

        return new UserDTO(
                user.getUserId(),
                user.getUsername(),
                null, // do not expose password
                user.getRole(),
                user.getStatus()
        );
    }

    @Override
    public boolean updateUser(UserDTO dto) throws Exception {
        return userDAO.update(new User(
                dto.getUserId(),
                dto.getUsername(),
                dto.getPassword(),
                dto.getRole(),
                dto.getStatus()
        ));
    }

    @Override
    public boolean deleteUser(long id) throws Exception {
        return userDAO.delete(id);
    }

    @Override
    public List<UserDTO> getAllUsers() throws Exception {
        List<UserDTO> list = new ArrayList<>();
        for (User u : userDAO.findAll()) {
            list.add(new UserDTO(
                    u.getUserId(),
                    u.getUsername(),
                    null,
                    u.getRole(),
                    u.getStatus()
            ));
        }
        return list;
    }
}
