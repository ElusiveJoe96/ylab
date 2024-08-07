package ru.ylab.service;

import ru.ylab.audit.AuditService;
import ru.ylab.domain.model.Order;
import ru.ylab.domain.enums.OrderStatus;
import ru.ylab.domain.enums.OrderType;
import ru.ylab.output.OrderRepository;


import java.time.LocalDateTime;
import java.util.List;

public class OrderService {
    private final OrderRepository orderRepository;
    private final AuditService auditService;

    public OrderService(OrderRepository orderRepository, AuditService auditService) {
        this.orderRepository = orderRepository;
        this.auditService = auditService;
    }

    public void createOrder(int carId, int userId, OrderType type) {

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

    public void updateOrderStatus(int orderId, OrderStatus status) {
        Order order = orderRepository.findById(orderId);
        if (order != null) {
            order.setStatus(status);
            orderRepository.save(order);
            System.out.println("Order status updated successfully.");
            auditService.logAction(AuditService.loggedInUser.getId(), "UPDATE_ORDER_STATUS",
                    "Updated order status: " + order);
        } else {
            System.out.println("Order not found");
        }
    }

    public void deleteOrder(int orderId) {
        if (orderRepository.findById(orderId) != null) {
            orderRepository.delete(orderId);
        } else {
            System.out.println("Order not found");
            return;
        }

        auditService.logAction(AuditService.loggedInUser.getId(), "DELETE_ORDER",
                "Deleted order with ID: " + orderId);
    }

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
