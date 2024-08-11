package ru.ylab.audit;

import ru.ylab.config.DatabaseConfig;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class AuditLogRepository {
    private final DatabaseConfig databaseConfig;

    public AuditLogRepository(DatabaseConfig databaseConfig) {
        this.databaseConfig = databaseConfig;
    }

    public void save(AuditLog auditLog) {
        String query = "INSERT INTO car_shop_log_schema.logs (user_id, action, timestamp, details) VALUES (?, ?, ?, ?)";

        try (Connection connection = databaseConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            statement.setInt(1, auditLog.getUserId());
            statement.setString(2, auditLog.getAction());
            statement.setTimestamp(3, Timestamp.valueOf(auditLog.getTimestamp()));
            statement.setString(4, auditLog.getDetails());

            int rowsAffected = statement.executeUpdate();

            if (rowsAffected > 0) {
                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        auditLog.setId(generatedKeys.getInt(1));
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error saving audit log: " + e.getMessage(), e);
        }
    }

    public List<AuditLog> findAll() {
        String query = "SELECT id, user_id, action, timestamp, details FROM car_shop_log_schema.logs";
        List<AuditLog> auditLogs = new ArrayList<>();

        try (Connection connection = databaseConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                auditLogs.add(mapRowToAuditLog(resultSet));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving audit logs: " + e.getMessage(), e);
        }
        return auditLogs;
    }

    public List<AuditLog> findByUserId(int userId) {
        String query = "SELECT id, user_id, action, timestamp, details FROM car_shop_log_schema.logs WHERE user_id = ?";
        List<AuditLog> auditLogs = new ArrayList<>();

        try (Connection connection = databaseConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, userId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    auditLogs.add(mapRowToAuditLog(resultSet));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving audit logs for user ID " + userId + ": " + e.getMessage(), e);
        }
        return auditLogs;
    }

    public List<AuditLog> findByAction(String action) {
        String query = "SELECT id, user_id, action, timestamp, details FROM car_shop_log_schema.logs WHERE action = ?";
        List<AuditLog> auditLogs = new ArrayList<>();

        try (Connection connection = databaseConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, action);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    auditLogs.add(mapRowToAuditLog(resultSet));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving audit logs for action '" + action + "': " + e.getMessage(), e);
        }
        return auditLogs;
    }

    public List<AuditLog> findByTimestampRange(LocalDateTime from, LocalDateTime to) {
        String query = "SELECT id, user_id, action, timestamp, details " +
                "FROM car_shop_log_schema.logs WHERE timestamp BETWEEN ? AND ?";
        List<AuditLog> auditLogs = new ArrayList<>();

        try (Connection connection = databaseConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setTimestamp(1, Timestamp.valueOf(from));
            statement.setTimestamp(2, Timestamp.valueOf(to));
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    auditLogs.add(mapRowToAuditLog(resultSet));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving audit logs between " + from + " and " + to + ": " + e.getMessage(), e);
        }
        return auditLogs;
    }

    private AuditLog mapRowToAuditLog(ResultSet resultSet) throws SQLException {
        return new AuditLog(
                resultSet.getInt("id"),
                resultSet.getInt("user_id"),
                resultSet.getString("action"),
                resultSet.getTimestamp("timestamp").toLocalDateTime(),
                resultSet.getString("details")
        );
    }
}
