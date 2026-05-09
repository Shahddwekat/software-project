package edu.najah.software.domain.appointmenttype;

import edu.najah.software.domain.Appointment;

/**
 * Rule for assessment appointments
 * Max duration 120 minutes, max 3 participants
 * @author shahd
 * @version 1.0
 */
public class AssessmentAppointmentRule implements AppointmentTypeRule {

    @Override
    public AppointmentType getType() {
        return AppointmentType.ASSESSMENT;
    }
    // Assessments can be longer max 120 min and max 3 people
    @Override
    public boolean isValid(Appointment appointment) {
        return appointment.getDuration() <= 120 && appointment.getParticipants() <= 3;
    }

    @Override
    public String getErrorMessage() {
        return "Assessment appointments must be at most 120 minutes with max 3 participants.";
    }
}