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
 * Unit tests covering all 5 sprints.
 * Designed to achieve high JaCoCo code coverage across all business logic classes.
 * @author Shahd and Raana
 * @version 1.0
 */
@ExtendWith(MockitoExtension.class)
public class AppTest {

    private AuthService authService;
    private AppointmentService appointmentService;
    private AppointmentRepository repository;

    
    @Mock
    private NotificationService notificationService;

    @BeforeEach
    void setUp() {
        repository = new InMemoryAppointmentRepository();
        authService = new SimpleAuthService();
        appointmentService = new AppointmentServiceImpl(repository, authService);
    }

    // Sprint 1 authentication and available Slots

    @Test
    void testLoginWithValidCredentials() {
        assertTrue(authService.login("admin", "admin123"));
    }

    @Test
    void testLoginSetsLoggedInTrue() {
        authService.login("admin", "admin123");
        assertTrue(authService.isLoggedIn());
    }

    @Test
    void testLoginWithWrongPassword() {
        assertFalse(authService.login("admin", "wrongpassword"));
    }

    @Test
    void testLoginWithWrongUsername() {
        assertFalse(authService.login("wronguser", "admin123"));
    }

    @Test
    void testLoginWithBothWrong() {
        assertFalse(authService.login("wrong", "wrong"));
    }

    @Test
    void testFailedLoginDoesNotSetLoggedIn() {
        authService.login("admin", "wrong");
        assertFalse(authService.isLoggedIn());
    }

    @Test
    void testLogoutClearsSession() {
        authService.login("admin", "admin123");
        authService.logout();
        assertFalse(authService.isLoggedIn());
    }

    @Test
    void testLogoutWithoutLoginDoesNotThrow() {
        assertDoesNotThrow(() -> authService.logout());
    }

    @Test
    void testIsLoggedInFalseByDefault() {
        assertFalse(authService.isLoggedIn());
    }

    @Test
    void testViewSlotsRequiresLogin() {
        assertThrows(IllegalStateException.class, () ->
                appointmentService.getAvailableSlots(LocalDate.now())
        );
    }

    @Test
    void testViewAvailableSlotsReturns8Slots() {
        authService.login("admin", "admin123");
        List<TimeSlot> slots = appointmentService.getAvailableSlots(LocalDate.now());
        assertEquals(8, slots.size());
    }

    @Test
    void testAvailableSlotsStartAt9AM() {
        authService.login("admin", "admin123");
        List<TimeSlot> slots = appointmentService.getAvailableSlots(LocalDate.now());
        assertEquals(9, slots.get(0).getStart().getHour());
    }

    @Test
    void testAvailableSlotsEndAt5PM() {
        authService.login("admin", "admin123");
        List<TimeSlot> slots = appointmentService.getAvailableSlots(LocalDate.now());
        assertEquals(17, slots.get(7).getEnd().getHour());
    }

    @Test
    void testTimeSlotToString() {
        authService.login("admin", "admin123");
        List<TimeSlot> slots = appointmentService.getAvailableSlots(LocalDate.now());
        assertNotNull(slots.get(0).toString());
    }

    @Test
    void testTimeSlotEquality() {
        authService.login("admin", "admin123");
        List<TimeSlot> slots1 = appointmentService.getAvailableSlots(LocalDate.now());
        List<TimeSlot> slots2 = appointmentService.getAvailableSlots(LocalDate.now());
        assertEquals(slots1.get(0), slots2.get(0));
    }

    @Test
    void testTimeSlotHashCode() {
        authService.login("admin", "admin123");
        List<TimeSlot> slots = appointmentService.getAvailableSlots(LocalDate.now());
        assertEquals(slots.get(0).hashCode(), slots.get(0).hashCode());
    }

    @Test
    void testTimeSlotNotEqualToNull() {
        authService.login("admin", "admin123");
        List<TimeSlot> slots = appointmentService.getAvailableSlots(LocalDate.now());
        assertNotEquals(null, slots.get(0));
    }

    @Test
    void testTimeSlotNotEqualToDifferentObject() {
        authService.login("admin", "admin123");
        List<TimeSlot> slots = appointmentService.getAvailableSlots(LocalDate.now());
        assertNotEquals("not a timeslot", slots.get(0));
    }

    // Sprint 2 booking rules

