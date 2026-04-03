package edu.najah.software.service;

import edu.najah.software.domain.Appointment;
import edu.najah.software.domain.TimeSlot;
import edu.najah.software.domain.appointmenttype.*;
import edu.najah.software.observer.Observer;
import edu.najah.software.repository.AppointmentRepository;
import edu.najah.software.strategy.BookingRuleStrategy;
import edu.najah.software.strategy.DurationRule;
import edu.najah.software.strategy.ParticipantLimitRule;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This is where all the appointment logic actually happens
 * It connects the repository the booking rules the type rules and the notification system
 * Uses the Strategy pattern to keep booking rules flexible
 * and the Observer pattern so multiple listeners can get notified
 * @author Team
 * @version 1.0
 */
public class AppointmentServiceImpl implements AppointmentService {

    /** Where we store and retrieve appointments */
    private final AppointmentRepository repository;

    /** Handles checking if the admin is logged in */
    private final AuthService authService;

    /** The general rules every appointment must pass */
    private final List<BookingRuleStrategy> bookingRules;

    /** Everyone who wants to be notified when something happens */
    private final List<Observer> observers;

    /** Maps each appointment type to its own specific rule */
    private final Map<AppointmentType, AppointmentTypeRule> typeRules;

    /**
     * Sets everything up  loads the default booking rules
     * and registers a rule for each appointment type
     * @param repository where appointments get saved
     * @param authService handles login and logout
     */
    public AppointmentServiceImpl(AppointmentRepository repository, AuthService authService) {
        this.repository = repository;
        this.authService = authService;
        this.bookingRules = new ArrayList<>();
        this.bookingRules.add(new DurationRule());
        this.bookingRules.add(new ParticipantLimitRule());
        this.observers = new ArrayList<>();
        this.typeRules = new HashMap<>();
        typeRules.put(AppointmentType.URGENT, new UrgentAppointmentRule());
        typeRules.put(AppointmentType.FOLLOW_UP, new FollowUpAppointmentRule());
        typeRules.put(AppointmentType.ASSESSMENT, new AssessmentAppointmentRule());
        typeRules.put(AppointmentType.VIRTUAL, new VirtualAppointmentRule());
        typeRules.put(AppointmentType.IN_PERSON, new InPersonAppointmentRule());
        typeRules.put(AppointmentType.INDIVIDUAL, new IndividualAppointmentRule());
        typeRules.put(AppointmentType.GROUP, new GroupAppointmentRule());
    }

    /**
     * Adds an observer so they start receiving notifications
     * @param observer the observer to add
     */
    @Override
    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    /**
     * Removes an observer so they stop receiving notifications
     * @param observer the observer to remove
     */
    @Override
    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    /**
     * Goes through everyone on the notification list and sends them the message
     * @param appointment the appointment the event is about
     * @param message what we want to tell them
     */
    private void notifyObservers(Appointment appointment, String message) {
        for (Observer observer : observers) {
            observer.update(appointment, message);
        }
    }

    /**
     * Returns all time slots that are still free on the given day
     * Builds a full day schedule first, then removes the already booked ones
     * @param date the day to check
     * @return a list of free time slots
     * @throws IllegalStateException if no admin is logged in
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
     * Books a new appointment after checking all the rules
     * First checks general rules, then checks the type-specific rule if a type was given
     * Notifies all observers once the booking is saved
     * @param id a unique ID for this appointment
     * @param dateTime when it should happen
     * @param duration how long in minutes
     * @param participants how many people are coming
     * @param type what kind of appointment this is
     * @return the saved appointment
     * @throws IllegalArgumentException if any rule is broken
     */
    @Override
    public Appointment bookAppointment(String id, LocalDateTime dateTime,
                                       int duration, int participants, AppointmentType type) {
        Appointment appointment = new Appointment(id, dateTime, duration, participants, type);
        for (BookingRuleStrategy rule : bookingRules) {
            if (!rule.isValid(appointment)) {
                throw new IllegalArgumentException(rule.getErrorMessage());
            }
        }
        if (type != null && typeRules.containsKey(type)) {
            AppointmentTypeRule typeRule = typeRules.get(type);
            if (!typeRule.isValid(appointment)) {
                throw new IllegalArgumentException(typeRule.getErrorMessage());
            }
        }
        repository.save(appointment);
        TimeSlot slot = new TimeSlot(
                dateTime.toLocalDate(),
                dateTime.toLocalTime(),
                dateTime.toLocalTime().plusMinutes(duration)
        );
        repository.saveBookedSlot(slot);
        notifyObservers(appointment, "Reminder: Your " + type + " appointment is scheduled for " + dateTime);
        System.out.println("Appointment booked successfully. Type = " + type + " Status = " + appointment.getStatus());
        return appointment;
    }

