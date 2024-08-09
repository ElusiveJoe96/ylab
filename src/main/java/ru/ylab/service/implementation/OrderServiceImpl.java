package ru.ylab.service.implementation;

import ru.ylab.audit.AuditService;
import ru.ylab.domain.model.Order;
import ru.ylab.domain.enums.OrderStatus;
import ru.ylab.domain.enums.OrderType;
import ru.ylab.repository.OrderRepository;
import ru.ylab.service.OrderService;
import ru.ylab.util.ValidationUtil;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Scanner;

public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final AuditService auditService;

    public OrderServiceImpl(OrderRepository orderRepository, AuditService auditService) {
        this.orderRepository = orderRepository;
        this.auditService = auditService;
    }

    //TODO swap args, more functions?
    public void createOrder(int userId, Scanner scanner) {
        System.out.print("Enter car ID: ");
        int carId = ValidationUtil.getValidInt(scanner);
        scanner.nextLine();
        System.out.print("Enter order type (PURCHASE, SERVICE): ");
        OrderType type = ValidationUtil.getValidEnumValue(scanner, OrderType.class);

        List<Order> existingOrders = orderRepository.findAll();
        boolean isSold = existingOrders.stream()
                .anyMatch(order -> order.getCarId() == carId && order.getStatus() == OrderStatus.COMPLETED);

        if (isSold) {
            System.out.println("Car sold");
        }

        Order order = new Order(0, carId, userId, LocalDateTime.now(), OrderStatus.PENDING, type);
        orderRepository.save(order);
        System.out.println("Order created successfully.");
        auditService.logAction(AuditService.loggedInUser.getId(), "CREATE_ORDER",
                "Created order: " + order);
    }

    public void updateOrderStatus(Scanner scanner) {
        System.out.print("Enter order ID to update: ");
        int orderId = ValidationUtil.getValidInt(scanner);

        Order order = orderRepository.findById(orderId);
        if (order != null) {
            scanner.nextLine();
            System.out.print("Enter new status (PENDING, COMPLETED, CANCELED): ");
            OrderStatus status = ValidationUtil.getValidEnumValue(scanner, OrderStatus.class);
            order.setStatus(status);
            orderRepository.save(order);
            System.out.println("Order status updated successfully.");
            auditService.logAction(AuditService.loggedInUser.getId(), "UPDATE_ORDER_STATUS",
                    "Updated order status: " + order);
        } else {
            System.out.println("Order not found");
        }
    }

    public void deleteOrder(Scanner scanner) {
        System.out.print("Enter order ID to delete: ");
        int orderId = ValidationUtil.getValidInt(scanner);

        if (orderRepository.findById(orderId) != null) {
            orderRepository.delete(orderId);
            auditService.logAction(AuditService.loggedInUser.getId(), "DELETE_ORDER",
                    "Deleted order with ID: " + orderId);
        } else {
            System.out.println("Order not found");
        }
    }

    //TODO creat method to close order(set status canceled)

    public void getAllOrders() {
        List<Order> orders = orderRepository.findAll();
        if (orders.isEmpty()) {
            System.out.println("No orders available.");
        } else {
            orders.forEach(order -> System.out.println(order.getId() + ": Car ID " + order.getCarId() +
                    ", User ID " + order.getUserId() + ", Date: " + order.getOrderDate() + ", Status: " +
                    order.getStatus() + ", Type: " + order.getType()));
        }
    }

    public List<Order> getOrdersByStatus(OrderStatus status) {
        return orderRepository.findByStatus(status);
    }

    public void getOrdersByUserId(int userId) {
        List<Order> orders = orderRepository.findByClientId(userId);
        if (orders.isEmpty()) {
            System.out.println("No orders found for your account.");
        } else {
            orders.forEach(order -> System.out.println(order.getId() + ": Car ID " + order.getCarId() +
                    ", Date: " + order.getOrderDate() + ", Status: " + order.getStatus() + ", Type: " + order.getType()));
        }
    }

    public List<Order> getOrdersByCarId(int carId) {
        return orderRepository.findByCarId(carId);
    }

    public Order getOrderById(int orderId) {
        return orderRepository.findById(orderId);
    }
}
