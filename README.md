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

* **Account (abstract)** — implements **`Deactivatable`**

  * Base class for all users
  * Handles login, activation state, email/password updates, and contact display


* **StudentAccount**

  * Manages enrolled sections and completed courses
  * Handles enrollment validation:

    * Schedule conflicts
    * Prerequisites
    * Capacity

* **ProfessorAccount**

  * Manages teaching sections
  * Calculates teaching load
  * Schedule conflict checks when assigning additional sections

* **Course**

  * Stores course details and prerequisites

* **Section** — implements **`Deactivatable`**

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

### 3. `interfaces`

* **Deactivatable** — `activate`, `deactivate`, `isActive`, `getStatus` (implemented by **`Account`** and **`Section`**)
---

### 4. `exceptions`

Custom exceptions used for validation:

* `InactiveEntityException`
* `ScheduleConflictException`
* `PrerequisiteNotMetException`
* `CourseFullException`

These exceptions ensure that invalid operations do not crash the system and provide meaningful error messages.

---

### 5. `ui`

* **Menu** - console I/O

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

Sample Data can be loaded by logging into admin account and choosing "6. Load Sample Data" option

Sample logins
| Role	    | ID  	| Password |
| --------- | ----- | ------ |
| Student	  | s1	  | pass   |
| Student	  | s2	  | pass   |
| Professor | p1	  | pass   |

Admin Login
* ID - admin
* Password - admin123

When the program starts, the main menu is displayed. Users can:

* Log in as a student or professor
* Create new accounts
* View and manage sections
* Enroll in or drop sections (students)
* View teaching assignments (professors)

### Key Features

* Role-based menus
* Authentication system
* Enrollment with validation:

  * Schedule conflicts
  * Prerequisite checks
  * Capacity limits
  * Inactive section
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
| Minh Long Hang| Implement and fixing bug relate to Student function relate, implement test unit for registration|
| Thiha Ye Lin  | Built initial UML and system structure, developed early menu implementation, and contributed to testing and bug fixes.|
| Yin Phyu Phyu Thaw | Designed the overall system architecture and UML diagram. Worked mainly on Account, Course and Registration System classes. Contributed to and debugged the Menu (UI) and improved overall system stability. Identified and removed dead code, and implemented an exit-anywhere feature. Contributed to writing and organizing the README file. |
| Cong Si Van   | Implemented the ProfessorAccount and Section classes; added data validation features (preventing duplicate ID registrations and ensuring only valid data is accepted); improved overall system functionality; and contributed to comprehensive testing to ensure reliability in real-world scenarios. |                                                                                             

---

## Notes

* Schedule times are stored as minutes from midnight for easier comparison and conflict detection.
* The system is designed to be modular and extensible for future enhancements.
* All exceptions are handled gracefully to prevent system crashes.

---

## Future Improvements

* GUI interface instead of console-based UI
* Persistent storage (database or file system)

---
