package ru.ylab.repository;

import org.junit.jupiter.api.*;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.ylab.config.DatabaseConfig;
import ru.ylab.config.LiquibaseConfig;
import ru.ylab.domain.enums.CarStatus;
import ru.ylab.domain.model.Car;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CarRepositoryTest {

    @Container
    private static final PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:latest")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");

    private static DatabaseConfig databaseConfig;
    private static CarRepository carRepository;

    @BeforeAll
    public static void setUpDatabaseConfig() {
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

        carRepository = new CarRepository(databaseConfig);
        LiquibaseConfig liquibaseConfig = new LiquibaseConfig(databaseConfig);
        liquibaseConfig.runMigrations();
    }



    @Test
    @DisplayName("Save a car and find it by ID")
    public void testSaveAndFindById() {
        Car car = new Car();
        car.setBrand("Toyota");
        car.setModel("Camry");
        car.setYear(2020);
        car.setPrice(30000.00);
        car.setStatus(CarStatus.AVAILABLE);
        car.setDescription("A reliable car.");

        carRepository.save(car);
        assertTrue(car.getId() > 0);

        Car foundCar = carRepository.findById(car.getId());
        assertNotNull(foundCar);
        assertEquals("Toyota", foundCar.getBrand());
    }

    @Test
    @DisplayName("Update a car's details")
    public void testUpdate() {
        Car car = new Car();
        car.setBrand("Honda");
        car.setModel("Civic");
        car.setYear(2018);
        car.setPrice(20000.00);
        car.setStatus(CarStatus.AVAILABLE);
        car.setDescription("Compact and efficient.");

        carRepository.save(car);

        car.setPrice(19000.00);
        carRepository.save(car);

        Car updatedCar = carRepository.findById(car.getId());
        assertEquals(19000.00, updatedCar.getPrice());
    }

    @Test
    @DisplayName("Delete a car by ID")
    public void testDelete() {
        Car car = new Car();
        car.setBrand("Ford");
        car.setModel("Mustang");
        car.setYear(2021);
        car.setPrice(55000.00);
        car.setStatus(CarStatus.AVAILABLE);
        car.setDescription("Powerful sports car.");

        carRepository.save(car);
        int carId = car.getId();

        carRepository.delete(carId);
        Car deletedCar = carRepository.findById(carId);
        assertNull(deletedCar);
    }

    @Test
    @DisplayName("Retrieve all cars from the database")
    public void testFindAll() {
        Car car1 = new Car();
        car1.setBrand("Tesla");
        car1.setModel("Model S");
        car1.setYear(2021);
        car1.setPrice(80000.00);
        car1.setStatus(CarStatus.AVAILABLE);
        car1.setDescription("Electric luxury car.");

        Car car2 = new Car();
        car2.setBrand("BMW");
        car2.setModel("X5");
        car2.setYear(2019);
        car2.setPrice(60000.00);
        car2.setStatus(CarStatus.AVAILABLE);
        car2.setDescription("Luxury SUV.");

        carRepository.save(car1);
        carRepository.save(car2);

        List<Car> cars = carRepository.findAll();
        assertEquals(2, cars.size());
    }
}