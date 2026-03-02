package edu.najah.software.service;

import edu.najah.software.domain.TimeSlot;

import java.time.LocalDate;
import java.util.List;

public interface AppointmentService {
    List<TimeSlot> getAvailableSlots(LocalDate date);
}