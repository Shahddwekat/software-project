package edu.najah.software.domain.appointmenttype;

import edu.najah.software.domain.Appointment;
/**
 * Rule for individual appointments
 * Max duration 60 minutes, exactly 1 participant
 * @author raana
 * @version 1.0
 */
public class IndividualAppointmentRule implements AppointmentTypeRule {

    @Override
    public AppointmentType getType() {
        return AppointmentType.INDIVIDUAL;
    }

    // Individual means one on one  max 60 min and only 1 person
    @Override
    public boolean isValid(Appointment appointment) {
        return appointment.getDuration() <= 60 && appointment.getParticipants() == 1;
    }

    @Override
    public String getErrorMessage() {
        return "Individual appointments must be at most 60 minutes with exactly 1 participant.";
    }
}