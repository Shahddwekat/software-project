package edu.najah.software.domain;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;

/**
 * Represents a specific time slot for appointments.
 * 
 * A time slot contains a date,
 * a start time, and an end time.
 * 
 * It is used to determine available
 * and booked appointment periods.
 * 
 * @author raana
 * @version 1.0
 */
public class TimeSlot {

    /** The date of the time slot */
    private final LocalDate date;

    /** The start time of the slot */
    private final LocalTime start;

    /** The end time of the slot */
    private final LocalTime end;

    /**
     * Constructs a new TimeSlot object.
     * 
     * @param date the slot date
     * @param start the slot start time
     * @param end the slot end time
     */
    public TimeSlot(LocalDate date,
                    LocalTime start,
                    LocalTime end) {

        this.date = date;
        this.start = start;
        this.end = end;
    }

    /**
     * Returns the date of the time slot.
     * 
     * @return slot date
     */
    public LocalDate getDate() {
        return date;
    }

    /**
     * Returns the start time.
     * 
     * @return slot start time
     */
    public LocalTime getStart() {
        return start;
    }

    /**
     * Returns the end time.
     * 
     * @return slot end time
     */
    public LocalTime getEnd() {
        return end;
    }

    /**
     * Compares this TimeSlot object
     * with another object.
     * 
     * Two time slots are equal if they
     * have the same date, start time,
     * and end time.
     * 
     * @param o object to compare
     * @return true if equal, otherwise false
     */
    @Override
    public boolean equals(Object o) {

        if (this == o) {
            return true;
        }

        if (!(o instanceof TimeSlot)) {
            return false;
        }

        TimeSlot timeSlot = (TimeSlot) o;

        return Objects.equals(date, timeSlot.date)
                && Objects.equals(start, timeSlot.start)
                && Objects.equals(end, timeSlot.end);
    }

    /**
     * Returns the hash code value
     * of this TimeSlot object.
     * 
     * @return hash code
     */
    @Override
    public int hashCode() {

        return Objects.hash(date, start, end);
    }

    /**
     * Returns a string representation
     * of the time slot.
     * 
     * @return formatted time slot string
     */
    @Override
    public String toString() {

        return date + " " + start + "-" + end;
    }
}