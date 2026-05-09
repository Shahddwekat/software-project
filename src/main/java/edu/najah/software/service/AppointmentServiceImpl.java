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
 * Implementation of the AppointmentService interface.
 * 
 * This class handles appointment booking,
 * cancellation, modification, slot management,
 * validation rules, and observer notifications.
 * 
 * It uses:
 * <ul>
 *     <li>Strategy Pattern for booking rules</li>
 *     <li>Observer Pattern for notifications</li>
 * </ul>
 * 
 * @author raana
 * @version 1.0
 */
public class AppointmentServiceImpl
        implements AppointmentService {

    private final AppointmentRepository repository;
    private final AuthService authService;
    private final List<BookingRuleStrategy> bookingRules;
    private final List<Observer> observers;
    private final Map<AppointmentType,
            AppointmentTypeRule> typeRules;

    /**
     * Constructs an AppointmentServiceImpl object.
     * 
     * Initializes booking rules,
     * observer list, and appointment type rules.
     * 
     * @param repository repository for appointments
     * @param authService authentication service
     */
    public AppointmentServiceImpl(
            AppointmentRepository repository,
            AuthService authService) {

        this.repository = repository;
        this.authService = authService;

        this.bookingRules = new ArrayList<>();
        this.bookingRules.add(new DurationRule());
        this.bookingRules.add(new ParticipantLimitRule());

        this.observers = new ArrayList<>();

        this.typeRules = new HashMap<>();

        typeRules.put(AppointmentType.URGENT,
                new UrgentAppointmentRule());

        typeRules.put(AppointmentType.FOLLOW_UP,
                new FollowUpAppointmentRule());

        typeRules.put(AppointmentType.ASSESSMENT,
                new AssessmentAppointmentRule());

        typeRules.put(AppointmentType.VIRTUAL,
                new VirtualAppointmentRule());

        typeRules.put(AppointmentType.IN_PERSON,
                new InPersonAppointmentRule());

        typeRules.put(AppointmentType.INDIVIDUAL,
                new IndividualAppointmentRule());

        typeRules.put(AppointmentType.GROUP,
                new GroupAppointmentRule());
    }

    /**
     * Adds an observer to the notification list.
     * 
     * @param observer observer to add
     */
    @Override
    public void addObserver(Observer observer) {

        observers.add(observer);
    }

    /**
     * Removes an observer from the notification list.
     * 
     * @param observer observer to remove
     */
    @Override
    public void removeObserver(Observer observer) {

        observers.remove(observer);
    }

    /**
     * Sends notifications to all observers.
     * 
     * @param appointment related appointment
     * @param message notification message
     */
    private void notifyObservers(
            Appointment appointment,
            String message) {

        for (Observer observer : observers) {

            observer.update(appointment, message);
        }
    }

    /**
     * Returns all available appointment slots
     * for a specific date.
     * 
     * @param date required date
     * @return list of available slots
     * 
     * @throws IllegalStateException
     *         if the administrator is not logged in
     */
    @Override
    public List<TimeSlot> getAvailableSlots(
            LocalDate date) {

        if (!authService.isLoggedIn()) {

            throw new IllegalStateException(
                    "You must be logged in to view available slots.");
        }

        List<TimeSlot> dailyTemplate =
                buildDailyTemplate(date);

        List<TimeSlot> booked =
                repository.getBookedSlotsForDate(date);

        List<TimeSlot> available =
                new ArrayList<>();

        for (TimeSlot slot : dailyTemplate) {

            if (!booked.contains(slot)) {

                available.add(slot);
            }
        }

        return available;
    }

    /**
     * Books a new appointment with a type.
     * 
     * General booking rules and
     * appointment type rules are validated
     * before saving the appointment.
     * 
     * @param id appointment ID
     * @param dateTime appointment date and time
     * @param duration duration in minutes
     * @param participants number of participants
     * @param type appointment type
     * @return booked appointment
     * 
     * @throws IllegalArgumentException
     *         if validation fails
     */
    @Override
    public Appointment bookAppointment(
            String id,
            LocalDateTime dateTime,
            int duration,
            int participants,
            AppointmentType type) {

        Appointment appointment =
                new Appointment(
                        id,
                        dateTime,
                        duration,
                        participants,
                        type
                );

        for (BookingRuleStrategy rule : bookingRules) {

            if (!rule.isValid(appointment)) {

                throw new IllegalArgumentException(
                        rule.getErrorMessage());
            }
        }

        if (type != null && typeRules.containsKey(type)) {

            AppointmentTypeRule typeRule =
                    typeRules.get(type);

            if (!typeRule.isValid(appointment)) {

                throw new IllegalArgumentException(
                        typeRule.getErrorMessage());
            }
        }

        repository.save(appointment);

        TimeSlot slot = new TimeSlot(
                dateTime.toLocalDate(),
                dateTime.toLocalTime(),
                dateTime.toLocalTime()
                        .plusMinutes(duration)
        );

        repository.saveBookedSlot(slot);

        notifyObservers(
                appointment,
                "Reminder: Your " + type
                        + " appointment is scheduled for "
                        + dateTime
        );

        System.out.println(
                "Appointment booked successfully. "
                        + "Type = " + type
                        + " Status = "
                        + appointment.getStatus());

        return appointment;
    }

    /**
     * Books a new appointment
     * without specifying a type.
     * 
     * @param id appointment ID
     * @param dateTime appointment date and time
     * @param duration duration in minutes
     * @param participants number of participants
     * @return booked appointment
     */
    @Override
    public Appointment bookAppointment(
            String id,
            LocalDateTime dateTime,
            int duration,
            int participants) {

        return bookAppointment(
                id,
                dateTime,
                duration,
                participants,
                null
        );
    }

    /**
     * Cancels an upcoming appointment.
     * 
     * @param appointmentId appointment ID
     */
    @Override
    public void cancelAppointment(
            String appointmentId) {

        Appointment appointment =
                findFutureAppointment(appointmentId);

        appointment.setStatus("Cancelled");

        notifyObservers(
                appointment,
                "Your appointment on "
                        + appointment.getDateTime()
                        + " has been cancelled."
        );

        System.out.println(
                "Appointment "
                        + appointmentId
                        + " has been cancelled.");
    }

    /**
     * Modifies an appointment date and time.
     * 
     * @param appointmentId appointment ID
     * @param newDateTime new appointment date and time
     */
    @Override
    public void modifyAppointment(
            String appointmentId,
            LocalDateTime newDateTime) {

        Appointment appointment =
                findFutureAppointment(appointmentId);

        appointment.setDateTime(newDateTime);

        notifyObservers(
                appointment,
                "Your appointment has been rescheduled to "
                        + newDateTime
        );

        System.out.println(
                "Appointment "
                        + appointmentId
                        + " modified to "
                        + newDateTime);
    }

    /**
     * Allows the administrator
     * to cancel an appointment.
     * 
     * @param appointmentId appointment ID
     */
    @Override
    public void adminCancelAppointment(
            String appointmentId) {

        if (!authService.isLoggedIn()) {

            throw new IllegalStateException(
                    "Admin must be logged in to cancel appointments.");
        }

        Appointment appointment =
                findFutureAppointment(appointmentId);

        appointment.setStatus("Cancelled");

        notifyObservers(
                appointment,
                "Your appointment was cancelled "
                        + "by the administrator."
        );

        System.out.println(
                "Admin cancelled appointment "
                        + appointmentId);
    }

    /**
     * Allows the administrator
     * to modify an appointment.
     * 
     * @param appointmentId appointment ID
     * @param newDateTime new appointment date and time
     */
    @Override
    public void adminModifyAppointment(
            String appointmentId,
            LocalDateTime newDateTime) {

        if (!authService.isLoggedIn()) {

            throw new IllegalStateException(
                    "Admin must be logged in "
                            + "to modify appointments.");
        }

        Appointment appointment =
                findFutureAppointment(appointmentId);

        appointment.setDateTime(newDateTime);

        notifyObservers(
                appointment,
                "Your appointment was rescheduled "
                        + "by the administrator to "
                        + newDateTime
        );

        System.out.println(
                "Admin modified appointment "
                        + appointmentId
                        + " to "
                        + newDateTime);
    }

    /**
     * Returns all appointments.
     * 
     * @return list of appointments
     */
    @Override
    public List<Appointment> getAllAppointments() {

        return repository.getAll();
    }

    /**
     * Finds an appointment by ID
     * and verifies that it is upcoming.
     * 
     * @param appointmentId appointment ID
     * @return matching appointment
     * 
     * @throws IllegalArgumentException
     *         if appointment does not exist
     * @throws IllegalStateException
     *         if appointment is in the past
     */
    private Appointment findFutureAppointment(
            String appointmentId) {

        for (Appointment appointment
                : repository.getAll()) {

            if (appointment.getId()
                    .equals(appointmentId)) {

                if (appointment.getDateTime()
                        .isBefore(LocalDateTime.now())) {

                    throw new IllegalStateException(
                            "Cannot modify or cancel "
                                    + "a past appointment.");
                }

                return appointment;
            }
        }

        throw new IllegalArgumentException(
                "Appointment not found: "
                        + appointmentId);
    }

    /**
     * Builds a standard daily schedule.
     * 
     * Creates 1-hour appointment slots
     * from 09:00 AM until 05:00 PM.
     * 
     * @param date required date
     * @return list of generated slots
     */
    private List<TimeSlot> buildDailyTemplate(
            LocalDate date) {

        List<TimeSlot> slots =
                new ArrayList<>();

        LocalTime start =
                LocalTime.of(9, 0);

        for (int i = 0; i < 8; i++) {

            LocalTime slotStart =
                    start.plusHours(i);

            LocalTime slotEnd =
                    slotStart.plusHours(1);

            slots.add(
                    new TimeSlot(
                            date,
                            slotStart,
                            slotEnd
                    )
            );
        }

        return slots;
    }
}