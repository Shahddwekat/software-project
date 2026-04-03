package edu.najah.software.service;

import edu.najah.software.domain.Appointment;
import edu.najah.software.domain.TimeSlot;
import edu.najah.software.domain.appointmenttype.AppointmentType;
import edu.najah.software.observer.Observer;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * The main interface for everything appointment related
 * It covers viewing slots booking modifying cancelling
 * and managing who gets notified when things happen
 *
 * @author Team
 * @version 1.0
 */
public interface AppointmentService {

    /**
     * Returns all the time slots that are still free on a given day
     * The admin needs to be logged in to call this
     * @param date the day we want to check
     * @return a list of available time slots
     */
    List<TimeSlot> getAvailableSlots(LocalDate date);

    /**
     * Books a new appointment with a specific type
     * Runs through all general and type-specific rules before saving
     * @param id a unique ID for this appointment
     * @param dateTime when it should happen
     * @param duration how long in minutes
     * @param participants how many people are coming
     * @param type what kind of appointment this is
     * @return the saved appointment with status Confirmed
     */
    Appointment bookAppointment(String id, LocalDateTime dateTime, int duration,
                                int participants, AppointmentType type);

    /**
     * Books an appointment without a specific type
     * Only the general rules are checked in this case
     * @param id a unique ID for this appointment
     * @param dateTime when it should happen
     * @param duration how long in minutes
     * @param participants how many people are coming
     * @return the saved appointment with status Confirmed
     */
    Appointment bookAppointment(String id, LocalDateTime dateTime, int duration, int participants);

    /**
     * Lets a user cancel their own upcoming appointment
     * Won't work on appointments that already happened
     * @param appointmentId the ID of the appointment to cancel
     */
    void cancelAppointment(String appointmentId);
    /**
     * Lets a user change the time of their own upcoming appointment
     * @param appointmentId the ID of the appointment to change
     * @param newDateTime the new date and time to move it to
     */
    void modifyAppointment(String appointmentId, LocalDateTime newDateTime);

    /**
     * Lets an admin cancel any appointment in the system
     * The admin must be logged in for this to work
     * @param appointmentId the ID of the appointment to cancel
     */
    void adminCancelAppointment(String appointmentId);

    /**
     * Lets an admin change the time of any appointment in the system
     * The admin must be logged in for this to work
     * @param appointmentId the ID of the appointment to change
     * @param newDateTime the new date and time to move it to
     */
    void adminModifyAppointment(String appointmentId, LocalDateTime newDateTime);

    /**
     * Returns every appointment that exists in the system
     * @return a list of all appointments
     */
    List<Appointment> getAllAppointments();

    /**
     * Adds someone to the notification list
     * They will start receiving updates from this point on
     * @param observer the observer to add
     */
    void addObserver(Observer observer);

    /**
     * Removes someone from the notification list
     * They will stop receiving updates after this
     * @param observer the observer to remove
     */
    void removeObserver(Observer observer);
}