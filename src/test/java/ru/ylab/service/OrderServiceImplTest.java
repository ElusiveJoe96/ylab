package ru.ylab.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.ylab.audit.AuditService;
import ru.ylab.domain.enums.OrderStatus;
import ru.ylab.domain.enums.OrderType;
import ru.ylab.domain.model.Order;
import ru.ylab.repository.OrderRepository;
import ru.ylab.service.implementation.OrderServiceImpl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class OrderServiceImplTest {
    private OrderRepository orderRepository;
    private AuditService auditService;
    private OrderServiceImpl orderService;

    @BeforeEach
    public void setUp() {
        orderRepository = mock(OrderRepository.class);
        auditService = mock(AuditService.class);
        orderService = new OrderServiceImpl(orderRepository, auditService);
    }

    @Test
    public void testCreateOrder_CarAlreadySold() {
        Scanner scanner = new Scanner("1\nPURCHASE\n");
        when(orderRepository.findAll()).thenReturn(List.of(
                new Order(1, 1, 1, LocalDateTime.now(), OrderStatus.COMPLETED, OrderType.PURCHASE)
        ));

        orderService.createOrder(scanner);

        verify(orderRepository, never()).save(any(Order.class));
        verify(auditService, never()).logAction(anyInt(), anyString(), anyString());
    }

    @Test
    public void testUpdateOrderStatus_OrderNotFound() {
        Scanner scanner = new Scanner("1\nCOMPLETED\n");
        when(orderRepository.findById(1)).thenReturn(null);

        orderService.updateOrderStatus(scanner);

        verify(orderRepository, never()).save(any(Order.class));
        verify(auditService, never()).logAction(anyInt(), eq("UPDATE_ORDER_STATUS"), anyString());
    }

    @Test
    public void testDeleteOrder_OrderNotFound() {
        Scanner scanner = new Scanner("1\n");
        when(orderRepository.findById(1)).thenReturn(null);

        orderService.deleteOrder(scanner);

        verify(orderRepository, never()).delete(1);
        verify(auditService, never()).logAction(anyInt(), eq("DELETE_ORDER"), anyString());
    }


    @Test
    public void testGetAllOrders_NoOrders() {
        when(orderRepository.findAll()).thenReturn(List.of());

        orderService.getAllOrders();

        verify(orderRepository, times(1)).findAll();
    }

    @Test
    public void testGetAllOrders_WithOrders() {
        when(orderRepository.findAll()).thenReturn(List.of(
                new Order(1, 1, 1, LocalDateTime.now(), OrderStatus.PENDING, OrderType.PURCHASE),
                new Order(2, 2, 2, LocalDateTime.now(), OrderStatus.COMPLETED, OrderType.SERVICE)
        ));

        orderService.getAllOrders();

        verify(orderRepository, times(1)).findAll();
    }

    @Test
    public void testGetOrdersByStatus() {
        when(orderRepository.findByStatus(OrderStatus.PENDING)).thenReturn(List.of(
                new Order(1, 1, 1, LocalDateTime.now(), OrderStatus.PENDING, OrderType.PURCHASE)
        ));

        List<Order> orders = orderService.getOrdersByStatus(OrderStatus.PENDING);

        assertEquals(1, orders.size());
        assertEquals(OrderStatus.PENDING, orders.get(0).getStatus());
    }

    @Test
    public void testGetOrdersByUserId_NoOrders() {
        when(orderRepository.findByClientId(1)).thenReturn(List.of());

        orderService.getOrdersByUserId(1);

        verify(orderRepository, times(1)).findByClientId(1);
    }

    @Test
    public void testGetOrdersByUserId_WithOrders() {
        when(orderRepository.findByClientId(1)).thenReturn(List.of(
                new Order(1, 1, 1, LocalDateTime.now(), OrderStatus.PENDING, OrderType.PURCHASE)
        ));

        orderService.getOrdersByUserId(1);

        verify(orderRepository, times(1)).findByClientId(1);
    }

    @Test
    public void testGetOrderById() {
        Order order = new Order(1, 1, 1, LocalDateTime.now(), OrderStatus.PENDING, OrderType.PURCHASE);
        when(orderRepository.findById(1)).thenReturn(order);

        Order foundOrder = orderService.getOrderById(1);

        assertNotNull(foundOrder);
        assertEquals(1, foundOrder.getId());
    }
}
