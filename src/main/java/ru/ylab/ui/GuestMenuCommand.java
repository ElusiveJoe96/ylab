package ru.ylab.ui;

import lombok.AllArgsConstructor;
import ru.ylab.service.UserService;

import java.util.Scanner;

@AllArgsConstructor
public class GuestMenuCommand implements MenuCommand {

    private final UserService userService;

    @Override
    public void execute(int choice, Scanner scanner, int userId) {
        switch (choice) {
            case 1 -> userService.registerUser(scanner);
            case 2 -> userService.login(scanner);
            case 3 -> exitApplication();
            default -> System.out.println("Invalid option. Please try again.");
        }
    }

    @Override
    public void showMenu() {
        System.out.println("1. Register\n2. Login\n3. Exit");
    }

}
