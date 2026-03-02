package edu.najah.software.repository;

import edu.najah.software.domain.TimeSlot;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class InMemoryAppointmentRepository implements AppointmentRepository {

    private final List<TimeSlot> bookedSlots = new ArrayList<>();

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

    @Override
    public void saveBookedSlot(TimeSlot slot) {
        bookedSlots.add(slot);
    }
}