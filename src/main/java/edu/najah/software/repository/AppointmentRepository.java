package edu.najah.software.repository;

import java.util.ArrayList;
import java.util.List;

import edu.najah.software.domain.Appointment;

/**
 * Repository class for managing appointments.
 * 
 * This class stores appointment objects
 * and provides methods to save and retrieve them.
 * 
 * @author Lojain
 * @version 1.0
 */
public class AppointmentRepository {

    private List<Appointment> appointments = new ArrayList<>();

    /**
     * Saves an appointment to the repository.
     * 
     * @param appointment the appointment to save
     */
    public void save(Appointment appointment) {
        appointments.add(appointment);
    }

    /**
     * Returns all stored appointments.
     * 
     * @return list of appointments
     */
    public List<Appointment> getAll() {
        return appointments;
    }
}