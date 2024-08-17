package ru.ylab.util;

import ru.ylab.ui.MainMenu;

import java.util.Scanner;

public class ApplicationRunner {
    private static final MainMenu menu = new MainMenu();
    private static final Scanner scanner = new Scanner(System.in);

    public static void runApp() {
        while (true) {
            menu.showMenu();
            System.out.print("Select an option: ");
            int choice = ValidationUtil.getValidInt(scanner);
            scanner.nextLine();
            menu.executeMenu(choice, scanner);
        }
    }
}
