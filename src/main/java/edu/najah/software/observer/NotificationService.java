package edu.najah.software.observer;

import edu.najah.software.domain.Appointment;

/**
 * Defines how notifications get sent out.
 * We keep this as an interface so we can easily swap the sending method 
 * email today, SMS tomorrow, without changing anything else
 * In tests Mockito replaces this with a fake version
 * @author Team
 * @version 1.0
 */
public interface NotificationService {

    /**
     * Sends a notification about a specific appointment
     * @param appointment the appointment this message is about
     * @param message the content of the notification
     */
    void sendNotification(Appointment appointment, String message);
}