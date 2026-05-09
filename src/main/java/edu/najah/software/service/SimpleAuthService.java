package edu.najah.software.service;

/**
 * Simple implementation of the AuthService interface.
 * 
 * This class provides basic authentication
 * functionality using hardcoded administrator
 * credentials.
 * 
 * @author Lojain
 * @version 1.0
 */
public class SimpleAuthService implements AuthService {

    private boolean loggedIn = false;

    /**
     * Default administrator username.
     */
    private static final String ADMIN_USER = "admin";

    /**
     * Default administrator password.
     */
    private static final String ADMIN_PASS = "admin123";

    /**
     * Authenticates the administrator using
     * predefined credentials.
     * 
     * @param username administrator username
     * @param password administrator password
     * @return true if authentication succeeds,
     *         otherwise false
     */
    @Override
    public boolean login(String username, String password) {

        if (ADMIN_USER.equals(username)
                && ADMIN_PASS.equals(password)) {

            loggedIn = true;

            return true;
        }

        loggedIn = false;

        return false;
    }

    /**
     * Logs out the currently authenticated administrator.
     */
    @Override
    public void logout() {
        loggedIn = false;
    }

    /**
     * Checks whether the administrator is logged in.
     * 
     * @return true if logged in,
     *         otherwise false
     */
    @Override
    public boolean isLoggedIn() {
        return loggedIn;
    }
}