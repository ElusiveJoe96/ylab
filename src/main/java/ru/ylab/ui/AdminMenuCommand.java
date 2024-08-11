package ru.ylab.ui;

import lombok.AllArgsConstructor;
import ru.ylab.audit.AuditService;
import ru.ylab.service.CarService;
import ru.ylab.service.OrderService;
import ru.ylab.service.UserService;

import java.util.Scanner;

@AllArgsConstructor
public class AdminMenuCommand implements MenuCommand {
    private final UserService userService;
    private final CarService carService;
    private final OrderService orderService;
    private final AuditService auditService;

    @Override
    public void execute(int choice, Scanner scanner, int userId) {
        switch (choice) {
            case 1 -> userService.viewAllUsers();
            case 2 -> userService.updateUser(scanner);
            case 3 -> carService.viewAllCars();
            case 4 -> userService.updateUserRole(scanner);
            case 5 -> carService.addCar(scanner);
            case 6 -> carService.updateCar(scanner);
            case 7 -> carService.deleteCar(scanner);
            case 8 -> orderService.createOrder(userId, scanner);
            case 9 -> orderService.updateOrderStatus(scanner);
            case 10 -> auditService.viewAuditLogs();
            case 11 -> userService.logout();
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
                11. Logout
                0. Exit
                """);
    }
}
