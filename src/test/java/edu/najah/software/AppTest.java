package edu.najah.software;

import edu.najah.software.domain.Appointment;
import edu.najah.software.domain.TimeSlot;
import edu.najah.software.observer.NotificationObserver;
import edu.najah.software.observer.NotificationService;
import edu.najah.software.repository.AppointmentRepository;
import edu.najah.software.repository.InMemoryAppointmentRepository;
import edu.najah.software.service.AppointmentService;
import edu.najah.software.service.AppointmentServiceImpl;
import edu.najah.software.service.AuthService;
import edu.najah.software.service.SimpleAuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for Sprint 1, 2 and 3.
 *
 * @author Team
 * @version 1.0
 */
@ExtendWith(MockitoExtension.class)
public class AppTest {

    private AuthService authService;
    private AppointmentService appointmentService;

    // Mockito creates a fake NotificationService — no real messages sent
    @Mock
    private NotificationService notificationService;

    // Fresh setup before every test
    @BeforeEach
    void setUp() {
        AppointmentRepository repository = new InMemoryAppointmentRepository();
        authService = new SimpleAuthService();
        appointmentService = new AppointmentServiceImpl(repository, authService);
    }

    // --- Sprint 1: Auth ---

    @Test
    void testLoginWithValidCredentials() {
        assertTrue(authService.login("admin", "admin123"));
    }

    @Test
    void testLoginWithInvalidCredentials() {
        assertFalse(authService.login("admin", "wrongpassword"));
    }

    @Test
    void testLogoutClearsSession() {
        authService.login("admin", "admin123");
        authService.logout();
        assertFalse(authService.isLoggedIn());
    }

    @Test
    void testViewSlotsRequiresLogin() {
        assertThrows(IllegalStateException.class, () ->
                appointmentService.getAvailableSlots(LocalDate.now())
        );
    }

    @Test
    void testViewAvailableSlotsWhenLoggedIn() {
        authService.login("admin", "admin123");
        List<TimeSlot> slots = appointmentService.getAvailableSlots(LocalDate.now());
        assertEquals(8, slots.size());
    }

    // --- Sprint 2: Booking Rules ---

    @Test
    void testBookAppointmentSuccess() {
        Appointment appointment = appointmentService.bookAppointment(
                "A1", LocalDateTime.now().plusDays(1), 60, 3
        );
        assertNotNull(appointment);
        assertEquals("Confirmed", appointment.getStatus());
    }

    @Test
    void testBookAppointmentExceedsDuration() {
        assertThrows(IllegalArgumentException.class, () ->
                appointmentService.bookAppointment("A2", LocalDateTime.now().plusDays(1), 150, 2)
        );
    }

    @Test
    void testBookAppointmentAtMaxDuration() {
        Appointment appointment = appointmentService.bookAppointment(
                "A3", LocalDateTime.now().plusDays(1), 120, 2
        );
        assertEquals("Confirmed", appointment.getStatus());
    }

    @Test
    void testBookAppointmentExceedsParticipants() {
        assertThrows(IllegalArgumentException.class, () ->
                appointmentService.bookAppointment("A4", LocalDateTime.now().plusDays(1), 60, 10)
        );
    }

    @Test
    void testBookAppointmentAtMaxParticipants() {
        Appointment appointment = appointmentService.bookAppointment(
                "A5", LocalDateTime.now().plusDays(1), 60, 5
        );
        assertEquals("Confirmed", appointment.getStatus());
    }

    @Test
    void testCancelFutureAppointment() {
        appointmentService.bookAppointment("A6", LocalDateTime.now().plusDays(1), 60, 2);
        appointmentService.cancelAppointment("A6");
        List<Appointment> all = appointmentService.getAllAppointments();
        assertEquals("Cancelled", all.get(0).getStatus());
    }

    @Test
    void testCancelNonExistentAppointment() {
        assertThrows(IllegalArgumentException.class, () ->
                appointmentService.cancelAppointment("FAKE_ID")
        );
    }

    // --- Sprint 3: Notifications ---

    @Test
    void testObserverNotifiedOnBooking() {
        NotificationObserver observer = new NotificationObserver(notificationService);
        appointmentService.addObserver(observer);

        Appointment appointment = appointmentService.bookAppointment(
                "A7", LocalDateTime.now().plusDays(1), 60, 2
        );

        // Should be called once when booking
        verify(notificationService, times(1))
                .sendNotification(eq(appointment), anyString());
    }

    @Test
    void testObserverNotifiedOnCancellation() {
        NotificationObserver observer = new NotificationObserver(notificationService);
        appointmentService.addObserver(observer);

        appointmentService.bookAppointment("A8", LocalDateTime.now().plusDays(1), 60, 2);
        appointmentService.cancelAppointment("A8");

        // Once for booking, once for cancellation
        verify(notificationService, times(2))
                .sendNotification(any(Appointment.class), anyString());
    }

    @Test
    void testNoNotificationWithoutObserver() {
        appointmentService.bookAppointment("A9", LocalDateTime.now().plusDays(1), 60, 2);

        // No observer registered so should never be called
        verify(notificationService, never())
                .sendNotification(any(Appointment.class), anyString());
    }

    @Test
    void testRemovedObserverNotNotified() {
        NotificationObserver observer = new NotificationObserver(notificationService);
        appointmentService.addObserver(observer);
        appointmentService.removeObserver(observer);

        appointmentService.bookAppointment("A10", LocalDateTime.now().plusDays(1), 60, 2);

        // Observer was removed so should get nothing
        verify(notificationService, never())
                .sendNotification(any(Appointment.class), anyString());
    }
}