package edu.najah.software.observer;

import edu.najah.software.domain.Appointment;

/**
 * This is the observer that actually does something when notified
 * It receives the event and passes it to the NotificationService to handle the sending
 * This way we keep the what happened separate from how we tell people
 * @author Team
 * @version 1.0
 */
public class NotificationObserver implements Observer {

    /** The service that handles actually sending the notification */
    private final NotificationService notificationService;

    /**
     * Creates a new observer with a notification service to use
     * Passing it in like this makes it easy to swap or mock in tests
     * @param notificationService the service that will send the messages
     */
    public NotificationObserver(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    /**
     * Called when an appointment event happens
     * Just forwards everything to the notification service
     * @param appointment the appointment that triggered this
     * @param message the message to send
     */
    @Override
    public void update(Appointment appointment, String message) {
        notificationService.sendNotification(appointment, message);
    }
}