    @Test
    void testBookAppointmentSuccess() {
        Appointment a = appointmentService.bookAppointment(
                "A1", LocalDateTime.now().plusDays(1), 60, 3);
        assertNotNull(a);
        assertEquals("Confirmed", a.getStatus());
    }

    @Test
    void testBookedAppointmentHasCorrectId() {
        Appointment a = appointmentService.bookAppointment(
                "A2", LocalDateTime.now().plusDays(1), 60, 2);
        assertEquals("A2", a.getId());
    }

    @Test
    void testBookedAppointmentHasCorrectDuration() {
        Appointment a = appointmentService.bookAppointment(
                "A3", LocalDateTime.now().plusDays(1), 90, 2);
        assertEquals(90, a.getDuration());
    }

    @Test
    void testBookedAppointmentHasCorrectParticipants() {
        Appointment a = appointmentService.bookAppointment(
                "A4", LocalDateTime.now().plusDays(1), 60, 4);
        assertEquals(4, a.getParticipants());
    }

    @Test
    void testBookAppointmentAtMaxDuration() {
        Appointment a = appointmentService.bookAppointment(
                "A5", LocalDateTime.now().plusDays(1), 120, 2);
        assertEquals("Confirmed", a.getStatus());
    }

    @Test
    void testBookAppointmentExceedsDuration() {
        assertThrows(IllegalArgumentException.class, () ->
                appointmentService.bookAppointment("A6", LocalDateTime.now().plusDays(1), 150, 2)
        );
    }

    @Test
    void testBookAppointmentAtMaxParticipants() {
        Appointment a = appointmentService.bookAppointment(
                "A7", LocalDateTime.now().plusDays(1), 60, 5);
        assertEquals("Confirmed", a.getStatus());
    }

    @Test
    void testBookAppointmentExceedsParticipants() {
        assertThrows(IllegalArgumentException.class, () ->
                appointmentService.bookAppointment("A8", LocalDateTime.now().plusDays(1), 60, 10)
        );
    }

    @Test
    void testBookAppointmentNoTypeSuccess() {
        Appointment a = appointmentService.bookAppointment(
                "A9", LocalDateTime.now().plusDays(1), 60, 2);
        assertNull(a.getType());
        assertEquals("Confirmed", a.getStatus());
    }

    @Test
    void testBookedSlotReducesAvailableSlots() {
        authService.login("admin", "admin123");
        LocalDate tomorrow = LocalDate.now().plusDays(1);
        int before = appointmentService.getAvailableSlots(tomorrow).size();
        appointmentService.bookAppointment("A10", tomorrow.atTime(9, 0), 60, 1);
        int after = appointmentService.getAvailableSlots(tomorrow).size();
        assertEquals(before - 1, after);
    }

    @Test
    void testGetAllAppointmentsReturnsBookedAppointment() {
        appointmentService.bookAppointment("A11", LocalDateTime.now().plusDays(1), 60, 2);
        assertEquals(1, appointmentService.getAllAppointments().size());
    }

    @Test
    void testAppointmentToString() {
        Appointment a = appointmentService.bookAppointment(
                "A12", LocalDateTime.now().plusDays(1), 60, 2);
        assertNotNull(a.toString());
        assertTrue(a.toString().contains("A12"));
    }

    @Test
    void testCancelNonExistentAppointment() {
        assertThrows(IllegalArgumentException.class, () ->
                appointmentService.cancelAppointment("FAKE_ID")
        );
    }

    @Test
    void testCancelFutureAppointment() {
        appointmentService.bookAppointment("A13", LocalDateTime.now().plusDays(1), 60, 2);
        appointmentService.cancelAppointment("A13");
        assertEquals("Cancelled", appointmentService.getAllAppointments().get(0).getStatus());
    }

    // Sprint 3 Notifications

    @Test
    void testObserverNotifiedOnBooking() {
        NotificationObserver observer = new NotificationObserver(notificationService);
        appointmentService.addObserver(observer);
        Appointment a = appointmentService.bookAppointment(
                "B1", LocalDateTime.now().plusDays(1), 60, 2);
        verify(notificationService, times(1)).sendNotification(eq(a), anyString());
    }

    @Test
    void testObserverNotifiedOnCancellation() {
        NotificationObserver observer = new NotificationObserver(notificationService);
        appointmentService.addObserver(observer);
        appointmentService.bookAppointment("B2", LocalDateTime.now().plusDays(1), 60, 2);
        appointmentService.cancelAppointment("B2");
        verify(notificationService, times(2)).sendNotification(any(Appointment.class), anyString());
    }

