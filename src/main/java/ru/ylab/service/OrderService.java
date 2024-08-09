package ru.ylab.service;

import ru.ylab.domain.enums.OrderStatus;
import ru.ylab.domain.model.Order;

import java.util.List;
import java.util.Scanner;

public interface OrderService {
    void createOrder(int userId, Scanner scanner);
    void updateOrderStatus(Scanner scanner);
    void deleteOrder(Scanner scanner);
    void getAllOrders();
    List<Order> getOrdersByStatus(OrderStatus status);
    void getOrdersByUserId(int userId);
    List<Order> getOrdersByCarId(int carId);
    Order getOrderById(int orderId);
}
