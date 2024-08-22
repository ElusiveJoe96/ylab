package ru.ylab.service;

import ru.ylab.domain.dto.OrderDTO;
import ru.ylab.domain.enums.OrderStatus;

import java.util.List;

public interface OrderService {
    void createOrder(OrderDTO orderDTO);
    boolean updateOrderStatus(int orderId, OrderStatus status);
    boolean deleteOrder(int orderId);
    List<OrderDTO> getAllOrders();
    List<OrderDTO> getOrdersByStatus(OrderStatus status);
    List<OrderDTO> getOrdersByUserId(int userId);
    List<OrderDTO> getOrdersByCarId(int carId);
    OrderDTO getOrderById(int orderId);
}
