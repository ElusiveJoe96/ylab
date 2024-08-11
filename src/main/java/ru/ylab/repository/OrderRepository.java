package ru.ylab.repository;

import ru.ylab.config.DatabaseConfig;
import ru.ylab.domain.enums.OrderType;
import ru.ylab.domain.model.Order;
import ru.ylab.domain.enums.OrderStatus;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class OrderRepository {
    private final DatabaseConfig databaseConfig;

    public OrderRepository(DatabaseConfig databaseConfig) {
        this.databaseConfig = databaseConfig;
    }

    public void save(Order order) {
        if (order.getId() > 0) {
            update(order);
        } else {
            insert(order);
        }
    }

    private void insert(Order order) {
        String query = "INSERT INTO car_shop_schema.orders (user_id, car_id, order_date, status, type) " +
                "VALUES (?, ?, ?, ?, ?)";

        try (Connection connection = databaseConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            statement.setInt(1, order.getUserId());
            statement.setInt(2, order.getCarId());
            statement.setTimestamp(3, Timestamp.valueOf(order.getOrderDate()));
            statement.setString(4, order.getStatus().name());
            statement.setString(5, order.getType().name());

            int rowsAffected = statement.executeUpdate();

            if (rowsAffected > 0) {
                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        order.setId(generatedKeys.getInt(1));
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void update(Order order) {
        String query = "UPDATE car_shop_schema.orders " +
                "SET user_id = ?, car_id = ?, order_date = ?, status = ?, type = ? WHERE id = ?";

        try (Connection connection = databaseConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, order.getUserId());
            statement.setInt(2, order.getCarId());
            statement.setTimestamp(3, Timestamp.valueOf(order.getOrderDate()));
            statement.setString(4, order.getStatus().name());
            statement.setString(5, order.getType().name());
            statement.setInt(6, order.getId());

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(int orderId) {
        String query = "DELETE FROM car_shop_schema.orders WHERE id = ?";

        try (Connection connection = databaseConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, orderId);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Order findById(int orderId) {
        String query = "SELECT id, user_id, car_id, order_date, status, type " +
                "FROM car_shop_schema.orders WHERE id = ?";

        try (Connection connection = databaseConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, orderId);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return mapRowToOrder(resultSet);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Order> findAll() {
        String query = "SELECT id, user_id, car_id, order_date, status, type " +
                "FROM car_shop_schema.orders";
        List<Order> orders = new ArrayList<>();

        try (Connection connection = databaseConfig.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                orders.add(mapRowToOrder(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orders;
    }

    private List<Order> findByField(String fieldName, Object value) {
        String query = "SELECT id, user_id, car_id, order_date, status, type " +
                "FROM car_shop_schema.orders WHERE " + fieldName + " = ?";
        List<Order> orders = new ArrayList<>();

        try (Connection connection = databaseConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

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

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    orders.add(mapRowToOrder(resultSet));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
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
