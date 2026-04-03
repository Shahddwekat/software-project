package edu.najah.software.domain.appointmenttype;

import edu.najah.software.domain.Appointment;

/**
 * Rule for in-person appointments
 * Max duration 60 minutes, max 3 participants
 * @author Team
 * @version 1.0
 */
public class InPersonAppointmentRule implements AppointmentTypeRule {

    @Override
    public AppointmentType getType() {
        return AppointmentType.IN_PERSON;
    }

    // In-person is limited by space  max 60 min and max 3 people
    @Override
    public boolean isValid(Appointment appointment) {
        return appointment.getDuration() <= 60 && appointment.getParticipants() <= 3;
    }

    @Override
    public String getErrorMessage() {
        return "In-person appointments must be at most 60 minutes with max 3 participants.";
    }
}