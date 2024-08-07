package ru.ylab;

import ru.ylab.domain.enums.Role;
import ru.ylab.service.MenuService;

import java.util.Scanner;


public class App {
        public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        MenuService menuService = new MenuService();

        while (true) {
            Role role = menuService.getCurrentUserRole();
            menuService.showMenu(role);
            System.out.print("Select an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine();
            menuService.executeMenuOption(role, choice, scanner);
        }
    }
}
