package lk.ijse.service.custom;

import lk.ijse.dto.UserDTO;
import lk.ijse.service.SuperService;

import java.util.List;

public interface UserService extends SuperService {

    boolean registerUser(UserDTO dto) throws Exception;
    UserDTO login(String username, String password) throws Exception;

    boolean updateUser(UserDTO dto) throws Exception;
    boolean deleteUser(long id) throws Exception;

    List<UserDTO> getAllUsers() throws Exception;
}

