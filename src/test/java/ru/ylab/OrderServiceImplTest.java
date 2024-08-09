package ru.ylab;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.ylab.audit.AuditService;
import ru.ylab.domain.enums.OrderStatus;
import ru.ylab.domain.enums.OrderType;
import ru.ylab.domain.enums.Role;
import ru.ylab.domain.model.Order;
import ru.ylab.domain.model.User;
import ru.ylab.repository.OrderRepository;
import ru.ylab.service.implementation.OrderServiceImpl;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

public class OrderServiceImplTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private AuditService auditService;

    @InjectMocks
    private OrderServiceImpl orderServiceImpl;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        AuditService.loggedInUser = new User(1, "Test User", "test@example.com",
                "password", Role.CLIENT, "123-456-7890");
    }


//    @Test
//    public void testCreateOrderCarAlreadySold() {
//        int carId = 1;
//        int userId = 1;
//        OrderType type = OrderType.SERVICE;
//
//        when(orderRepository.findAll()).thenReturn(List.of(new Order(1, carId, userId,
//                LocalDateTime.now(), OrderStatus.COMPLETED, type)));
//
//        orderServiceImpl.createOrder(carId, userId, type);
//
//        verify(orderRepository, times(1)).save(any(Order.class));
//        verify(auditService, times(1)).logAction(anyInt(),
//                eq("CREATE_ORDER"), anyString());
//    }

//    @Test
//    public void testUpdateOrderStatusSuccess() {
//        int orderId = 1;
//        OrderStatus newStatus = OrderStatus.COMPLETED;
//        Order order = new Order(orderId, 1, 1, LocalDateTime.now(), OrderStatus.PENDING, OrderType.PURCHASE);
//
//        when(orderRepository.findById(orderId)).thenReturn(order);
//
//        orderServiceImpl.updateOrderStatus(orderId, newStatus);
//
//        verify(orderRepository, times(1)).save(order);
//        verify(auditService, times(1)).logAction(anyInt(),
//                eq("UPDATE_ORDER_STATUS"), anyString());
//        assertEquals(newStatus, order.getStatus());
//    }

//    @Test
//    public void testUpdateOrderStatusOrderNotFound() {
//        int orderId = 1;
//        OrderStatus newStatus = OrderStatus.COMPLETED;
//
//        when(orderRepository.findById(orderId)).thenReturn(null);
//
//        orderServiceImpl.updateOrderStatus(orderId, newStatus);
//
//        verify(orderRepository, never()).save(any(Order.class));
//        verify(auditService, never()).logAction(anyInt(), anyString(), anyString());
//    }

//    @Test
//    public void testDeleteOrderSuccess() {
//        int orderId = 1;
//        Order order = new Order(orderId, 1, 1, LocalDateTime.now(), OrderStatus.PENDING, OrderType.PURCHASE);
//
//        when(orderRepository.findById(orderId)).thenReturn(order);
//
//        orderServiceImpl.deleteOrder(orderId);
//
//        verify(orderRepository, times(1)).delete(orderId);
//        verify(auditService, times(1)).logAction(anyInt(),
//                eq("DELETE_ORDER"), eq("Deleted order with ID: " + orderId));
//    }
//
//    @Test
//    public void testDeleteOrderOrderNotFound() {
//        int orderId = 1;
//
//        when(orderRepository.findById(orderId)).thenReturn(null);
//
//        orderServiceImpl.deleteOrder(orderId);
//
//        verify(orderRepository, never()).delete(anyInt());
//        verify(auditService, never()).logAction(anyInt(), anyString(), anyString());
//    }

    @Test
    public void testGetAllOrders() {
        Order order1 = new Order(1, 1, 1, LocalDateTime.now(), OrderStatus.PENDING, OrderType.PURCHASE);
        Order order2 = new Order(2, 2, 2, LocalDateTime.now(), OrderStatus.COMPLETED, OrderType.SERVICE);

        when(orderRepository.findAll()).thenReturn(List.of(order1, order2));

        orderServiceImpl.getAllOrders();

        verify(orderRepository, times(1)).findAll();
    }

    @Test
    public void testGetOrdersByStatus() {
        OrderStatus status = OrderStatus.PENDING;
        Order order1 = new Order(1, 1, 1, LocalDateTime.now(), status, OrderType.PURCHASE);
        Order order2 = new Order(2, 2, 2, LocalDateTime.now(), status, OrderType.SERVICE);

        when(orderRepository.findByStatus(status)).thenReturn(List.of(order1, order2));

        List<Order> orders = orderServiceImpl.getOrdersByStatus(status);

        assertEquals(2, orders.size());
        assertTrue(orders.contains(order1));
        assertTrue(orders.contains(order2));
    }

    @Test
    public void testGetOrdersByUserId() {
        int userId = 1;
        Order order1 = new Order(1, 1, userId, LocalDateTime.now(), OrderStatus.PENDING, OrderType.PURCHASE);
        Order order2 = new Order(2, 2, userId, LocalDateTime.now(), OrderStatus.COMPLETED, OrderType.SERVICE);

        when(orderRepository.findByClientId(userId)).thenReturn(List.of(order1, order2));

        orderServiceImpl.getOrdersByUserId(userId);

        verify(orderRepository, times(1)).findByClientId(userId);
    }

    @Test
    public void testGetOrdersByCarId() {
        int carId = 1;
        Order order1 = new Order(1, carId, 1, LocalDateTime.now(), OrderStatus.PENDING, OrderType.PURCHASE);
        Order order2 = new Order(2, carId, 2, LocalDateTime.now(), OrderStatus.COMPLETED, OrderType.SERVICE);

        when(orderRepository.findByCarId(carId)).thenReturn(List.of(order1, order2));

        List<Order> orders = orderServiceImpl.getOrdersByCarId(carId);

        assertEquals(2, orders.size());
        assertTrue(orders.contains(order1));
        assertTrue(orders.contains(order2));
    }

    @Test
    public void testGetOrderById() {
        int orderId = 1;
        Order order = new Order(orderId, 1, 1, LocalDateTime.now(), OrderStatus.PENDING, OrderType.PURCHASE);

        when(orderRepository.findById(orderId)).thenReturn(order);

        Order result = orderServiceImpl.getOrderById(orderId);

        assertEquals(order, result);
    }
}
