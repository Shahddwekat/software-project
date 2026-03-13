package edu.najah.software.observer;

import edu.najah.software.domain.Appointment;

/**
 * Concrete observer that forwards events to the NotificationService.
 *
 * @author Team
 * @version 1.0
 */
public class NotificationObserver implements Observer {

    private final NotificationService notificationService;

    public NotificationObserver(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    // When notified, forward the message to the notification service
    @Override
    public void update(Appointment appointment, String message) {
        notificationService.sendNotification(appointment, message);
    }
}