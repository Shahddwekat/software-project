package edu.najah.software.repository;

import edu.najah.software.domain.Appointment;
import edu.najah.software.domain.TimeSlot;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Stores all appointments and booked slots in simple lists while the app is running
 * Everything is lost when the app stops, thats fine for now since we're not using a database yet
 * @author Team
 * @version 1.0
 */
public class InMemoryAppointmentRepository implements AppointmentRepository {

    /** The list where we keep all saved appointments */
    private final List<Appointment> appointments = new ArrayList<>();

    /** The list where we keep track of which time slots are taken */
    private final List<TimeSlot> bookedSlots = new ArrayList<>();
    /**
     * Adds an appointment to our in-memory list
     * @param appointment the appointment to save
     */
    @Override
    public void save(Appointment appointment) {
        appointments.add(appointment);
    }

    /**
     * Returns all the appointments we have saved so far
     * @return the full list of appointments
     */
    @Override
    public List<Appointment> getAll() {
        return appointments;
    }

    /**
     * Looks through our booked slots and returns only the ones on the given date
     * @param date the day we want to check
     * @return a list of booked slots on that day
     */
    
    @Override
    public List<TimeSlot> getBookedSlotsForDate(LocalDate date) {
        List<TimeSlot> result = new ArrayList<>();
        for (TimeSlot slot : bookedSlots) {
        	
            if (slot.getDate().equals(date)) {
                result.add(slot);
            }
        }
        return result;
    }

    /**
     * Add a time slot to our booked list so we know its taken
     * @param slot the time slot to mark as booked
     */
    @Override
    public void saveBookedSlot(TimeSlot slot) {
        bookedSlots.add(slot);
    }
}