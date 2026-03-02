package edu.najah.software.service;

import edu.najah.software.domain.TimeSlot;
import edu.najah.software.repository.AppointmentRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentRepository repository;
    private final AuthService authService;

    public AppointmentServiceImpl(AppointmentRepository repository, AuthService authService) {
        this.repository = repository;
        this.authService = authService;
    }

    @Override
    public List<TimeSlot> getAvailableSlots(LocalDate date) {
        if (!authService.isLoggedIn()) {
            throw new IllegalStateException("Admin must be logged in to view available slots.");
        }

        List<TimeSlot> dailyTemplate = buildDailyTemplate(date);
        List<TimeSlot> booked = repository.getBookedSlotsForDate(date);

        List<TimeSlot> available = new ArrayList<>();
        for (TimeSlot slot : dailyTemplate) {
            if (!booked.contains(slot)) {
                available.add(slot);
            }
        }
        return available;
    }

    private List<TimeSlot> buildDailyTemplate(LocalDate date) {
        // Sprint 1: simple fixed schedule 09:00-17:00, 1-hour slots
        List<TimeSlot> slots = new ArrayList<>();
        LocalTime start = LocalTime.of(9, 0);

        for (int i = 0; i < 8; i++) {
            LocalTime slotStart = start.plusHours(i);
            LocalTime slotEnd = slotStart.plusHours(1);
            slots.add(new TimeSlot(date, slotStart, slotEnd));
        }
        return slots;
    }
}