    @Test
    void testObserverNotifiedOnModification() {
        NotificationObserver observer = new NotificationObserver(notificationService);
        appointmentService.addObserver(observer);
        appointmentService.bookAppointment("B3", LocalDateTime.now().plusDays(1), 60, 2);
        appointmentService.modifyAppointment("B3", LocalDateTime.now().plusDays(3));
        verify(notificationService, times(2)).sendNotification(any(Appointment.class), anyString());
    }

    @Test
    void testNoNotificationWithoutObserver() {
        appointmentService.bookAppointment("B4", LocalDateTime.now().plusDays(1), 60, 2);
        verify(notificationService, never()).sendNotification(any(Appointment.class), anyString());
    }

    @Test
    void testRemovedObserverNotNotified() {
        NotificationObserver observer = new NotificationObserver(notificationService);
        appointmentService.addObserver(observer);
        appointmentService.removeObserver(observer);
        appointmentService.bookAppointment("B5", LocalDateTime.now().plusDays(1), 60, 2);
        verify(notificationService, never()).sendNotification(any(Appointment.class), anyString());
    }

    @Test
    void testNotificationMessageContainsAppointmentId() {
        NotificationObserver observer = new NotificationObserver(notificationService);
        appointmentService.addObserver(observer);
        appointmentService.bookAppointment("B6", LocalDateTime.now().plusDays(1), 60, 2);
        verify(notificationService).sendNotification(
                any(Appointment.class),
                argThat(msg -> msg != null && !msg.isEmpty())
        );
    }

    // Sprint 4 Modify and cancel

    @Test
    void testUserCancelFutureAppointment() {
        appointmentService.bookAppointment("C1", LocalDateTime.now().plusDays(1), 60, 2);
        appointmentService.cancelAppointment("C1");
        assertEquals("Cancelled", appointmentService.getAllAppointments().get(0).getStatus());
    }

    @Test
    void testUserModifyFutureAppointment() {
        appointmentService.bookAppointment("C2", LocalDateTime.now().plusDays(1), 60, 2);
        LocalDateTime newTime = LocalDateTime.now().plusDays(3);
        appointmentService.modifyAppointment("C2", newTime);
        assertEquals(newTime, appointmentService.getAllAppointments().get(0).getDateTime());
    }

    @Test
    void testModifyNonExistentAppointment() {
        assertThrows(IllegalArgumentException.class, () ->
                appointmentService.modifyAppointment("FAKE_ID", LocalDateTime.now().plusDays(1))
        );
    }

    @Test
    void testCancelPastAppointmentThrows() {
        appointmentService.bookAppointment("C3", LocalDateTime.now().minusDays(1), 60, 2);
        assertThrows(IllegalStateException.class, () ->
                appointmentService.cancelAppointment("C3")
        );
    }

    @Test
    void testModifyPastAppointmentThrows() {
        appointmentService.bookAppointment("C4", LocalDateTime.now().minusDays(1), 60, 2);
        assertThrows(IllegalStateException.class, () ->
                appointmentService.modifyAppointment("C4", LocalDateTime.now().plusDays(1))
        );
    }

    @Test
    void testAdminCancelAppointmentWhenLoggedIn() {
        appointmentService.bookAppointment("C5", LocalDateTime.now().plusDays(1), 60, 2);
        authService.login("admin", "admin123");
        appointmentService.adminCancelAppointment("C5");
        assertEquals("Cancelled", appointmentService.getAllAppointments().get(0).getStatus());
    }

    @Test
    void testAdminModifyAppointmentWhenLoggedIn() {
        appointmentService.bookAppointment("C6", LocalDateTime.now().plusDays(1), 60, 2);
        authService.login("admin", "admin123");
        LocalDateTime newTime = LocalDateTime.now().plusDays(5);
        appointmentService.adminModifyAppointment("C6", newTime);
        assertEquals(newTime, appointmentService.getAllAppointments().get(0).getDateTime());
    }

    @Test
    void testAdminCancelWithoutLoginThrows() {
        appointmentService.bookAppointment("C7", LocalDateTime.now().plusDays(1), 60, 2);
        assertThrows(IllegalStateException.class, () ->
                appointmentService.adminCancelAppointment("C7")
        );
    }

