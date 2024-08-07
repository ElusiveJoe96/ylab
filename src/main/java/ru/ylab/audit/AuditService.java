package ru.ylab.audit;


import ru.ylab.domain.model.User;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


public class AuditService {
    private final List<AuditLog> auditLogs = new ArrayList<>();
    public static User loggedInUser;
    private int idCounter = 1;

    public void logAction(int userId, String action, String details) {
        AuditLog log = new AuditLog(idCounter++, userId, action, LocalDateTime.now(), details);
        auditLogs.add(log);
    }

    public void viewAuditLogs() {
        if (auditLogs.isEmpty()) {
            System.out.println("No audit logs available.");
        } else {
            auditLogs.forEach(log -> System.out.println("Log ID: " + log.getId()
                    + ", User ID: " + log.getUserId() + ", Action: " + log.getAction()
                    + ", Date: " + log.getTimestamp() + ", Details: " + log.getDetails()));
        }
    }


    public List<AuditLog> filterByUserId(int userId) {
        List<AuditLog> result = new ArrayList<>();
        for (AuditLog log : auditLogs) {
            if (log.getUserId() == userId) {
                result.add(log);
            }
        }
        return result;
    }

    public List<AuditLog> filterByAction(String action) {
        List<AuditLog> result = new ArrayList<>();
        for (AuditLog log : auditLogs) {
            if (log.getAction().equals(action)) {
                result.add(log);
            }
        }
        return result;
    }

    public List<AuditLog> filterByTimestamp(LocalDateTime from, LocalDateTime to) {
        List<AuditLog> result = new ArrayList<>();
        for (AuditLog log : auditLogs) {
            if ((log.getTimestamp().isEqual(from) || log.getTimestamp().isAfter(from)) &&
                    (log.getTimestamp().isEqual(to) || log.getTimestamp().isBefore(to))) {
                result.add(log);
            }
        }
        return result;
    }

    public void exportAuditLogsToFile(String filename) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            for (AuditLog log : auditLogs) {
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
