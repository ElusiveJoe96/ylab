package ru.ylab.audit;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuditLog {
    private int id;
    private int userId;
    private String action;
    private LocalDateTime timestamp;
    private String details;
}
