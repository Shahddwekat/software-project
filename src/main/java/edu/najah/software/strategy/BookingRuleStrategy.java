package edu.najah.software.strategy;

import edu.najah.software.domain.Appointment;

/**
 * Strategy interface for booking rule validation.
 * Each implementation represents one specific booking rule.
 * Follows the Strategy design pattern to allow flexible rule enforcement.
 *
 * @author Team
 * @version 1.0
 */
public interface BookingRuleStrategy {

    /**
     * Validates whether the given appointment satisfies this rule.
     *
     * @param appointment the appointment to validate
     * @return true if the appointment passes this rule, false otherwise
     */
    boolean isValid(Appointment appointment);

    /**
     * Returns a human-readable error message when this rule is violated.
     *
     * @return the error message
     */
    String getErrorMessage();
}
