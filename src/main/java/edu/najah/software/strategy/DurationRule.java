package edu.najah.software.strategy;

import edu.najah.software.domain.Appointment;

/**
 * Makes sure no appointment lasts longer than 2 hours
 * Anything over 120 minutes gets rejected
 * @author Team
 * @version 1.0
 */
public class DurationRule implements BookingRuleStrategy {

    /** The longest an appointment is allowed to be, in minutes */
    private static final int MAX_DURATION = 120;
    /**
     * Checks if the appointment duration is within the allowed limit
     * @param appointment the appointment to check
     * @return true if the duration is 120 minutes or less
     */
    @Override
    public boolean isValid(Appointment appointment) {
        return appointment.getDuration() <= MAX_DURATION;
    }

    /**
     * Returns the message we show when an appointment is too long
     * @return the error message
     */
    @Override
    public String getErrorMessage() {
        return "Appointment duration exceeds the maximum allowed limit of " + MAX_DURATION + " minutes.";
    }
}