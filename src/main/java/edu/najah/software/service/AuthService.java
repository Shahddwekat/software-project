package edu.najah.software.service;

/**
 * Interface defining authentication operations for the system.
 *
 * @author Team
 * @version 1.0
 */
public interface AuthService {

    /**
     * Attempts to log in with the given credentials.
     *
     * @param username the admin username
     * @param password the admin password
     * @return true if login is successful, false otherwise
     */
    boolean login(String username, String password);

    /**
     * Logs out the currently logged-in administrator.
     */
    void logout();

    /**
     * Checks whether an administrator is currently logged in.
     *
     * @return true if logged in, false otherwise
     */
    boolean isLoggedIn();
}
