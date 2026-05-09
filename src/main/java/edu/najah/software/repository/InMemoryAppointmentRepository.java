package edu.najah.software.repository;

import edu.najah.software.domain.TimeSlot;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * In-memory implementation of the AppointmentRepository interface.
 * 
 * This class stores booked appointment time slots
 * temporarily in memory using a list.
 * 
 * @author Lojain
 * @version 1.0
 */
public class InMemoryAppointmentRepository implements AppointmentRepository {

    private final List<TimeSlot> bookedSlots = new ArrayList<>();

    /**
     * Returns all booked time slots for a specific date.
     * 
     * @param date the required date
     * @return list of booked time slots for the given date
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
     * Saves a booked time slot.
     * 
     * @param slot the time slot to save
     */
    @Override
    public void saveBookedSlot(TimeSlot slot) {
        bookedSlots.add(slot);
    }
}