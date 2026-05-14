package edu.najah.software.repository;

import edu.najah.software.domain.Appointment;
import edu.najah.software.domain.TimeSlot;

import java.time.LocalDate;
import java.util.List;

/**
 * Repository interface for managing appointments.
 * 
 * This interface defines the operations required
 * for storing and retrieving appointment data.
 * 
 * Different storage implementations such as
 * in-memory storage or databases can implement
 * this interface.
 * 
 * @author xhahd
 * @version 1.0
 */
public interface AppointmentRepository {

    /**
     * Saves an appointment.
     * 
     * @param appointment appointment to save
     */
    void save(Appointment appointment);

    /**
     * Returns all stored appointments.
     * 
     * @return list of appointments
     */
    List<Appointment> getAll();

    /**
     * Returns all booked time slots
     * for a specific date.
     * 
     * @param date required date
     * @return list of booked time slots
     */
    List<TimeSlot> getBookedSlotsForDate(LocalDate date);

    /**
     * Saves a booked time slot.
     * 
     * @param slot booked time slot
     */
    void saveBookedSlot(TimeSlot slot);
}