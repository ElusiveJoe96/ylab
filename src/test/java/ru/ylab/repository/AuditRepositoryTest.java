//package ru.ylab.repository;
//
//import org.junit.jupiter.api.*;
//import org.testcontainers.containers.PostgreSQLContainer;
//import org.testcontainers.junit.jupiter.Container;
//import org.testcontainers.junit.jupiter.Testcontainers;
//import ru.ylab.audit.AuditLog;
//import ru.ylab.audit.AuditLogRepository;
//import ru.ylab.config.DatabaseConfig;
//
//import java.sql.Connection;
//import java.sql.DriverManager;
//import java.sql.Statement;
//import java.time.LocalDateTime;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@Testcontainers
//public class AuditRepositoryTest {
//
//    @Container
//    public static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:latest")
//            .withDatabaseName("testdb")
//            .withUsername("test")
//            .withPassword("test");
//
//    private DatabaseConfig databaseConfig;
//    private AuditLogRepository auditLogRepository;
//
//    @BeforeEach
//    public void setUp() throws Exception {
//        databaseConfig = new DatabaseConfig() {
//            @Override
//            public Connection getConnection() {
//                try {
//                    return DriverManager.getConnection(
//                            postgreSQLContainer.getJdbcUrl(),
//                            postgreSQLContainer.getUsername(),
//                            postgreSQLContainer.getPassword());
//                } catch (Exception e) {
//                    throw new RuntimeException("Failed to connect to the database", e);
//                }
//            }
//        };
//
//        auditLogRepository = new AuditLogRepository(databaseConfig);
//
//        try (Connection connection = databaseConfig.getConnection();
//             Statement statement = connection.createStatement()) {
//            statement.execute("CREATE SCHEMA IF NOT EXISTS car_shop_log_schema");
//            statement.execute("CREATE TABLE IF NOT EXISTS car_shop_log_schema.logs (" +
//                    "id SERIAL PRIMARY KEY, " +
//                    "user_id INT NOT NULL, " +
//                    "action VARCHAR(255) NOT NULL, " +
//                    "timestamp TIMESTAMP NOT NULL, " +
//                    "details TEXT)");
//        }
//    }
//
//    @AfterEach
//    public void tearDown() throws Exception {
//        try (Connection connection = databaseConfig.getConnection();
//             Statement statement = connection.createStatement()) {
//            statement.execute("DROP TABLE IF EXISTS car_shop_log_schema.logs");
//        }
//    }
//
//    @Test
//    @DisplayName("Verify saving log to the database")
//    public void testSave() {
//        AuditLog auditLog = new AuditLog();
//        auditLog.setUserId(1);
//        auditLog.setAction("LOGIN");
//        auditLog.setTimestamp(LocalDateTime.now());
//        auditLog.setDetails("User logged in");
//
//        auditLogRepository.save(auditLog);
//
//        assertTrue(auditLog.getId() > 0);
//
//        AuditLog foundLog = auditLogRepository.findAll().stream()
//                .filter(log -> log.getId() == auditLog.getId())
//                .findFirst()
//                .orElse(null);
//        assertNotNull(foundLog);
//        assertEquals(auditLog.getUserId(), foundLog.getUserId());
//        assertEquals(auditLog.getAction(), foundLog.getAction());
//        assertEquals(auditLog.getDetails(), foundLog.getDetails());
//    }
//
//    @Test
//    @DisplayName("Verify retrieving all logs from the database")
//    public void testFindAll() {
//        AuditLog log1 = new AuditLog();
//        log1.setUserId(1);
//        log1.setAction("LOGIN");
//        log1.setTimestamp(LocalDateTime.now());
//        log1.setDetails("User logged in");
//
//        AuditLog log2 = new AuditLog();
//        log2.setUserId(2);
//        log2.setAction("LOGOUT");
//        log2.setTimestamp(LocalDateTime.now().plusMinutes(5));
//        log2.setDetails("User logged out");
//
//        auditLogRepository.save(log1);
//        auditLogRepository.save(log2);
//
//        List<AuditLog> logs = auditLogRepository.findAll();
//        assertEquals(2, logs.size());
//    }
//
//    @Test
//    @DisplayName("Verify retrieving logs by userId")
//    public void testFindByUserId() {
//        AuditLog log = new AuditLog();
//        log.setUserId(1);
//        log.setAction("LOGIN");
//        log.setTimestamp(LocalDateTime.now());
//        log.setDetails("User logged in");
//        auditLogRepository.save(log);
//
//        List<AuditLog> logs = auditLogRepository.findByUserId(1);
//        assertEquals(1, logs.size());
//        assertEquals(1, logs.get(0).getUserId());
//    }
//
//    @Test
//    @DisplayName("Verify retrieving logs by action")
//    public void testFindByAction() {
//        AuditLog log = new AuditLog();
//        log.setUserId(1);
//        log.setAction("LOGIN");
//        log.setTimestamp(LocalDateTime.now());
//        log.setDetails("User logged in");
//        auditLogRepository.save(log);
//
//        List<AuditLog> logs = auditLogRepository.findByAction("LOGIN");
//        assertEquals(1, logs.size());
//        assertEquals("LOGIN", logs.get(0).getAction());
//    }
//
//    @Test
//    @DisplayName("Verify retrieving logs by time range")
//    public void testFindByTimestampRange() {
//        LocalDateTime now = LocalDateTime.now();
//        LocalDateTime later = now.plusHours(1);
//
//        AuditLog log1 = new AuditLog();
//        log1.setUserId(1);
//        log1.setAction("LOGIN");
//        log1.setTimestamp(now);
//        log1.setDetails("User logged in");
//        auditLogRepository.save(log1);
//
//        AuditLog log2 = new AuditLog();
//        log2.setUserId(2);
//        log2.setAction("LOGOUT");
//        log2.setTimestamp(later);
//        log2.setDetails("User logged out");
//        auditLogRepository.save(log2);
//
//        List<AuditLog> logs = auditLogRepository.findByTimestampRange(now.minusMinutes(1), later.plusMinutes(1));
//        assertEquals(2, logs.size());
//    }
//}
