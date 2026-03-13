package edu.najah.software.service;

/**
 * Simple implementation of AuthService using hardcoded admin credentials.
 * Tracks login state with a boolean flag.
 *
 * @author Team
 * @version 1.0
 */
public class SimpleAuthService implements AuthService {

    /** Tracks whether an admin is currently logged in. */
    private boolean loggedIn = false;

    /** Hardcoded admin username. */
    private static final String ADMIN_USER = "admin";

    /** Hardcoded admin password. */
    private static final String ADMIN_PASS = "admin123";

    /**
     * Logs in using the provided credentials.
     * Sets loggedIn to true only if credentials match.
     *
     * @param username the entered username
     * @param password the entered password
     * @return true if credentials are valid, false otherwise
     */
    @Override
    public boolean login(String username, String password) {
        if (ADMIN_USER.equals(username) && ADMIN_PASS.equals(password)) {
            loggedIn = true;
            return true;
        }
        loggedIn = false;
        return false;
    }

    /**
     * Logs out the current admin by clearing the login flag.
     */
    @Override
    public void logout() {
        loggedIn = false;
    }

    /**
     * Returns whether an admin is currently logged in.
     *
     * @return true if logged in, false otherwise
     */
    @Override
    public boolean isLoggedIn() {
        return loggedIn;
    }
}
