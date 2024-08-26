package ru.ylab.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.ylab.domain.dto.OrderDTO;
import ru.ylab.service.OrderService;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class OrderControllerTest {

    private OrderController orderController;
    private OrderService orderService;

    private OrderDTO sampleOrder;

    @BeforeEach
    public void setUp() {
        orderService = Mockito.mock(OrderService.class);
        orderController = new OrderController(orderService);

        sampleOrder = new OrderDTO(1, 1, LocalDateTime.now(), null, null);
    }

    @Test
    @DisplayName("getAllOrders should return a list of orders")
    public void getAllOrders_ShouldReturnOrderList() {
        when(orderService.getAllOrders()).thenReturn(Collections.singletonList(sampleOrder));

        ResponseEntity<List<OrderDTO>> response = orderController.getAllOrders();

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(1, Objects.requireNonNull(response.getBody()).size());
        Assertions.assertEquals(sampleOrder.getCarId(), response.getBody().get(0).getCarId());
        Assertions.assertEquals(sampleOrder.getUserId(), response.getBody().get(0).getUserId());
    }

    @Test
    @DisplayName("getOrdersByUserId should return a list of orders for a specific user")
    public void getOrdersByUserId_ShouldReturnOrderList() {
        when(orderService.getOrdersByUserId(1)).thenReturn(Collections.singletonList(sampleOrder));

        ResponseEntity<List<OrderDTO>> response = orderController.getOrdersByUserId(1);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(1, Objects.requireNonNull(response.getBody()).size());
        Assertions.assertEquals(sampleOrder.getCarId(), response.getBody().get(0).getCarId());
        Assertions.assertEquals(sampleOrder.getUserId(), response.getBody().get(0).getUserId());
    }

    @Test
    @DisplayName("addOrder should return the created order")
    public void addOrder_ShouldReturnCreatedOrder() {
        when(orderService.addOrder(ArgumentMatchers.any(OrderDTO.class))).thenReturn(sampleOrder);

        ResponseEntity<OrderDTO> response = orderController.addOrder(sampleOrder);

        Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());
        Assertions.assertEquals(sampleOrder.getCarId(), Objects.requireNonNull(response.getBody()).getCarId());
        Assertions.assertEquals(sampleOrder.getUserId(), response.getBody().getUserId());
    }

    @Test
    @DisplayName("updateOrder should return the updated order when it exists")
    public void updateOrder_ShouldReturnUpdatedOrder_WhenOrderExists() {
        when(orderService.updateOrder(eq(1), any(OrderDTO.class))).thenReturn(sampleOrder);

        ResponseEntity<OrderDTO> response = orderController.updateOrder(1, sampleOrder);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(sampleOrder.getCarId(), Objects.requireNonNull(response.getBody()).getCarId());
        Assertions.assertEquals(sampleOrder.getUserId(), response.getBody().getUserId());
    }

    @Test
    @DisplayName("updateOrder should return 404 when the order does not exist")
    public void updateOrder_ShouldReturnNotFound_WhenOrderDoesNotExist() {
        when(orderService.updateOrder(eq(1), any(OrderDTO.class))).thenReturn(null);

        ResponseEntity<OrderDTO> response = orderController.updateOrder(1, sampleOrder);

        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        Assertions.assertNull(response.getBody());
    }

    @Test
    @DisplayName("deleteOrder should return 204 No Content when the order is deleted")
    public void deleteOrder_ShouldReturnNoContent() {
        doNothing().when(orderService).deleteOrder(1);

        ResponseEntity<Void> response = orderController.deleteOrder(1);

        Assertions.assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(orderService, times(1)).deleteOrder(1);
    }
}
