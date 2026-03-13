package edu.najah.software.service;

import edu.najah.software.domain.Appointment;
import edu.najah.software.domain.TimeSlot;
import edu.najah.software.observer.Observer;
import edu.najah.software.repository.AppointmentRepository;
import edu.najah.software.strategy.BookingRuleStrategy;
import edu.najah.software.strategy.DurationRule;
import edu.najah.software.strategy.ParticipantLimitRule;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of AppointmentService.
 * Uses Strategy pattern for booking rules
 * and Observer pattern for notifications.
 *
 * @author Team
 * @version 1.0
 */
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentRepository repository;
    private final AuthService authService;
    private final List<BookingRuleStrategy> bookingRules;
    private final List<Observer> observers;

    public AppointmentServiceImpl(AppointmentRepository repository, AuthService authService) {
        this.repository = repository;
        this.authService = authService;
        this.bookingRules = new ArrayList<>();
        this.bookingRules.add(new DurationRule());
        this.bookingRules.add(new ParticipantLimitRule());
        this.observers = new ArrayList<>();
    }

    // Add an observer to the notification list
    @Override
    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    // Remove an observer from the notification list
    @Override
    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    // Send a message to all registered observers
    private void notifyObservers(Appointment appointment, String message) {
        for (Observer observer : observers) {
            observer.update(appointment, message);
        }
    }

    // Returns free slots for the day — admin login required
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

    // Books an appointment if it passes all rules, then notifies observers
    @Override
    public Appointment bookAppointment(String id, LocalDateTime dateTime, int duration, int participants) {
        Appointment appointment = new Appointment(id, dateTime, duration, participants);

        // Check all booking rules
        for (BookingRuleStrategy rule : bookingRules) {
            if (!rule.isValid(appointment)) {
                throw new IllegalArgumentException(rule.getErrorMessage());
            }
        }

        repository.save(appointment);

        // Mark the slot as booked
        TimeSlot slot = new TimeSlot(
                dateTime.toLocalDate(),
                dateTime.toLocalTime(),
                dateTime.toLocalTime().plusMinutes(duration)
        );
        repository.saveBookedSlot(slot);

        // Notify observers about the new booking
        notifyObservers(appointment, "Reminder: Your appointment is scheduled for " + dateTime);

        System.out.println("Appointment booked successfully. Status = " + appointment.getStatus());
        return appointment;
    }

    // Cancels a future appointment and notifies observers
    @Override
    public void cancelAppointment(String appointmentId) {
        List<Appointment> all = repository.getAll();
        for (Appointment appointment : all) {
            if (appointment.getId().equals(appointmentId)) {
                if (appointment.getDateTime().isBefore(LocalDateTime.now())) {
                    throw new IllegalStateException("Cannot cancel a past appointment.");
                }
                appointment.setStatus("Cancelled");
                // Notify observers about the cancellation
                notifyObservers(appointment, "Your appointment on "
                        + appointment.getDateTime() + " has been cancelled.");
                System.out.println("Appointment " + appointmentId + " has been cancelled.");
                return;
            }
        }
        throw new IllegalArgumentException("Appointment not found: " + appointmentId);
    }

    // Returns all saved appointments
    @Override
    public List<Appointment> getAllAppointments() {
        return repository.getAll();
    }

    // Builds 8 hourly slots from 9am to 5pm for a given day
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