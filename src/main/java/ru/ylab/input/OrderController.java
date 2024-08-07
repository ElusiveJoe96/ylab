package ru.ylab.input;


import ru.ylab.domain.enums.OrderStatus;
import ru.ylab.domain.enums.OrderType;
import ru.ylab.service.OrderService;

import java.util.Scanner;

public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    public void createOrder(Scanner scanner, int userId) {
        System.out.print("Enter car ID: ");
        int carId = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Enter order type (PURCHASE, SERVICE): ");
        OrderType type = OrderType.valueOf(scanner.nextLine().toUpperCase());

        orderService.createOrder(carId, userId, type);
    }

    public void viewAllOrders() {
        orderService.getAllOrders();
    }

    public void updateOrderStatus(Scanner scanner) {
        System.out.print("Enter order ID to update: ");
        int orderId = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Enter new status (PENDING, COMPLETED, CANCELED): ");
        OrderStatus status = OrderStatus.valueOf(scanner.nextLine().toUpperCase());

        orderService.updateOrderStatus(orderId, status);
    }

    public void viewMyOrders(int userId) {
        orderService.getOrdersByUserId(userId);
    }
}
