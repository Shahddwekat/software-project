package edu.najah.software.domain;

/**
 * An admin who has extra powers in the system
 * Admins can manage any appointment not just their own
 * This class extends User because an admin is still a user at the end of the day
 * @author shahd and raana
 * @version 1.0
 */
public class Administrator extends User {

    /** A separate ID just for admin purposes */
    private String adminId;
    /** Tracks whether this admin account is currently active */
    private boolean active;
    /**
     * Creates a new admin account with the given details.
     * The account starts as active by default.
     * @param userId the user ID inherited from User
     * @param name the full name of the admin
     * @param email the admin's email address
     * @param adminId a unique ID specific to this admin
     */
    public Administrator(String userId, String name, String email, String adminId) {
        super(userId, name, email);
        this.adminId = adminId;
        this.active = true;
    }

    /**
     * Returns the admin-specific ID
     *
     * @return the admin ID
     */
    public String getAdminId() { return adminId; }

    /**
     * Tells us whether this admin account is currently active
     * @return true if active, false if deactivated
     */
    public boolean isActive() { return active; }

    /**
     * Activate or deactivates this admin account
     * @param active pass true to activate false to deactivate
     */
    public void setActive(boolean active) { this.active = active; }

    @Override
    public String toString() {
        return "Administrator{adminId='" + adminId + "', name='" + getName() + "', active=" + active + "}";
    }
}