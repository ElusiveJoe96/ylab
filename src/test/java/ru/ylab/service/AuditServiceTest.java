package ru.ylab.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.ylab.audit.AuditLog;
import ru.ylab.audit.AuditLogRepository;
import ru.ylab.audit.AuditService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AuditServiceTest {
    @Mock
    private AuditLogRepository auditLogRepository;

    @InjectMocks
    private AuditService auditService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testLogAction() {
        int userId = 1;
        String action = "TEST_ACTION";
        String details = "Test details";

        auditService.logAction(userId, action, details);

        ArgumentCaptor<AuditLog> logCaptor = ArgumentCaptor.forClass(AuditLog.class);
        verify(auditLogRepository).save(logCaptor.capture());
        AuditLog capturedLog = logCaptor.getValue();

        assertEquals(userId, capturedLog.getUserId());
        assertEquals(action, capturedLog.getAction());
        assertEquals(details, capturedLog.getDetails());
        assertNotNull(capturedLog.getTimestamp());
    }

    @Test
    public void testViewAuditLogs() {
        List<AuditLog> logs = Arrays.asList(
                new AuditLog(1, 1, "LOGIN", LocalDateTime.now(), "User logged in"),
                new AuditLog(2, 2, "LOGOUT", LocalDateTime.now(), "User logged out")
        );
        when(auditLogRepository.findAll()).thenReturn(logs);

        auditService.viewAuditLogs();

        verify(auditLogRepository).findAll();
    }

    @Test
    public void testFilterByUserId() {
        List<AuditLog> logs = Arrays.asList(
                new AuditLog(1, 1, "LOGIN", LocalDateTime.now(), "User logged in"),
                new AuditLog(2, 2, "LOGOUT", LocalDateTime.now(), "User logged out")
        );
        when(auditLogRepository.findByUserId(1)).thenReturn(logs);

        List<AuditLog> result = auditService.filterByUserId(1);

        assertEquals(2, result.size());
        assertEquals(1, result.get(0).getUserId());
    }

    @Test
    public void testFilterByAction() {
        List<AuditLog> logs = Arrays.asList(
                new AuditLog(1, 1, "LOGIN", LocalDateTime.now(), "User logged in"),
                new AuditLog(2, 1, "LOGIN", LocalDateTime.now(), "User logged in again")
        );
        when(auditLogRepository.findByAction("LOGIN")).thenReturn(logs);

        List<AuditLog> result = auditService.filterByAction("LOGIN");

        assertEquals(2, result.size());
        assertEquals("LOGIN", result.get(0).getAction());
    }

    @Test
    public void testFilterByTimestamp() {
        LocalDateTime from = LocalDateTime.now().minusDays(1);
        LocalDateTime to = LocalDateTime.now();
        List<AuditLog> logs = Arrays.asList(
                new AuditLog(1, 1, "LOGIN", LocalDateTime.now().minusHours(1), "User logged in")
        );
        when(auditLogRepository.findByTimestampRange(from, to)).thenReturn(logs);

        List<AuditLog> result = auditService.filterByTimestamp(from, to);

        assertEquals(1, result.size());
        assertTrue(result.get(0).getTimestamp().isAfter(from));
        assertTrue(result.get(0).getTimestamp().isBefore(to));
    }

    @Test
    public void testExportAuditLogsToFile() throws IOException {
        List<AuditLog> logs = Arrays.asList(
                new AuditLog(1, 1, "LOGIN", LocalDateTime.now(), "User logged in")
        );
        when(auditLogRepository.findAll()).thenReturn(logs);

        String filename = "test_audit_logs.txt";

        auditService.exportAuditLogsToFile(filename);

        String content = new String(Files.readAllBytes(Paths.get(filename)));
        assertTrue(content.contains("Log ID: 1"));
        assertTrue(content.contains("User ID: 1"));
        assertTrue(content.contains("Action: LOGIN"));
        assertTrue(content.contains("Details: User logged in"));

        Files.deleteIfExists(Paths.get(filename));
    }
}
