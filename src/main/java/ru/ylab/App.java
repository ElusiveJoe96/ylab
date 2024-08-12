package ru.ylab;

import ru.ylab.ui.MainMenu;
import ru.ylab.util.ValidationUtil;

import java.util.Scanner;

public class App {
        public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        MainMenu menu = new MainMenu();

        while (true) {
            menu.showMenu();
            System.out.print("Select an option: ");
            int choice = ValidationUtil.getValidInt(scanner);
            scanner.nextLine();
            menu.executeMenu(choice, scanner);
        }
    }
}