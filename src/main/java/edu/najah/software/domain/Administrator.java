package edu.najah.software.domain;

/**
 * Represents an administrator in the system.
 * 
 * Administrators have additional permissions
 * and management capabilities compared to normal users.
 * 
 * This class extends the User class because
 * an administrator is also considered a system user.
 * 
 * @author raana
 * @version 1.0
 */
public class Administrator extends User {

    /** Unique administrator identifier */
    private String adminId;

    /** Indicates whether the administrator account is active */
    private boolean active;

    /**
     * Constructs a new Administrator object.
     * 
     * The administrator account is active by default.
     * 
     * @param userId unique user ID
     * @param name administrator full name
     * @param email administrator email address
     * @param adminId unique administrator ID
     */
    public Administrator(String userId,
                         String name,
                         String email,
                         String adminId) {

        super(userId, name, email);

        this.adminId = adminId;
        this.active = true;
    }

    /**
     * Returns the administrator ID.
     * 
     * @return administrator ID
     */
    public String getAdminId() {
        return adminId;
    }

    /**
     * Checks whether the administrator account is active.
     * 
     * @return true if active, otherwise false
     */
    public boolean isActive() {
        return active;
    }

    /**
     * Activates or deactivates the administrator account.
     * 
     * @param active account status
     */
    public void setActive(boolean active) {
        this.active = active;
    }

    /**
     * Returns a string representation
     * of the administrator object.
     * 
     * @return formatted administrator information
     */
    @Override
    public String toString() {

        return "Administrator{" +
                "adminId='" + adminId + '\'' +
                ", name='" + getName() + '\'' +
                ", active=" + active +
                '}';
    }
}