package ru.ylab.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.ylab.domain.enums.OrderStatus;
import ru.ylab.domain.enums.OrderType;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDTO {
    private int carId;
    private int userId;
    private LocalDateTime orderDate;
    private OrderStatus status;
    private OrderType type;
}
