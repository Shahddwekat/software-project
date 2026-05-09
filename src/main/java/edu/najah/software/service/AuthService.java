package edu.najah.software.service;

/**
 * Interface for authentication operations.
 * 
 * This interface defines methods for user login,
 * logout, and authentication status checking.
 * 
 * @author Lojain
 * @version 1.0
 */
public interface AuthService {

    /**
     * Authenticates a user using username and password.
     * 
     * @param username the username of the user
     * @param password the user's password
     * @return true if login is successful,
     *         otherwise false
     */
    boolean login(String username, String password);

    /**
     * Logs out the currently authenticated user.
     */
    void logout();

    /**
     * Checks whether a user is currently logged in.
     * 
     * @return true if a user is logged in,
     *         otherwise false
     */
    boolean isLoggedIn();
}