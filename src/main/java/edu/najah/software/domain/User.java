package edu.najah.software.domain;

/**
 * Represents a user in the system.
 * 
 * A user can interact with the system
 * and book appointments.
 * 
 * Each user has a unique ID,
 * a full name, and an email address.
 * 
 * @author raana
 * @version 1.0
 */
public class User {

    /** Unique identifier for the user */
    private String userId;

    /** Full name of the user */
    private String name;

    /** Email address of the user */
    private String email;

    /**
     * Constructs a new User object.
     * 
     * @param userId unique user ID
     * @param name full user name
     * @param email user email address
     */
    public User(String userId,
                String name,
                String email) {

        this.userId = userId;
        this.name = name;
        this.email = email;
    }

    /**
     * Returns the user ID.
     * 
     * @return user ID
     */
    public String getUserId() {
        return userId;
    }

    /**
     * Returns the user name.
     * 
     * @return user name
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the user email address.
     * 
     * @return user email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Returns a string representation
     * of the user object.
     * 
     * @return formatted user information
     */
    @Override
    public String toString() {

        return "User{" +
                "userId='" + userId + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}