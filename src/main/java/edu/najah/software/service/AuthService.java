package edu.najah.software.service;

public interface AuthService {
    boolean login(String username, String password);
    void logout();
    boolean isLoggedIn();
}