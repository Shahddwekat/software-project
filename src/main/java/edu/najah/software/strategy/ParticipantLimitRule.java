package edu.najah.software.strategy;

import edu.najah.software.domain.Appointment;

/**
 * Makes sure no appointment has more than 5 participants
 * If someone tries to book with 6 or more people, we reject it
 * @author Team
 * @version 1.0
 */
public class ParticipantLimitRule implements BookingRuleStrategy {

    /** The maximum number of people allowed in one appointment */
    private static final int MAX_PARTICIPANTS = 5;

    /**
     * Checks if the number of participants is within the allowed limit
     * @param appointment the appointment to check
     * @return true if participants are 5 or fewer
     */
    @Override
    public boolean isValid(Appointment appointment) {
        return appointment.getParticipants() <= MAX_PARTICIPANTS;
    }

    /**
     * Returns the message we show when too many people are booked
     * @return the error message
     */
    @Override
    public String getErrorMessage() {
        return "Number of participants exceeds the maximum allowed limit of " + MAX_PARTICIPANTS + ".";
    }
}