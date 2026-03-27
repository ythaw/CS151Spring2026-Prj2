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

    private String getInput() {
        String input = scanner.nextLine().trim();
        if(input.equalsIgnoreCase("EXIT")){
            System.out.println("Exiting...");
            System.exit(0);
        }
        return input;
    }

    private String promptNonEmpty(String prompt) {
        while (true) {
            System.out.print(prompt);
            String value = getInput();
            if (!value.isBlank()) {
                return value;
            }
            System.out.println("Input cannot be empty. Try again.");
        }
    }
    
	private String promptValidName() {
		while (true) {
			System.out.print("Enter name: ");
			String name = getInput();

			if (name.isBlank()) {
				System.out.println("Error: Name cannot be empty.");
			} else {
				return name;
			}
		}
	}

	private String promptValidEmail() {
		while (true) {
			System.out.print("Enter email: ");
			String email = getInput();

			if (email.isEmpty()) {
				System.out.println("Error: Email cannot be empty.");
			} else if (!email.contains("@") || !email.contains(".")) {
				System.out.println("Error: Invalid email format.");
			} else {
				return email;
			}
		}
	}

	private String promptValidPassword() {
		while (true) {
			System.out.print("Enter password: ");
			String password = getInput();

			if (password.isEmpty()) {
				System.out.println("Error: Password cannot be empty.");
			} else {
				return password;
			}
		}
	}
	
	private String promptUniqueAccountId(String prompt) {
		while (true) {
			String id = promptNonEmpty(prompt);

			if (system.accountIdExists(id)) {
				System.out.println("Error: Account ID already exists.");
			} else {
				return id;
			}
		}
	}
	
    private boolean showMainMenu() {
        System.out.println("\n========================================");
        System.out.println(" UNIVERSITY COURSE REGISTRATION SYSTEM");
        System.out.println("========================================");
        System.out.println("1. Login");
        System.out.println("2. Create Account");
        System.out.println("3. Exit");

        String choice = promptNonEmpty("Enter choice: ");

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
        String id = promptNonEmpty("Enter ID: ");
        String password = promptNonEmpty("Enter password: ");

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

            String choice = promptNonEmpty("Enter choice: ");

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
            System.out.println("8. View Contact Card");
            System.out.println("9. Update Email");
            System.out.println("10. Change Password");
            System.out.println("11. Logout");

            String choice = promptNonEmpty("Enter choice: ");

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
                    System.out.println(student.getContactCard());
                    break;
                case "9":
                    handleUpdateEmail(student);
                    break;
                case "10":
                    handleChangePassword(student);
                    break;
                case "11":
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
            System.out.println("5. View Contact Card");
            System.out.println("6. Update Email");
            System.out.println("7. Change Password");
            System.out.println("8. Logout");

            String choice = promptNonEmpty("Enter choice: ");

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
                    System.out.println(professor.getContactCard());
                    break;
                case "6":
                    handleUpdateEmail(professor);
                    break;
                case "7":
                    handleChangePassword(professor);
                    break;
                case "8":
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

            String choice = promptNonEmpty("Enter choice: ");

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
                    system.loadSampleData();
                    System.out.println("Sample data loaded.");
                    break;
                case "7":
                    System.out.println("Logging out...");
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

            String choice = promptNonEmpty("Enter choice: ");

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
            System.out.println("3. View Course Catalog Summary");
            System.out.println("4. Add Prerequisite to Course");
            System.out.println("5. Remove Prerequisite from Course");
            System.out.println("6. Back");

            String choice = promptNonEmpty("Enter choice: ");

            switch (choice) {
                case "1":
                    addCourse();
                    break;
                case "2":
                    removeCourse();
                    break;
                case "3":
                    viewCourseCatalogSummary();
                    break;
                case "4":
                    addCoursePrerequisite();
                    break;
                case "5":
                    removeCoursePrerequisite();
                    break;
                case "6":
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

            String choice = promptNonEmpty("Enter choice: ");

            switch (choice) {
                case "1":
                    createSection();
                    break;
                case "2":
                    removeSection();
                    break;
                case "3":
                    assignProfessorToSection();
                    break;
                case "4":
                    toggleSectionActive();
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

            String choice = promptNonEmpty("Enter choice: ");

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

            String choice = promptNonEmpty("Enter choice: ");

            switch (choice) {
                case "1":
                    listSectionsByCourse();
                    break;
                case "2":
                    listSectionsByTerm();
                    break;
                case "3":
                    System.out.println(system.getSystemSummary());
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
		while (true) {
			try {
				String id = promptUniqueAccountId("Enter student ID: ");

				String name = promptValidName();
				String email = promptValidEmail();
				String password = promptValidPassword();

				String major = promptNonEmpty("Enter major: ");

				StudentAccount student = new StudentAccount(id, name, email, password, major);
				system.registerStudent(student);

				System.out.println("Student account created successfully.");
				return;

			} catch (IllegalArgumentException e) {
				System.out.println("Error: " + e.getMessage());
				System.out.println("Please try again.");
			}
		}
    }

    private void registerProfessorAccount() {
		while (true) {
			try {
				String id = promptUniqueAccountId("Enter professor ID: ");
				String name = promptValidName();
				String email = promptValidEmail();
				String password = promptValidPassword();
				String department = promptNonEmpty("Enter department: ");

				ProfessorAccount professor = new ProfessorAccount(id, name, email, password, department);
				system.registerProfessor(professor);

				System.out.println("Professor account created successfully.");
				return;

			} catch (IllegalArgumentException e) {
				System.out.println("Error: " + e.getMessage());
				System.out.println("Please try again.");
			}
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
        String term = promptNonEmpty("Enter term: ");

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
        String sectionId = promptNonEmpty("Enter section ID to enroll: ");

        try {
            system.enrollStudentInSection(student.getAccountId(), sectionId);
            System.out.println("Enrollment request processed.");
        } catch (Exception e) {
            System.out.println("Could not enroll: " + e.getMessage());
        }
    }

    private void handleDropSection(StudentAccount student) {
        String sectionId = promptNonEmpty("Enter section ID to drop: ");

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

        String sectionId = promptNonEmpty("Enter section ID to view roster: ");

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
            String code = promptNonEmpty("Enter course code: ");
            String title = promptNonEmpty("Enter course title: ");
            int credits;
            while (true) {
                System.out.print("Enter credits (1-6): ");
                try {
                    credits = Integer.parseInt(getInput());
                    if (credits >= 1 && credits <= 6) {
                        break;
                    }
                    System.out.println("Credits must be between 1 and 6.");
                } catch (NumberFormatException e) {
                    System.out.println("Credits must be a number.");
                }
            }
            String description = promptNonEmpty("Enter description: ");

            Course course = new Course(code, title, credits, description);
            system.addCourse(course);
            System.out.println("Course added successfully.");
        } catch (Exception e) {
            System.out.println("Could not add course: " + e.getMessage());
        }
    }

    private void viewCourseCatalogSummary() {
        List<Course> courses = system.getAllCourses();
        if (courses.isEmpty()) {
            System.out.println("No courses found.");
            return;
        }

        System.out.println("\n--- COURSE CATALOG ---");
        for (Course c : courses) {
            System.out.println(c.getCatalogSummary());
            System.out.println("-----------------------------------");
        }
    }

    private void addCoursePrerequisite() {
        String courseCode = promptNonEmpty("Enter course code to add prerequisite to: ");
        String prereqCode = promptNonEmpty("Enter prerequisite course code: ");

        Course course = system.getCourseByCode(courseCode);
        Course prereq = system.getCourseByCode(prereqCode);

        if (course == null) {
            System.out.println("Course not found: " + courseCode);
            return;
        }
        if (prereq == null) {
            System.out.println("Prerequisite course not found: " + prereqCode);
            return;
        }
        if (course == prereq) {
            System.out.println("A course cannot be its own prerequisite.");
            return;
        }

        course.addPrerequisite(prereq);
        System.out.println("Prerequisite added.");
    }

    private void removeCoursePrerequisite() {
        String courseCode = promptNonEmpty("Enter course code to remove prerequisite from: ");
        String prereqCode = promptNonEmpty("Enter prerequisite course code to remove: ");

        Course course = system.getCourseByCode(courseCode);
        Course prereq = system.getCourseByCode(prereqCode);

        if (course == null) {
            System.out.println("Course not found: " + courseCode);
            return;
        }
        if (prereq == null) {
            System.out.println("Prerequisite course not found: " + prereqCode);
            return;
        }

        course.removePrerequisite(prereq);
        System.out.println("Prerequisite removed (if it existed).");
    }

    private void removeCourse() {
        String courseCode = promptNonEmpty("Enter course code to remove: ");

        try {
            system.removeCourse(courseCode);
            System.out.println("Remove course request processed.");
        } catch (Exception e) {
            System.out.println("Could not remove course: " + e.getMessage());
        }
    }

    private void removeSection() {
        String sectionId = promptNonEmpty("Enter section ID to remove: ");

        try {
            system.removeSection(sectionId);
            System.out.println("Remove section request processed.");
        } catch (Exception e) {
            System.out.println("Could not remove section: " + e.getMessage());
        }
    }

    private void createSection() {
        try {
            String sectionId = promptNonEmpty("Enter new section ID: ");
            if (system.getSectionById(sectionId) != null) {
                System.out.println("Section ID already exists.");
                return;
            }

            String courseCode = promptNonEmpty("Enter course code: ");
            Course course = system.getCourseByCode(courseCode);
            if (course == null) {
                System.out.println("Course not found: " + courseCode);
                return;
            }

            String term = promptNonEmpty("Enter term (e.g., Fall): ");
            int capacity;
            while (true) {
                System.out.print("Enter capacity: ");
                try {
                    capacity = Integer.parseInt(getInput());
                    if (capacity > 0) {
                        break;
                    }
                    System.out.println("Capacity must be positive.");
                } catch (NumberFormatException e) {
                    System.out.println("Please enter a valid number.");
                }
            }

            String daysRaw = promptNonEmpty("Enter meeting days (comma-separated, e.g., Monday,Wednesday): ");
            java.util.Set<String> days = new java.util.HashSet<>();
            for (String d : daysRaw.split(",")) {
                String day = d.trim();
                if (!day.isEmpty()) {
                    days.add(day);
                }
            }
            if (days.isEmpty()) {
                System.out.println("Days cannot be empty.");
                return;
            }

            int startHour;
            while (true) {
                System.out.print("Enter start hour (0-23): ");
                try {
                    startHour = Integer.parseInt(getInput());
                    if (startHour >= 0 && startHour <= 23) {
                        break;
                    }
                    System.out.println("Hour must be between 0 and 23.");
                } catch (NumberFormatException e) {
                    System.out.println("Please enter a valid number.");
                }
            }
            int startMin;
            while (true) {
                System.out.print("Enter start minute (0-59): ");
                try {
                    startMin = Integer.parseInt(getInput());
                    if (startMin >= 0 && startMin <= 59) {
                        break;
                    }
                    System.out.println("Minute must be between 0 and 59.");
                } catch (NumberFormatException e) {
                    System.out.println("Please enter a valid number.");
                }
            }
            int endHour;
            while (true) {
                System.out.print("Enter end hour (0-23): ");
                try {
                    endHour = Integer.parseInt(getInput());
                    if (endHour >= 0 && endHour <= 23) {
                        break;
                    }
                    System.out.println("Hour must be between 0 and 23.");
                } catch (NumberFormatException e) {
                    System.out.println("Please enter a valid number.");
                }
            }
            int endMin;
            while (true) {
                System.out.print("Enter end minute (0-59): ");
                try {
                    endMin = Integer.parseInt(getInput());
                    if (endMin >= 0 && endMin <= 59) {
                        break;
                    }
                    System.out.println("Minute must be between 0 and 59.");
                } catch (NumberFormatException e) {
                    System.out.println("Please enter a valid number.");
                }
            }
            String location = promptNonEmpty("Enter location (e.g., Room 101): ");

            int startMinutes = startHour * 60 + startMin;
            int endMinutes = endHour * 60 + endMin;
            model.Schedule schedule = new model.Schedule(days, startMinutes, endMinutes, location);

            ProfessorAccount instructor = null;
            System.out.print("Assign professor now? (y/n): ");
            String assignNow = getInput();
            if (assignNow.equalsIgnoreCase("y")) {
                String professorId = promptNonEmpty("Enter professor ID: ");
                instructor = system.getProfessorById(professorId);
                if (instructor == null) {
                    System.out.println("Professor not found. Section will be created with TBA instructor.");
                    instructor = null;
                }
            }

            Section section = new Section(sectionId, course, instructor, schedule, term, capacity);
            system.addSection(section);
            if (instructor != null) {
                try {
                    system.assignProfessorToSection(instructor.getAccountId(), sectionId);
                } catch (Exception e) {
                    System.out.println("Section created, but could not assign professor: " + e.getMessage());
                }
            }
            System.out.println("Section created successfully.");
        } catch (Exception e) {
            System.out.println("Could not create section: " + e.getMessage());
        }
    }

    private void assignProfessorToSection() {
        try {
            String sectionId = promptNonEmpty("Enter section ID: ");
            String professorId = promptNonEmpty("Enter professor ID: ");
            system.assignProfessorToSection(professorId, sectionId);
            System.out.println("Professor assigned to section.");
        } catch (Exception e) {
            System.out.println("Could not assign professor: " + e.getMessage());
        }
    }

    private void toggleSectionActive() {
        String sectionId = promptNonEmpty("Enter section ID: ");
        Section section = system.getSectionById(sectionId);
        if (section == null) {
            System.out.println("Section not found.");
            return;
        }

        try {
            if (section.isActive()) {
                String reason = promptNonEmpty("Enter reason for deactivation: ");
                system.deactivateSection(sectionId, reason);
                System.out.println("Section deactivated.");
            } else {
                system.activateSection(sectionId);
                System.out.println("Section reactivated.");
            }
        } catch (Exception e) {
            System.out.println("Could not update section status: " + e.getMessage());
        }
    }

    private void enrollStudentByAdmin() {
        String studentId = promptNonEmpty("Enter student ID: ");
        String sectionId = promptNonEmpty("Enter section ID: ");

        try {
            system.enrollStudentInSection(studentId, sectionId);
            System.out.println("Enrollment request processed.");
        } catch (Exception e) {
            System.out.println("Could not enroll student: " + e.getMessage());
        }
    }

    private void dropStudentByAdmin() {
        String studentId = promptNonEmpty("Enter student ID: ");
        String sectionId = promptNonEmpty("Enter section ID: ");

        try {
            system.dropStudentFromSection(studentId, sectionId);
            System.out.println("Drop request processed.");
        } catch (Exception e) {
            System.out.println("Could not drop student: " + e.getMessage());
        }
    }

    private void listSectionsByCourse() {
        String courseCode = promptNonEmpty("Enter course code: ");
        String term = promptNonEmpty("Enter term: ");

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

    private void listSectionsByTerm() {
        String term = promptNonEmpty("Enter term: ");
        try {
            List<Section> sections = system.listSectionsByTerm(term);
            System.out.println("\n--- SECTIONS IN TERM " + term + " ---");
            if (sections.isEmpty()) {
                System.out.println("No sections found.");
                return;
            }
            for (Section s : sections) {
                System.out.println(s);
            }
        } catch (Exception e) {
            System.out.println("Could not list sections: " + e.getMessage());
        }
    }

    private void handleUpdateEmail(Account account) {
        try {
            String newEmail = promptNonEmpty("Enter new email: ");
            account.updateEmail(newEmail);
            System.out.println("Email updated.");
        } catch (Exception e) {
            System.out.println("Could not update email: " + e.getMessage());
        }
    }

    private void handleChangePassword(Account account) {
        try {
            String oldPw = promptNonEmpty("Enter current password: ");
            String newPw = promptNonEmpty("Enter new password: ");
            account.updatePassword(oldPw, newPw);
            System.out.println("Password updated.");
        } catch (Exception e) {
            System.out.println("Could not change password: " + e.getMessage());
        }
    }
}
