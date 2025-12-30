package lk.ijse.util;

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

    public static String getRole() {
        return loggedUser != null ? loggedUser.getRole() : null;
    }

    public static boolean hasRole(String role) {
        return loggedUser != null && role.equals(loggedUser.getRole());
    }

    public static void clear() {
        loggedUser = null;
    }
}
