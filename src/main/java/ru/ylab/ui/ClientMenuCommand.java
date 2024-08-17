package ru.ylab.ui;

import lombok.AllArgsConstructor;
import ru.ylab.service.CarService;
import ru.ylab.service.OrderService;
import ru.ylab.service.UserService;

import java.util.Scanner;

@AllArgsConstructor
public class ClientMenuCommand implements MenuCommand {
    private final UserService userService;
    private final CarService carService;
    private final OrderService orderService;

    @Override
    public void execute(int choice, Scanner scanner, int userId) {
        switch (choice) {
            case 1 -> carService.viewAllCars();
            case 2 -> orderService.getOrdersByUserId(userId);
            case 3 -> userService.viewMyInfo();
            case 4 -> userService.logout();
            case 0 -> exitApplication();
            default -> System.out.println("Invalid option. Please try again.");
        }
    }

    @Override
    public void showMenu() {
        System.out.println("""
                1. View cars
                2. View orders
                3. View my info
                4. Logout
                0. Exit
                """);
    }
}
