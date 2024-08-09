package ru.ylab.ui;

import lombok.AllArgsConstructor;
import ru.ylab.controller.CarController;
import ru.ylab.controller.OrderController;
import ru.ylab.controller.UserController;

import java.util.Scanner;

@AllArgsConstructor
public class ManagerMenuCommand implements MenuCommand {
    private final UserController userController;
    private final CarController carController;
    private final OrderController orderController;

    @Override
    public void execute(int choice, Scanner scanner, int userId) {
        switch (choice) {
            case 1 -> carController.addCar(scanner);
            case 2 -> userController.viewAllUsers();
            case 3 -> carController.updateCar(scanner);
            case 4 -> carController.deleteCar(scanner);
            case 5 -> orderController.createOrder(scanner, userId);
            case 6 -> orderController.updateOrderStatus(scanner);
            case 0 -> exitApplication();
            default -> System.out.println("Invalid option. Please try again.");
        }
    }

    @Override
    public void showMenu() {
        System.out.println("""
                1. Add car
                2. View users
                3. Update car
                4. Delete car
                5. Create order
                6. Update order status
                0. Exit
                """);
    }

    private void exitApplication() {
        System.out.println("Exiting application...");
        System.exit(0);
    }
}
