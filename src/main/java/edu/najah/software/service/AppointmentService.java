package edu.najah.software.service;

import edu.najah.software.domain.Appointment;
import edu.najah.software.domain.TimeSlot;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Interface defining the contract for appointment management operations.
 *
 * @author Team
 * @version 1.0
 */
public interface AppointmentService {

    /**
     * Returns all available time slots for a given date.
     * Requires admin to be logged in.
     * @param date the date to check
     * @return list of available TimeSlots
     */
    List<TimeSlot> getAvailableSlots(LocalDate date);

    /**
     * Books a new appointment after validating all booking rules.
     * @param id           unique appointment ID
     * @param dateTime     date and time for the appointment
     * @param duration     duration in minutes
     * @param participants number of participants
     * @return the created Appointment
     */
    Appointment bookAppointment(String id, LocalDateTime dateTime, int duration, int participants);

    /**
     * Cancels an existing appointment by its ID.
     * Only future appointments can be cancelled.
     * @param appointmentId the ID of the appointment to cancel
     */
    void cancelAppointment(String appointmentId);

    /**
     * Returns all appointments in the system.
     * @return list of all appointments
     */
    List<Appointment> getAllAppointments();
}