package ru.ylab.ui;

import lombok.AllArgsConstructor;
import ru.ylab.controller.CarController;
import ru.ylab.controller.OrderController;
import ru.ylab.controller.UserController;

import java.util.Scanner;

@AllArgsConstructor
public class ClientMenuCommand implements MenuCommand {

    private final CarController carController;
    private final OrderController orderController;
    private final UserController userController;
    @Override
    public void execute(int choice, Scanner scanner, int userId) {
        switch (choice) {
            case 1 -> carController.viewAllCars();
            case 2 -> orderController.viewMyOrders(userId);
            case 3 -> userController.showMyInfo();
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
                0. Exit
                """);
    }

    private void exitApplication() {
        System.out.println("Exiting application...");
        System.exit(0);
    }
}
