package edu.najah.software.domain;

/**
 * Represents an administrator who can manage the appointment scheduling system.
 * Extends User with additional admin privileges.
 *
 * @author Team
 * @version 1.0
 */
public class Administrator extends User {

    /** Unique identifier for the administrator. */
    private String adminId;

    /** Indicates whether the administrator has active privileges. */
    private boolean active;

    /**
     * Constructs a new Administrator.
     *
     * @param userId  unique user ID
     * @param name    full name
     * @param email   email address
     * @param adminId unique admin ID
     */
    public Administrator(String userId, String name, String email, String adminId) {
        super(userId, name, email);
        this.adminId = adminId;
        this.active = true;
    }

    /**
     * Returns the administrator's unique admin ID.
     * @return adminId
     */
    public String getAdminId() { return adminId; }

    /**
     * Returns whether this administrator is active.
     * @return true if active, false otherwise
     */
    public boolean isActive() { return active; }

    /**
     * Sets the active status of this administrator.
     * @param active the new active status
     */
    public void setActive(boolean active) { this.active = active; }

    @Override
    public String toString() {
        return "Administrator{adminId='" + adminId + "', name='" + getName() + "', active=" + active + "}";
    }
}
