package ui;

import java.util.Scanner;

public class Menu {
    private Scanner scanner;

    public Menu(){
        scanner = new Scanner(System.in);
    }

    public void start(){
        boolean running = true;
        while(running){
            running = showMainMenu();
        }
    }

    private boolean showMainMenu(){
        System.out.println("\n=== University System ===");
        System.out.println("1. Exit");
        System.out.print("Enter choice: ");

        String choice = scanner.nextLine();

        switch (choice) {
            case "1":
                System.out.println("Goodbye!");
                return false;
            default:
                System.out.println("Invalid choice.");
                return true;
        }
    }
}
