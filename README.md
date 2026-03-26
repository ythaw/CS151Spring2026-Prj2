# CS151Spring2026-Prj2
# University Registration System

## Overview

This project is a console-based University Registration System implemented in Java. It allows users to manage students, professors, courses, and sections within a centralized system. The system supports account creation, authentication, course enrollment, and schedule management while enforcing constraints such as prerequisites, schedule conflicts, and capacity limits.

The system is designed following object-oriented principles and adheres closely to the provided UML diagram.

---

## Design

The system is structured into multiple packages:

### 1. `system`

* **RegistrationSystem**

  * Central controller of the application
  * Manages collections of:

    * Students
    * Professors
    * Courses
    * Sections
  * Handles core operations such as:

    * Authentication
    * Enrollment
    * Dropping sections
    * Listing sections
    * System summary

---

### 2. `model`

Contains core domain classes:

* **Account (abstract)**

  * Base class for all users
  * Handles login, status, and account details

* **StudentAccount**

  * Manages enrolled sections and completed courses
  * Handles enrollment validation:

    * Schedule conflicts
    * Prerequisites
    * Capacity

* **ProfessorAccount**

  * Manages teaching sections
  * Calculates teaching load

* **Course**

  * Stores course details and prerequisites

* **Section**

  * Represents a course offering
  * Contains:

    * Instructor
    * Schedule
    * Enrolled students
    * Capacity

* **Schedule**

  * Defines meeting days, time (in minutes), and location
  * Supports conflict detection

---

### 3. `exceptions`

Custom exceptions used for validation:

* `InactiveEntityException`
* `ScheduleConflictException`
* `PrerequisiteNotMetException`
* `CourseFullException`

These exceptions ensure that invalid operations do not crash the system and provide meaningful error messages.

---

### 4. `ui`

* **Menu**

  * Handles all user interaction
  * Displays menus and processes input
  * Communicates with `RegistrationSystem`

---

## Design Considerations

* Separation of concerns:

  * UI (`Menu`) handles input/output
  * `RegistrationSystem` handles logic
  * Model classes encapsulate behavior

* Data integrity:

  * Relationships between students, sections, and professors are maintained consistently

* Validation:

  * Enrollment checks are handled inside `StudentAccount`

* Scalability:

  * System supports up to **100 instances per entity type**
  * Prevents overflow with error messages

---

## Installation Instructions

### Prerequisites

* Java JDK (version 8 or higher)
* VS Code or any Java IDE

### Steps

1. Clone the repository:

```bash
git clone https://github.com/ythaw/CS151Spring2026-Prj2.git
```

2. Navigate to the project directory:

```bash
cd project-folder
```

3. Compile the project:

```bash
cd src
javac main/Main.java
```

4. Run the program:

```bash
java main.Main
```

---

## Usage

When the program starts, the main menu is displayed. Users can:

* Log in as a student or professor
* Create new accounts
* View and manage sections
* Enroll in or drop sections (students)
* View teaching assignments (professors)

### Key Features

* Authentication system
* Section enrollment with validation:

  * Schedule conflicts
  * Prerequisite checks
  * Capacity limits
* Dynamic listing of sections by course or term
* System summary reporting

### Exit Feature

Users can exit the program at any prompt by typing:

```
exit
```

---

## Contributions

| Name          | Contributions                                                                                  |
| ------------- | ---------------------------------------------------------------------------------------------- |
| Name          |                                                                                                |
| Name          |                                                                                                |
| Name          |                                                                                                |
| Name          |                                                                                                |

---

## Notes

* Schedule times are stored as minutes from midnight for easier comparison and conflict detection.
* The system is designed to be modular and extensible for future enhancements.
* All exceptions are handled gracefully to prevent system crashes.

---

## Future Improvements

* GUI interface instead of console-based UI
* Persistent storage (database or file system)
* More advanced scheduling features
* Admin-level controls and analytics

---
