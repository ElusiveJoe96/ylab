package ru.ylab.repository;

import ru.ylab.config.DatabaseConfig;
import ru.ylab.domain.model.User;
import ru.ylab.domain.enums.Role;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserRepository {
    private final DatabaseConfig databaseConfig;
    private static final String INSERT_USER_QUERY = "INSERT INTO car_shop_schema.users " +
            "(name, email, password, role, contact_info) " +
            "VALUES (?, ?, ?, ?, ?)";
    private static final String UPDATE_USER_QUERY = "UPDATE car_shop_schema.users " +
            "SET name = ?, email = ?, password = ?, role = ?, contact_info = ? " +
            "WHERE id = ?";
    private static final String DELETE_USER_QUERY = "DELETE FROM car_shop_schema.users WHERE id = ?";
    private static final String FIND_BY_ID_QUERY = "SELECT id, name, email, password, role, contact_info " +
            "FROM car_shop_schema.users WHERE id = ?";
    private static final String FIND_BY_EMAIL_QUERY = "SELECT id, name, email, password, role, contact_info " +
            "FROM car_shop_schema.users WHERE email = ?";
    private static final String FIND_ALL_QUERY = "SELECT id, name, email, password, role, contact_info " +
            "FROM car_shop_schema.users";
    private static final String FIND_BY_ROLE_QUERY = "SELECT id, name, email, password, role, contact_info " +
            "FROM car_shop_schema.users WHERE role = ?";

    public UserRepository(DatabaseConfig databaseConfig) {
        this.databaseConfig = databaseConfig;
    }

    public void save(User user) {
        if (user.getId() > 0) {
            update(user);
        } else {
            insert(user);
        }
    }

    private void insert(User user) {
        try (Connection connection = databaseConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(INSERT_USER_QUERY,
                     Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, user.getName());
            statement.setString(2, user.getEmail());
            statement.setString(3, user.getPassword());
            statement.setString(4, user.getRole().name());
            statement.setString(5, user.getContactInfo());

            statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void update(User user) {
        try (Connection connection = databaseConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(UPDATE_USER_QUERY)) {

            statement.setString(1, user.getName());
            statement.setString(2, user.getEmail());
            statement.setString(3, user.getPassword());
            statement.setString(4, user.getRole().name());
            statement.setString(5, user.getContactInfo());
            statement.setInt(6, user.getId());

            statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(int userId) {
        try (Connection connection = databaseConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(DELETE_USER_QUERY)) {

            statement.setInt(1, userId);
            statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Optional<User> findById(int userId) {
        try (Connection connection = databaseConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(FIND_BY_ID_QUERY)) {

            statement.setInt(1, userId);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    User user = mapRowToUser(resultSet);
                    return Optional.of(user);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error finding user by ID: " + e.getMessage());
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public Optional<User> findByEmail(String email) {
        try (Connection connection = databaseConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(FIND_BY_EMAIL_QUERY)) {

            statement.setString(1, email);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    User user = mapRowToUser(resultSet);
                    return Optional.of(user);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public List<User> findAll() {
        List<User> users = new ArrayList<>();

        try (Connection connection = databaseConfig.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(FIND_ALL_QUERY)) {

            while (resultSet.next()) {
                User user = mapRowToUser(resultSet);
                users.add(user);
            }
        } catch (SQLException e) {
            System.err.println("Error finding all users: " + e.getMessage());
            e.printStackTrace();
        }
        return users;
    }

    public List<User> findByRole(Role role) {
        List<User> users = new ArrayList<>();

        try (Connection connection = databaseConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(FIND_BY_ROLE_QUERY)) {

            statement.setString(1, role.name());

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    User user = mapRowToUser(resultSet);
                    users.add(user);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error finding users by role: " + e.getMessage());
            e.printStackTrace();
        }
        return users;
    }

    private User mapRowToUser(ResultSet resultSet) throws SQLException {
        User user = new User();
        user.setId(resultSet.getInt("id"));
        user.setName(resultSet.getString("name"));
        user.setEmail(resultSet.getString("email"));
        user.setPassword(resultSet.getString("password"));
        user.setRole(Role.valueOf(resultSet.getString("role")));
        user.setContactInfo(resultSet.getString("contact_info"));
        return user;
    }
}
