package edu.najah.software.repository;

import edu.najah.software.domain.Appointment;
import edu.najah.software.domain.TimeSlot;

import java.time.LocalDate;
import java.util.List;

/**
 * Interface defining the contract for appointment storage operations.
 *
 * @author Team
 * @version 1.0
 */
public interface AppointmentRepository {

    /**
     * Saves an appointment to the repository.
     * @param appointment the appointment to save
     */
    void save(Appointment appointment);

    /**
     * Returns all saved appointments.
     * @return list of all appointments
     */
    List<Appointment> getAll();

    /**
     * Returns all booked time slots for a given date.
     * @param date the date to check
     * @return list of booked TimeSlots on that date
     */
    List<TimeSlot> getBookedSlotsForDate(LocalDate date);

    /**
     * Saves a booked time slot.
     * @param slot the TimeSlot to mark as booked
     */
    void saveBookedSlot(TimeSlot slot);
}