package edu.najah.software;

import java.time.LocalDateTime;
import edu.najah.software.service.AppointmentService;

/**
 * Main application class for running the appointment booking system.
 * 
 * This class creates an instance of the AppointmentService
 * and books a sample appointment.
 * 
 * @author Lojain
 * @version 1.0
 */
public class App {

    /**
     * Main method of the application.
     * 
     * Creates an AppointmentService object and books
     * an appointment using sample data.
     * 
     * @param args command-line arguments
     */
    public static void main(String[] args) {

        AppointmentService service = new AppointmentService();

        service.bookAppointment(
                "A1",
                LocalDateTime.now(),
                60,
                3
        );
    }
}