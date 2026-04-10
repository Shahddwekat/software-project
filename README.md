# Appointment Scheduling System

A Java based scheduling system we built for our Software Engineering course. We built it sprint by sprint, kind of like how real agile teams work.

## Team
- Shahd Dwekat
- Raana Sa'deldin

---

## What It Does

Users can book, view, modify, and cancel appointments. Admins get extra control, they can manage any appointment in the system, not just their own.

There's also a notification system that fires whenever something changes, booking, modification, or cancellation. We even hooked up real email notifications using Gmail SMTP so if you enter a recipient email when booking they actually get an email.

We support 7 appointment types, each with its own rules that get enforced automatically:

| Type | Max Duration | Max Participants |
|---|---|---|
| Urgent | 30 min | 2 |
| Follow-Up | 60 min | 2 |
| Assessment | 120 min | 3 |
| Virtual | 90 min | 5 |
| In-Person | 60 min | 3 |
| Individual | 60 min | 1 |
| Group | 120 min | 3–5 |

---

## How We Built It

We split the work across 5 sprints:

- **Sprint 1** : admin login/logout and viewing available time slots
- **Sprint 2** : booking logic with rules for max duration (2 hours) and max participants (5 people)
- **Sprint 3** : notification system so users get notified when things change
- **Sprint 4** : modify and cancel appointments with a guard that blocks touching past appointments
- **Sprint 5** : all 7 appointment types with their own validation rules

---

## Architecture

We used a layered architecture to keep things clean and separated:

```
Domain Layer      →  Appointment, User, Administrator, TimeSlot
Repository Layer  →  In memory storage (acts like a simple database)
Service Layer     →  All the business logic lives here
Observer Layer    →  Handles notifications
Strategy Layer    →  Handles booking rule validation
GUI Layer         →  Swing desktop interface
AI Layer          →  AI powered appointment summary using Groq / Llama 3
```

---

## Design Patterns

**Strategy Pattern** : for booking rules. Each rule is its own class that implements a common interface, adding new rules doesn't touch the booking logic at all.

**Observer Pattern** : for notifications. Any listener that subscribes gets notified automatically when something happens. We mocked this in tests using Mockito so no real messages get sent during testing.

---

## Extra Features

Beyond what the spec required, we added a few things:

- **Desktop GUI** built with Java Swing. Has a sidebar, login screen, booking form, appointment table and a notification log.
- **Real email notifications** via Gmail SMTP. Enter a recipient email when booking and they get an actual HTML email with the appointment details.
- **AI Summary** powered by Groq / Llama 3. Generates a smart analysis of all current appointments. Reads from a `groq.properties` file for the API key.

---

## Tech Stack

- Java 17
- Maven
- JUnit 5
- Mockito
- JaCoCo
- JavaMail (for email notifications)
- Swing (for the GUI)
- Groq API / Llama 3 (for AI summary)

---

## Project Structure

```
src/
├── main/java/edu/najah/software/
│   ├── domain/           core entities and appointment types
│   ├── repository/       in-memory data storage
│   ├── service/          booking and auth logic
│   ├── observer/         notification interfaces and classes
│   ├── strategy/         booking rule implementations
│   ├── gui/              Swing desktop interface
│   └── ai/               AI summary service
└── test/
    └── AppTest.java      78 unit tests covering all 5 sprints
```

---

## Running It

```bash
# run all tests and generate coverage report
mvn test

# open coverage report
target/site/jacoco/index.html
```

## Test Coverage

We have 78 unit tests covering all 5 sprints with **86% overall instruction coverage** measured by JaCoCo.

| Package | Coverage |
|---|---|
| service | 95% |
| observer | 90% |
| domain.appointmenttype | 92% |
| domain | 94% |
| repository | 100% |
| strategy | 100% |

```bash

# run the desktop app
# right-click AppointmentGUI.java in Eclipse → Run As → Java Application
```

**Login credentials:**
- Admin: `admin` / `admin123`
- User: `user` / `user123`

---

## Notes

We wrote comments in every class explaining not just what the code does but why we wrote it that way. The goal was that anyone reading it later( including us ) can understand the thinking without having to guess. Everything is documented with Javadoc. We used an in-memory list instead of a real database
