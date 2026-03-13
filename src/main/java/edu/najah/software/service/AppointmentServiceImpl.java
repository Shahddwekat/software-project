package edu.najah.software.service;

import edu.najah.software.domain.Appointment;
import edu.najah.software.domain.TimeSlot;
import edu.najah.software.repository.AppointmentRepository;
import edu.najah.software.strategy.BookingRuleStrategy;
import edu.najah.software.strategy.DurationRule;
import edu.najah.software.strategy.ParticipantLimitRule;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;


public class AppointmentServiceImpl implements AppointmentService {

    /** Repository for storing and retrieving appointments. */
    private final AppointmentRepository repository;

    private final AuthService authService;

    /** List of booking rules applied using the Strategy pattern. */
    private final List<BookingRuleStrategy> bookingRules;

    /**
     * Constructs the service with the given repository and auth service.
     * Initializes default booking rules (duration and participant limits).
     *
     * @param repository  the appointment repository
     * @param authService the authentication service
     */
    public AppointmentServiceImpl(AppointmentRepository repository, AuthService authService) {
        this.repository = repository;
        this.authService = authService;
        this.bookingRules = new ArrayList<>();
        this.bookingRules.add(new DurationRule());
        this.bookingRules.add(new ParticipantLimitRule());
    }

    /**
     * Returns all available time slots for the given date.
     * Requires admin to be logged in.
     *
     * @param date the date to check
     * @return list of available TimeSlots
     * @throws IllegalStateException if admin is not logged in
     */
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

    /**
     * Books a new appointment after validating all booking rules.
     * Saves the appointment and marks the corresponding slot as booked.
     *
     * @param id           unique appointment ID
     * @param dateTime     date and time of the appointment
     * @param duration     duration in minutes
     * @param participants number of participants
     * @return the confirmed Appointment object
     * @throws IllegalArgumentException if any booking rule is violated
     */
    @Override
    public Appointment bookAppointment(String id, LocalDateTime dateTime, int duration, int participants) {
        Appointment appointment = new Appointment(id, dateTime, duration, participants);

        // Apply all booking rules using the Strategy pattern
        for (BookingRuleStrategy rule : bookingRules) {
            if (!rule.isValid(appointment)) {
                throw new IllegalArgumentException(rule.getErrorMessage());
            }
        }

        repository.save(appointment);

        // Mark the time slot as booked
        TimeSlot slot = new TimeSlot(
                dateTime.toLocalDate(),
                dateTime.toLocalTime(),
                dateTime.toLocalTime().plusMinutes(duration)
        );
        repository.saveBookedSlot(slot);

        System.out.println("Appointment booked successfully. Status = " + appointment.getStatus());
        return appointment;
    }

    /**
     * Cancels an existing appointment by ID.
     * Only future appointments can be cancelled.
     *
     * @param appointmentId the ID of the appointment to cancel
     * @throws IllegalArgumentException if the appointment is not found
     * @throws IllegalStateException    if the appointment is not in the future
     */
    @Override
    public void cancelAppointment(String appointmentId) {
        List<Appointment> all = repository.getAll();
        for (Appointment appointment : all) {
            if (appointment.getId().equals(appointmentId)) {
                if (appointment.getDateTime().isBefore(LocalDateTime.now())) {
                    throw new IllegalStateException("Cannot cancel a past appointment.");
                }
                appointment.setStatus("Cancelled");
                System.out.println("Appointment " + appointmentId + " has been cancelled.");
                return;
            }
        }
        throw new IllegalArgumentException("Appointment not found: " + appointmentId);
    }

    /**
     * Returns all appointments currently stored in the repository.
     *
     * @return list of all appointments
     */
    @Override
    public List<Appointment> getAllAppointments() {
        return repository.getAll();
    }

    /**
     * Builds a daily template of 1-hour time slots from 09:00 to 17:00.
     *
     * @param date the date to build the template for
     * @return list of TimeSlots covering the working day
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
