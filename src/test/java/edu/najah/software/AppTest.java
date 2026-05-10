package edu.najah.software;

import edu.najah.software.domain.Appointment;
import edu.najah.software.domain.TimeSlot;
import edu.najah.software.domain.appointmenttype.*;
import edu.najah.software.observer.NotificationObserver;
import edu.najah.software.observer.NotificationService;
import edu.najah.software.repository.AppointmentRepository;
import edu.najah.software.repository.InMemoryAppointmentRepository;
import edu.najah.software.service.AppointmentService;
import edu.najah.software.service.AppointmentServiceImpl;
import edu.najah.software.service.AuthService;
import edu.najah.software.service.SimpleAuthService;
import edu.najah.software.strategy.DurationRule;
import edu.najah.software.strategy.ParticipantLimitRule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Comprehensive unit tests covering all sprints and all business logic classes.
 * @author Shahd and Raana
 * @version 2.0
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

    // ===================== SimpleAuthService =====================

    @Test
    void testLoginAdminValid() {
        assertTrue(authService.login("admin", "admin123"));
    }

    @Test
    void testLoginAdminSetsLoggedIn() {
        authService.login("admin", "admin123");
        assertTrue(authService.isLoggedIn());
    }

    @Test
    void testLoginAdminSetsAdminRole() {
        authService.login("admin", "admin123");
        assertTrue(authService.isAdmin());
    }

    @Test
    void testLoginUserValid() {
        assertTrue(authService.login("user", "user123"));
    }

    @Test
    void testLoginUserSetsLoggedIn() {
        authService.login("user", "user123");
        assertTrue(authService.isLoggedIn());
    }

    @Test
    void testLoginUserNotAdmin() {
        authService.login("user", "user123");
        assertFalse(authService.isAdmin());
    }

    @Test
    void testLoginWrongPassword() {
        assertFalse(authService.login("admin", "wrong"));
    }

    @Test
    void testLoginWrongUsername() {
        assertFalse(authService.login("wrong", "admin123"));
    }

    @Test
    void testLoginBothWrong() {
        assertFalse(authService.login("wrong", "wrong"));
    }

    @Test
    void testFailedLoginNotLoggedIn() {
        authService.login("admin", "wrong");
        assertFalse(authService.isLoggedIn());
    }

    @Test
    void testFailedLoginNotAdmin() {
        authService.login("admin", "wrong");
        assertFalse(authService.isAdmin());
    }

    @Test
    void testLogoutClearsLogin() {
        authService.login("admin", "admin123");
        authService.logout();
        assertFalse(authService.isLoggedIn());
    }

    @Test
    void testLogoutClearsAdminRole() {
        authService.login("admin", "admin123");
        authService.logout();
        assertFalse(authService.isAdmin());
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
    void testIsAdminFalseByDefault() {
        assertFalse(authService.isAdmin());
    }

    // ===================== TimeSlot =====================

    @Test
    void testTimeSlotGetDate() {
        LocalDate date = LocalDate.now();
        TimeSlot slot = new TimeSlot(date, LocalTime.of(9, 0), LocalTime.of(10, 0));
        assertEquals(date, slot.getDate());
    }

    @Test
    void testTimeSlotGetStart() {
        TimeSlot slot = new TimeSlot(LocalDate.now(), LocalTime.of(9, 0), LocalTime.of(10, 0));
        assertEquals(LocalTime.of(9, 0), slot.getStart());
    }

    @Test
    void testTimeSlotGetEnd() {
        TimeSlot slot = new TimeSlot(LocalDate.now(), LocalTime.of(9, 0), LocalTime.of(10, 0));
        assertEquals(LocalTime.of(10, 0), slot.getEnd());
    }

    @Test
    void testTimeSlotToString() {
        TimeSlot slot = new TimeSlot(LocalDate.of(2025, 1, 1), LocalTime.of(9, 0), LocalTime.of(10, 0));
        assertNotNull(slot.toString());
        assertTrue(slot.toString().contains("09:00"));
    }

    @Test
    void testTimeSlotEqualsSameValues() {
        LocalDate date = LocalDate.now();
        TimeSlot s1 = new TimeSlot(date, LocalTime.of(9, 0), LocalTime.of(10, 0));
        TimeSlot s2 = new TimeSlot(date, LocalTime.of(9, 0), LocalTime.of(10, 0));
        assertEquals(s1, s2);
    }

    @Test
    void testTimeSlotEqualsSameObject() {
        TimeSlot slot = new TimeSlot(LocalDate.now(), LocalTime.of(9, 0), LocalTime.of(10, 0));
        assertEquals(slot, slot);
    }

    @Test
    void testTimeSlotNotEqualsNull() {
        TimeSlot slot = new TimeSlot(LocalDate.now(), LocalTime.of(9, 0), LocalTime.of(10, 0));
        assertNotEquals(null, slot);
    }

    @Test
    void testTimeSlotNotEqualsDifferentObject() {
        TimeSlot slot = new TimeSlot(LocalDate.now(), LocalTime.of(9, 0), LocalTime.of(10, 0));
        assertNotEquals("string", slot);
    }

    @Test
    void testTimeSlotNotEqualsDifferentDate() {
        TimeSlot s1 = new TimeSlot(LocalDate.now(), LocalTime.of(9, 0), LocalTime.of(10, 0));
        TimeSlot s2 = new TimeSlot(LocalDate.now().plusDays(1), LocalTime.of(9, 0), LocalTime.of(10, 0));
        assertNotEquals(s1, s2);
    }

    @Test
    void testTimeSlotNotEqualsDifferentStart() {
        LocalDate date = LocalDate.now();
        TimeSlot s1 = new TimeSlot(date, LocalTime.of(9, 0), LocalTime.of(10, 0));
        TimeSlot s2 = new TimeSlot(date, LocalTime.of(10, 0), LocalTime.of(11, 0));
        assertNotEquals(s1, s2);
    }

    @Test
    void testTimeSlotHashCodeConsistent() {
        TimeSlot slot = new TimeSlot(LocalDate.now(), LocalTime.of(9, 0), LocalTime.of(10, 0));
        assertEquals(slot.hashCode(), slot.hashCode());
    }

    @Test
    void testTimeSlotHashCodeEqualForEqualSlots() {
        LocalDate date = LocalDate.now();
        TimeSlot s1 = new TimeSlot(date, LocalTime.of(9, 0), LocalTime.of(10, 0));
        TimeSlot s2 = new TimeSlot(date, LocalTime.of(9, 0), LocalTime.of(10, 0));
        assertEquals(s1.hashCode(), s2.hashCode());
    }

    // ===================== InMemoryAppointmentRepository =====================

    @Test
    void testRepositorySaveAndGetAll() {
        Appointment a = new Appointment("R1", LocalDateTime.now().plusDays(1), 60, 2);
        repository.save(a);
        assertEquals(1, repository.getAll().size());
    }

    @Test
    void testRepositoryGetAllEmpty() {
        assertTrue(repository.getAll().isEmpty());
    }

    @Test
    void testRepositorySaveMultiple() {
        repository.save(new Appointment("R1", LocalDateTime.now().plusDays(1), 60, 2));
        repository.save(new Appointment("R2", LocalDateTime.now().plusDays(2), 60, 2));
        assertEquals(2, repository.getAll().size());
    }

    @Test
    void testRepositorySaveBookedSlot() {
        LocalDate date = LocalDate.now().plusDays(1);
        TimeSlot slot = new TimeSlot(date, LocalTime.of(9, 0), LocalTime.of(10, 0));
        repository.saveBookedSlot(slot);
        assertEquals(1, repository.getBookedSlotsForDate(date).size());
    }

    @Test
    void testRepositoryGetBookedSlotsForDateEmpty() {
        assertTrue(repository.getBookedSlotsForDate(LocalDate.now()).isEmpty());
    }

    @Test
    void testRepositoryGetBookedSlotsForCorrectDate() {
        LocalDate date1 = LocalDate.now().plusDays(1);
        LocalDate date2 = LocalDate.now().plusDays(2);
        repository.saveBookedSlot(new TimeSlot(date1, LocalTime.of(9, 0), LocalTime.of(10, 0)));
        repository.saveBookedSlot(new TimeSlot(date2, LocalTime.of(9, 0), LocalTime.of(10, 0)));
        assertEquals(1, repository.getBookedSlotsForDate(date1).size());
        assertEquals(1, repository.getBookedSlotsForDate(date2).size());
    }

    // ===================== DurationRule =====================

    @Test
    void testDurationRuleValidAt120() {
        DurationRule rule = new DurationRule();
        assertTrue(rule.isValid(new Appointment("DR1", LocalDateTime.now().plusDays(1), 120, 2)));
    }

    @Test
    void testDurationRuleValidBelow120() {
        DurationRule rule = new DurationRule();
        assertTrue(rule.isValid(new Appointment("DR2", LocalDateTime.now().plusDays(1), 60, 2)));
    }

    @Test
    void testDurationRuleInvalidAbove120() {
        DurationRule rule = new DurationRule();
        assertFalse(rule.isValid(new Appointment("DR3", LocalDateTime.now().plusDays(1), 121, 2)));
    }

    @Test
    void testDurationRuleErrorMessage() {
        DurationRule rule = new DurationRule();
        assertNotNull(rule.getErrorMessage());
        assertTrue(rule.getErrorMessage().contains("120"));
    }

    // ===================== ParticipantLimitRule =====================

    @Test
    void testParticipantRuleValidAt5() {
        ParticipantLimitRule rule = new ParticipantLimitRule();
        assertTrue(rule.isValid(new Appointment("PR1", LocalDateTime.now().plusDays(1), 60, 5)));
    }

    @Test
    void testParticipantRuleValidBelow5() {
        ParticipantLimitRule rule = new ParticipantLimitRule();
        assertTrue(rule.isValid(new Appointment("PR2", LocalDateTime.now().plusDays(1), 60, 3)));
    }

    @Test
    void testParticipantRuleInvalidAbove5() {
        ParticipantLimitRule rule = new ParticipantLimitRule();
        assertFalse(rule.isValid(new Appointment("PR3", LocalDateTime.now().plusDays(1), 60, 6)));
    }

    @Test
    void testParticipantRuleErrorMessage() {
        ParticipantLimitRule rule = new ParticipantLimitRule();
        assertNotNull(rule.getErrorMessage());
        assertTrue(rule.getErrorMessage().contains("5"));
    }

    // ===================== Appointment Type Rules =====================

    @Test
    void testUrgentRuleValid() {
        UrgentAppointmentRule rule = new UrgentAppointmentRule();
        assertTrue(rule.isValid(new Appointment("T1", LocalDateTime.now().plusDays(1), 30, 2, AppointmentType.URGENT)));
        assertEquals(AppointmentType.URGENT, rule.getType());
        assertNotNull(rule.getErrorMessage());
    }

    @Test
    void testUrgentRuleInvalidDuration() {
        assertFalse(new UrgentAppointmentRule().isValid(
                new Appointment("T2", LocalDateTime.now().plusDays(1), 31, 2, AppointmentType.URGENT)));
    }

    @Test
    void testUrgentRuleInvalidParticipants() {
        assertFalse(new UrgentAppointmentRule().isValid(
                new Appointment("T3", LocalDateTime.now().plusDays(1), 30, 3, AppointmentType.URGENT)));
    }

    @Test
    void testFollowUpRuleValid() {
        FollowUpAppointmentRule rule = new FollowUpAppointmentRule();
        assertTrue(rule.isValid(new Appointment("T4", LocalDateTime.now().plusDays(1), 60, 2, AppointmentType.FOLLOW_UP)));
        assertEquals(AppointmentType.FOLLOW_UP, rule.getType());
        assertNotNull(rule.getErrorMessage());
    }

    @Test
    void testFollowUpRuleInvalidDuration() {
        assertFalse(new FollowUpAppointmentRule().isValid(
                new Appointment("T5", LocalDateTime.now().plusDays(1), 61, 2, AppointmentType.FOLLOW_UP)));
    }

    @Test
    void testFollowUpRuleInvalidParticipants() {
        assertFalse(new FollowUpAppointmentRule().isValid(
                new Appointment("T6", LocalDateTime.now().plusDays(1), 60, 3, AppointmentType.FOLLOW_UP)));
    }

    @Test
    void testAssessmentRuleValid() {
        AssessmentAppointmentRule rule = new AssessmentAppointmentRule();
        assertTrue(rule.isValid(new Appointment("T7", LocalDateTime.now().plusDays(1), 120, 3, AppointmentType.ASSESSMENT)));
        assertEquals(AppointmentType.ASSESSMENT, rule.getType());
        assertNotNull(rule.getErrorMessage());
    }

    @Test
    void testAssessmentRuleInvalidDuration() {
        assertFalse(new AssessmentAppointmentRule().isValid(
                new Appointment("T8", LocalDateTime.now().plusDays(1), 121, 3, AppointmentType.ASSESSMENT)));
    }

    @Test
    void testAssessmentRuleInvalidParticipants() {
        assertFalse(new AssessmentAppointmentRule().isValid(
                new Appointment("T9", LocalDateTime.now().plusDays(1), 60, 4, AppointmentType.ASSESSMENT)));
    }

    @Test
    void testVirtualRuleValid() {
        VirtualAppointmentRule rule = new VirtualAppointmentRule();
        assertTrue(rule.isValid(new Appointment("T10", LocalDateTime.now().plusDays(1), 90, 5, AppointmentType.VIRTUAL)));
        assertEquals(AppointmentType.VIRTUAL, rule.getType());
        assertNotNull(rule.getErrorMessage());
    }

    @Test
    void testVirtualRuleInvalidDuration() {
        assertFalse(new VirtualAppointmentRule().isValid(
                new Appointment("T11", LocalDateTime.now().plusDays(1), 91, 5, AppointmentType.VIRTUAL)));
    }

    @Test
    void testVirtualRuleInvalidParticipants() {
        assertFalse(new VirtualAppointmentRule().isValid(
                new Appointment("T12", LocalDateTime.now().plusDays(1), 90, 6, AppointmentType.VIRTUAL)));
    }

    @Test
    void testInPersonRuleValid() {
        InPersonAppointmentRule rule = new InPersonAppointmentRule();
        assertTrue(rule.isValid(new Appointment("T13", LocalDateTime.now().plusDays(1), 60, 3, AppointmentType.IN_PERSON)));
        assertEquals(AppointmentType.IN_PERSON, rule.getType());
        assertNotNull(rule.getErrorMessage());
    }

    @Test
    void testInPersonRuleInvalidDuration() {
        assertFalse(new InPersonAppointmentRule().isValid(
                new Appointment("T14", LocalDateTime.now().plusDays(1), 61, 3, AppointmentType.IN_PERSON)));
    }

    @Test
    void testInPersonRuleInvalidParticipants() {
        assertFalse(new InPersonAppointmentRule().isValid(
                new Appointment("T15", LocalDateTime.now().plusDays(1), 60, 4, AppointmentType.IN_PERSON)));
    }

    @Test
    void testIndividualRuleValid() {
        IndividualAppointmentRule rule = new IndividualAppointmentRule();
        assertTrue(rule.isValid(new Appointment("T16", LocalDateTime.now().plusDays(1), 60, 1, AppointmentType.INDIVIDUAL)));
        assertEquals(AppointmentType.INDIVIDUAL, rule.getType());
        assertNotNull(rule.getErrorMessage());
    }

    @Test
    void testIndividualRuleInvalidParticipants() {
        assertFalse(new IndividualAppointmentRule().isValid(
                new Appointment("T17", LocalDateTime.now().plusDays(1), 60, 2, AppointmentType.INDIVIDUAL)));
    }

    @Test
    void testIndividualRuleInvalidDuration() {
        assertFalse(new IndividualAppointmentRule().isValid(
                new Appointment("T18", LocalDateTime.now().plusDays(1), 61, 1, AppointmentType.INDIVIDUAL)));
    }

    @Test
    void testGroupRuleValid() {
        GroupAppointmentRule rule = new GroupAppointmentRule();
        assertTrue(rule.isValid(new Appointment("T19", LocalDateTime.now().plusDays(1), 120, 4, AppointmentType.GROUP)));
        assertEquals(AppointmentType.GROUP, rule.getType());
        assertNotNull(rule.getErrorMessage());
    }

    @Test
    void testGroupRuleTooFewParticipants() {
        assertFalse(new GroupAppointmentRule().isValid(
                new Appointment("T20", LocalDateTime.now().plusDays(1), 60, 2, AppointmentType.GROUP)));
    }

    @Test
    void testGroupRuleTooManyParticipants() {
        assertFalse(new GroupAppointmentRule().isValid(
                new Appointment("T21", LocalDateTime.now().plusDays(1), 60, 6, AppointmentType.GROUP)));
    }

    @Test
    void testGroupRuleInvalidDuration() {
        assertFalse(new GroupAppointmentRule().isValid(
                new Appointment("T22", LocalDateTime.now().plusDays(1), 121, 4, AppointmentType.GROUP)));
    }

    // ===================== Appointment Domain =====================

    @Test
    void testAppointmentConstructorWithType() {
        Appointment a = new Appointment("A1", LocalDateTime.now().plusDays(1), 60, 2, AppointmentType.VIRTUAL);
        assertEquals("A1", a.getId());
        assertEquals(60, a.getDuration());
        assertEquals(2, a.getParticipants());
        assertEquals(AppointmentType.VIRTUAL, a.getType());
        assertEquals("Confirmed", a.getStatus());
    }

    @Test
    void testAppointmentConstructorWithoutType() {
        Appointment a = new Appointment("A2", LocalDateTime.now().plusDays(1), 60, 2);
        assertNull(a.getType());
        assertEquals("Confirmed", a.getStatus());
    }

    @Test
    void testAppointmentSetStatus() {
        Appointment a = new Appointment("A3", LocalDateTime.now().plusDays(1), 60, 2);
        a.setStatus("Cancelled");
        assertEquals("Cancelled", a.getStatus());
    }

    @Test
    void testAppointmentSetDateTime() {
        Appointment a = new Appointment("A4", LocalDateTime.now().plusDays(1), 60, 2);
        LocalDateTime newTime = LocalDateTime.now().plusDays(5);
        a.setDateTime(newTime);
        assertEquals(newTime, a.getDateTime());
    }

    @Test
    void testAppointmentToStringContainsId() {
        Appointment a = new Appointment("A5", LocalDateTime.now().plusDays(1), 60, 2);
        assertTrue(a.toString().contains("A5"));
    }

    @Test
    void testAppointmentToStringContainsStatus() {
        Appointment a = new Appointment("A6", LocalDateTime.now().plusDays(1), 60, 2);
        assertTrue(a.toString().contains("Confirmed"));
    }

    // ===================== User and Administrator =====================

    @Test
    void testUserGetters() {
        edu.najah.software.domain.User user = new edu.najah.software.domain.User("U1", "Shahd", "shahd@test.com");
        assertEquals("U1", user.getUserId());
        assertEquals("Shahd", user.getName());
        assertEquals("shahd@test.com", user.getEmail());
    }

    @Test
    void testUserToString() {
        edu.najah.software.domain.User user = new edu.najah.software.domain.User("U1", "Shahd", "shahd@test.com");
        assertNotNull(user.toString());
        assertTrue(user.toString().contains("Shahd"));
    }

    @Test
    void testAdministratorGetters() {
        edu.najah.software.domain.Administrator admin =
                new edu.najah.software.domain.Administrator("U2", "Raana", "raana@test.com", "ADM-1");
        assertEquals("ADM-1", admin.getAdminId());
        assertEquals("U2", admin.getUserId());
        assertEquals("Raana", admin.getName());
        assertEquals("raana@test.com", admin.getEmail());
    }

    @Test
    void testAdministratorActiveByDefault() {
        edu.najah.software.domain.Administrator admin =
                new edu.najah.software.domain.Administrator("U3", "Test", "test@test.com", "ADM-2");
        assertTrue(admin.isActive());
    }

    @Test
    void testAdministratorSetActive() {
        edu.najah.software.domain.Administrator admin =
                new edu.najah.software.domain.Administrator("U4", "Test", "test@test.com", "ADM-3");
        admin.setActive(false);
        assertFalse(admin.isActive());
    }

    @Test
    void testAdministratorToString() {
        edu.najah.software.domain.Administrator admin =
                new edu.najah.software.domain.Administrator("U5", "Raana", "raana@test.com", "ADM-4");
        assertNotNull(admin.toString());
        assertTrue(admin.toString().contains("ADM-4"));
    }

    // ===================== AppointmentServiceImpl =====================

    @Test
    void testGetAvailableSlotsRequiresLogin() {
        assertThrows(IllegalStateException.class, () ->
                appointmentService.getAvailableSlots(LocalDate.now()));
    }

    @Test
    void testGetAvailableSlotsReturns8() {
        authService.login("admin", "admin123");
        assertEquals(8, appointmentService.getAvailableSlots(LocalDate.now()).size());
    }

    @Test
    void testGetAvailableSlotsStartAt9() {
        authService.login("admin", "admin123");
        assertEquals(9, appointmentService.getAvailableSlots(LocalDate.now()).get(0).getStart().getHour());
    }

    @Test
    void testGetAvailableSlotsEndAt5() {
        authService.login("admin", "admin123");
        List<TimeSlot> slots = appointmentService.getAvailableSlots(LocalDate.now());
        assertEquals(17, slots.get(7).getEnd().getHour());
    }

    @Test
    void testBookAppointmentSuccess() {
        Appointment a = appointmentService.bookAppointment("S1", LocalDateTime.now().plusDays(1), 60, 2);
        assertEquals("Confirmed", a.getStatus());
        assertEquals("S1", a.getId());
    }

    @Test
    void testBookAppointmentExceedsDuration() {
        assertThrows(IllegalArgumentException.class, () ->
                appointmentService.bookAppointment("S2", LocalDateTime.now().plusDays(1), 150, 2));
    }

    @Test
    void testBookAppointmentExceedsParticipants() {
        assertThrows(IllegalArgumentException.class, () ->
                appointmentService.bookAppointment("S3", LocalDateTime.now().plusDays(1), 60, 10));
    }

    @Test
    void testBookAppointmentWithType() {
        Appointment a = appointmentService.bookAppointment("S4", LocalDateTime.now().plusDays(1), 30, 2, AppointmentType.URGENT);
        assertEquals(AppointmentType.URGENT, a.getType());
    }

    @Test
    void testBookAppointmentTypeRuleViolation() {
        assertThrows(IllegalArgumentException.class, () ->
                appointmentService.bookAppointment("S5", LocalDateTime.now().plusDays(1), 60, 2, AppointmentType.URGENT));
    }

    @Test
    void testBookedSlotReducesAvailable() {
        authService.login("admin", "admin123");
        LocalDate tomorrow = LocalDate.now().plusDays(1);
        int before = appointmentService.getAvailableSlots(tomorrow).size();
        appointmentService.bookAppointment("S6", tomorrow.atTime(9, 0), 60, 1);
        int after = appointmentService.getAvailableSlots(tomorrow).size();
        assertEquals(before - 1, after);
    }

    @Test
    void testGetAllAppointments() {
        appointmentService.bookAppointment("S7", LocalDateTime.now().plusDays(1), 60, 2);
        assertEquals(1, appointmentService.getAllAppointments().size());
    }

    @Test
    void testCancelFutureAppointment() {
        appointmentService.bookAppointment("S8", LocalDateTime.now().plusDays(1), 60, 2);
        appointmentService.cancelAppointment("S8");
        assertEquals("Cancelled", appointmentService.getAllAppointments().get(0).getStatus());
    }

    @Test
    void testCancelNonExistentThrows() {
        assertThrows(IllegalArgumentException.class, () ->
                appointmentService.cancelAppointment("FAKE"));
    }

    @Test
    void testCancelPastAppointmentThrows() {
        appointmentService.bookAppointment("S9", LocalDateTime.now().minusDays(1), 60, 2);
        assertThrows(IllegalStateException.class, () ->
                appointmentService.cancelAppointment("S9"));
    }

    @Test
    void testModifyFutureAppointment() {
        appointmentService.bookAppointment("S10", LocalDateTime.now().plusDays(1), 60, 2);
        LocalDateTime newTime = LocalDateTime.now().plusDays(3);
        appointmentService.modifyAppointment("S10", newTime);
        assertEquals(newTime, appointmentService.getAllAppointments().get(0).getDateTime());
    }

    @Test
    void testModifyNonExistentThrows() {
        assertThrows(IllegalArgumentException.class, () ->
                appointmentService.modifyAppointment("FAKE", LocalDateTime.now().plusDays(1)));
    }

    @Test
    void testModifyPastAppointmentThrows() {
        appointmentService.bookAppointment("S11", LocalDateTime.now().minusDays(1), 60, 2);
        assertThrows(IllegalStateException.class, () ->
                appointmentService.modifyAppointment("S11", LocalDateTime.now().plusDays(1)));
    }

    @Test
    void testAdminCancelRequiresLogin() {
        appointmentService.bookAppointment("S12", LocalDateTime.now().plusDays(1), 60, 2);
        assertThrows(IllegalStateException.class, () ->
                appointmentService.adminCancelAppointment("S12"));
    }

    @Test
    void testAdminCancelSuccess() {
        appointmentService.bookAppointment("S13", LocalDateTime.now().plusDays(1), 60, 2);
        authService.login("admin", "admin123");
        appointmentService.adminCancelAppointment("S13");
        assertEquals("Cancelled", appointmentService.getAllAppointments().get(0).getStatus());
    }

    @Test
    void testAdminModifyRequiresLogin() {
        appointmentService.bookAppointment("S14", LocalDateTime.now().plusDays(1), 60, 2);
        assertThrows(IllegalStateException.class, () ->
                appointmentService.adminModifyAppointment("S14", LocalDateTime.now().plusDays(2)));
    }

    @Test
    void testAdminModifySuccess() {
        appointmentService.bookAppointment("S15", LocalDateTime.now().plusDays(1), 60, 2);
        authService.login("admin", "admin123");
        LocalDateTime newTime = LocalDateTime.now().plusDays(5);
        appointmentService.adminModifyAppointment("S15", newTime);
        assertEquals(newTime, appointmentService.getAllAppointments().get(0).getDateTime());
    }

    // ===================== Observer / Notification =====================

    @Test
    void testObserverNotifiedOnBook() {
        NotificationObserver observer = new NotificationObserver(notificationService);
        appointmentService.addObserver(observer);
        Appointment a = appointmentService.bookAppointment("N1", LocalDateTime.now().plusDays(1), 60, 2);
        verify(notificationService, times(1)).sendNotification(eq(a), anyString());
    }

    @Test
    void testObserverNotifiedOnCancel() {
        NotificationObserver observer = new NotificationObserver(notificationService);
        appointmentService.addObserver(observer);
        appointmentService.bookAppointment("N2", LocalDateTime.now().plusDays(1), 60, 2);
        appointmentService.cancelAppointment("N2");
        verify(notificationService, times(2)).sendNotification(any(Appointment.class), anyString());
    }

    @Test
    void testObserverNotifiedOnModify() {
        NotificationObserver observer = new NotificationObserver(notificationService);
        appointmentService.addObserver(observer);
        appointmentService.bookAppointment("N3", LocalDateTime.now().plusDays(1), 60, 2);
        appointmentService.modifyAppointment("N3", LocalDateTime.now().plusDays(3));
        verify(notificationService, times(2)).sendNotification(any(Appointment.class), anyString());
    }

    @Test
    void testNoNotificationWithoutObserver() {
        appointmentService.bookAppointment("N4", LocalDateTime.now().plusDays(1), 60, 2);
        verify(notificationService, never()).sendNotification(any(Appointment.class), anyString());
    }

    @Test
    void testRemovedObserverNotNotified() {
        NotificationObserver observer = new NotificationObserver(notificationService);
        appointmentService.addObserver(observer);
        appointmentService.removeObserver(observer);
        appointmentService.bookAppointment("N5", LocalDateTime.now().plusDays(1), 60, 2);
        verify(notificationService, never()).sendNotification(any(Appointment.class), anyString());
    }

    @Test
    void testNotificationObserverUpdate() {
        NotificationObserver observer = new NotificationObserver(notificationService);
        Appointment a = new Appointment("N6", LocalDateTime.now().plusDays(1), 60, 2);
        observer.update(a, "Test message");
        verify(notificationService, times(1)).sendNotification(a, "Test message");
    }

    // ===================== All Appointment Types via Service =====================

    @Test
    void testBookUrgentValid() {
        Appointment a = appointmentService.bookAppointment("AT1", LocalDateTime.now().plusDays(1), 30, 2, AppointmentType.URGENT);
        assertEquals(AppointmentType.URGENT, a.getType());
    }

    @Test
    void testBookFollowUpValid() {
        Appointment a = appointmentService.bookAppointment("AT2", LocalDateTime.now().plusDays(1), 60, 2, AppointmentType.FOLLOW_UP);
        assertEquals(AppointmentType.FOLLOW_UP, a.getType());
    }

    @Test
    void testBookAssessmentValid() {
        Appointment a = appointmentService.bookAppointment("AT3", LocalDateTime.now().plusDays(1), 120, 3, AppointmentType.ASSESSMENT);
        assertEquals(AppointmentType.ASSESSMENT, a.getType());
    }

    @Test
    void testBookVirtualValid() {
        Appointment a = appointmentService.bookAppointment("AT4", LocalDateTime.now().plusDays(1), 90, 5, AppointmentType.VIRTUAL);
        assertEquals(AppointmentType.VIRTUAL, a.getType());
    }

    @Test
    void testBookInPersonValid() {
        Appointment a = appointmentService.bookAppointment("AT5", LocalDateTime.now().plusDays(1), 60, 3, AppointmentType.IN_PERSON);
        assertEquals(AppointmentType.IN_PERSON, a.getType());
    }

    @Test
    void testBookIndividualValid() {
        Appointment a = appointmentService.bookAppointment("AT6", LocalDateTime.now().plusDays(1), 60, 1, AppointmentType.INDIVIDUAL);
        assertEquals(AppointmentType.INDIVIDUAL, a.getType());
    }

    @Test
    void testBookGroupValid() {
        Appointment a = appointmentService.bookAppointment("AT7", LocalDateTime.now().plusDays(1), 120, 4, AppointmentType.GROUP);
        assertEquals(AppointmentType.GROUP, a.getType());
    }

    @Test
    void testBookGroupTooFew() {
        assertThrows(IllegalArgumentException.class, () ->
                appointmentService.bookAppointment("AT8", LocalDateTime.now().plusDays(1), 60, 2, AppointmentType.GROUP));
    }

    @Test
    void testBookGroupTooMany() {
        assertThrows(IllegalArgumentException.class, () ->
                appointmentService.bookAppointment("AT9", LocalDateTime.now().plusDays(1), 60, 6, AppointmentType.GROUP));
    }
}