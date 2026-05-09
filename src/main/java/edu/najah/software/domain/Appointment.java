package edu.najah.software.domain;

import java.time.LocalDateTime;

/**
 * Represents an appointment in the system.
 * 
 * An appointment contains information about
 * the appointment ID, date and time, duration,
 * number of participants, and current status.
 * 
 * @author Lojain
 * @version 1.0
 */
public class Appointment {

    private String id;
    private LocalDateTime dateTime;
    private int duration;
    private int participants;
    private String status;

    /**
     * Constructs a new Appointment object.
     * 
     * The appointment status is automatically
     * set to "Confirmed".
     * 
     * @param id unique appointment ID
     * @param dateTime date and time of the appointment
     * @param duration appointment duration in minutes
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
     * Returns the duration of the appointment.
     * 
     * @return appointment duration in minutes
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
}