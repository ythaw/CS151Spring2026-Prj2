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

}