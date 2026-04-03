package edu.najah.software.domain.appointmenttype;

import edu.najah.software.domain.Appointment;

/**
 * Rule for virtual appointments.
 * Max duration 90 minutes, max 5 participants
 * @author shahd
 * @version 1.0
 */
public class VirtualAppointmentRule implements AppointmentTypeRule {

    @Override
    public AppointmentType getType() {
        return AppointmentType.VIRTUAL;
    }

    // Virtual meetings can have more people  max 90 min and max 5
    @Override
    public boolean isValid(Appointment appointment) {
        return appointment.getDuration() <= 90 && appointment.getParticipants() <= 5;
    }

    @Override
    public String getErrorMessage() {
        return "Virtual appointments must be at most 90 minutes with max 5 participants.";
    }
}