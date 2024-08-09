package ru.ylab;

import ru.ylab.domain.enums.Role;
import ru.ylab.ui.MainMenu;

import java.util.Scanner;

public class App {
        public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        MainMenu menu = new MainMenu();

        while (true) {
            Role role = menu.getCurrentUserRole();
            menu.showMenu(role);
            System.out.print("Select an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine();
            menu.executeMenu(role, choice, scanner);
        }
    }
}