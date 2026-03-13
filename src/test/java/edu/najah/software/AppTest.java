package edu.najah.software;

import edu.najah.software.domain.Appointment;
import edu.najah.software.domain.TimeSlot;
import edu.najah.software.repository.AppointmentRepository;
import edu.najah.software.repository.InMemoryAppointmentRepository;
import edu.najah.software.service.AppointmentService;
import edu.najah.software.service.AppointmentServiceImpl;
import edu.najah.software.service.AuthService;
import edu.najah.software.service.SimpleAuthService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for Sprint 1 and Sprint 2 functionality.
 *
 * @author Team
 * @version 1.0
 */
public class AppTest {

    private AuthService authService;
    private AppointmentService appointmentService;

    /**
     * Sets up a fresh service and auth instance before each test.
     */
    @BeforeEach
    void setUp() {
        AppointmentRepository repository = new InMemoryAppointmentRepository();
        authService = new SimpleAuthService();
        appointmentService = new AppointmentServiceImpl(repository, authService);
    }

    // ========== Sprint 1 - Auth Tests ==========

    /** US1.1 - Valid credentials should return true. */
    @Test
    void testLoginWithValidCredentials() {
        assertTrue(authService.login("admin", "admin123"));
    }

    /** US1.1 - Invalid credentials should return false. */
    @Test
    void testLoginWithInvalidCredentials() {
        assertFalse(authService.login("admin", "wrongpassword"));
    }

    /** US1.2 - After logout, isLoggedIn should return false. */
    @Test
    void testLogoutClearsSession() {
        authService.login("admin", "admin123");
        authService.logout();
        assertFalse(authService.isLoggedIn());
    }

    /** US1.3 - Viewing slots without login should throw exception. */
    @Test
    void testViewSlotsRequiresLogin() {
        assertThrows(IllegalStateException.class, () ->
                appointmentService.getAvailableSlots(LocalDate.now())
        );
    }

    /** US1.3 - Logged-in admin should see 8 available slots. */
    @Test
    void testViewAvailableSlotsWhenLoggedIn() {
        authService.login("admin", "admin123");
        List<TimeSlot> slots = appointmentService.getAvailableSlots(LocalDate.now());
        assertEquals(8, slots.size());
    }

    // ========== Sprint 2 - Booking Tests ==========

    /** US2.1 - A valid booking should return status Confirmed. */
    @Test
    void testBookAppointmentSuccess() {
        Appointment appointment = appointmentService.bookAppointment(
                "A1", LocalDateTime.now().plusDays(1), 60, 3
        );
        assertNotNull(appointment);
        assertEquals("Confirmed", appointment.getStatus());
    }

    /** US2.2 - Duration above 120 min should be rejected. */
    @Test
    void testBookAppointmentExceedsDuration() {
        assertThrows(IllegalArgumentException.class, () ->
                appointmentService.bookAppointment("A2", LocalDateTime.now().plusDays(1), 150, 2)
        );
    }

    /** US2.2 - Duration exactly at limit (120 min) should be accepted. */
    @Test
    void testBookAppointmentAtMaxDuration() {
        Appointment appointment = appointmentService.bookAppointment(
                "A3", LocalDateTime.now().plusDays(1), 120, 2
        );
        assertEquals("Confirmed", appointment.getStatus());
    }

    /** US2.3 - Participants above 5 should be rejected. */
    @Test
    void testBookAppointmentExceedsParticipants() {
        assertThrows(IllegalArgumentException.class, () ->
                appointmentService.bookAppointment("A4", LocalDateTime.now().plusDays(1), 60, 10)
        );
    }

    /** US2.3 - Exactly 5 participants should be accepted. */
    @Test
    void testBookAppointmentAtMaxParticipants() {
        Appointment appointment = appointmentService.bookAppointment(
                "A5", LocalDateTime.now().plusDays(1), 60, 5
        );
        assertEquals("Confirmed", appointment.getStatus());
    }

    /** US4.1 - Cancelling a future appointment should set status to Cancelled. */
    @Test
    void testCancelFutureAppointment() {
        appointmentService.bookAppointment("A6", LocalDateTime.now().plusDays(1), 60, 2);
        appointmentService.cancelAppointment("A6");
        List<Appointment> all = appointmentService.getAllAppointments();
        assertEquals("Cancelled", all.get(0).getStatus());
    }

    /** US4.1 - Cancelling a non-existent appointment should throw exception. */
    @Test
    void testCancelNonExistentAppointment() {
        assertThrows(IllegalArgumentException.class, () ->
                appointmentService.cancelAppointment("FAKE_ID")
        );
    }
}