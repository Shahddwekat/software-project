package edu.najah.software.observer;

import edu.najah.software.domain.Appointment;

/**
 * Interface for sending notifications.
 * Mocked in tests using Mockito.
 *
 * @author Team
 * @version 1.0
 */
public interface NotificationService {

    // Sends a notification message for the given appointment
    void sendNotification(Appointment appointment, String message);
}