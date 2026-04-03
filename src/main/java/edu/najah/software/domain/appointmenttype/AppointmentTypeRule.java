package edu.najah.software.domain.appointmenttype;

import edu.najah.software.domain.Appointment;

/**
 * Interface for type-specific booking rules.
 * Each appointment type has its own rule implementation
 * @author shahd
 * @version 1.0
 */
public interface AppointmentTypeRule {

    // Returns the type this rule applies to
    AppointmentType getType();

    // Validates the appointment against type specific rules
    boolean isValid(Appointment appointment);
    // Returns the error message if the rule is violated
    String getErrorMessage();
}