package edu.najah.software.service;

import edu.najah.software.domain.TimeSlot;
import edu.najah.software.repository.AppointmentRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of the AppointmentService interface.
 * 
 * This class provides functionality for retrieving
 * available appointment slots after checking
 * administrator authentication.
 * 
 * @author Lojain
 * @version 1.0
 */
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentRepository repository;
    private final AuthService authService;

    /**
     * Constructs an AppointmentServiceImpl object.
     * 
     * @param repository repository used for storing
     *                   and retrieving appointments
     * @param authService authentication service
     */
    public AppointmentServiceImpl(AppointmentRepository repository,
                                  AuthService authService) {

        this.repository = repository;
        this.authService = authService;
    }

    /**
     * Returns all available appointment slots
     * for a specific date.
     * 
     * The administrator must be logged in
     * before accessing available slots.
     * 
     * @param date the required appointment date
     * @return list of available time slots
     * 
     * @throws IllegalStateException if the administrator
     *                               is not logged in
     */
    @Override
    public List<TimeSlot> getAvailableSlots(LocalDate date) {

        if (!authService.isLoggedIn()) {

            throw new IllegalStateException(
                    "Admin must be logged in to view available slots.");
        }

        List<TimeSlot> dailyTemplate = buildDailyTemplate(date);

        List<TimeSlot> booked =
                repository.getBookedSlotsForDate(date);

        List<TimeSlot> available = new ArrayList<>();

        for (TimeSlot slot : dailyTemplate) {

            if (!booked.contains(slot)) {
                available.add(slot);
            }
        }

        return available;
    }

    /**
     * Builds a fixed daily schedule template.
     * 
     * The schedule starts from 09:00 AM
     * until 05:00 PM using 1-hour slots.
     * 
     * @param date the required date
     * @return list of generated time slots
     */
    private List<TimeSlot> buildDailyTemplate(LocalDate date) {

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