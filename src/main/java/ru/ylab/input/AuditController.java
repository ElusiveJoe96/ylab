package ru.ylab.input;

import ru.ylab.audit.AuditService;

public class AuditController {
    private final AuditService auditService;

    public AuditController(AuditService auditService) {
        this.auditService = auditService;
    }

    public void viewLogs() {
        auditService.viewAuditLogs();
    }
}
