package edu.najah.software.service;

import edu.najah.software.domain.Appointment;
import edu.najah.software.domain.TimeSlot;
import edu.najah.software.domain.appointmenttype.AppointmentType;
import edu.najah.software.observer.Observer;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * The main interface for everything appointment related
 * It covers viewing slots booking modifying cancelling
 * and managing who gets notified when things happen
 *
 * @author Team
 * @version 1.0
 */
public interface AppointmentService {

    List<TimeSlot> getAvailableSlots(LocalDate date);
    Appointment bookAppointment(String id, LocalDateTime dateTime, int duration,int participants, AppointmentType type);
    Appointment bookAppointment(String id, LocalDateTime dateTime, int duration, int participants);

 
    void cancelAppointment(String appointmentId);
    void modifyAppointment(String appointmentId, LocalDateTime newDateTime);
    void adminCancelAppointment(String appointmentId);    
    void adminModifyAppointment(String appointmentId, LocalDateTime newDateTime);   
    
    List<Appointment> getAllAppointments();
    void addObserver(Observer observer);
    void removeObserver(Observer observer);
}