package edu.najah.software.domain;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;

/**
 * Represents a time slot for scheduling appointments.
 * 
 * A time slot contains a specific date,
 * start time, and end time.
 * 
 * @author Lojain
 * @version 1.0
 */
public class TimeSlot {

    private final LocalDate date;
    private final LocalTime start;
    private final LocalTime end;

    /**
     * Constructs a new TimeSlot object.
     * 
     * @param date the date of the time slot
     * @param start the start time
     * @param end the end time
     */
    public TimeSlot(LocalDate date, LocalTime start, LocalTime end) {
        this.date = date;
        this.start = start;
        this.end = end;
    }

    /**
     * Returns the date of the time slot.
     * 
     * @return time slot date
     */
    public LocalDate getDate() {
        return date;
    }

    /**
     * Returns the start time.
     * 
     * @return start time
     */
    public LocalTime getStart() {
        return start;
    }

    /**
     * Returns the end time.
     * 
     * @return end time
     */
    public LocalTime getEnd() {
        return end;
    }

    /**
     * Compares this TimeSlot with another object.
     * 
     * Two time slots are considered equal if
     * they have the same date, start time, and end time.
     * 
     * @param o object to compare
     * @return true if objects are equal, otherwise false
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TimeSlot)) return false;

        TimeSlot timeSlot = (TimeSlot) o;

        return Objects.equals(date, timeSlot.date)
                && Objects.equals(start, timeSlot.start)
                && Objects.equals(end, timeSlot.end);
    }

    /**
     * Returns the hash code of the TimeSlot object.
     * 
     * @return hash code value
     */
    @Override
    public int hashCode() {
        return Objects.hash(date, start, end);
    }

    /**
     * Returns a string representation of the time slot.
     * 
     * @return formatted time slot string
     */
    @Override
    public String toString() {
        return date + " " + start + "-" + end;
    }
}