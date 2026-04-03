package edu.najah.software.service;

/**
 * A basic implementation of AuthService that uses hardcoded credentials
 * We just check if the username and password match what we have stored
 * Good enough for now — in a real system this would check a database
 * @author Team
 * @version 1.0
 */
public class SimpleAuthService implements AuthService {

    /** track ifsomeone is currently logged in*/
    private boolean loggedIn = false;

    /** The only valid username in the system right now */
    private static final String ADMIN_USER = "admin";

    /** The only valid password in the system right now */
    private static final String ADMIN_PASS = "admin123";

    /**
     * Checks the given credentials against our hardcoded values
     * If they match, we set the logged-in flag to true
     * @param username the username being entered
     * @param password the password being entered
     * @return true if the credentials are correct, false otherwise
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
     * Clears the login state so the session is closed.
     */
    @Override
    public void logout() {
        loggedIn = false;
    }

    /**
     * Tells us whether someone is currently logged in
     * @return true if logged in, false if not
     */
    @Override
    public boolean isLoggedIn() {
        return loggedIn;
    }
}