package lk.ijse.Session;

import lk.ijse.dto.UserDTO;

public class Session {

    private static UserDTO loggedUser;

    private Session() {}

    public static void setUser(UserDTO user) {
        loggedUser = user;
    }

    public static UserDTO getUser() {
        return loggedUser;
    }

    public static void clear() {
        loggedUser = null;
    }
}
