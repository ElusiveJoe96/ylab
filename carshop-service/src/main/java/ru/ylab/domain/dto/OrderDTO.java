package ru.ylab.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.ylab.domain.enums.OrderStatus;
import ru.ylab.domain.enums.OrderType;

import java.time.LocalDateTime;

/**
 * Data Transfer Object (DTO) for representing an order.
 * <p>
 * This class is used to encapsulate the data related to an order. It includes information
 * about the car being ordered, the user who placed the order, the date and time of the order,
 * and the status and type of the order.
 * </p>
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDTO {

    /**
     * The ID of the car being ordered.
     * <p>
     * This is a reference to the car that is associated with this order.
     * </p>
     */
    private int carId;

    /**
     * The ID of the user who placed the order.
     * <p>
     * This is a reference to the user who made the order.
     * </p>
     */
    private int userId;

    /**
     * The date and time when the order was placed.
     * <p>
     * This provides the timestamp of when the order was created.
     * </p>
     */
    private LocalDateTime orderDate;

    /**
     * The current status of the order.
     * <p>
     * This indicates the current state of the order, such as pending, completed, or cancelled.
     * It is represented by the {@link OrderStatus} enum.
     * </p>
     */
    private OrderStatus status;

    /**
     * The type of the order.
     * <p>
     * This describes the nature of the order, such as a purchase or a lease.
     * It is represented by the {@link OrderType} enum.
     * </p>
     */
    private OrderType type;
}