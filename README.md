# Appointment Scheduling System

A Java based scheduling system we built for our Software Engineering course. The idea was to build it step by step across multiple sprints kind of like how real agile teams work.

## Team

- Shahd
- Raana

---

## What This Project Does

The system lets users book, view, modify, and cancel appointments. Admins have extra control they can manage any appointment in the system not just their own. We also built in a notification system that sends reminders whenever something changes with an appointment.

We covered a bunch of appointment types too: Urgent, Follow-Up, Assessment, Virtual, In-Person, Individual, and Group — each one has its own rules that get enforced automatically when you try to book.

---

## How We Built It

We split the work across 5 sprints:

- **Sprint 1** : got the admin login/logout working and set up viewing available time slots
- **Sprint 2** : added the actual booking logic with rules for max duration (2 hours) and max participants (5 people)
- **Sprint 3** : wired up the notification system so users get notified when appointments are booked or cancelled
- **Sprint 4** : added the ability to modify and cancel appointments, with a guard that prevents touching past appointments
- **Sprint 5** : added all 7 appointment types, each with their own specific validation rules

---

## Architecture

We used a layered architecture to keep things organized and separated:

```
Domain Layer      →  Appointment, User, Administrator, TimeSlot
Repository Layer  →  In-memory storage (acts like a simple database)
Service Layer     →  All the business logic lives here
Observer Layer    →  Handles notifications
Strategy Layer    →  Handles booking rule validation
```

---

## Design Patterns

We used two design patterns that were required for the project:

**Strategy Pattern** :for booking rules. Instead of hardcoding all the rules inside the booking logic, each rule is its own class that implements a common interface. This made it really easy to add type specific rules later without breaking anything.

**Observer Pattern** : for notifications. Any listener that subscribes to the service automatically gets notified when something happens. We mocked this in tests using Mockito so we didn't need to actually send messages during testing.

---

## Tech Stack

- Java 17
- Maven
- JUnit 5 for testing
- Mockito for mocking the notification service
- JaCoCo for test coverage

---

## Project Structure

```
src/
├── main/java/edu/najah/software/
│   ├── domain/                  core entities and appointment types
│   ├── repository/              in-memory data storage
│   ├── service/                 booking and auth logic
│   ├── observer/                notification interfaces and classes
│   └── strategy/                booking rule implementations
└── test/
    └── AppTest.java             unit tests for all 5 sprints (~35 tests)
```

---

## Running It

```bash
# run all tests
mvn test

# generate coverage report (opens in target/site/jacoco/index.html)
mvn test
```

---

## Notes

You'll notice that in every class we added comments explaining how things work and why we wrote them that way not just what the code does but the thinking behind it. We did this to make it easier for ourselves to come back to later, and so that any other developer reading the code can actually understand what we were going for without having to guess.

Everything is documented with Javadoc. We used an in-memory list instead of a real database since this is phase 1 — that can be swapped out later without touching the service or domain layers since we coded to interfaces throughout.
