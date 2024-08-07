package ru.ylab.audit;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class AuditLog {
    private int id;
    private int userId;
    private String action;
    private LocalDateTime timestamp;
    private String details;

    public AuditLog(int id, int userId, String action, LocalDateTime timestamp, String details) {
        this.id = id;
        this.userId = userId;
        this.action = action;
        this.timestamp = timestamp;
        this.details = details;
    }
}
