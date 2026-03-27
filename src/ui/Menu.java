package ui;

import exceptions.CourseFullException;
import exceptions.InactiveEntityException;
import exceptions.PrerequisiteNotMetException;
import exceptions.ScheduleConflictException;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import model.Course;
import model.ProfessorAccount;
import model.Schedule;
import model.Section;
import model.StudentAccount;
import system.RegistrationSystem;

public class Menu {
    private Scanner scanner;
    private RegistrationSystem system;

    public Menu() {
        scanner = new Scanner(System.in);
        system = new RegistrationSystem();
        system.loadSampleData();
    }

    public void start() {
        boolean running = true;
        while (running) {
            running = showMainMenu();
        }
    }

    // ─────────────────────────────────────────────
    //  MAIN MENU
    // ─────────────────────────────────────────────
    private boolean showMainMenu() {
        System.out.println("\n========================================");
        System.out.println(" UNIVERSITY COURSE REGISTRATION SYSTEM");
        System.out.println("========================================");
        System.out.println("1. Student Menu");
        System.out.println("2. Professor Menu");
        System.out.println("3. Course Menu");
        System.out.println("4. Section Menu");
        System.out.println("5. Registration Menu");
        System.out.println("6. Reports Menu");
        System.out.println("7. Exit");
        System.out.print("Enter choice: ");
        String choice = scanner.nextLine();
        switch (choice) {
            case "1": showStudentMenu(); break;
            case "2": showProfessorMenu(); break;
            case "3": showCourseMenu(); break;
            case "4": showSectionMenu(); break;
            case "5": showRegistrationMenu(); break;
            case "6": showReportsMenu(); break;
            case "7": System.out.println("Goodbye!"); return false;
            default:  System.out.println("Invalid choice.");
        }
        return true;
    }

    // ─────────────────────────────────────────────
    //  1) STUDENT MENU
    // ─────────────────────────────────────────────
    private void showStudentMenu() {
        while (true) {
            System.out.println("\n=========== STUDENT MENU ===========");
            System.out.println(" 1. Add Student");
            System.out.println(" 2. Remove Student");
            System.out.println(" 3. View Student Profile");
            System.out.println(" 4. View Student Schedule (all sections)");
            System.out.println(" 5. View Student Schedule by Term");
            System.out.println(" 6. View Completed Courses");
            System.out.println(" 7. Add Completed Course");
            System.out.println(" 8. View Current Credit Load");
            System.out.println(" 9. Activate Student");
            System.out.println("10. Deactivate Student");
            System.out.println("11. Back");
            System.out.print("Enter choice: ");
            String choice = scanner.nextLine();
            switch (choice) {
                case "1":  addStudent(); break;
                case "2":  removeStudent(); break;
                case "3":  viewStudentProfile(); break;
                case "4":  viewStudentSchedule(); break;
                case "5":  viewStudentScheduleByTerm(); break;
                case "6":  viewCompletedCourses(); break;
                case "7":  addCompletedCourse(); break;
                case "8":  viewCreditLoad(); break;
                case "9":  activateStudent(); break;
                case "10": deactivateStudent(); break;
                case "11": return;
                default:   System.out.println("Invalid choice.");
            }
        }
    }

