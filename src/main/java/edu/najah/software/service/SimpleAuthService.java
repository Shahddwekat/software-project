package edu.najah.software.service;
/**
 * A basic implementation of AuthService supporting two roles admin and user
 * Admin credentials: admin / admin123
 * User credentials:  user / user123
 * @author Shahd and Raana
 * @version 1.0
 */
public class SimpleAuthService implements AuthService {

    private boolean loggedIn = false;
    private boolean adminRole = false;

    private static final String ADMIN_USER = "admin";
    private static final String ADMIN_PASS = "admin123";
    private static final String USER_USER  = "user";
    private static final String USER_PASS  = "user123";

    /**
     * Checks credentials against admin and user accounts
     * Sets role accordingly
     * @param username the username being entered
     * @param password the password being entered
     * @return true if credentials matched either account false otherwise
     */
    @Override
    public boolean login(String username, String password) {
        if (ADMIN_USER.equals(username) && ADMIN_PASS.equals(password)) {
            loggedIn = true;
            adminRole = true;
            return true;
        }
        if (USER_USER.equals(username) && USER_PASS.equals(password)) {
            loggedIn = true;
            adminRole = false;
            return true;
        }
        loggedIn = false;
        adminRole = false;
        return false;
    }

    /**
     * Clears the login state and role
     */
    @Override
    public void logout() {
        loggedIn = false;
        adminRole = false;
    }

    /**
     * Tells us whether someone is currently logged in
     * @return true if logged in, false if not
     */
    @Override
    public boolean isLoggedIn() {
        return loggedIn;
    }

    /**
     * Tells us whether the current logged-in user is an admin
     * @return true if admin false if regular user or not logged in
     */
    @Override
    public boolean isAdmin() {
        return loggedIn && adminRole;
    }
}