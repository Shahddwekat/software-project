package edu.najah.software.strategy;

import edu.najah.software.domain.Appointment;

/**
 * A simple interface that every booking rule must follow
 * We use this so we can add new rules later without touching the booking logic
 * Each rule just needs to say whether an appointment is valid or not
 * @author Team
 * @version 1.0
 */
public interface BookingRuleStrategy {

    /**
     * Checks whether the given appointment passes this rule
     * @param appointment the appointment we want to validate
     * @return true if it's valid, false if it breaks the rule
     */
    boolean isValid(Appointment appointment);

    /**
     * Returns a message explaining why the rule was broken
     * This gets shown to the user when their booking is rejected
     * @return a human-readable error message
     */
    String getErrorMessage();
}