    private void addStudent() {
        System.out.print("Enter Student ID: ");
        String id = scanner.nextLine().trim();
        if (system.getStudent(id) != null) { System.out.println("Student ID already exists."); return; }
        System.out.print("Enter Name: ");
        String name = scanner.nextLine().trim();
        System.out.print("Enter Email: ");
        String email = scanner.nextLine().trim();
        System.out.print("Enter Password: ");
        String pass = scanner.nextLine().trim();
        System.out.print("Enter Major: ");
        String major = scanner.nextLine().trim();
        try {
            StudentAccount s = new StudentAccount(id, name, email, pass, major);
            system.registerStudent(s);
            System.out.println("Student added: " + s);
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void removeStudent() {
        System.out.print("Enter Student ID to remove: ");
        String id = scanner.nextLine().trim();
        StudentAccount s = system.getStudent(id);
        if (s == null) { System.out.println("Student not found."); return; }
        // Drop all sections first
        List<Section> copy = new java.util.ArrayList<>(s.getEnrolledSections());
        for (Section sec : copy) {
            system.dropStudentFromSection(id, sec.getSectionId());
        }
        system.getAllStudents().remove(id);
        System.out.println("Student " + id + " removed.");
    }

    private void viewStudentProfile() {
        StudentAccount s = promptStudent();
        if (s != null) System.out.println(s);
    }

    private void viewStudentSchedule() {
        StudentAccount s = promptStudent();
        if (s == null) return;
        List<Section> sections = s.getEnrolledSections();
        if (sections.isEmpty()) { System.out.println("No enrolled sections."); return; }
        System.out.println("\n--- Schedule for " + s.getName() + " ---");
        for (Section sec : sections) System.out.println(sec);
    }

    private void viewStudentScheduleByTerm() {
        StudentAccount s = promptStudent();
        if (s == null) return;
        System.out.print("Enter term: ");
        String term = scanner.nextLine().trim();
        List<Section> sections = s.getSectionsByTerm(term);
        if (sections.isEmpty()) { System.out.println("No sections found for term: " + term); return; }
        System.out.println("\n--- Schedule for " + s.getName() + " (" + term + ") ---");
        for (Section sec : sections) System.out.println(sec);
    }

    private void viewCompletedCourses() {
        StudentAccount s = promptStudent();
        if (s == null) return;
        List<Course> completed = s.getCompletedCourses();
        if (completed.isEmpty()) { System.out.println("No completed courses."); return; }
        System.out.println("\n--- Completed Courses for " + s.getName() + " ---");
        for (Course c : completed) System.out.println(c);
    }

    private void addCompletedCourse() {
        StudentAccount s = promptStudent();
        if (s == null) return;
        System.out.print("Enter Course Code to mark completed: ");
        String code = scanner.nextLine().trim();
        Course c = system.getCourse(code);
        if (c == null) { System.out.println("Course not found."); return; }
        s.addCompletedCourse(c);
        System.out.println("Marked " + c.getTitle() + " as completed for " + s.getName() + ".");
    }

    private void viewCreditLoad() {
        StudentAccount s = promptStudent();
        if (s == null) return;
        System.out.println("Current credit load for " + s.getName() + ": " + s.calculateCurrentCreditLoad());
    }

    private void activateStudent() {
        StudentAccount s = promptStudent();
        if (s == null) return;
        s.activate();
        System.out.println(s.getName() + " has been activated.");
    }

    private void deactivateStudent() {
        StudentAccount s = promptStudent();
        if (s == null) return;
        System.out.print("Enter reason for deactivation: ");
        String reason = scanner.nextLine().trim();
        s.deactivate(reason);
        System.out.println(s.getName() + " has been deactivated. Reason: " + reason);
    }

    // ─────────────────────────────────────────────
    //  2) PROFESSOR MENU
    // ─────────────────────────────────────────────
    private void showProfessorMenu() {
        while (true) {
            System.out.println("\n========== PROFESSOR MENU ==========");
            System.out.println("1. Add Professor");
            System.out.println("2. Remove Professor");
            System.out.println("3. View Professor Profile");
            System.out.println("4. View Teaching Sections");
            System.out.println("5. View Teaching Load Credits");
            System.out.println("6. View Roster for a Section");
            System.out.println("7. Activate Professor");
            System.out.println("8. Deactivate Professor");
            System.out.println("9. Back");
            System.out.print("Enter choice: ");
            String choice = scanner.nextLine();
            switch (choice) {
                case "1": addProfessor(); break;
                case "2": removeProfessor(); break;
                case "3": viewProfessorProfile(); break;
                case "4": viewTeachingSections(); break;
                case "5": viewTeachingLoad(); break;
                case "6": viewRosterForSection(); break;
                case "7": activateProfessor(); break;
                case "8": deactivateProfessor(); break;
                case "9": return;
                default:  System.out.println("Invalid choice.");
            }
        }
    }

    private void addProfessor() {
        System.out.print("Enter Professor ID: ");
        String id = scanner.nextLine().trim();
        if (system.getProfessor(id) != null) { System.out.println("Professor ID already exists."); return; }
        System.out.print("Enter Name: ");
        String name = scanner.nextLine().trim();
        System.out.print("Enter Email: ");
        String email = scanner.nextLine().trim();
        System.out.print("Enter Password: ");
        String pass = scanner.nextLine().trim();
        System.out.print("Enter Department: ");
        String dept = scanner.nextLine().trim();
        try {
            ProfessorAccount p = new ProfessorAccount(id, name, email, pass, dept);
            system.registerProfessor(p);
            System.out.println("Professor added: " + p);
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void removeProfessor() {
        System.out.print("Enter Professor ID to remove: ");
        String id = scanner.nextLine().trim();
        ProfessorAccount p = system.getProfessor(id);
        if (p == null) { System.out.println("Professor not found."); return; }
        // Unassign from sections
        for (Section sec : new java.util.ArrayList<>(p.getTeachingSections())) {
            sec.setInstructor(null);
            p.removeSection(sec);
        }
        system.getAllProfessors().remove(id);
        System.out.println("Professor " + id + " removed.");
    }

    private void viewProfessorProfile() {
        ProfessorAccount p = promptProfessor();
        if (p != null) System.out.println(p);
    }

    private void viewTeachingSections() {
        ProfessorAccount p = promptProfessor();
        if (p == null) return;
        List<Section> sections = p.getTeachingSections();
        if (sections.isEmpty()) { System.out.println("No teaching sections."); return; }
        System.out.println("\n--- Teaching Sections for " + p.getName() + " ---");
        for (Section sec : sections) System.out.println(sec);
    }

    private void viewTeachingLoad() {
        ProfessorAccount p = promptProfessor();
        if (p == null) return;
        System.out.println("Teaching load for " + p.getName() + ": " + p.calculateTeachingLoadCredits() + " credits");
    }

    private void viewRosterForSection() {
        ProfessorAccount p = promptProfessor();
        if (p == null) return;
        List<Section> sections = p.getTeachingSections();
        if (sections.isEmpty()) { System.out.println("No teaching sections."); return; }
        System.out.println("Teaching sections:");
        for (int i = 0; i < sections.size(); i++)
            System.out.println((i + 1) + ". " + sections.get(i).getSectionId() + " - " + sections.get(i).getCourse().getTitle());
        System.out.print("Enter section number: ");
        try {
            int idx = Integer.parseInt(scanner.nextLine().trim()) - 1;
            if (idx < 0 || idx >= sections.size()) { System.out.println("Invalid selection."); return; }
            Section sec = sections.get(idx);
            List<StudentAccount> roster = p.viewRoster(sec);
            if (roster.isEmpty()) { System.out.println("No students enrolled."); return; }
            System.out.println("\n--- Roster for " + sec.getSectionId() + " ---");
            for (StudentAccount s : roster) System.out.println(s);
        } catch (NumberFormatException e) {
            System.out.println("Invalid input.");
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void activateProfessor() {
        ProfessorAccount p = promptProfessor();
        if (p == null) return;
        p.activate();
        System.out.println(p.getName() + " has been activated.");
    }

    private void deactivateProfessor() {
        ProfessorAccount p = promptProfessor();
        if (p == null) return;
        System.out.print("Enter reason for deactivation: ");
        String reason = scanner.nextLine().trim();
        p.deactivate(reason);
        System.out.println(p.getName() + " has been deactivated. Reason: " + reason);
    }

    // ─────────────────────────────────────────────
    //  3) COURSE MENU
    // ─────────────────────────────────────────────
    private void showCourseMenu() {
        while (true) {
            System.out.println("\n=========== COURSE MENU ===========");
            System.out.println("1. Add Course");
            System.out.println("2. Remove Course");
            System.out.println("3. View Course Catalog Summary");
            System.out.println("4. View Course Prerequisites");
            System.out.println("5. Add Prerequisite to Course");
            System.out.println("6. Remove Prerequisite from Course");
            System.out.println("7. Back");
            System.out.print("Enter choice: ");
            String choice = scanner.nextLine();
            switch (choice) {
                case "1": addCourse(); break;
                case "2": removeCourse(); break;
                case "3": viewCourseCatalog(); break;
                case "4": viewCoursePrerequisites(); break;
                case "5": addPrerequisite(); break;
                case "6": removePrerequisite(); break;
                case "7": return;
                default:  System.out.println("Invalid choice.");
            }
        }
    }

    private void addCourse() {
        System.out.print("Enter Course Code: ");
        String code = scanner.nextLine().trim();
        if (system.getCourse(code) != null) { System.out.println("Course code already exists."); return; }
        System.out.print("Enter Title: ");
        String title = scanner.nextLine().trim();
        System.out.print("Enter Credits (1-6): ");
        try {
            int credits = Integer.parseInt(scanner.nextLine().trim());
            System.out.print("Enter Description: ");
            String desc = scanner.nextLine().trim();
            Course c = new Course(code, title, credits, desc);
            system.addCourse(c);
            System.out.println("Course added: " + c);
        } catch (NumberFormatException e) {
            System.out.println("Invalid credits.");
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void removeCourse() {
        System.out.print("Enter Course Code to remove: ");
        String code = scanner.nextLine().trim();
        if (system.getCourse(code) == null) { System.out.println("Course not found."); return; }
        system.removeCourse(code);
        System.out.println("Course " + code + " and its sections removed.");
    }

    private void viewCourseCatalog() {
        Course c = promptCourse();
        if (c != null) System.out.println("\n" + c.getCatalogSummary());
    }

    private void viewCoursePrerequisites() {
        Course c = promptCourse();
        if (c == null) return;
        List<Course> prereqs = c.getPrerequisites();
        if (prereqs.isEmpty()) { System.out.println("No prerequisites."); return; }
        System.out.println("Prerequisites for " + c.getCourseCode() + ":");
        for (Course p : prereqs) System.out.println("  " + p);
    }

    private void addPrerequisite() {
        System.out.print("Enter Course Code to add prerequisite TO: ");
        String code = scanner.nextLine().trim();
        Course c = system.getCourse(code);
        if (c == null) { System.out.println("Course not found."); return; }
        System.out.print("Enter prerequisite Course Code: ");
        String prereqCode = scanner.nextLine().trim();
        Course prereq = system.getCourse(prereqCode);
        if (prereq == null) { System.out.println("Prerequisite course not found."); return; }
        if (prereq.equals(c)) { System.out.println("A course cannot be its own prerequisite."); return; }
        c.addPrerequisite(prereq);
        System.out.println(prereqCode + " added as prerequisite to " + code + ".");
    }

    private void removePrerequisite() {
        System.out.print("Enter Course Code to remove prerequisite FROM: ");
        String code = scanner.nextLine().trim();
        Course c = system.getCourse(code);
        if (c == null) { System.out.println("Course not found."); return; }
        System.out.print("Enter prerequisite Course Code to remove: ");
        String prereqCode = scanner.nextLine().trim();
        Course prereq = system.getCourse(prereqCode);
        if (prereq == null) { System.out.println("Prerequisite course not found."); return; }
        c.removePrerequisite(prereq);
        System.out.println(prereqCode + " removed from prerequisites of " + code + ".");
    }

    // ─────────────────────────────────────────────
    //  4) SECTION MENU
    // ─────────────────────────────────────────────
    private void showSectionMenu() {
        while (true) {
            System.out.println("\n=========== SECTION MENU ===========");
            System.out.println("1. Create Section");
            System.out.println("2. Remove Section");
            System.out.println("3. View Section Details");
            System.out.println("4. List Sections by Course");
            System.out.println("5. List Sections by Term");
            System.out.println("6. View Section Roster");
            System.out.println("7. Cancel / Deactivate Section");
            System.out.println("8. Reactivate Section");
            System.out.println("9. Back");
            System.out.print("Enter choice: ");
            String choice = scanner.nextLine();
            switch (choice) {
                case "1": createSection(); break;
                case "2": removeSection(); break;
                case "3": viewSectionDetails(); break;
                case "4": listSectionsByCourse(); break;
                case "5": listSectionsByTerm(); break;
                case "6": viewSectionRoster(); break;
                case "7": cancelSection(); break;
                case "8": reactivateSection(); break;
                case "9": return;
                default:  System.out.println("Invalid choice.");
            }
        }
    }

    private void createSection() {
        System.out.print("Enter Section ID: ");
        String sectionId = scanner.nextLine().trim();
        if (system.getSection(sectionId) != null) { System.out.println("Section ID already exists."); return; }

        System.out.print("Enter Course Code: ");
        String code = scanner.nextLine().trim();
        Course course = system.getCourse(code);
        if (course == null) { System.out.println("Course not found."); return; }

        System.out.print("Enter Term: ");
        String term = scanner.nextLine().trim();

        System.out.print("Enter Capacity: ");
        int capacity;
        try { capacity = Integer.parseInt(scanner.nextLine().trim()); }
        catch (NumberFormatException e) { System.out.println("Invalid capacity."); return; }

        System.out.print("Enter Professor ID (or leave blank for TBA): ");
        String profId = scanner.nextLine().trim();
        ProfessorAccount prof = profId.isEmpty() ? null : system.getProfessor(profId);
        if (!profId.isEmpty() && prof == null) { System.out.println("Professor not found."); return; }

        // Build schedule
        System.out.println("Enter schedule days (comma-separated, e.g. Monday,Wednesday,Friday): ");
        String[] dayArr = scanner.nextLine().trim().split(",");
        Set<String> days = new HashSet<>();
        for (String d : dayArr) days.add(d.trim());

        System.out.print("Enter start time in minutes from midnight (e.g. 480 = 8:00 AM): ");
        int start;
        System.out.print("Enter end time in minutes from midnight (e.g. 570 = 9:30 AM): ");
        int end;
        try {
            start = Integer.parseInt(scanner.nextLine().trim());
            end   = Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) { System.out.println("Invalid time."); return; }

        System.out.print("Enter location: ");
        String location = scanner.nextLine().trim();

        try {
            Schedule schedule = new Schedule(days, start, end, location);
            Section section = new Section(sectionId, course, prof, schedule, term, capacity);
            system.addSection(section);
            if (prof != null) prof.assignSection(section);
            System.out.println("Section created: " + section);
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void removeSection() {
        System.out.print("Enter Section ID to remove: ");
        String id = scanner.nextLine().trim();
        if (system.getSection(id) == null) { System.out.println("Section not found."); return; }
        system.removeSection(id);
        System.out.println("Section " + id + " removed.");
    }

    private void viewSectionDetails() {
        Section sec = promptSection();
        if (sec != null) {
            System.out.println("\n--- Section Details ---");
            System.out.println(sec);
            System.out.println("Schedule: " + sec.getSchedule());
        }
    }

    private void listSectionsByCourse() {
        System.out.print("Enter Course Code: ");
        String code = scanner.nextLine().trim();
        List<Section> sections = system.listSectionsByCourse(code);
        if (sections.isEmpty()) { System.out.println("No sections found for course: " + code); return; }
        System.out.println("\n--- Sections for " + code + " ---");
        for (Section s : sections) System.out.println(s);
    }

    private void listSectionsByTerm() {
        System.out.print("Enter Term: ");
        String term = scanner.nextLine().trim();
        List<Section> sections = system.listSectionsByTerm(term);
        if (sections.isEmpty()) { System.out.println("No sections found for term: " + term); return; }
        System.out.println("\n--- Sections for term: " + term + " ---");
        for (Section s : sections) System.out.println(s);
    }

    private void viewSectionRoster() {
        Section sec = promptSection();
        if (sec == null) return;
        List<StudentAccount> roster = sec.getEnrolledStudents();
        if (roster.isEmpty()) { System.out.println("No students enrolled."); return; }
        System.out.println("\n--- Roster for " + sec.getSectionId() + " ---");
        for (StudentAccount s : roster) System.out.println(s);
    }

    private void cancelSection() {
        Section sec = promptSection();
        if (sec == null) return;
        System.out.print("Enter reason for cancellation: ");
        String reason = scanner.nextLine().trim();
        sec.deactivate(reason);
        System.out.println("Section " + sec.getSectionId() + " cancelled. Reason: " + reason);
    }

    private void reactivateSection() {
        Section sec = promptSection();
        if (sec == null) return;
        sec.activate();
        System.out.println("Section " + sec.getSectionId() + " reactivated.");
    }

    // ─────────────────────────────────────────────
    //  5) REGISTRATION MENU
    // ─────────────────────────────────────────────
    private void showRegistrationMenu() {
        while (true) {
            System.out.println("\n======== REGISTRATION MENU ========");
            System.out.println("1. Enroll Student in Section");
            System.out.println("2. Drop Student from Section");
            System.out.println("3. Back");
            System.out.print("Enter choice: ");
            String choice = scanner.nextLine();
            switch (choice) {
                case "1": enrollStudent(); break;
                case "2": dropStudent(); break;
                case "3": return;
                default:  System.out.println("Invalid choice.");
            }
        }
    }

    private void enrollStudent() {
        System.out.print("Enter Student ID: ");
        String studentId = scanner.nextLine().trim();
        StudentAccount student = system.getStudent(studentId);
        if (student == null) { System.out.println("Student not found."); return; }

        System.out.print("Enter Section ID: ");
        String sectionId = scanner.nextLine().trim();
        Section section = system.getSection(sectionId);
        if (section == null) { System.out.println("Section not found."); return; }

        try {
            student.addSection(section);
            System.out.println(student.getName() + " successfully enrolled in section " + sectionId + ".");
        } catch (InactiveEntityException e) {
            System.out.println("Enrollment failed - Inactive entity: " + e.getMessage());
        } catch (ScheduleConflictException e) {
            System.out.println("Enrollment failed - Schedule conflict: " + e.getMessage());
        } catch (PrerequisiteNotMetException e) {
            System.out.println("Enrollment failed - Prerequisites not met: " + e.getMessage());
        } catch (CourseFullException e) {
            System.out.println("Enrollment failed - Course is full: " + e.getMessage());
        }
    }

    private void dropStudent() {
        System.out.print("Enter Student ID: ");
        String studentId = scanner.nextLine().trim();
        StudentAccount student = system.getStudent(studentId);
        if (student == null) { System.out.println("Student not found."); return; }

        System.out.print("Enter Section ID: ");
        String sectionId = scanner.nextLine().trim();
        Section section = system.getSection(sectionId);
        if (section == null) { System.out.println("Section not found."); return; }

        try {
            student.dropSection(section);
            System.out.println(student.getName() + " successfully dropped section " + sectionId + ".");
        } catch (InactiveEntityException e) {
            System.out.println("Drop failed - Not enrolled: " + e.getMessage());
        }
    }

    // ─────────────────────────────────────────────
    //  6) REPORTS MENU
    // ─────────────────────────────────────────────
    private void showReportsMenu() {
        while (true) {
            System.out.println("\n============= REPORTS ==============");
            System.out.println("1. List All Students");
            System.out.println("2. List All Professors");
            System.out.println("3. List All Courses");
            System.out.println("4. List All Sections");
            System.out.println("5. Show Summary Stats");
            System.out.println("6. Back");
            System.out.print("Enter choice: ");
            String choice = scanner.nextLine();
            switch (choice) {
                case "1": listAllStudents(); break;
                case "2": listAllProfessors(); break;
                case "3": listAllCourses(); break;
                case "4": listAllSections(); break;
                case "5": System.out.println(system.getSystemSummary()); break;
                case "6": return;
                default:  System.out.println("Invalid choice.");
            }
        }
    }

    private void listAllStudents() {
        if (system.getAllStudents().isEmpty()) { System.out.println("No students registered."); return; }
        System.out.println("\n--- All Students ---");
        for (StudentAccount s : system.getAllStudents().values()) System.out.println(s);
    }

    private void listAllProfessors() {
        if (system.getAllProfessors().isEmpty()) { System.out.println("No professors registered."); return; }
        System.out.println("\n--- All Professors ---");
        for (ProfessorAccount p : system.getAllProfessors().values()) System.out.println(p);
    }

    private void listAllCourses() {
        if (system.getAllCourses().isEmpty()) { System.out.println("No courses registered."); return; }
        System.out.println("\n--- All Courses ---");
        for (Course c : system.getAllCourses().values()) System.out.println(c);
    }

    private void listAllSections() {
        if (system.getAllSections().isEmpty()) { System.out.println("No sections created."); return; }
        System.out.println("\n--- All Sections ---");
        for (Section s : system.getAllSections().values()) System.out.println(s);
    }

    // ─────────────────────────────────────────────
    //  PROMPT HELPERS
    // ─────────────────────────────────────────────
    private StudentAccount promptStudent() {
        System.out.print("Enter Student ID: ");
        String id = scanner.nextLine().trim();
        StudentAccount s = system.getStudent(id);
        if (s == null) System.out.println("Student not found.");
        return s;
    }

    private ProfessorAccount promptProfessor() {
        System.out.print("Enter Professor ID: ");
        String id = scanner.nextLine().trim();
        ProfessorAccount p = system.getProfessor(id);
        if (p == null) System.out.println("Professor not found.");
        return p;
    }

    private Course promptCourse() {
        System.out.print("Enter Course Code: ");
        String code = scanner.nextLine().trim();
        Course c = system.getCourse(code);
        if (c == null) System.out.println("Course not found.");
        return c;
    }

    private Section promptSection() {
        System.out.print("Enter Section ID: ");
        String id = scanner.nextLine().trim();
        Section s = system.getSection(id);
        if (s == null) System.out.println("Section not found.");
        return s;
    }
}