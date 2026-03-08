package edu.najah.software.domain;

import java.time.LocalDateTime;

public class Appointment {

    private String id;
    private LocalDateTime dateTime;
    private int duration;
    private int participants;
    private String status;

    public Appointment(String id, LocalDateTime dateTime, int duration, int participants) {
        this.id = id;
        this.dateTime = dateTime;
        this.duration = duration;
        this.participants = participants;
        this.status = "Confirmed";
    }

    public int getDuration() {
        return duration;
    }

    public int getParticipants() {
        return participants;
    }

    public String getStatus() {
        return status;
    }
}