package edu.najah.software.service;

import edu.najah.software.domain.Appointment;
import edu.najah.software.domain.TimeSlot;
import edu.najah.software.observer.Observer;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Main service interface for managing appointments.
 *
 * @author Team
 * @version 1.0
 */
public interface AppointmentService {

    // Returns available slots for a given date (admin must be logged in)
    List<TimeSlot> getAvailableSlots(LocalDate date);

    // Books a new appointment after validating all rules
    Appointment bookAppointment(String id, LocalDateTime dateTime, int duration, int participants);

    // --- User actions (US4.1) ---

    // Cancels the user's own future appointment
    void cancelAppointment(String appointmentId);

    // Modifies the date/time of the user's own future appointment
    void modifyAppointment(String appointmentId, LocalDateTime newDateTime);

    // --- Admin actions (US4.2) ---

    // Admin cancels any appointment — requires admin to be logged in
    void adminCancelAppointment(String appointmentId);

    // Admin modifies any appointment — requires admin to be logged in
    void adminModifyAppointment(String appointmentId, LocalDateTime newDateTime);

    // Returns all appointments in the system
    List<Appointment> getAllAppointments();

    // Subscribes an observer to receive notifications
    void addObserver(Observer observer);

    // Unsubscribes an observer from notifications
    void removeObserver(Observer observer);
}