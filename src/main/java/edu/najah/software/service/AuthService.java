package edu.najah.software.service;

/**
 * Defines the login and logout behavior for the system
 * Any class that handles authentication must implement this
 * @author Team
 * @version 1.0
 */
public interface AuthService {

    /**
     * Tries to log in with the given username and pass
     * Returns true if it worked, false if the credentials are wrong
     * @param username the username being entered
     * @param password the password being entered
     * @return true if login succeeded, false otherwise
     */
    boolean login(String username, String password);

    /**
     * Logs out whoever is currently logged in
     * After this, isLoggedIn() will return false
     */
    void logout();

    /**
     * Checks if someone is currently logged in
     * @return true if logged in, false if not
     */
    boolean isLoggedIn();
}