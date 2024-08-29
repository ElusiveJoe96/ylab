package ru.ylab.service;

import ru.ylab.domain.dto.OrderDTO;

import java.util.List;

/**
 * Service interface for managing orders.
 * Provides methods for retrieving, adding, updating, and deleting orders.
 */
public interface OrderService {

    /**
     * Retrieves all orders.
     *
     * @return a list of all orders as {@link OrderDTO}.
     */
    List<OrderDTO> getAllOrders();

    /**
     * Retrieves all orders placed by a specific user.
     *
     * @param userId the unique identifier of the user.
     * @return a list of orders as {@link OrderDTO} placed by the specified user.
     */
    List<OrderDTO> getOrdersByUserId(int userId);

    /**
     * Adds a new order to the system.
     *
     * @param orderDTO the order to be added, represented as {@link OrderDTO}.
     * @return the created order as {@link OrderDTO}.
     */
    OrderDTO addOrder(OrderDTO orderDTO);

    /**
     * Updates an existing order.
     *
     * @param id the unique identifier of the order to be updated.
     * @param orderDTO the updated order data, represented as {@link OrderDTO}.
     * @return the updated order as {@link OrderDTO}, or {@code null} if no order is found with the given ID.
     */
    OrderDTO updateOrder(int id, OrderDTO orderDTO);

    /**
     * Deletes an order by its unique identifier.
     *
     * @param id the unique identifier of the order to be deleted.
     */
    void deleteOrder(int id);
}
