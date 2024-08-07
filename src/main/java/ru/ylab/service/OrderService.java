package ru.ylab.service;

import ru.ylab.domain.enums.OrderStatus;
import ru.ylab.domain.enums.OrderType;
import ru.ylab.domain.model.Order;

import java.util.List;

public interface OrderService {
    void createOrder(int carId, int userId, OrderType type);
    void updateOrderStatus(int orderId, OrderStatus status);
    void deleteOrder(int orderId);
    void getAllOrders();
    List<Order> getOrdersByStatus(OrderStatus status);
    void getOrdersByUserId(int userId);
    List<Order> getOrdersByCarId(int carId);
    Order getOrderById(int orderId);
}
