package edu.najah.software.domain.appointmenttype;

import edu.najah.software.domain.Appointment;

/**
 * Rule for urgent appointments
 * Max duration 30 minutes, max 2 participants
 * @author shahd
 * @version 1.0
 */
public class UrgentAppointmentRule implements AppointmentTypeRule {

    @Override
    public AppointmentType getType() {
        return AppointmentType.URGENT;
    }

    // Urgent appointments must be short max 30 min and max 2 people
    @Override
    public boolean isValid(Appointment appointment) {
        return appointment.getDuration() <= 30 && appointment.getParticipants() <= 2;
    }

    @Override
    public String getErrorMessage() {
        return "Urgent appointments must be at most 30 minutes with max 2 participants.";
    }
}