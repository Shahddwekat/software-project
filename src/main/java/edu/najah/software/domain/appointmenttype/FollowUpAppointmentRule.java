package edu.najah.software.domain.appointmenttype;

import edu.najah.software.domain.Appointment;

/**
 * Rule for follow-up appointments
 * Max duration 60 minutes, max 2 participants
 * @author Team
 * @version 1.0
 */
public class FollowUpAppointmentRule implements AppointmentTypeRule {

    @Override
    public AppointmentType getType() {
        return AppointmentType.FOLLOW_UP;
    }

    // Follow ups are short check-ins  max 60 min and max 2 people
    @Override
    public boolean isValid(Appointment appointment) {
        return appointment.getDuration() <= 60 && appointment.getParticipants() <= 2;
    }

    @Override
    public String getErrorMessage() {
        return "Follow-up appointments must be at most 60 minutes with max 2 participants.";
    }
}