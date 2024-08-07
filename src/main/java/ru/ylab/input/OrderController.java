package ru.ylab.input;


import ru.ylab.domain.enums.OrderStatus;
import ru.ylab.domain.enums.OrderType;
import ru.ylab.service.OrderServiceImpl;

import java.util.Scanner;

public class OrderController {

    private final OrderServiceImpl orderServiceImpl;

    public OrderController(OrderServiceImpl orderServiceImpl) {
        this.orderServiceImpl = orderServiceImpl;
    }

    public void createOrder(Scanner scanner, int userId) {
        System.out.print("Enter car ID: ");
        int carId = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Enter order type (PURCHASE, SERVICE): ");
        OrderType type = OrderType.valueOf(scanner.nextLine().toUpperCase());

        orderServiceImpl.createOrder(carId, userId, type);
    }

    public void viewAllOrders() {
        orderServiceImpl.getAllOrders();
    }

    public void updateOrderStatus(Scanner scanner) {
        System.out.print("Enter order ID to update: ");
        int orderId = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Enter new status (PENDING, COMPLETED, CANCELED): ");
        OrderStatus status = OrderStatus.valueOf(scanner.nextLine().toUpperCase());

        orderServiceImpl.updateOrderStatus(orderId, status);
    }

    public void viewMyOrders(int userId) {
        orderServiceImpl.getOrdersByUserId(userId);
    }
}
