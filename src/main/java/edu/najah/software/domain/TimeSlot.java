package edu.najah.software.domain;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;

/**
 * Represents a time slot with a specific date, start time, and end time.
 * Used to track available and booked appointment windows.
 *
 * @author Team
 * @version 1.0
 */
public class TimeSlot {

    /** The date of this time slot. */
    private final LocalDate date;

    /** The start time of this time slot. */
    private final LocalTime start;

    /** The end time of this time slot. */
    private final LocalTime end;

    /**
     * Constructs a new TimeSlot.
     *
     * @param date  the date of the slot
     * @param start the start time
     * @param end   the end time
     */
    public TimeSlot(LocalDate date, LocalTime start, LocalTime end) {
        this.date = date;
        this.start = start;
        this.end = end;
    }

    /**
     * Returns the date of this slot.
     * @return date
     */
    public LocalDate getDate() { return date; }

    /**
     * Returns the start time of this slot.
     * @return start time
     */
    public LocalTime getStart() { return start; }

    /**
     * Returns the end time of this slot.
     * @return end time
     */
    public LocalTime getEnd() { return end; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TimeSlot)) return false;
        TimeSlot timeSlot = (TimeSlot) o;
        return Objects.equals(date, timeSlot.date)
                && Objects.equals(start, timeSlot.start)
                && Objects.equals(end, timeSlot.end);
    }

    @Override
    public int hashCode() {
        return Objects.hash(date, start, end);
    }

    @Override
    public String toString() {
        return date + " " + start + "-" + end;
    }
}
