package edu.najah.software.strategy;

import edu.najah.software.domain.Appointment;

/**
 * Booking rule that enforces a maximum appointment duration.
 * Rejects any appointment that exceeds the allowed duration limit.
 *
 * @author Team
 * @version 1.0
 */
public class DurationRule implements BookingRuleStrategy {

    /** Maximum allowed duration for an appointment in minutes. */
    private static final int MAX_DURATION = 120;

    /**
     * Checks whether the appointment duration is within the allowed limit.
     *
     * @param appointment the appointment to validate
     * @return true if duration is within limit, false otherwise
     */
    @Override
    public boolean isValid(Appointment appointment) {
        return appointment.getDuration() <= MAX_DURATION;
    }

    /**
     * Returns the error message for a duration rule violation.
     *
     * @return error message string
     */
    @Override
    public String getErrorMessage() {
        return "Appointment duration exceeds the maximum allowed limit of " + MAX_DURATION + " minutes.";
    }
}
