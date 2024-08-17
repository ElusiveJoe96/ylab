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
            case 1 -> userService.viewAllUsers();
            case 2 -> carService.addCar(scanner);
            case 3 -> carService.updateCar(scanner);
            case 4 -> carService.deleteCar(scanner);
            case 5 -> orderService.getAllOrders();
            case 6 -> orderService.createOrder(scanner);
            case 7 -> orderService.updateOrderStatus(scanner);
            case 8 -> userService.logout();
            case 0 -> exitApplication();
            default -> System.out.println("Invalid option. Please try again.");
        }
    }

    @Override
    public void showMenu() {
        System.out.println("""
                1. View users
                2. Add car
                3. Update car
                4. Delete car
                5. View all orders
                6. Create order
                7. Update order status
                8. Logout
                0. Exit
                """);
    }
}
