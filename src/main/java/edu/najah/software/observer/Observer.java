package edu.najah.software.observer;

import edu.najah.software.domain.Appointment;

/**
 * Observer interface for the Observer pattern.
 * Classes that want notifications must implement this.
 *
 * @author Team
 * @version 1.0
 */
public interface Observer {

    // Called when an appointment event happens
    void update(Appointment appointment, String message);
}