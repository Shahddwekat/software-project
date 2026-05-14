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
 * Main application class for running
 * the appointment booking system.
 * 
 * This class demonstrates:
 * <ul>
 *     <li>Administrator authentication</li>
 *     <li>Viewing available appointment slots</li>
 *     <li>Booking appointments</li>
 *     <li>Validation handling</li>
 *     <li>Logout functionality</li>
 * </ul>
 * 
 * @author shahd
 * @version 1.0
 */
public class App {

    /**
     * Main method of the application.
     * 
     * The method initializes required services,
     * performs login operations, displays
     * available appointment slots, books
     * appointments, validates booking rules,
     * and finally logs out the administrator.
     * 
     * @param args command-line arguments
     */
    public static void main(String[] args) {

        // Setup system components
        AppointmentRepository repository =
                new InMemoryAppointmentRepository();

        AuthService authService =
                new SimpleAuthService();

        AppointmentService service =
                new AppointmentServiceImpl(
                        repository,
                        authService
                );

        // Authentication
        System.out.println(
                "=== Sprint 1: Authentication ===");

        boolean loginResult =
                authService.login("admin", "admin123");

        System.out.println(
                "Login successful: " + loginResult);

        // Display available appointment slots
        System.out.println(
                "\nAvailable slots for today:");

        service.getAvailableSlots(LocalDate.now())
               .forEach(System.out::println);

        // Book a valid appointment
        System.out.println(
                "\n=== Sprint 2: Booking ===");

        service.bookAppointment(
                "A1",
                LocalDateTime.now().plusDays(1),
                60,
                3
        );

        // Test invalid appointment duration
        System.out.println(
                "\nTrying invalid duration (150 min):");

        try {

            service.bookAppointment(
                    "A2",
                    LocalDateTime.now().plusDays(1),
                    150,
                    2
            );

        } catch (IllegalArgumentException e) {

            System.out.println(
                    "Rejected: " + e.getMessage());
        }

        // Test invalid participant count
        System.out.println(
                "\nTrying too many participants (10):");

        try {

            service.bookAppointment(
                    "A3",
                    LocalDateTime.now().plusDays(1),
                    60,
                    10
            );

        } catch (IllegalArgumentException e) {

            System.out.println(
                    "Rejected: " + e.getMessage());
        }

        // Logout
        System.out.println("\n=== Logout ===");

        authService.logout();

        System.out.println(
                "Logged out. isLoggedIn: "
                        + authService.isLoggedIn());
    }
}