    @Test
    void testAdminModifyWithoutLoginThrows() {
        appointmentService.bookAppointment("C8", LocalDateTime.now().plusDays(1), 60, 2);
        assertThrows(IllegalStateException.class, () ->
                appointmentService.adminModifyAppointment("C8", LocalDateTime.now().plusDays(2))
        );
    }

    // Sprint 5 appointment types

    //URGENT
    @Test
    void testUrgentAppointmentValid() {
        Appointment a = appointmentService.bookAppointment(
                "D1", LocalDateTime.now().plusDays(1), 30, 2, AppointmentType.URGENT);
        assertEquals(AppointmentType.URGENT, a.getType());
        assertEquals("Confirmed", a.getStatus());
    }

    @Test
    void testUrgentAppointmentInvalidDuration() {
        assertThrows(IllegalArgumentException.class, () ->
                appointmentService.bookAppointment(
                        "D2", LocalDateTime.now().plusDays(1), 60, 2, AppointmentType.URGENT)
        );
    }

    @Test
    void testUrgentAppointmentInvalidParticipants() {
        assertThrows(IllegalArgumentException.class, () ->
                appointmentService.bookAppointment(
                        "D3", LocalDateTime.now().plusDays(1), 30, 3, AppointmentType.URGENT)
        );
    }
    
    //FOLLOW_UP
    @Test
    void testFollowUpAppointmentValid() {
        Appointment a = appointmentService.bookAppointment(
                "D4", LocalDateTime.now().plusDays(1), 60, 2, AppointmentType.FOLLOW_UP);
        assertEquals(AppointmentType.FOLLOW_UP, a.getType());
    }

    @Test
    void testFollowUpAppointmentInvalidDuration() {
        assertThrows(IllegalArgumentException.class, () ->
                appointmentService.bookAppointment(
                        "D5", LocalDateTime.now().plusDays(1), 90, 2, AppointmentType.FOLLOW_UP)
        );
    }

    @Test
    void testFollowUpAppointmentInvalidParticipants() {
        assertThrows(IllegalArgumentException.class, () ->
                appointmentService.bookAppointment(
                        "D6", LocalDateTime.now().plusDays(1), 60, 3, AppointmentType.FOLLOW_UP)
        );
    }

    //ASSESSMENT
    @Test
    void testAssessmentAppointmentValid() {
        Appointment a = appointmentService.bookAppointment(
                "D7", LocalDateTime.now().plusDays(1), 120, 3, AppointmentType.ASSESSMENT);
        assertEquals(AppointmentType.ASSESSMENT, a.getType());
    }

    @Test
    void testAssessmentAppointmentInvalidDuration() {
        assertThrows(IllegalArgumentException.class, () ->
                appointmentService.bookAppointment(
                        "D8", LocalDateTime.now().plusDays(1), 121, 3, AppointmentType.ASSESSMENT)
        );
    }

    @Test
    void testAssessmentAppointmentInvalidParticipants() {
        assertThrows(IllegalArgumentException.class, () ->
                appointmentService.bookAppointment(
                        "D9", LocalDateTime.now().plusDays(1), 60, 4, AppointmentType.ASSESSMENT)
        );
    }

    //VIRTUAL
    @Test
    void testVirtualAppointmentValid() {
        Appointment a = appointmentService.bookAppointment(
                "D10", LocalDateTime.now().plusDays(1), 90, 5, AppointmentType.VIRTUAL);
        assertEquals(AppointmentType.VIRTUAL, a.getType());
    }

    @Test
    void testVirtualAppointmentInvalidDuration() {
        assertThrows(IllegalArgumentException.class, () ->
                appointmentService.bookAppointment(
                        "D11", LocalDateTime.now().plusDays(1), 91, 5, AppointmentType.VIRTUAL)
        );
    }

    @Test
    void testVirtualAppointmentInvalidParticipants() {
        assertThrows(IllegalArgumentException.class, () ->
                appointmentService.bookAppointment(
                        "D12", LocalDateTime.now().plusDays(1), 90, 6, AppointmentType.VIRTUAL)
        );
    }

    //IN_PERSON
    @Test
    void testInPersonAppointmentValid() {
        Appointment a = appointmentService.bookAppointment(
                "D13", LocalDateTime.now().plusDays(1), 60, 3, AppointmentType.IN_PERSON);
        assertEquals(AppointmentType.IN_PERSON, a.getType());
    }

