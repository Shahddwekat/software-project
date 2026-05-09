package edu.najah.software.domain;

import edu.najah.software.domain.appointmenttype.AppointmentType;
import java.time.LocalDateTime;

/**
 * Represents an appointment in the system.
 * 
 * An appointment stores all booking information
 * including date, duration, participants,
 * appointment type, and current status.
 * 
 * @author raana
 * @version 1.0
 */
public class Appointment {

    /** Unique appointment identifier */
    private String id;

    /** Date and time of the appointment */
    private LocalDateTime dateTime;

    /** Appointment duration in minutes */
    private int duration;

    /** Number of participants attending */
    private int participants;

    /** Current appointment status */
    private String status;

    /** Type/category of the appointment */
    private AppointmentType type;

    /**
     * Constructs a new appointment with a specific type.
     * 
     * The appointment status is automatically
     * initialized as "Confirmed".
     * 
     * @param id unique appointment ID
     * @param dateTime appointment date and time
     * @param duration appointment duration in minutes
     * @param participants number of participants
     * @param type appointment category/type
     */
    public Appointment(String id,
                       LocalDateTime dateTime,
                       int duration,
                       int participants,
                       AppointmentType type) {

        this.id = id;
        this.dateTime = dateTime;
        this.duration = duration;
        this.participants = participants;
        this.type = type;
        this.status = "Confirmed";
    }

    /**
     * Constructs a new appointment without specifying a type.
     * 
     * @param id unique appointment ID
     * @param dateTime appointment date and time
     * @param duration appointment duration in minutes
     * @param participants number of participants
     */
    public Appointment(String id,
                       LocalDateTime dateTime,
                       int duration,
                       int participants) {

        this(id, dateTime, duration, participants, null);
    }

    /**
     * Returns the appointment ID.
     * 
     * @return appointment ID
     */
    public String getId() {
        return id;
    }

    /**
     * Returns the appointment date and time.
     * 
     * @return appointment date and time
     */
    public LocalDateTime getDateTime() {
        return dateTime;
    }

    /**
     * Returns the appointment duration.
     * 
     * @return duration in minutes
     */
    public int getDuration() {
        return duration;
    }

    /**
     * Returns the number of participants.
     * 
     * @return number of participants
     */
    public int getParticipants() {
        return participants;
    }

    /**
     * Returns the current appointment status.
     * 
     * @return appointment status
     */
    public String getStatus() {
        return status;
    }

    /**
     * Returns the appointment type.
     * 
     * @return appointment type
     */
    public AppointmentType getType() {
        return type;
    }

    /**
     * Updates the appointment status.
     * 
     * @param status new appointment status
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * Updates the appointment date and time.
     * 
     * @param dateTime new appointment date and time
     */
    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    /**
     * Returns a string representation
     * of the appointment object.
     * 
     * @return formatted appointment information
     */
    @Override
    public String toString() {

        return "Appointment{id='" + id + '\'' +
                ", type=" + type +
                ", dateTime=" + dateTime +
                ", duration=" + duration +
                ", participants=" + participants +
                ", status='" + status + '\'' +
                '}';
    }
}