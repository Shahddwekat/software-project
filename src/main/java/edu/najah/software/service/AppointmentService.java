package edu.najah.software.service;

import java.time.LocalDateTime;

import edu.najah.software.domain.Appointment;
import edu.najah.software.repository.AppointmentRepository;

/**
 * Service class responsible for appointment operations.
 * 
 * This class validates appointment information
 * and stores appointments using the repository layer.
 * 
 * @author Lojain
 * @version 1.0
 */
public class AppointmentService {

    private AppointmentRepository repo = new AppointmentRepository();

    private static final int MAX_DURATION = 120;
    private static final int MAX_PARTICIPANTS = 5;

    /**
     * Books a new appointment after validating
     * duration and participant limits.
     * 
     * @param id unique appointment ID
     * @param dateTime appointment date and time
     * @param duration appointment duration in minutes
     * @param participants number of participants
     * 
     * @throws IllegalArgumentException if duration exceeds
     *                                  the allowed limit
     * @throws IllegalArgumentException if participants exceed
     *                                  the allowed limit
     */
    public void bookAppointment(String id,
                                LocalDateTime dateTime,
                                int duration,
                                int participants) {

        if (duration > MAX_DURATION) {
            throw new IllegalArgumentException(
                    "Duration exceeds maximum limit");
        }

        if (participants > MAX_PARTICIPANTS) {
            throw new IllegalArgumentException(
                    "Participants exceed limit");
        }

        Appointment appointment =
                new Appointment(id, dateTime, duration, participants);

        repo.save(appointment);

        System.out.println(
                "Appointment booked successfully. Status = Confirmed");
    }
}