package ru.ylab.repository;

import org.junit.jupiter.api.*;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.ylab.config.DatabaseConfig;
import ru.ylab.domain.enums.Role;
import ru.ylab.domain.model.User;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
public class UserRepositoryTest {

    @Container
    public static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:latest")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");

    private DatabaseConfig databaseConfig;
    private UserRepository userRepository;

    @BeforeEach
    public void setUp() throws Exception {
        databaseConfig = new DatabaseConfig() {
            @Override
            public Connection getConnection() {
                try {
                    return DriverManager.getConnection(
                            postgreSQLContainer.getJdbcUrl(),
                            postgreSQLContainer.getUsername(),
                            postgreSQLContainer.getPassword());
                } catch (Exception e) {
                    throw new RuntimeException("Failed to connect to the database", e);
                }
            }
        };

        userRepository = new UserRepository(databaseConfig);

        try (Connection connection = databaseConfig.getConnection();
             Statement statement = connection.createStatement()) {
            statement.execute("CREATE SCHEMA IF NOT EXISTS car_shop_schema");
            statement.execute("CREATE TABLE IF NOT EXISTS car_shop_schema.users (" +
                    "id SERIAL PRIMARY KEY, " +
                    "name VARCHAR(255) NOT NULL, " +
                    "email VARCHAR(255) NOT NULL UNIQUE, " +
                    "password VARCHAR(255) NOT NULL, " +
                    "role VARCHAR(50) NOT NULL, " +
                    "contact_info TEXT)");
        }
    }

    @AfterEach
    public void tearDown() throws Exception {
        try (Connection connection = databaseConfig.getConnection();
             Statement statement = connection.createStatement()) {
            statement.execute("DROP TABLE IF EXISTS car_shop_schema.users");
        }
    }

    @Test
    @DisplayName("Save a new user and verify it is saved correctly")
    public void testSave_InsertNewUser() {
        User user = new User();
        user.setName("John Doe");
        user.setEmail("john.doe@example.com");
        user.setPassword("password123");
        user.setRole(Role.CLIENT);
        user.setContactInfo("Contact info");

        userRepository.save(user);

        assertTrue(user.getId() > 0);

        Optional<User> foundUser = userRepository.findById(user.getId());
        assertTrue(foundUser.isPresent());
        assertEquals("John Doe", foundUser.get().getName());
        assertEquals("john.doe@example.com", foundUser.get().getEmail());
    }

    @Test
    @DisplayName("Update an existing user and verify the update is correct")
    public void testSave_UpdateExistingUser() {
        User user = new User();
        user.setName("John Doe");
        user.setEmail("john.doe@example.com");
        user.setPassword("password123");
        user.setRole(Role.CLIENT);
        user.setContactInfo("Contact info");
        userRepository.save(user);

        user.setName("John Smith");
        userRepository.save(user);

        Optional<User> foundUser = userRepository.findById(user.getId());
        assertTrue(foundUser.isPresent());
        assertEquals("John Smith", foundUser.get().getName());
    }

    @Test
    @DisplayName("Delete a user and verify it is removed from the database")
    public void testDelete() {
        User user = new User();
        user.setName("John Doe");
        user.setEmail("john.doe@example.com");
        user.setPassword("password123");
        user.setRole(Role.CLIENT);
        user.setContactInfo("Contact info");
        userRepository.save(user);

        userRepository.delete(user.getId());

        Optional<User> foundUser = userRepository.findById(user.getId());
        assertTrue(foundUser.isEmpty());
    }

    @Test
    @DisplayName("Find a user by ID and verify their details")
    public void testFindById() {
        User user = new User();
        user.setName("John Doe");
        user.setEmail("john.doe@example.com");
        user.setPassword("password123");
        user.setRole(Role.CLIENT);
        user.setContactInfo("Contact info");
        userRepository.save(user);

        Optional<User> foundUser = userRepository.findById(user.getId());
        assertTrue(foundUser.isPresent());
        assertEquals("John Doe", foundUser.get().getName());
    }

    @Test
    @DisplayName("Find a user by email and verify their details")
    public void testFindByEmail() {
        User user = new User();
        user.setName("John Doe");
        user.setEmail("john.doe@example.com");
        user.setPassword("password123");
        user.setRole(Role.CLIENT);
        user.setContactInfo("Contact info");
        userRepository.save(user);

        Optional<User> foundUser = userRepository.findByEmail("john.doe@example.com");
        assertTrue(foundUser.isPresent());
        assertEquals("John Doe", foundUser.get().getName());
    }

    @Test
    @DisplayName("Retrieve all users from the database")
    public void testFindAll() {
        User user1 = new User();
        user1.setName("John Doe");
        user1.setEmail("john.doe@example.com");
        user1.setPassword("password123");
        user1.setRole(Role.CLIENT);
        user1.setContactInfo("Contact info");

        User user2 = new User();
        user2.setName("Jane Doe");
        user2.setEmail("jane.doe@example.com");
        user2.setPassword("password456");
        user2.setRole(Role.MANAGER);
        user2.setContactInfo("Contact info");

        userRepository.save(user1);
        userRepository.save(user2);

        List<User> users = userRepository.findAll();
        assertEquals(2, users.size());
    }

    @Test
    @DisplayName("Find users by role and verify the result")
    public void testFindByRole() {
        User user = new User();
        user.setName("John Doe");
        user.setEmail("john.doe@example.com");
        user.setPassword("password123");
        user.setRole(Role.CLIENT);
        user.setContactInfo("Contact info");
        userRepository.save(user);

        List<User> users = userRepository.findByRole(Role.CLIENT);
        assertEquals(1, users.size());
        assertEquals("John Doe", users.get(0).getName());
    }
}
