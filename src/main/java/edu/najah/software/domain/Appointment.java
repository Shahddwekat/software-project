package edu.najah.software.domain;

import edu.najah.software.domain.appointmenttype.AppointmentType;
import java.time.LocalDateTime;

/**
 * Represents a single appointment in the system
 * holds all the details we need when it is, how long it lasts
 * how many people are coming, what type it is and whether its confirmed or cancelled
 * @author Team
 * @version 1.0
 */
public class Appointment {

    /** unique ID so we can find this appointment later */
    private String id;
    
    /** The date and time this appointment is scheduled for */
    private LocalDateTime dateTime;

    /** How long the appointment lasts, measured in minutes */
    private int duration;

    /** How many people are attending this appointment*/
    private int participants;

    /** The current state of the appointment Confirmed or Cancelled */
    private String status;

    /** The category of this appointment like URGENT or VIRTUAL. */
    private AppointmentType type;

    /**
     * Creates a new appointment with a specific type
     * The status is automatically set to Confirmed when created
     * @param id a unique ID for this appointment
     * @param dateTime when the appointment is scheduled
     * @param duration how long it lasts in minutes
     * @param participants how many people are attending
     * @param type what kind of appointment this is
     */
    public Appointment(String id, LocalDateTime dateTime, int duration, int participants, AppointmentType type) {
        this.id = id;
        this.dateTime = dateTime;
        this.duration = duration;
        this.participants = participants;
        this.type = type;
        this.status = "Confirmed";
    }

    /**
     * Creates a new appointment without specifying a type
     * Useful for basic bookings where the type doesn't matter
     * @param id a unique ID for this appointment
     * @param dateTime when the appointment is scheduled
     * @param duration how long it lasts in minutes
     * @param participants how many people are attending
     */
    public Appointment(String id, LocalDateTime dateTime, int duration, int participants) {
        this(id, dateTime, duration, participants, null);
    }

    /**
     * Returns the unique ID of this appointment
     * @return the appointment ID
     */
    public String getId() {
    	return id;
    	}

    /**
     * Returns when this appointment is scheduled.
     * @return the date and time
     */
    public LocalDateTime getDateTime() { return dateTime; }

    /**
     * return how long this appointment lasts
     * @return duration in minutes
     */
    
    public int getDuration() { return duration; }

    /**
     * Returns how many people are attending
     * @return number of participants
     */
    public int getParticipants() { return participants; }

    /**
     * Returns the current status of this appointment.
     * @return either "Confirmed" or "Cancelled"
     */
    public String getStatus() { return status; }

    /**
     * Returns the type of this appointment
     * @return the appointment type, or null if none was set
     */
    public AppointmentType getType() { return type; }

    /**
     * Updates the status of this appointment
     * Usually called when cancelling
     * @param status the new status to set
     */
    public void setStatus(String status) { this.status = status; }

    /**
     * Changes the scheduled date and time of this appointment
     * Called when a user or admin reschedules
     * @param dateTime the new date and time
     */
    
    public void setDateTime(LocalDateTime dateTime) { this.dateTime = dateTime; }

    @Override
    public String toString() {
        return "Appointment{id='" + id + "', type=" + type
                + ", dateTime=" + dateTime + ", duration=" + duration
                + ", participants=" + participants + ", status='" + status + "'}";
    }
}