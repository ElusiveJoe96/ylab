package ru.ylab.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.ylab.domain.dto.OrderDTO;
import ru.ylab.domain.dto.mapper.OrderMapper;
import ru.ylab.domain.enums.OrderStatus;
import ru.ylab.domain.enums.OrderType;
import ru.ylab.domain.model.Order;
import ru.ylab.repository.OrderRepository;
import ru.ylab.service.implementation.OrderServiceImpl;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class OrderServiceImplTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderMapper orderMapper;

    @InjectMocks
    private OrderServiceImpl orderService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Create order when car is already sold and ensure no actions are performed")
    public void testCreateOrder_CarAlreadySold() {
        OrderDTO orderDTO = new OrderDTO(1, 1, 1, null, OrderStatus.PENDING, OrderType.PURCHASE);

        when(orderRepository.findAll()).thenReturn(List.of(
                new Order(1, 1, 1, LocalDateTime.now(), OrderStatus.COMPLETED, OrderType.PURCHASE)
        ));

        orderService.createOrder(orderDTO);

        verify(orderRepository, never()).save(any(Order.class));
    }

    @Test
    @DisplayName("Create order when car is not sold and ensure order is saved")
    public void testCreateOrder_CarNotSold() {
        OrderDTO orderDTO = new OrderDTO(2, 2, 2, null, OrderStatus.PENDING, OrderType.PURCHASE);
        Order order = new Order(2, 2, 2, LocalDateTime.now(), OrderStatus.PENDING, OrderType.PURCHASE);

        when(orderRepository.findAll()).thenReturn(List.of());
        when(orderMapper.toEntity(any(OrderDTO.class))).thenReturn(order);

        orderService.createOrder(orderDTO);

        verify(orderRepository, times(1)).save(order);
    }

    @Test
    @DisplayName("Update order status when order is not found and ensure no actions are performed")
    public void testUpdateOrderStatus_OrderNotFound() {
        when(orderRepository.findById(1)).thenReturn(null);

        boolean result = orderService.updateOrderStatus(1, OrderStatus.COMPLETED);

        assertFalse(result);
        verify(orderRepository, never()).save(any(Order.class));
    }

    @Test
    @DisplayName("Update order status when order is found and ensure order is saved")
    public void testUpdateOrderStatus_OrderFound() {
        Order order = new Order(1, 1, 1, LocalDateTime.now(), OrderStatus.PENDING, OrderType.PURCHASE);

        when(orderRepository.findById(1)).thenReturn(order);

        boolean result = orderService.updateOrderStatus(1, OrderStatus.COMPLETED);

        assertTrue(result);
        assertEquals(OrderStatus.COMPLETED, order.getStatus());
        verify(orderRepository, times(1)).save(order);
    }

    @Test
    @DisplayName("Delete order when order is not found and ensure no actions are performed")
    public void testDeleteOrder_OrderNotFound() {
        when(orderRepository.findById(1)).thenReturn(null);

        boolean result = orderService.deleteOrder(1);

        assertFalse(result);
        verify(orderRepository, never()).delete(1);
    }

    @Test
    @DisplayName("Delete order when order is found and ensure order is deleted")
    public void testDeleteOrder_OrderFound() {
        Order order = new Order(1, 1, 1, LocalDateTime.now(), OrderStatus.PENDING, OrderType.PURCHASE);

        when(orderRepository.findById(1)).thenReturn(order);

        boolean result = orderService.deleteOrder(1);

        assertTrue(result);
        verify(orderRepository, times(1)).delete(1);
    }

    @Test
    @DisplayName("Get all orders when there are no orders in the repository")
    public void testGetAllOrders_NoOrders() {
        when(orderRepository.findAll()).thenReturn(List.of());

        List<OrderDTO> result = orderService.getAllOrders();

        assertTrue(result.isEmpty());
        verify(orderRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Get all orders when there are orders in the repository")
    public void testGetAllOrders_WithOrders() {
        Order order1 = new Order(1, 1, 1, LocalDateTime.now(), OrderStatus.PENDING, OrderType.PURCHASE);
        Order order2 = new Order(2, 2, 2, LocalDateTime.now(), OrderStatus.COMPLETED, OrderType.SERVICE);

        when(orderRepository.findAll()).thenReturn(List.of(order1, order2));
        when(orderMapper.toDTO(any(Order.class)))
                .thenAnswer(invocation -> {
                    Order order = invocation.getArgument(0);
                    return new OrderDTO(order.getId(), order.getCarId(),
                            order.getUserId(), order.getOrderDate(), order.getStatus(), order.getType());
                });

        List<OrderDTO> result = orderService.getAllOrders();

        assertEquals(2, result.size());
        verify(orderRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Get orders by status and verify the results")
    public void testGetOrdersByStatus() {
        Order order = new Order(1, 1, 1, LocalDateTime.now(), OrderStatus.PENDING, OrderType.PURCHASE);

        when(orderRepository.findByStatus(OrderStatus.PENDING)).thenReturn(List.of(order));
        when(orderMapper.toDTO(any(Order.class))).thenReturn(new OrderDTO(order.getId(),
                order.getCarId(), order.getUserId(), order.getOrderDate(), order.getStatus(), order.getType()));

        List<OrderDTO> orders = orderService.getOrdersByStatus(OrderStatus.PENDING);

        assertEquals(1, orders.size());
        assertEquals(OrderStatus.PENDING, orders.get(0).getStatus());
    }

    @Test
    @DisplayName("Get orders by user ID when no orders exist for the user")
    public void testGetOrdersByUserId_NoOrders() {
        when(orderRepository.findByClientId(1)).thenReturn(List.of());

        List<OrderDTO> result = orderService.getOrdersByUserId(1);

        assertTrue(result.isEmpty());
        verify(orderRepository, times(1)).findByClientId(1);
    }

    @Test
    @DisplayName("Get orders by user ID when orders exist for the user")
    public void testGetOrdersByUserId_WithOrders() {
        Order order = new Order(1, 1, 1, LocalDateTime.now(), OrderStatus.PENDING, OrderType.PURCHASE);

        when(orderRepository.findByClientId(1)).thenReturn(List.of(order));
        when(orderMapper.toDTO(any(Order.class))).thenReturn(new OrderDTO(order.getId(),
                order.getCarId(), order.getUserId(), order.getOrderDate(), order.getStatus(), order.getType()));

        List<OrderDTO> result = orderService.getOrdersByUserId(1);

        assertEquals(1, result.size());
        verify(orderRepository, times(1)).findByClientId(1);
    }

    @Test
    @DisplayName("Get order by ID and verify the result")
    public void testGetOrderById() {
        Order order = new Order(1, 1, 1, LocalDateTime.now(), OrderStatus.PENDING, OrderType.PURCHASE);

        when(orderRepository.findById(1)).thenReturn(order);
        when(orderMapper.toDTO(any(Order.class))).thenReturn(new OrderDTO(order.getId(), order.getCarId(),
                order.getUserId(), order.getOrderDate(), order.getStatus(), order.getType()));

        OrderDTO result = orderService.getOrderById(1);

        assertNotNull(result);
        assertEquals(1, result.getId());
    }
}
