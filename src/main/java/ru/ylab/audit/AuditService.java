package ru.ylab.audit;

import ru.ylab.domain.model.User;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

public class AuditService {
    public static User loggedInUser;
    private final AuditLogRepository auditLogRepository;

    public AuditService(AuditLogRepository auditLogRepository) {
        this.auditLogRepository = auditLogRepository;
    }

    public void logAction(int userId, String action, String details) {
        AuditLog log = new AuditLog(0, userId, action, LocalDateTime.now(), details);
        auditLogRepository.save(log);
    }

    public void viewAuditLogs() {
        List<AuditLog> auditLogs = auditLogRepository.findAll();
        if (auditLogs.isEmpty()) {
            System.out.println("No audit logs available.");
        } else {
            auditLogs.forEach(log -> System.out.println("Log ID: " + log.getId()
                    + ", User ID: " + log.getUserId() + ", Action: " + log.getAction()
                    + ", Date: " + log.getTimestamp() + ", Details: " + log.getDetails()));
        }
    }

    public List<AuditLog> filterByUserId(int userId) {
        return auditLogRepository.findByUserId(userId);
    }

    public List<AuditLog> filterByAction(String action) {
        return auditLogRepository.findByAction(action);
    }

    public List<AuditLog> filterByTimestamp(LocalDateTime from, LocalDateTime to) {
        return auditLogRepository.findByTimestampRange(from, to);
    }

    public void exportAuditLogsToFile(String filename) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            List<AuditLog> logs = auditLogRepository.findAll();
            for (AuditLog log : logs) {
                writer.write("Log ID: " + log.getId()
                        + ", User ID: " + log.getUserId() + ", Action: " + log.getAction()
                        + ", Date: " + log.getTimestamp() + ", Details: " + log.getDetails());
                writer.newLine();
            }
            System.out.println("Audit logs have been exported to " + filename);
        } catch (IOException e) {
            System.out.println("An error occurred while exporting audit logs: " + e.getMessage());
        }
    }
}
