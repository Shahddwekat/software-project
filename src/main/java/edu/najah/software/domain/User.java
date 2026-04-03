package edu.najah.software.domain;

/**
 * Represents a person who uses the system to book appointments.
 * Every user has a unique ID, a name, and an email address.
 * @author Team
 * @version 1.0
 */
public class User {

    /** The unique ID that identifies this user. */
    private String userId;
    /** The full name of the user. */
    private String name;
    /** The email address we use to reach this user. */
    private String email;

    /**
     * Creates a new user with all their basic details
     * @param userId a unique ID for this user
     * @param name the full name of the user
     * @param email the email address of the user
     */
    public User(String userId, String name, String email) {
    	
        this.userId = userId;
        this.name = name;
        this.email = email;
    }

    /**
     * Returns the unique ID of this user.
     * @return the user ID
     */
    public String getUserId() { return userId; }

    /**
     * Returns the full name of this user
     * @return the user's name
     */
    public String getName() { return name; }

    /**
     * Returns the email address of this user
     * @return the user's email
     */
    public String getEmail() { return email; }

    @Override
    public String toString() {
    	
        return "User{UserId='" + userId + "', Name='" + name + "', Email='" + email + "'}";
    }
}