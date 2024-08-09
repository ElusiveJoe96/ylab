package ru.ylab.ui;

import lombok.AllArgsConstructor;
import ru.ylab.controller.UserController;

import java.util.Scanner;

@AllArgsConstructor
public class GuestMenuCommand implements MenuCommand {

    private final UserController userController;

    @Override
    public void execute(int choice, Scanner scanner, int userId) {
        switch (choice) {
            case 1 -> userController.registerUser(scanner);
            case 2 -> userController.loginUser(scanner);
            case 3 -> exitApplication();
            default -> System.out.println("Invalid option. Please try again.");
        }
    }

    @Override
    public void showMenu() {
        System.out.println("1. Register\n2. Login\n3. Exit");
    }

    private void exitApplication() {
        System.out.println("Exiting application...");
        System.exit(0);
    }
}
