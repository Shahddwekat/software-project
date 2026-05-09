package edu.najah.software.service;

/**
 * Interface for authentication operations.
 * 
 * This interface defines methods for:
 * <ul>
 *     <li>User login</li>
 *     <li>User logout</li>
 *     <li>Authentication status checking</li>
 *     <li>Administrator privilege checking</li>
 * </ul>
 * 
 * Any authentication service implementation
 * must provide these operations.
 * 
 * @author raana
 * @version 1.0
 */
public interface AuthService {

    /**
     * Authenticates a user using
     * a username and password.
     * 
     * @param username entered username
     * @param password entered password
     * @return true if login succeeds,
     *         otherwise false
     */
    boolean login(String username,
                  String password);

    /**
     * Logs out the currently authenticated user.
     */
    void logout();

    /**
     * Checks whether a user
     * is currently logged in.
     * 
     * @return true if logged in,
     *         otherwise false
     */
    boolean isLoggedIn();

    /**
     * Checks whether the current user
     * has administrator privileges.
     * 
     * @return true if the user is an administrator,
     *         otherwise false
     */
    boolean isAdmin();
}