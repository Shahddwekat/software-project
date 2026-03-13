package edu.najah.software.strategy;

import edu.najah.software.domain.Appointment;

/**
 * Booking rule that enforces a maximum number of participants per appointment.
 * Rejects any appointment that exceeds the allowed participant limit.
 *
 * @author Team
 * @version 1.0
 */
public class ParticipantLimitRule implements BookingRuleStrategy {

    /** Maximum allowed number of participants per appointment. */
    private static final int MAX_PARTICIPANTS = 5;

    /**
     * Checks whether the number of participants is within the allowed limit.
     *
     * @param appointment the appointment to validate
     * @return true if participants are within limit, false otherwise
     */
    @Override
    public boolean isValid(Appointment appointment) {
        return appointment.getParticipants() <= MAX_PARTICIPANTS;
    }

    /**
     * Returns the error message for a participant limit violation.
     *
     * @return error message string
     */
    @Override
    public String getErrorMessage() {
        return "Number of participants exceeds the maximum allowed limit of " + MAX_PARTICIPANTS + ".";
    }
}
