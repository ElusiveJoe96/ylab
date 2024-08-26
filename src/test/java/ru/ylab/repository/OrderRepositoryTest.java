package ru.ylab.repository;

import org.junit.jupiter.api.*;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.ylab.config.DatabaseConfig;
import ru.ylab.config.LiquibaseConfig;
import ru.ylab.domain.enums.OrderStatus;
import ru.ylab.domain.enums.OrderType;
import ru.ylab.domain.model.Order;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
public class OrderRepositoryTest {

    @Container
    public static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:latest")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");

    private static DatabaseConfig databaseConfig;
    private static OrderRepository orderRepository;
    private static LiquibaseConfig liquibaseConfig;

    @BeforeAll
    public static void setUpDatabaseConfig() {
        databaseConfig = new DatabaseConfig() {
            @Override
            public Connection getConnection() {
                try {
                    return DriverManager.getConnection(
                            postgreSQLContainer.getJdbcUrl(),
                            postgreSQLContainer.getUsername(),
                            postgreSQLContainer.getPassword());
                } catch (Exception e) {
                    throw new RuntimeException("Failed to connect to the database", e);
                }
            }
        };

        orderRepository = new OrderRepository(databaseConfig);
        liquibaseConfig = new LiquibaseConfig(databaseConfig);
        liquibaseConfig.runMigrations();
    }


    @Test
    @DisplayName("Save a new order and verify it is saved correctly")
    public void testSave_InsertNewOrder() {
        Order order = new Order();
        order.setUserId(1);
        order.setCarId(2);
        order.setOrderDate(LocalDateTime.now());
        order.setStatus(OrderStatus.PENDING);
        order.setType(OrderType.PURCHASE);

        orderRepository.save(order);

        assertTrue(order.getId() > 0);

        Order foundOrder = orderRepository.findById(order.getId());
        assertNotNull(foundOrder);
        assertEquals(OrderStatus.PENDING, foundOrder.getStatus());
        assertEquals(OrderType.PURCHASE, foundOrder.getType());
    }

    @Test
    @DisplayName("Update an existing order and verify the update is correct")
    public void testSave_UpdateExistingOrder() {
        Order order = new Order();
        order.setUserId(1);
        order.setCarId(2);
        order.setOrderDate(LocalDateTime.now());
        order.setStatus(OrderStatus.PENDING);
        order.setType(OrderType.PURCHASE);
        orderRepository.save(order);

        order.setStatus(OrderStatus.COMPLETED);
        orderRepository.save(order);

        Order foundOrder = orderRepository.findById(order.getId());
        assertNotNull(foundOrder);
        assertEquals(OrderStatus.COMPLETED, foundOrder.getStatus());
    }

    @Test
    @DisplayName("Delete an order and verify it is removed from the database")
    public void testDelete() {
        Order order = new Order();
        order.setUserId(1);
        order.setCarId(2);
        order.setOrderDate(LocalDateTime.now());
        order.setStatus(OrderStatus.PENDING);
        order.setType(OrderType.PURCHASE);
        orderRepository.save(order);

        orderRepository.delete(order.getId());

        Order foundOrder = orderRepository.findById(order.getId());
        assertNull(foundOrder);
    }

    @Test
    @DisplayName("Find an order by ID and verify its details")
    public void testFindById() {
        Order order = new Order();
        order.setUserId(1);
        order.setCarId(2);
        order.setOrderDate(LocalDateTime.now());
        order.setStatus(OrderStatus.PENDING);
        order.setType(OrderType.PURCHASE);
        orderRepository.save(order);

        Order foundOrder = orderRepository.findById(order.getId());
        assertNotNull(foundOrder);
        assertEquals(OrderStatus.PENDING, foundOrder.getStatus());
        assertEquals(OrderType.PURCHASE, foundOrder.getType());
    }

    @Test
    @DisplayName("Retrieve all orders from the database")
    public void testFindAll() {
        Order order1 = new Order();
        order1.setUserId(1);
        order1.setCarId(2);
        order1.setOrderDate(LocalDateTime.now());
        order1.setStatus(OrderStatus.PENDING);
        order1.setType(OrderType.PURCHASE);

        Order order2 = new Order();
        order2.setUserId(2);
        order2.setCarId(3);
        order2.setOrderDate(LocalDateTime.now());
        order2.setStatus(OrderStatus.CANCELED);
        order2.setType(OrderType.SERVICE);

        orderRepository.save(order1);
        orderRepository.save(order2);

        List<Order> orders = orderRepository.findAll();
        assertEquals(2, orders.size());
    }

    @Test
    @DisplayName("Find orders by status and verify the result")
    public void testFindByStatus() {
        Order order = new Order();
        order.setUserId(1);
        order.setCarId(2);
        order.setOrderDate(LocalDateTime.now());
        order.setStatus(OrderStatus.PENDING);
        order.setType(OrderType.PURCHASE);
        orderRepository.save(order);

        List<Order> orders = orderRepository.findByStatus(OrderStatus.PENDING);
        assertEquals(1, orders.size());
        assertEquals(OrderStatus.PENDING, orders.get(0).getStatus());
    }

    @Test
    @DisplayName("Find orders by client ID and verify the result")
    public void testFindByClientId() {
        Order order = new Order();
        order.setUserId(1);
        order.setCarId(2);
        order.setOrderDate(LocalDateTime.now());
        order.setStatus(OrderStatus.PENDING);
        order.setType(OrderType.PURCHASE);
        orderRepository.save(order);

        List<Order> orders = orderRepository.findByClientId(1);
        assertEquals(1, orders.size());
        assertEquals(1, orders.get(0).getUserId());
    }

    @Test
    @DisplayName("Find orders by car ID and verify the result")
    public void testFindByCarId() {
        Order order = new Order();
        order.setUserId(1);
        order.setCarId(2);
        order.setOrderDate(LocalDateTime.now());
        order.setStatus(OrderStatus.PENDING);
        order.setType(OrderType.PURCHASE);
        orderRepository.save(order);

        List<Order> orders = orderRepository.findByCarId(2);
        assertEquals(1, orders.size());
        assertEquals(2, orders.get(0).getCarId());
    }

    @Test
    @DisplayName("Find orders by type and verify the result")
    public void testFindByType() {
        Order order = new Order();
        order.setUserId(1);
        order.setCarId(2);
        order.setOrderDate(LocalDateTime.now());
        order.setStatus(OrderStatus.PENDING);
        order.setType(OrderType.PURCHASE);
        orderRepository.save(order);

        List<Order> orders = orderRepository.findByType(OrderType.PURCHASE);
        assertEquals(1, orders.size());
        assertEquals(OrderType.PURCHASE, orders.get(0).getType());
    }
}
