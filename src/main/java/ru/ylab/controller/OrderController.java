package ru.ylab.controller;

import ru.ylab.service.OrderService;

import java.util.Scanner;

public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    public void createOrder(Scanner scanner, int userId) {
        orderService.createOrder(userId, scanner);
    }

    public void updateOrderStatus(Scanner scanner) {
        orderService.updateOrderStatus(scanner);
    }

    public void deleteOrder(Scanner scanner) {
        orderService.deleteOrder(scanner);
    }

    public void viewAllOrders() {
        orderService.getAllOrders();
    }

    public void viewMyOrders(int userId) {
        orderService.getOrdersByUserId(userId);
    }
}
