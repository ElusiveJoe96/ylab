package ru.ylab.domain.model;

import lombok.Data;
import ru.ylab.domain.enums.OrderStatus;
import ru.ylab.domain.enums.OrderType;

import java.time.LocalDateTime;

@Data
public class Order {
    private int id;
    private int carId;
    private int userId;
    private LocalDateTime orderDate;
    private OrderStatus status;
    private OrderType type;

    public Order(int id, int carId, int userId, LocalDateTime orderDate, OrderStatus status, OrderType type) {
        this.id = id;
        this.carId = carId;
        this.userId = userId;
        this.orderDate = orderDate;
        this.status = status;
        this.type = type;
    }

}
