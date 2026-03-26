package ui;

import java.util.List;
import java.util.Scanner;

import model.Account;
import model.Course;
import model.ProfessorAccount;
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

    private boolean showMainMenu() {
        System.out.println("\n========================================");
        System.out.println(" UNIVERSITY COURSE REGISTRATION SYSTEM");
        System.out.println("========================================");
        System.out.println("1. Login");
        System.out.println("2. Create Account");
        System.out.println("3. Exit");
        System.out.print("Enter choice: ");

        String choice = scanner.nextLine();

        switch (choice) {
            case "1":
                handleLogin();
                return true;
            case "2":
                showCreateAccountMenu();
                return true;
            case "3":
                System.out.println("Goodbye!");
                return false;
            default:
                System.out.println("Invalid choice.");
                return true;
        }
    }

    private void handleLogin() {
        System.out.print("Enter ID: ");
        String id = scanner.nextLine().trim();

        System.out.print("Enter password: ");
        String password = scanner.nextLine().trim();

        // Hardcoded admin login for now
        if (id.equals("admin") && password.equals("admin123")) {
            System.out.println("Admin login successful!");
            showAdminMenu();
            return;
        }

        Account account = system.authenticate(id, password);

        if (account == null) {
            System.out.println("Invalid ID or password.");
            return;
        }

        if (account instanceof StudentAccount) {
            System.out.println("Student login successful!");
            showStudentMenu((StudentAccount) account);
        } else if (account instanceof ProfessorAccount) {
            System.out.println("Professor login successful!");
            showProfessorMenu((ProfessorAccount) account);
        } else {
            System.out.println("Unknown account type.");
        }
    }

    private void showCreateAccountMenu() {
        while (true) {
            System.out.println("\n---------- CREATE ACCOUNT ----------");
            System.out.println("1. Create Student Account");
            System.out.println("2. Create Professor Account");
            System.out.println("3. Back");
            System.out.print("Enter choice: ");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    createStudentAccount();
                    break;
                case "2":
                    createProfessorAccount();
                    break;
                case "3":
                    return;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    private void showStudentMenu(StudentAccount student) {
        while (true) {
            System.out.println("\n=========== STUDENT MENU ===========");
            System.out.println("1. View Profile");
            System.out.println("2. View Current Schedule");
            System.out.println("3. View Schedule by Term");
            System.out.println("4. View Completed Courses");
            System.out.println("5. View Credit Load");
            System.out.println("6. Enroll in Section");
            System.out.println("7. Drop Section");
            System.out.println("8. Logout");
            System.out.print("Enter choice: ");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    System.out.println(student);
                    break;
                case "2":
                    viewCurrentSchedule(student);
                    break;
                case "3":
                    viewScheduleByTerm(student);
                    break;
                case "4":
                    viewCompletedCourses(student);
                    break;
                case "5":
                    System.out.println("Current credit load: " + student.calculateCurrentCreditLoad());
                    break;
                case "6":
                    handleEnrollInSection(student);
                    break;
                case "7":
                    handleDropSection(student);
                    break;
                case "8":
                    System.out.println("Logging out...");
                    return;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    private void showProfessorMenu(ProfessorAccount professor) {
        while (true) {
            System.out.println("\n========== PROFESSOR MENU ==========");
            System.out.println("1. View Profile");
            System.out.println("2. View Teaching Sections");
            System.out.println("3. View Teaching Load");
            System.out.println("4. View Roster for a Section");
            System.out.println("5. Logout");
            System.out.print("Enter choice: ");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    System.out.println(professor);
                    break;
                case "2":
                    viewTeachingSections(professor);
                    break;
                case "3":
                    System.out.println("Teaching load: " + professor.calculateTeachingLoadCredits() + " credits");
                    break;
                case "4":
                    viewRosterForProfessorSection(professor);
                    break;
                case "5":
                    System.out.println("Logging out...");
                    return;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    private void showAdminMenu() {
        while (true) {
            System.out.println("\n============= ADMIN MENU =============");
            System.out.println("1. Manage Accounts");
            System.out.println("2. Manage Courses");
            System.out.println("3. Manage Sections");
            System.out.println("4. Manage Registration");
            System.out.println("5. View Reports");
            System.out.println("6. Load Sample Data");
            System.out.println("7. Logout");
            System.out.print("Enter choice: ");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    showManageAccountsMenu();
                    break;
                case "2":
                    showManageCoursesMenu();
                    break;
                case "3":
                    showManageSectionsMenu();
                    break;
                case "4":
                    showManageRegistrationMenu();
                    break;
                case "5":
                    showReportsMenu();
                    break;
                case "6":
                    loadSampleData();
                    break;
                case "7":
                    return;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    private void showManageAccountsMenu() {
        while (true) {
            System.out.println("\n---------- MANAGE ACCOUNTS ----------");
            System.out.println("1. Register Student Account");
            System.out.println("2. Register Professor Account");
            System.out.println("3. Back");
            System.out.print("Enter choice: ");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    registerStudentAccount();
                    break;
                case "2":
                    registerProfessorAccount();
                    break;
                case "3":
                    return;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    private void showManageCoursesMenu() {
        while (true) {
            System.out.println("\n----------- MANAGE COURSES ----------");
            System.out.println("1. Add Course");
            System.out.println("2. Remove Course");
            System.out.println("3. Back");
            System.out.print("Enter choice: ");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    addCourse();
                    break;
                case "2":
                    removeCourse();
                    break;
                case "3":
                    return;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    private void showManageSectionsMenu() {
        while (true) {
            System.out.println("\n----------- MANAGE SECTIONS ----------");
            System.out.println("1. Create Section");
            System.out.println("2. Remove Section");
            System.out.println("3. Assign Professor to Section");
            System.out.println("4. Cancel / Reactivate Section");
            System.out.println("5. Back");
            System.out.print("Enter choice: ");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    createSectionPlaceholder();
                    break;
                case "2":
                    removeSection();
                    break;
                case "3":
                    assignProfessorPlaceholder();
                    break;
                case "4":
                    toggleSectionPlaceholder();
                    break;
                case "5":
                    return;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    private void showManageRegistrationMenu() {
        while (true) {
            System.out.println("\n-------- MANAGE REGISTRATION --------");
            System.out.println("1. Enroll Student in Section");
            System.out.println("2. Drop Student from Section");
            System.out.println("3. Back");
            System.out.print("Enter choice: ");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    enrollStudentByAdmin();
                    break;
                case "2":
                    dropStudentByAdmin();
                    break;
                case "3":
                    return;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    private void showReportsMenu() {
        while (true) {
            System.out.println("\n-------------- REPORTS --------------");
            System.out.println("1. List Sections by Course");
            System.out.println("2. List Sections by Term");
            System.out.println("3. View System Summary");
            System.out.println("4. Back");
            System.out.print("Enter choice: ");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    listSectionsByCourse();
                    break;
                case "2":
                    listSectionsByTerm();
                    break;
                case "3":
                    systemSummaryPlaceholder();
                    break;
                case "4":
                    return;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }
    // END OF DISPLAYS

    private void createStudentAccount() {
        registerStudentAccount();
    }

    private void createProfessorAccount() {
        registerProfessorAccount();
    }

    // STUDENT
    private void registerStudentAccount() {
        try {
            System.out.print("Enter student ID: ");
            String id = scanner.nextLine().trim();

            System.out.print("Enter name: ");
            String name = scanner.nextLine().trim();

            System.out.print("Enter email: ");
            String email = scanner.nextLine().trim();

            System.out.print("Enter password: ");
            String password = scanner.nextLine().trim();

            System.out.print("Enter major: ");
            String major = scanner.nextLine().trim();

            StudentAccount student = new StudentAccount(id, name, email, password, major);
            system.registerStudent(student);
            System.out.println("Student account created successfully.");
        } catch (Exception e) {
            System.out.println("Could not create student account: " + e.getMessage());
        }
    }

    private void registerProfessorAccount() {
        try {
            System.out.print("Enter professor ID: ");
            String id = scanner.nextLine().trim();

            System.out.print("Enter name: ");
            String name = scanner.nextLine().trim();

            System.out.print("Enter email: ");
            String email = scanner.nextLine().trim();

            System.out.print("Enter password: ");
            String password = scanner.nextLine().trim();

            System.out.print("Enter department: ");
            String department = scanner.nextLine().trim();

            ProfessorAccount professor = new ProfessorAccount(id, name, email, password, department);
            system.registerProfessor(professor);
            System.out.println("Professor account created successfully.");
        } catch (Exception e) {
            System.out.println("Could not create professor account: " + e.getMessage());
        }
    }

    private void viewCurrentSchedule(StudentAccount student) {
        List<Section> sections = student.getEnrolledSections();

        System.out.println("\n--- CURRENT SCHEDULE ---");
        if (sections.isEmpty()) {
            System.out.println("No enrolled sections.");
            return;
        }

        for (Section section : sections) {
            System.out.println(section);
        }
    }

    private void viewScheduleByTerm(StudentAccount student) {
        System.out.print("Enter term: ");
        String term = scanner.nextLine().trim();

        try {
            List<Section> sections = student.getSectionsByTerm(term);

            System.out.println("\n--- SCHEDULE FOR TERM: " + term + " ---");
            if (sections.isEmpty()) {
                System.out.println("No sections found for that term.");
                return;
            }

            for (Section section : sections) {
                System.out.println(section);
            }
        } catch (Exception e) {
            System.out.println("Could not view schedule: " + e.getMessage());
        }
    }

    private void viewCompletedCourses(StudentAccount student) {
        List<Course> courses = student.getCompletedCourses();

        System.out.println("\n--- COMPLETED COURSES ---");
        if (courses.isEmpty()) {
            System.out.println("No completed courses.");
            return;
        }

        for (Course course : courses) {
            System.out.println(course);
        }
    }

    private void handleEnrollInSection(StudentAccount student) {
        System.out.print("Enter section ID to enroll: ");
        String sectionId = scanner.nextLine().trim();

        try {
            system.enrollStudentInSection(student.getAccountId(), sectionId);
            System.out.println("Enrollment request processed.");
        } catch (Exception e) {
            System.out.println("Could not enroll: " + e.getMessage());
        }
    }

    private void handleDropSection(StudentAccount student) {
        System.out.print("Enter section ID to drop: ");
        String sectionId = scanner.nextLine().trim();

        try {
            system.dropStudentFromSection(student.getAccountId(), sectionId);
            System.out.println("Drop request processed.");
        } catch (Exception e) {
            System.out.println("Could not drop section: " + e.getMessage());
        }
    }

    // PROFESSOR
    private void viewTeachingSections(ProfessorAccount professor) {
        List<Section> sections = professor.getTeachingSections();

        System.out.println("\n--- TEACHING SECTIONS ---");
        if (sections.isEmpty()) {
            System.out.println("No assigned teaching sections.");
            return;
        }

        for (Section section : sections) {
            System.out.println(section);
        }
    }

    private void viewRosterForProfessorSection(ProfessorAccount professor) {
        List<Section> sections = professor.getTeachingSections();

        if (sections.isEmpty()) {
            System.out.println("No assigned teaching sections.");
            return;
        }

        System.out.println("\nYour sections:");
        for (Section section : sections) {
            System.out.println("- " + section.getSectionId() + " | " + section.getCourse());
        }

        System.out.print("Enter section ID to view roster: ");
        String sectionId = scanner.nextLine().trim();

        Section selected = null;
        for (Section section : sections) {
            if (section.getSectionId().equalsIgnoreCase(sectionId)) {
                selected = section;
                break;
            }
        }

        if (selected == null) {
            System.out.println("Section not found in your teaching assignments.");
            return;
        }

        try {
            List<StudentAccount> roster = professor.viewRoster(selected);
            System.out.println("\n--- ROSTER FOR SECTION " + selected.getSectionId() + " ---");

            if (roster.isEmpty()) {
                System.out.println("No students enrolled.");
                return;
            }

            for (StudentAccount student : roster) {
                System.out.println(student);
            }
        } catch (Exception e) {
            System.out.println("Could not view roster: " + e.getMessage());
        }
    }

    // ADMIN
    private void addCourse() {
        try {
            System.out.print("Enter course code: ");
            String code = scanner.nextLine().trim();

            System.out.print("Enter course title: ");
            String title = scanner.nextLine().trim();

            System.out.print("Enter credits: ");
            int credits = Integer.parseInt(scanner.nextLine().trim());

            System.out.print("Enter description: ");
            String description = scanner.nextLine().trim();

            Course course = new Course(code, title, credits, description);
            system.addCourse(course);
            System.out.println("Course added successfully.");
        } catch (NumberFormatException e) {
            System.out.println("Credits must be a number.");
        } catch (Exception e) {
            System.out.println("Could not add course: " + e.getMessage());
        }
    }

    private void removeCourse() {
        System.out.print("Enter course code to remove: ");
        String courseCode = scanner.nextLine().trim();

        try {
            system.removeCourse(courseCode);
            System.out.println("Remove course request processed.");
        } catch (Exception e) {
            System.out.println("Could not remove course: " + e.getMessage());
        }
    }

    private void removeSection() {
        System.out.print("Enter section ID to remove: ");
        String sectionId = scanner.nextLine().trim();

        try {
            system.removeSection(sectionId);
            System.out.println("Remove section request processed.");
        } catch (Exception e) {
            System.out.println("Could not remove section: " + e.getMessage());
        }
    }

    private void enrollStudentByAdmin() {
        System.out.print("Enter student ID: ");
        String studentId = scanner.nextLine().trim();

        System.out.print("Enter section ID: ");
        String sectionId = scanner.nextLine().trim();

        try {
            system.enrollStudentInSection(studentId, sectionId);
            System.out.println("Enrollment request processed.");
        } catch (Exception e) {
            System.out.println("Could not enroll student: " + e.getMessage());
        }
    }

    private void dropStudentByAdmin() {
        System.out.print("Enter student ID: ");
        String studentId = scanner.nextLine().trim();

        System.out.print("Enter section ID: ");
        String sectionId = scanner.nextLine().trim();

        try {
            system.dropStudentFromSection(studentId, sectionId);
            System.out.println("Drop request processed.");
        } catch (Exception e) {
            System.out.println("Could not drop student: " + e.getMessage());
        }
    }

    private void listSectionsByCourse() {
        System.out.print("Enter course code: ");
        String courseCode = scanner.nextLine().trim();

        System.out.print("Enter term: ");
        String term = scanner.nextLine().trim();

        try {
            List<Section> sections = system.listSectionsByCourse(courseCode, term);

            System.out.println("\n--- SECTIONS FOR COURSE " + courseCode + " IN TERM " + term + " ---");
            if (sections == null || sections.isEmpty()) {
                System.out.println("No sections found.");
                return;
            }

            for (Section section : sections) {
                System.out.println(section);
            }
        } catch (Exception e) {
            System.out.println("Could not list sections: " + e.getMessage());
        }
    }
}