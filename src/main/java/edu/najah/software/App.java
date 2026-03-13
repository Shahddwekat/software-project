package edu.najah.software;

import edu.najah.software.repository.AppointmentRepository;
import edu.najah.software.repository.InMemoryAppointmentRepository;
import edu.najah.software.service.AppointmentService;
import edu.najah.software.service.AppointmentServiceImpl;
import edu.najah.software.service.AuthService;
import edu.najah.software.service.SimpleAuthService;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Entry point for the Appointment Scheduling System.
 *
 * @author Team
 * @version 1.0
 */
public class App {

    public static void main(String[] args) {

        // Setup
        AppointmentRepository repository = new InMemoryAppointmentRepository();
        AuthService authService = new SimpleAuthService();
        AppointmentService service = new AppointmentServiceImpl(repository, authService);

        // Login
        System.out.println("=== Sprint 1: Authentication ===");
        boolean loginResult = authService.login("admin", "admin123");
        System.out.println("Login successful: " + loginResult);

        // View available slots
        System.out.println("\nAvailable slots for today:");
        service.getAvailableSlots(LocalDate.now())
               .forEach(System.out::println);

        // Book an appointment
        System.out.println("\n=== Sprint 2: Booking ===");
        service.bookAppointment("A1", LocalDateTime.now().plusDays(1), 60, 3);

        // Try invalid duration
        System.out.println("\nTrying invalid duration (150 min):");
        try {
            service.bookAppointment("A2", LocalDateTime.now().plusDays(1), 150, 2);
        } catch (IllegalArgumentException e) {
            System.out.println("Rejected: " + e.getMessage());
        }

        // Try too many participants
        System.out.println("\nTrying too many participants (10):");
        try {
            service.bookAppointment("A3", LocalDateTime.now().plusDays(1), 60, 10);
        } catch (IllegalArgumentException e) {
            System.out.println("Rejected: " + e.getMessage());
        }

        // Logout
        System.out.println("\n=== Logout ===");
        authService.logout();
        System.out.println("Logged out. isLoggedIn: " + authService.isLoggedIn());
    }
}