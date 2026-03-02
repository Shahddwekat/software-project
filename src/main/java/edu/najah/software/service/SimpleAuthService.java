package edu.najah.software.service;

public class SimpleAuthService implements AuthService {

    private boolean loggedIn = false;

    // Sprint 1: hardcoded admin creds (replace later if needed)
    private static final String ADMIN_USER = "admin";
    private static final String ADMIN_PASS = "admin123";

    @Override
    public boolean login(String username, String password) {
        if (ADMIN_USER.equals(username) && ADMIN_PASS.equals(password)) {
            loggedIn = true;
            return true;
        }
        loggedIn = false;
        return false;
    }

    @Override
    public void logout() {
        loggedIn = false;
    }

    @Override
    public boolean isLoggedIn() {
        return loggedIn;
    }
}