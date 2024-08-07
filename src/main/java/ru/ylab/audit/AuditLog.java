package ru.ylab.audit;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class AuditLog {
    private int id;
    private int userId;
    private String action;
    private LocalDateTime timestamp;
    private String details;
}
