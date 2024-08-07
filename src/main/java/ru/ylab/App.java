package ru.ylab;

import ru.ylab.domain.enums.Role;
import ru.ylab.service.MenuServiceImpl;

import java.util.Scanner;


public class App {
        public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        MenuServiceImpl menuServiceImpl = new MenuServiceImpl();

        while (true) {
            Role role = menuServiceImpl.getCurrentUserRole();
            menuServiceImpl.showMenu(role);
            System.out.print("Select an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine();
            menuServiceImpl.executeMenuOption(role, choice, scanner);
        }
    }
}
