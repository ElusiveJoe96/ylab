package ru.ylab.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.ylab.domain.enums.OrderStatus;
import ru.ylab.domain.enums.OrderType;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Order {
    private int id;
    private int carId;
    private int userId;
    private LocalDateTime orderDate;
    private OrderStatus status;
    private OrderType type;
}