    /**
     * Books an appointment without a type — only general rules apply
     * @param id a unique ID for this appointment
     * @param dateTime when it should happen
     * @param duration how long in minutes
     * @param participants how many people are coming
     * @return the saved appointment
     */
    @Override
    public Appointment bookAppointment(String id, LocalDateTime dateTime, int duration, int participants) {
        return bookAppointment(id, dateTime, duration, participants, null);
    }

    /**
     * Cancels a user's own upcoming appointment
     * We won't let anyone cancel something that already happened
     * @param appointmentId the ID of the appointment to cancel
     * @throws IllegalArgumentException if the appointment doesn't exist
     * @throws IllegalStateException if the appointment is in the past
     */
    @Override
    public void cancelAppointment(String appointmentId) {
        Appointment appointment = findFutureAppointment(appointmentId);
        appointment.setStatus("Cancelled");
        notifyObservers(appointment, "Your appointment on "
                + appointment.getDateTime() + " has been cancelled.");
        System.out.println("Appointment " + appointmentId + " has been cancelled.");
    }

    /**
     * Changes the time of a user's own upcoming appointment
     * @param appointmentId the ID of the appointment to change
     * @param newDateTime the new date and time
     * @throws IllegalArgumentException if the appointment doesn't exist
     * @throws IllegalStateException if the appointment is in the past
     */
    @Override
    public void modifyAppointment(String appointmentId, LocalDateTime newDateTime) {
        Appointment appointment = findFutureAppointment(appointmentId);
        appointment.setDateTime(newDateTime);
        notifyObservers(appointment, "Your appointment has been rescheduled to " + newDateTime);
        System.out.println("Appointment " + appointmentId + " modified to " + newDateTime);
    }

    /**
     * Admin cancels any appointment — they need to be logged in first
     * @param appointmentId the ID of the appointment to cancel
     * @throws IllegalStateException if the admin is not logged in
     */
    @Override
    public void adminCancelAppointment(String appointmentId) {
        if (!authService.isLoggedIn()) {
            throw new IllegalStateException("Admin must be logged in to cancel appointments.");
        }
        Appointment appointment = findFutureAppointment(appointmentId);
        appointment.setStatus("Cancelled");
        notifyObservers(appointment, "Your appointment was cancelled by the administrator.");
        System.out.println("Admin cancelled appointment " + appointmentId);
    }

    /**
     * Admin changes the time of any appointment — they need to be logged in first
     * @param appointmentId the ID of the appointment to change
     * @param newDateTime the new date and time
     * @throws IllegalStateException if the admin is not logged in
     */
    @Override
    public void adminModifyAppointment(String appointmentId, LocalDateTime newDateTime) {
        if (!authService.isLoggedIn()) {
            throw new IllegalStateException("Admin must be logged in to modify appointments.");
        }
        Appointment appointment = findFutureAppointment(appointmentId);
        appointment.setDateTime(newDateTime);
        notifyObservers(appointment, "Your appointment was rescheduled by the administrator to " + newDateTime);
        System.out.println("Admin modified appointment " + appointmentId + " to " + newDateTime);
    }

    /**
     * Returns every appointment currently in the system
     * @return the full list of appointments
     */
    @Override
    public List<Appointment> getAllAppointments() {
        return repository.getAll();
    }

    /**
     * Looks for an appointment by ID and makes sure it's in the future
     * Throws an error if we can't find it or if it already happened
     * @param appointmentId the ID we're looking for
     * @return the appointment if found and it's in the future
     * @throws IllegalArgumentException if no appointment with that ID exists
     * @throws IllegalStateException if the appointment is already in the past
     */
    private Appointment findFutureAppointment(String appointmentId) {
        for (Appointment appointment : repository.getAll()) {
            if (appointment.getId().equals(appointmentId)) {
                if (appointment.getDateTime().isBefore(LocalDateTime.now())) {
                    throw new IllegalStateException("Cannot modify or cancel a past appointment.");
                }
                return appointment;
            }
        }
        throw new IllegalArgumentException("Appointment not found: " + appointmentId);
    }

    /**
     * Builds a list of 8 one hour slots covering a standard work day from 9am to 5pm
     * @param date the day to build the schedule for
     * @return a list of hourly time slots
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