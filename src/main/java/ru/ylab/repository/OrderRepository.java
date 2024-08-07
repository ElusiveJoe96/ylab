package ru.ylab.repository;

import ru.ylab.domain.model.Order;
import ru.ylab.domain.enums.OrderStatus;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class OrderRepository {
    private final HashMap<Integer, Order> orders = new HashMap<>();
    private int idCounter = 1;

    public void save(Order order) {
        if (order.getId() == 0) {
            order.setId(idCounter++);
        }
        orders.put(order.getId(), order);
    }

    public void delete(int orderId) {
        orders.remove(orderId);
    }

    public Order findById(int orderId) {
        return orders.get(orderId);
    }

    public List<Order> findAll() {
        return List.copyOf(orders.values());
    }

    public List<Order> findByStatus(OrderStatus status) {
        return orders.values().stream()
                .filter(order -> order.getStatus() == status)
                .collect(Collectors.toList());
    }

    public List<Order> findByDate(LocalDate date) {
        return orders.values().stream()
                .filter(order -> order.getOrderDate().toLocalDate().equals(date))
                .collect(Collectors.toList());
    }

    public List<Order> findByClientId(int clientId) {
        return orders.values().stream()
                .filter(order -> order.getUserId() == clientId)
                .collect(Collectors.toList());
    }

    public List<Order> findByCarId(int carId) {
        return orders.values().stream()
                .filter(order -> order.getCarId() == carId)
                .collect(Collectors.toList());
    }
}
