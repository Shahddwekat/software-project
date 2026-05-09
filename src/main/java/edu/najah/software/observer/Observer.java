package edu.najah.software.observer;

import edu.najah.software.domain.Appointment;

/**
 * Anyone who wants to receive appointment notifications must implement this
 * it is like subscribing to updates, when something happens everyone on the list gets the message
 * @author Team
 * @version 1.0
 */
public interface Observer {

    /**
     * Gets called automatically when an appointment event happens
     * @param appointment the appointment that triggered this notification
     * @param message what we want to tell the observer
     */
    void update(Appointment appointment, String message);
}