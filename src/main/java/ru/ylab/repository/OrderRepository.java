package ru.ylab.repository;

import ru.ylab.domain.enums.OrderType;
import ru.ylab.domain.model.Order;
import ru.ylab.domain.enums.OrderStatus;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class OrderRepository {

    private final DataSource dataSource;

    private static final String INSERT_ORDER_QUERY = "INSERT INTO car_shop_schema.orders " +
            "(user_id, car_id, order_date, status, type) " +
            "VALUES (?, ?, ?, ?, ?)";
    private static final String UPDATE_ORDER_QUERY = "UPDATE car_shop_schema.orders " +
            "SET user_id = ?, car_id = ?, order_date = ?, status = ?, type = ? WHERE id = ?";
    private static final String DELETE_ORDER_QUERY = "DELETE FROM car_shop_schema.orders WHERE id = ?";
    private static final String FIND_BY_ID_QUERY = "SELECT id, user_id, car_id, order_date, status, type " +
            "FROM car_shop_schema.orders WHERE id = ?";
    private static final String FIND_ALL_QUERY = "SELECT id, user_id, car_id, order_date, status, type " +
            "FROM car_shop_schema.orders";
    private static final String FIND_BY_FIELD_QUERY = "SELECT id, user_id, car_id, order_date, status, type " +
            "FROM car_shop_schema.orders WHERE %s = ?";

    public OrderRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void save(Order order) {
        if (order.getId() > 0) {
            update(order);
        } else {
            insert(order);
        }
    }

    private void insert(Order order) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(INSERT_ORDER_QUERY, Statement.RETURN_GENERATED_KEYS)) {

            statement.setInt(1, order.getUserId());
            statement.setInt(2, order.getCarId());
            statement.setTimestamp(3, Timestamp.valueOf(order.getOrderDate()));
            statement.setString(4, order.getStatus().name());
            statement.setString(5, order.getType().name());

            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error inserting order", e);
        }
    }

    private void update(Order order) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(UPDATE_ORDER_QUERY)) {

            statement.setInt(1, order.getUserId());
            statement.setInt(2, order.getCarId());
            statement.setTimestamp(3, Timestamp.valueOf(order.getOrderDate()));
            statement.setString(4, order.getStatus().name());
            statement.setString(5, order.getType().name());
            statement.setInt(6, order.getId());

            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error updating order", e);
        }
    }

    public void delete(int orderId) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(DELETE_ORDER_QUERY)) {

            statement.setInt(1, orderId);
            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error deleting order", e);
        }
    }

    public Optional<Order> findById(int orderId) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(FIND_BY_ID_QUERY)) {

            statement.setInt(1, orderId);
            ResultSet resultSet = statement.executeQuery();
                if (resultSet.next()) {
                    return Optional.of(mapRowToOrder(resultSet));
                }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding order by ID", e);
        }
        return Optional.empty();
    }

    public List<Order> findAll() {
        List<Order> orders = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(FIND_ALL_QUERY)) {

            while (resultSet.next()) {
                orders.add(mapRowToOrder(resultSet));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding all orders", e);
        }
        return orders;
    }

    private List<Order> findByField(String fieldName, Object value) {
        List<Order> orders = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(String.format(FIND_BY_FIELD_QUERY, fieldName))) {
            //TODO
            if (value instanceof String) {
                statement.setString(1, (String) value);
            } else if (value instanceof Integer) {
                statement.setInt(1, (Integer) value);
            } else if (value instanceof LocalDateTime) {
                statement.setTimestamp(1, Timestamp.valueOf((LocalDateTime) value));
            } else if (value instanceof OrderStatus) {
                statement.setString(1, ((OrderStatus) value).name());
            } else if (value instanceof OrderType) {
                statement.setString(1, ((OrderType) value).name());
            } else {
                throw new IllegalArgumentException("Unsupported field type: " + value.getClass().getName());
            }

            ResultSet resultSet = statement.executeQuery();
                while (resultSet.next()) {
                    orders.add(mapRowToOrder(resultSet));
                }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding orders by field", e);
        }
        return orders;
    }

    public List<Order> findByStatus(OrderStatus status) {
        return findByField("status", status);
    }

    public List<Order> findByDate(LocalDateTime orderDate) {
        return findByField("order_date", orderDate);
    }

    public List<Order> findByClientId(int clientId) {
        return findByField("user_id", clientId);
    }

    public List<Order> findByCarId(int carId) {
        return findByField("car_id", carId);
    }

    public List<Order> findByType(OrderType type) {
        return findByField("type", type);
    }

    private Order mapRowToOrder(ResultSet resultSet) throws SQLException {
        Order order = new Order();
        order.setId(resultSet.getInt("id"));
        order.setUserId(resultSet.getInt("user_id"));
        order.setCarId(resultSet.getInt("car_id"));
        order.setOrderDate(resultSet.getTimestamp("order_date").toLocalDateTime());
        order.setStatus(OrderStatus.valueOf(resultSet.getString("status")));
        order.setType(OrderType.valueOf(resultSet.getString("type")));
        return order;
    }
}
