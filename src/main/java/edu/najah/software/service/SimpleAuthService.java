package edu.najah.software.service;

/**
 * Simple implementation of the AuthService interface.
 * 
 * This class provides basic authentication
 * functionality using predefined
 * administrator and user credentials.
 * 
 * Supported accounts:
 * <ul>
 *     <li>Admin: admin / admin123</li>
 *     <li>User: user / user123</li>
 * </ul>
 * 
 * @author raana
 * @version 1.0
 */
public class SimpleAuthService
        implements AuthService {

    /** Indicates whether a user is logged in */
    private boolean loggedIn = false;

    /** Indicates whether the logged-in user is an admin */
    private boolean adminRole = false;

    /** Default administrator username */
    private static final String ADMIN_USER = "admin";

    /** Default administrator password */
    private static final String ADMIN_PASS = "admin123";

    /** Default regular username */
    private static final String USER_USER = "user";

    /** Default regular user password */
    private static final String USER_PASS = "user123";

    /**
     * Authenticates a user using
     * predefined credentials.
     * 
     * Assigns administrator or user role
     * depending on the provided credentials.
     * 
     * @param username entered username
     * @param password entered password
     * @return true if authentication succeeds,
     *         otherwise false
     */
    @Override
    public boolean login(String username,
                         String password) {

        if (ADMIN_USER.equals(username)
                && ADMIN_PASS.equals(password)) {

            loggedIn = true;
            adminRole = true;

            return true;
        }

        if (USER_USER.equals(username)
                && USER_PASS.equals(password)) {

            loggedIn = true;
            adminRole = false;

            return true;
        }

        loggedIn = false;
        adminRole = false;

        return false;
    }

    /**
     * Logs out the currently authenticated user
     * and clears role information.
     */
    @Override
    public void logout() {

        loggedIn = false;
        adminRole = false;
    }

    /**
     * Checks whether a user
     * is currently logged in.
     * 
     * @return true if logged in,
     *         otherwise false
     */
    @Override
    public boolean isLoggedIn() {

        return loggedIn;
    }

    /**
     * Checks whether the current
     * logged-in user is an administrator.
     * 
     * @return true if administrator,
     *         otherwise false
     */
    @Override
    public boolean isAdmin() {

        return loggedIn && adminRole;
    }
}