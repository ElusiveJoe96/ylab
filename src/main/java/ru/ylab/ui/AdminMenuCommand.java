package ru.ylab.ui;

import lombok.AllArgsConstructor;
import ru.ylab.controller.*;
import java.util.Scanner;

@AllArgsConstructor
public class AdminMenuCommand implements MenuCommand {
    private final UserController userController;
    private final CarController carController;
    private final OrderController orderController;
    private final AuditController auditController;

    @Override
    public void execute(int choice, Scanner scanner, int userId) {
        switch (choice) {
            case 1 -> userController.viewAllUsers();
            case 2 -> userController.updateUser(scanner);
            case 3 -> carController.viewAllCars();
            case 4 -> userController.setUserRole(scanner);
            case 5 -> carController.addCar(scanner);
            case 6 -> carController.updateCar(scanner);
            case 7 -> carController.deleteCar(scanner);
            case 8 -> orderController.createOrder(scanner, userId);
            case 9 -> orderController.updateOrderStatus(scanner);
            case 10 -> auditController.viewLogs();
            case 0 -> exitApplication();
            default -> System.out.println("Invalid option. Please try again.");
        }
    }

    @Override
    public void showMenu() {
        System.out.println("""
                1. View all users
                2. Update user
                3. View all cars
                4. Change Role
                5. Add car
                6. Update car
                7. Delete car
                8. Create order
                9. Update order status
                10. Show logs
                0. Exit
                """);
    }

    private void exitApplication() {
        System.out.println("Exiting application...");
        System.exit(0);
    }

}
