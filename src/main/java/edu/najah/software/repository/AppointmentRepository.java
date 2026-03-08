/*package edu.najah.software.repository;

import edu.najah.software.domain.TimeSlot;

import java.time.LocalDate;
import java.util.List;

public interface AppointmentRepository {
    List<TimeSlot> getBookedSlotsForDate(LocalDate date);
    void saveBookedSlot(TimeSlot slot);
}*/
package edu.najah.software.repository;

import java.util.ArrayList;
import java.util.List;
import edu.najah.software.domain.Appointment;

public class AppointmentRepository {

    private List<Appointment> appointments = new ArrayList<>();

    public void save(Appointment appointment) {
        appointments.add(appointment);
    }

    public List<Appointment> getAll() {
        return appointments;
    }
}