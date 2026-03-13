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

    // Cancels a future appointment by ID
    void cancelAppointment(String appointmentId);

    // Returns all appointments in the system
    List<Appointment> getAllAppointments();

    // Subscribes an observer to receive notifications
    void addObserver(Observer observer);

    // Unsubscribes an observer from notifications
    void removeObserver(Observer observer);
}