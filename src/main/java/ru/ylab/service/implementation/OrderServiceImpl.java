package ru.ylab.service.implementation;

import ru.ylab.domain.dto.OrderDTO;
import ru.ylab.domain.dto.mapper.OrderMapper;
import ru.ylab.domain.model.Order;
import ru.ylab.domain.enums.OrderStatus;
import ru.ylab.repository.OrderRepository;
import ru.ylab.service.OrderService;


import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper = OrderMapper.INSTANCE;

    public OrderServiceImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public void createOrder(OrderDTO orderDTO) {
        List<Order> existingOrders = orderRepository.findAll();
        boolean isSold = existingOrders.stream()
                .anyMatch(order -> order.getCarId() == orderDTO.getCarId()
                        && order.getStatus() == OrderStatus.COMPLETED);

        if (isSold) {
            System.out.println("Car sold");
        } else {
            Order order = orderMapper.toEntity(orderDTO);
            order.setOrderDate(LocalDateTime.now());
            order.setStatus(OrderStatus.PENDING);
            orderRepository.save(order);
            System.out.println("Order created successfully.");
        }
    }

    @Override
    public boolean updateOrderStatus(int orderId, OrderStatus status) {
        Order order = orderRepository.findById(orderId);
        if (order != null) {
            order.setStatus(status);
            orderRepository.save(order);
            System.out.println("Order status updated successfully.");
            return true;
        }
        return false;
    }

    @Override
    public boolean deleteOrder(int orderId) {
        if (orderRepository.findById(orderId) != null) {
            orderRepository.delete(orderId);
            System.out.println("Order deleted successfully.");
            return true;
        }
        return false;
    }

    @Override
    public OrderDTO getOrderById(int orderId) {
        Order order = orderRepository.findById(orderId);
        return order != null ? orderMapper.toDTO(order) : null;
    }

    @Override
    public List<OrderDTO> getAllOrders() {
        List<Order> orders = orderRepository.findAll();
        return orders.stream()
                .map(orderMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<OrderDTO> getOrdersByStatus(OrderStatus status) {
        List<Order> orders = orderRepository.findByStatus(status);
        return orders.stream()
                .map(orderMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<OrderDTO> getOrdersByUserId(int userId) {
        List<Order> orders = orderRepository.findByClientId(userId);
        return orders.stream()
                .map(orderMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<OrderDTO> getOrdersByCarId(int carId) {
        List<Order> orders = orderRepository.findByCarId(carId);
        return orders.stream()
                .map(orderMapper::toDTO)
                .collect(Collectors.toList());
    }
}