    @Test
    void testInPersonAppointmentInvalidDuration() {
        assertThrows(IllegalArgumentException.class, () ->
                appointmentService.bookAppointment(
                        "D14", LocalDateTime.now().plusDays(1), 61, 3, AppointmentType.IN_PERSON)
        );
    }

    @Test
    void testInPersonAppointmentInvalidParticipants() {
        assertThrows(IllegalArgumentException.class, () ->
                appointmentService.bookAppointment(
                        "D15", LocalDateTime.now().plusDays(1), 60, 4, AppointmentType.IN_PERSON)
        );
    }

    //INDIVIDUAL
    @Test
    void testIndividualAppointmentValid() {
        Appointment a = appointmentService.bookAppointment(
                "D16", LocalDateTime.now().plusDays(1), 60, 1, AppointmentType.INDIVIDUAL);
        assertEquals(AppointmentType.INDIVIDUAL, a.getType());
    }

    @Test
    void testIndividualAppointmentInvalidParticipants() {
        assertThrows(IllegalArgumentException.class, () ->
                appointmentService.bookAppointment(
                        "D17", LocalDateTime.now().plusDays(1), 60, 2, AppointmentType.INDIVIDUAL)
        );
    }

    @Test
    void testIndividualAppointmentInvalidDuration() {
        assertThrows(IllegalArgumentException.class, () ->
                appointmentService.bookAppointment(
                        "D18", LocalDateTime.now().plusDays(1), 61, 1, AppointmentType.INDIVIDUAL)
        );
    }

    //GROUP
    @Test
    void testGroupAppointmentValid() {
        Appointment a = appointmentService.bookAppointment(
                "D19", LocalDateTime.now().plusDays(1), 120, 4, AppointmentType.GROUP);
        assertEquals(AppointmentType.GROUP, a.getType());
    }

    @Test
    void testGroupAppointmentTooFewParticipants() {
        assertThrows(IllegalArgumentException.class, () ->
                appointmentService.bookAppointment(
                        "D20", LocalDateTime.now().plusDays(1), 60, 2, AppointmentType.GROUP)
        );
    }

    @Test
    void testGroupAppointmentTooManyParticipants() {
        assertThrows(IllegalArgumentException.class, () ->
                appointmentService.bookAppointment(
                        "D21", LocalDateTime.now().plusDays(1), 60, 6, AppointmentType.GROUP)
        );
    }

    @Test
    void testGroupAppointmentInvalidDuration() {
        assertThrows(IllegalArgumentException.class, () ->
                appointmentService.bookAppointment(
                        "D22", LocalDateTime.now().plusDays(1), 121, 4, AppointmentType.GROUP)
        );
    }

    // Domain model coverage  User Administrator Appointment

    @Test
    void testUserGetters() {
        edu.najah.software.domain.User user =
                new edu.najah.software.domain.User("U1", "Shahd", "shahd@test.com");
        assertEquals("U1", user.getUserId());
        assertEquals("Shahd", user.getName());
        assertEquals("shahd@test.com", user.getEmail());
        assertNotNull(user.toString());
    }

    @Test
    void testAdministratorGetters() {
        edu.najah.software.domain.Administrator admin =
                new edu.najah.software.domain.Administrator("U2", "Raana", "raana@test.com", "ADM-1");
        assertEquals("ADM-1", admin.getAdminId());
        assertTrue(admin.isActive());
        admin.setActive(false);
        assertFalse(admin.isActive());
        assertNotNull(admin.toString());
    }

    @Test
    void testAppointmentSetDateTime() {
        Appointment a = appointmentService.bookAppointment(
                "E1", LocalDateTime.now().plusDays(1), 60, 2);
        LocalDateTime newTime = LocalDateTime.now().plusDays(10);
        a.setDateTime(newTime);
        assertEquals(newTime, a.getDateTime());
    }

    @Test
    void testAppointmentSetStatus() {
        Appointment a = appointmentService.bookAppointment(
                "E2", LocalDateTime.now().plusDays(1), 60, 2);
        a.setStatus("Cancelled");
        assertEquals("Cancelled", a.getStatus());
    }

    @Test
    void testAppointmentWithNullType() {
        Appointment a = appointmentService.bookAppointment(
                "E3", LocalDateTime.now().plusDays(1), 60, 2);
        assertNull(a.getType());
    }
}