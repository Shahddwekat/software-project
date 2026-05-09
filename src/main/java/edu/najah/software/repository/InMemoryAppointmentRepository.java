package edu.najah.software.repository;

import edu.najah.software.domain.Appointment;
import edu.najah.software.domain.TimeSlot;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * In-memory implementation of the AppointmentRepository interface.
 * 
 * This class stores appointments and booked
 * time slots temporarily in memory using lists.
 * 
 * Data is not permanently saved and will be lost
 * when the application stops.
 * 
 * @author raana
 * @version 1.0
 */
public class InMemoryAppointmentRepository
        implements AppointmentRepository {

    /** List of stored appointments */
    private final List<Appointment> appointments =
            new ArrayList<>();

    /** List of booked time slots */
    private final List<TimeSlot> bookedSlots =
            new ArrayList<>();

    /**
     * Saves an appointment in memory.
     * 
     * @param appointment appointment to save
     */
    @Override
    public void save(Appointment appointment) {

        appointments.add(appointment);
    }

    /**
     * Returns all stored appointments.
     * 
     * @return list of appointments
     */
    @Override
    public List<Appointment> getAll() {

        return appointments;
    }

    /**
     * Returns all booked time slots
     * for a specific date.
     * 
     * @param date required date
     * @return list of booked slots
     */
    @Override
    public List<TimeSlot> getBookedSlotsForDate(
            LocalDate date) {

        List<TimeSlot> result = new ArrayList<>();

        for (TimeSlot slot : bookedSlots) {

            if (slot.getDate().equals(date)) {

                result.add(slot);
            }
        }

        return result;
    }

    /**
     * Saves a booked time slot.
     * 
     * @param slot time slot to save
     */
    @Override
    public void saveBookedSlot(TimeSlot slot) {

        bookedSlots.add(slot);
    }
}