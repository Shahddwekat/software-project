package edu.najah.software.domain;

/**
 * Represents a regular user of the appointment scheduling system.
 *
 * @author Team
 * @version 1.0
 */
public class User {

    /** Unique identifier for the user. */
    private String userId;

    /** Full name of the user. */
    private String name;

    /** Email address of the user. */
    private String email;

    /**
     * Constructs a new User with the given details.
     *
     * @param userId unique ID for this user
     * @param name   full name of the user
     * @param email  email address of the user
     */
    public User(String userId, String name, String email) {
        this.userId = userId;
        this.name = name;
        this.email = email;
    }

    /**
     * Returns the user's unique ID.
     * @return userId
     */
    public String getUserId() { return userId; }

    /**
     * Returns the user's full name.
     * @return name
     */
    public String getName() { return name; }

    /**
     * Returns the user's email address.
     * @return email
     */
    public String getEmail() { return email; }

    @Override
    public String toString() {
        return "User{userId='" + userId + "', name='" + name + "', email='" + email + "'}";
    }
}
