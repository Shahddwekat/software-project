package edu.najah.software;

import edu.najah.software.domain.Appointment;
import edu.najah.software.domain.TimeSlot;
import edu.najah.software.domain.appointmenttype.AppointmentType;
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
 * Unit tests for Sprint 1, 2, 3, 4, 5
 * @author Team
 * @version 1.0
 */
@ExtendWith(MockitoExtension.class)
public class AppTest {

    private AuthService authService;
    private AppointmentService appointmentService;
    @Mock
    private NotificationService notificationService;
    @BeforeEach
    void setUp() {
        AppointmentRepository repository = new InMemoryAppointmentRepository();
        authService = new SimpleAuthService();
        appointmentService = new AppointmentServiceImpl(repository, authService);
    }

    // Sprint 1: Auth

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

    //Sprint 2: Booking Rules 

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
        assertEquals("Cancelled", appointmentService.getAllAppointments().get(0).getStatus());
    }

    @Test
    void testCancelNonExistentAppointment() {
        assertThrows(IllegalArgumentException.class, () ->
                appointmentService.cancelAppointment("FAKE_ID")
        );
    }

    // Sprint 3: Notifications

    @Test
    void testObserverNotifiedOnBooking() {
        NotificationObserver observer = new NotificationObserver(notificationService);
        appointmentService.addObserver(observer);
        Appointment appointment = appointmentService.bookAppointment(
                "A7", LocalDateTime.now().plusDays(1), 60, 2
        );
        verify(notificationService, times(1)).sendNotification(eq(appointment), anyString());
    }

    @Test
    void testObserverNotifiedOnCancellation() {
        NotificationObserver observer = new NotificationObserver(notificationService);
        appointmentService.addObserver(observer);
        appointmentService.bookAppointment("A8", LocalDateTime.now().plusDays(1), 60, 2);
        appointmentService.cancelAppointment("A8");
        verify(notificationService, times(2)).sendNotification(any(Appointment.class), anyString());
    }

    @Test
    void testNoNotificationWithoutObserver() {
        appointmentService.bookAppointment("A9", LocalDateTime.now().plusDays(1), 60, 2);
        verify(notificationService, never()).sendNotification(any(Appointment.class), anyString());
    }

    @Test
    void testRemovedObserverNotNotified() {
        NotificationObserver observer = new NotificationObserver(notificationService);
        appointmentService.addObserver(observer);
        appointmentService.removeObserver(observer);
        appointmentService.bookAppointment("A10", LocalDateTime.now().plusDays(1), 60, 2);
        verify(notificationService, never()).sendNotification(any(Appointment.class), anyString());
    }

    //Sprint 4: Modify & Cancel

    @Test
    void testUserCancelAppointment() {
        appointmentService.bookAppointment("B1", LocalDateTime.now().plusDays(1), 60, 2);
        appointmentService.cancelAppointment("B1");
        assertEquals("Cancelled", appointmentService.getAllAppointments().get(0).getStatus());
    }

    @Test
    void testUserModifyAppointment() {
        appointmentService.bookAppointment("B2", LocalDateTime.now().plusDays(1), 60, 2);
        LocalDateTime newTime = LocalDateTime.now().plusDays(3);
        appointmentService.modifyAppointment("B2", newTime);
        assertEquals(newTime, appointmentService.getAllAppointments().get(0).getDateTime());
    }

    @Test
    void testModifyNonExistentAppointment() {
        assertThrows(IllegalArgumentException.class, () ->
                appointmentService.modifyAppointment("FAKE_ID", LocalDateTime.now().plusDays(1))
        );
    }

    @Test
    void testAdminCancelAppointment() {
        appointmentService.bookAppointment("B3", LocalDateTime.now().plusDays(1), 60, 2);
        authService.login("admin", "admin123");
        appointmentService.adminCancelAppointment("B3");
        assertEquals("Cancelled", appointmentService.getAllAppointments().get(0).getStatus());
    }

    @Test
    void testAdminModifyAppointment() {
        appointmentService.bookAppointment("B4", LocalDateTime.now().plusDays(1), 60, 2);
        authService.login("admin", "admin123");
        LocalDateTime newTime = LocalDateTime.now().plusDays(5);
        appointmentService.adminModifyAppointment("B4", newTime);
        assertEquals(newTime, appointmentService.getAllAppointments().get(0).getDateTime());
    }

    @Test
    void testAdminCancelWithoutLogin() {
        appointmentService.bookAppointment("B5", LocalDateTime.now().plusDays(1), 60, 2);
        assertThrows(IllegalStateException.class, () ->
                appointmentService.adminCancelAppointment("B5")
        );
    }

    @Test
    void testAdminModifyWithoutLogin() {
        appointmentService.bookAppointment("B6", LocalDateTime.now().plusDays(1), 60, 2);
        assertThrows(IllegalStateException.class, () ->
                appointmentService.adminModifyAppointment("B6", LocalDateTime.now().plusDays(2))
        );
    }

    // Sprint 5: Appointment Types

    // Urgent appointment with valid rules
    @Test
    void testUrgentAppointmentValid() {
        Appointment a = appointmentService.bookAppointment(
                "C1", LocalDateTime.now().plusDays(1), 30, 2, AppointmentType.URGENT
        );
        assertEquals(AppointmentType.URGENT, a.getType());
        assertEquals("Confirmed", a.getStatus());
    }

    // Urgent appointment that breaks the type rule
    @Test
    void testUrgentAppointmentInvalidDuration() {
        assertThrows(IllegalArgumentException.class, () ->
                appointmentService.bookAppointment(
                        "C2", LocalDateTime.now().plusDays(1), 60, 2, AppointmentType.URGENT)
        );
    }

    // Followup appointment with valid rules
    @Test
    void testFollowUpAppointmentValid() {
        Appointment a = appointmentService.bookAppointment(
                "C3", LocalDateTime.now().plusDays(1), 60, 2, AppointmentType.FOLLOW_UP
        );
        assertEquals(AppointmentType.FOLLOW_UP, a.getType());
    }

    // Virtual appointment with valid rules
    @Test
    void testVirtualAppointmentValid() {
        Appointment a = appointmentService.bookAppointment(
                "C4", LocalDateTime.now().plusDays(1), 90, 5, AppointmentType.VIRTUAL
        );
        assertEquals(AppointmentType.VIRTUAL, a.getType());
    }

    // Group appointment with valid rules
    @Test
    void testGroupAppointmentValid() {
        Appointment a = appointmentService.bookAppointment(
                "C5", LocalDateTime.now().plusDays(1), 120, 4, AppointmentType.GROUP
        );
        assertEquals(AppointmentType.GROUP, a.getType());
    }

    // Group appointment with too few participants
    @Test
    void testGroupAppointmentTooFewParticipants() {
        assertThrows(IllegalArgumentException.class, () ->
                appointmentService.bookAppointment(
                        "C6", LocalDateTime.now().plusDays(1), 60, 2, AppointmentType.GROUP)
        );
    }

    // Individual appointment with valid rules
    @Test
    void testIndividualAppointmentValid() {
        Appointment a = appointmentService.bookAppointment(
                "C7", LocalDateTime.now().plusDays(1), 60, 1, AppointmentType.INDIVIDUAL
        );
        assertEquals(AppointmentType.INDIVIDUAL, a.getType());
    }

    // Individual appointment with more than 1 participant
    @Test
    void testIndividualAppointmentInvalidParticipants() {
        assertThrows(IllegalArgumentException.class, () ->
                appointmentService.bookAppointment(
                        "C8", LocalDateTime.now().plusDays(1), 60, 2, AppointmentType.INDIVIDUAL)
        );
    }

    // Assessment appointment with valid rules
    @Test
    void testAssessmentAppointmentValid() {
        Appointment a = appointmentService.bookAppointment(
                "C9", LocalDateTime.now().plusDays(1), 120, 3, AppointmentType.ASSESSMENT
        );
        assertEquals(AppointmentType.ASSESSMENT, a.getType());
    }

    // In person appointment with valid rules
    @Test
    void testInPersonAppointmentValid() {
        Appointment a = appointmentService.bookAppointment(
                "C10", LocalDateTime.now().plusDays(1), 60, 3, AppointmentType.IN_PERSON
        );
        assertEquals(AppointmentType.IN_PERSON, a.getType());
    }
}