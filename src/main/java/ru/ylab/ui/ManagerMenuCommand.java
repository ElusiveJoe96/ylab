package ru.ylab.ui;

import lombok.AllArgsConstructor;
import ru.ylab.service.CarService;
import ru.ylab.service.OrderService;
import ru.ylab.service.UserService;

import java.util.Scanner;

@AllArgsConstructor
public class ManagerMenuCommand implements MenuCommand {
    private final UserService userService;
    private final CarService carService;
    private final OrderService orderService;

    @Override
    public void execute(int choice, Scanner scanner, int userId) {
        switch (choice) {
            case 1 -> carService.addCar(scanner);
            case 2 -> userService.viewAllUsers();
            case 3 -> carService.updateCar(scanner);
            case 4 -> carService.deleteCar(scanner);
            case 5 -> orderService.createOrder(userId, scanner);
            case 6 -> orderService.updateOrderStatus(scanner);
            case 7 -> userService.logout();
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
                7. Logout
                0. Exit
                """);
    }
}
