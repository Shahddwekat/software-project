package edu.najah.software.domain;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;

public class TimeSlot {
    private final LocalDate date;
    private final LocalTime start;
    private final LocalTime end;

    public TimeSlot(LocalDate date, LocalTime start, LocalTime end) {
        this.date = date;
        this.start = start;
        this.end = end;
    }

    public LocalDate getDate() { return date; }
    public LocalTime getStart() { return start; }
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