package ru.ylab.service;

import ru.ylab.domain.dto.OrderDTO;

import java.util.List;

public interface OrderService {
    List<OrderDTO> getAllOrders();
    List<OrderDTO> getOrdersByUserId(int userId);
    OrderDTO addOrder(OrderDTO orderDTO);
    OrderDTO updateOrder(int id, OrderDTO orderDTO);
    void deleteOrder(int id);
}
