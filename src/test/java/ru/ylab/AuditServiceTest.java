package ru.ylab;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.ylab.audit.AuditLog;
import ru.ylab.audit.AuditService;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AuditServiceTest {
    private AuditService auditService;

    @BeforeEach
    public void setUp() {
        auditService = new AuditService();
    }

    @Test
    public void testLogAction() {
        int userId = 1;
        String action = "TEST_ACTION";
        String details = "Test action details";

        auditService.logAction(userId, action, details);

        List<AuditLog> logs = auditService.filterByUserId(userId);
        assertEquals(1, logs.size());
        AuditLog log = logs.get(0);
        assertEquals(userId, log.getUserId());
        assertEquals(action, log.getAction());
        assertEquals(details, log.getDetails());
    }

    @Test
    public void testViewAuditLogsEmpty() {
        auditService.viewAuditLogs();
    }

    @Test
    public void testViewAuditLogsNonEmpty() {
        int userId = 1;
        String action = "TEST_ACTION";
        String details = "Test action details";

        auditService.logAction(userId, action, details);

        auditService.viewAuditLogs();
    }

    @Test
    public void testFilterByUserId() {
        int userId1 = 1;
        int userId2 = 2;
        auditService.logAction(userId1, "ACTION1", "Details1");
        auditService.logAction(userId2, "ACTION2", "Details2");

        List<AuditLog> logs = auditService.filterByUserId(userId1);

        assertEquals(1, logs.size());
        assertEquals(userId1, logs.get(0).getUserId());
    }

    @Test
    public void testFilterByAction() {
        auditService.logAction(1, "ACTION1", "Details1");
        auditService.logAction(2, "ACTION2", "Details2");

        List<AuditLog> logs = auditService.filterByAction("ACTION1");

        assertEquals(1, logs.size());
        assertEquals("ACTION1", logs.get(0).getAction());
    }

    @Test
    public void testFilterByTimestamp() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime past = now.minusDays(1);
        LocalDateTime future = now.plusDays(1);

        auditService.logAction(1, "ACTION1", "Details1");

        List<AuditLog> logs = auditService.filterByTimestamp(past, future);

        assertEquals(1, logs.size());
        assertTrue(logs.get(0).getTimestamp().isAfter(past));
        assertTrue(logs.get(0).getTimestamp().isBefore(future));
    }


}
