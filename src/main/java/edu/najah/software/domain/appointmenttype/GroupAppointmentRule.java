package edu.najah.software.domain.appointmenttype;

import edu.najah.software.domain.Appointment;

/**
 * Rule for group appointments
 * Max duration 120 minutes, between 3 and 5 participants
 * @author shahd
 * @version 1.0
 */
public class GroupAppointmentRule implements AppointmentTypeRule {

    @Override
    public AppointmentType getType() {
        return AppointmentType.GROUP;
    }

    // Groups need at least 3 people and no more than 5 max 120 min
    @Override
    public boolean isValid(Appointment appointment) {
        return appointment.getDuration() <= 120
                && appointment.getParticipants() >= 3
                && appointment.getParticipants() <= 5;
    }

    @Override
    public String getErrorMessage() {
        return "Group appointments must be at most 120 minutes with between 3 and 5 participants.";
    }
}