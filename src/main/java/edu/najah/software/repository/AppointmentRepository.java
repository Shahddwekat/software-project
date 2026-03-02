package edu.najah.software.repository;

import edu.najah.software.domain.TimeSlot;

import java.time.LocalDate;
import java.util.List;

public interface AppointmentRepository {
    List<TimeSlot> getBookedSlotsForDate(LocalDate date);
    void saveBookedSlot(TimeSlot slot);
}