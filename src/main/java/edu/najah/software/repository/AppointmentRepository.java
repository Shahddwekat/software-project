package edu.najah.software.repository;

import edu.najah.software.domain.Appointment;
import edu.najah.software.domain.TimeSlot;
import java.time.LocalDate;
import java.util.List;

/**
 * This interface defines how we store and retrieve appointments
 * Any class that wants to act as storage must implement these methods
 * Right now we use in memory list but this could be swapped for a database later
 * @author Team
 * @version 1.0
 */
public interface AppointmentRepository {

    /**
     * Saves a new appointment to wherever we're storing data
     * @param appointment the appointment we want to save
     */
    void save(Appointment appointment);

    /**
     * Returns every appointment that has been saved so far
     * @return a list of all appointments
     */
    List<Appointment> getAll();

    /**
     * Returns all the time slots that are already booked on a specific day
     * We use this to figure out which slots are still free
     * @param date the day we want to check
     * @return a list of booked time slots on that day
     */
    List<TimeSlot> getBookedSlotsForDate(LocalDate date);

    /**
     * Marks a time slot as booked so no one else can take it
     * @param slot the time slot to mark as taken
     */
    void saveBookedSlot(TimeSlot slot);
}