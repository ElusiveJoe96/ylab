package ru.ylab.repository;

import org.junit.jupiter.api.*;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.ylab.config.DatabaseConfig;
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

    private DatabaseConfig databaseConfig;
    private OrderRepository orderRepository;

    @BeforeEach
    public void setUp() throws Exception {
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

        try (Connection connection = databaseConfig.getConnection();
             Statement statement = connection.createStatement()) {
            statement.execute("CREATE SCHEMA IF NOT EXISTS car_shop_schema");
            statement.execute("CREATE TABLE IF NOT EXISTS car_shop_schema.orders (" +
                    "id SERIAL PRIMARY KEY, " +
                    "user_id INT NOT NULL, " +
                    "car_id INT NOT NULL, " +
                    "order_date TIMESTAMP NOT NULL, " +
                    "status VARCHAR(50) NOT NULL, " +
                    "type VARCHAR(50) NOT NULL)");
        }
    }

    @AfterEach
    public void tearDown() throws Exception {
        try (Connection connection = databaseConfig.getConnection();
             Statement statement = connection.createStatement()) {
            statement.execute("DROP TABLE IF EXISTS car_shop_schema.orders");
        }
    }

    @Test
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
