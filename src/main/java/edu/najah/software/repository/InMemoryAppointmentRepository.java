package edu.najah.software.repository;

import edu.najah.software.domain.Appointment;
import edu.najah.software.domain.TimeSlot;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


public class InMemoryAppointmentRepository implements AppointmentRepository {

    /** In-memory list of all appointments. */
    private final List<Appointment> appointments = new ArrayList<>();

    /** In-memory list of all booked time slots. */
    private final List<TimeSlot> bookedSlots = new ArrayList<>();

    /**
     * Saves an appointment to the in-memory list.
     *
     * @param appointment the appointment to save
     */
    @Override
    public void save(Appointment appointment) {
        appointments.add(appointment);
    }

    /**
     * Returns all saved appointments.
     *
     * @return list of all appointments
     */
    @Override
    public List<Appointment> getAll() {
        return appointments;
    }

    /**
     * Returns all booked time slots for a specific date.
     *
     * @param date the date to filter by
     * @return list of booked TimeSlots on that date
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
     * Marks a time slot as booked.
     *
     * @param slot the TimeSlot to mark as booked
     */
    @Override
    public void saveBookedSlot(TimeSlot slot) {
        bookedSlots.add(slot);
    }
}
