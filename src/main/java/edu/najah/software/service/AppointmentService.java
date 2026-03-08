/*package edu.najah.software.service;

import edu.najah.software.domain.TimeSlot;

import java.time.LocalDate;
import java.util.List;

public interface AppointmentService {
    List<TimeSlot> getAvailableSlots(LocalDate date);
}*/
package edu.najah.software.service;

import java.time.LocalDateTime;
import edu.najah.software.domain.Appointment;
import edu.najah.software.repository.AppointmentRepository;

public class AppointmentService {

    private AppointmentRepository repo = new AppointmentRepository();

    private static final int MAX_DURATION = 120; 
    private static final int MAX_PARTICIPANTS = 5;

    public void bookAppointment(String id, LocalDateTime dateTime, int duration, int participants) {

        if (duration > MAX_DURATION) {
            throw new IllegalArgumentException("Duration exceeds maximum limit");
        }

        if (participants > MAX_PARTICIPANTS) {
            throw new IllegalArgumentException("Participants exceed limit");
        }

        Appointment appointment = new Appointment(id, dateTime, duration, participants);

        repo.save(appointment);

        System.out.println("Appointment booked successfully. Status = Confirmed");
    }
}