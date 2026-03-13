package edu.najah.software.domain;

import java.time.LocalDateTime;

/**
 * Represents an appointment in the scheduling system.
 * Holds all details about a booking including time, duration, participants and status.
 *
 * @author Team
 * @version 1.0
 */
public class Appointment {

    /** Unique identifier for this appointment. */
    private String id;

    /** The date and time of this appointment. */
    private LocalDateTime dateTime;

    /** Duration of the appointment in minutes. */
    private int duration;

    /** Number of participants in this appointment. */
    private int participants;

    /** Current status of the appointment (e.g. Confirmed, Cancelled). */
    private String status;

    /**
     * Constructs a new Appointment with status set to "Confirmed".
     *
     * @param id           unique appointment ID
     * @param dateTime     date and time of the appointment
     * @param duration     duration in minutes
     * @param participants number of participants
     */
    public Appointment(String id, LocalDateTime dateTime, int duration, int participants) {
        this.id = id;
        this.dateTime = dateTime;
        this.duration = duration;
        this.participants = participants;
        this.status = "Confirmed";
    }

    /**
     * Returns the appointment ID.
     * @return id
     */
    public String getId() { return id; }

    /**
     * Returns the date and time of the appointment.
     * @return dateTime
     */
    public LocalDateTime getDateTime() { return dateTime; }

    /**
     * Returns the duration of the appointment in minutes.
     * @return duration
     */
    public int getDuration() { return duration; }

    /**
     * Returns the number of participants.
     * @return participants
     */
    public int getParticipants() { return participants; }

    /**
     * Returns the current status of the appointment.
     * @return status
     */
    public String getStatus() { return status; }

    /**
     * Sets the status of the appointment.
     * @param status the new status (e.g. "Confirmed", "Cancelled")
     */
    public void setStatus(String status) { this.status = status; }

    /**
     * Sets the date and time of the appointment.
     * @param dateTime the new date and time
     */
    public void setDateTime(LocalDateTime dateTime) { this.dateTime = dateTime; }

    @Override
    public String toString() {
        return "Appointment{id='" + id + "', dateTime=" + dateTime
                + ", duration=" + duration + ", participants=" + participants
                + ", status='" + status + "'}";
    }
}
