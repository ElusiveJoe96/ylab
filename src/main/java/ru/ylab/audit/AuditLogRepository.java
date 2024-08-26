package ru.ylab.audit;

import ru.ylab.config.DatabaseConfig;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class AuditLogRepository {
    private final DatabaseConfig databaseConfig;

    private static final String INSERT_LOG_QUERY =
            "INSERT INTO car_shop_schema.logs (user_id, action, timestamp, details) VALUES (?, ?, ?, ?)";

    private static final String SELECT_ALL_LOGS_QUERY =
            "SELECT id, user_id, action, timestamp, details FROM car_shop_schema.logs";

    private static final String SELECT_LOGS_BY_USER_ID_QUERY =
            "SELECT id, user_id, action, timestamp, details FROM car_shop_schema.logs WHERE user_id = ?";

    private static final String SELECT_LOGS_BY_ACTION_QUERY =
            "SELECT id, user_id, action, timestamp, details FROM car_shop_schema.logs WHERE action = ?";

    private static final String SELECT_LOGS_BY_TIMESTAMP_RANGE_QUERY =
            "SELECT id, user_id, action, timestamp, details FROM car_shop_schema.logs WHERE timestamp BETWEEN ? AND ?";

    public AuditLogRepository(DatabaseConfig databaseConfig) {
        this.databaseConfig = databaseConfig;
    }

    public void save(AuditLog auditLog) {
        try (Connection connection = databaseConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(INSERT_LOG_QUERY, Statement.RETURN_GENERATED_KEYS)) {

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
        List<AuditLog> auditLogs = new ArrayList<>();

        try (Connection connection = databaseConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(SELECT_ALL_LOGS_QUERY);
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
        List<AuditLog> auditLogs = new ArrayList<>();

        try (Connection connection = databaseConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(SELECT_LOGS_BY_USER_ID_QUERY)) {

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
        List<AuditLog> auditLogs = new ArrayList<>();

        try (Connection connection = databaseConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(SELECT_LOGS_BY_ACTION_QUERY)) {

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
        List<AuditLog> auditLogs = new ArrayList<>();

        try (Connection connection = databaseConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(SELECT_LOGS_BY_TIMESTAMP_RANGE_QUERY)) {

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
