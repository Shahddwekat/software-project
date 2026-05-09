package edu.najah.software.service;

import edu.najah.software.domain.Appointment;
import edu.najah.software.domain.TimeSlot;
import edu.najah.software.domain.appointmenttype.AppointmentType;
import edu.najah.software.observer.Observer;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Interface for appointment management operations.
 * 
 * This interface defines the main services
 * related to appointment scheduling,
 * booking, cancellation, modification,
 * and observer notifications.
 * 
 * @author raana
 * @version 1.0
 */
public interface AppointmentService {

    /**
     * Returns all available time slots
     * for a specific date.
     * 
     * @param date required date
     * @return list of available time slots
     */
    List<TimeSlot> getAvailableSlots(LocalDate date);

    /**
     * Books a new appointment with a specific type.
     * 
     * @param id unique appointment ID
     * @param dateTime appointment date and time
     * @param duration appointment duration in minutes
     * @param participants number of participants
     * @param type appointment type
     * @return booked appointment
     */
    Appointment bookAppointment(String id,
                                LocalDateTime dateTime,
                                int duration,
                                int participants,
                                AppointmentType type);

    /**
     * Books a new appointment without specifying a type.
     * 
     * @param id unique appointment ID
     * @param dateTime appointment date and time
     * @param duration appointment duration in minutes
     * @param participants number of participants
     * @return booked appointment
     */
    Appointment bookAppointment(String id,
                                LocalDateTime dateTime,
                                int duration,
                                int participants);

    /**
     * Cancels an appointment.
     * 
     * @param appointmentId appointment ID
     */
    void cancelAppointment(String appointmentId);

    /**
     * Modifies the appointment date and time.
     * 
     * @param appointmentId appointment ID
     * @param newDateTime new appointment date and time
     */
    void modifyAppointment(String appointmentId,
                           LocalDateTime newDateTime);

    /**
     * Allows the administrator
     * to cancel an appointment.
     * 
     * @param appointmentId appointment ID
     */
    void adminCancelAppointment(String appointmentId);

    /**
     * Allows the administrator
     * to modify an appointment.
     * 
     * @param appointmentId appointment ID
     * @param newDateTime new appointment date and time
     */
    void adminModifyAppointment(String appointmentId,
                                LocalDateTime newDateTime);

    /**
     * Returns all stored appointments.
     * 
     * @return list of appointments
     */
    List<Appointment> getAllAppointments();

    /**
     * Adds an observer to the system.
     * 
     * @param observer observer to add
     */
    void addObserver(Observer observer);

    /**
     * Removes an observer from the system.
     * 
     * @param observer observer to remove
     */
    void removeObserver(Observer observer);
}