package edu.najah.software.domain;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;

/**
 * Represents specific time window on a given day
 * We use this to track which slots are available and which ones are taken
 * EX: March 13, 2026 from 9:00 to 10:00.
 * @author Team
 * @version 1.0
 */
public class TimeSlot {

    /** The date this slot belongs to*/
    private final LocalDate date;

    /** The time this slot starts */
    private final LocalTime start;

    /** The time this slot end */
    private final LocalTime end;

    /**
     * Create a new time slot with a date start time and end time
     * @param date the date of this slot
     * @param start what time the slot starts
     * @param end what time the slot ends
     */
    public TimeSlot(LocalDate date, LocalTime start, LocalTime end) {
        this.date = date;
        this.start = start;
        this.end = end;
    }

    /**
     * Returns the date of this slot
     * @return the date
     */
    public LocalDate getDate() { return date; }

    /**
     * Returns the start time of this slot
     * @return the start time
     */
    public LocalTime getStart() { 
    	return start; 
    	}

    /**
     * returns the end time of this slot
     * @return the end time
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