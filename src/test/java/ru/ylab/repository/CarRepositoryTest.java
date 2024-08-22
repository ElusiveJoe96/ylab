//package ru.ylab.repository;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.testcontainers.containers.PostgreSQLContainer;
//import org.testcontainers.junit.jupiter.Container;
//import org.testcontainers.junit.jupiter.Testcontainers;
//import ru.ylab.config.DatabaseConfig;
//import ru.ylab.domain.enums.CarStatus;
//import ru.ylab.domain.model.Car;
//
//import java.sql.Connection;
//import java.sql.Statement;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@Testcontainers
//@ExtendWith(MockitoExtension.class)
//public class CarRepositoryTest {
//    @Container
//    private static final PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:latest")
//            .withDatabaseName("testdb")
//            .withUsername("test")
//            .withPassword("test");
//
//    private CarRepository carRepository;
//
//    @BeforeEach
//    public void setUp() throws Exception {
//        DatabaseConfig databaseConfig = new DatabaseConfig(
//                postgreSQLContainer.getJdbcUrl(),
//                postgreSQLContainer.getUsername(),
//                postgreSQLContainer.getPassword()
//        );
//        carRepository = new CarRepository(databaseConfig);
//
//        try (Connection connection = databaseConfig.getConnection()) {
//            String createTableSQL = "CREATE SCHEMA IF NOT EXISTS car_shop_schema;" +
//                    "CREATE TABLE IF NOT EXISTS car_shop_schema.cars (" +
//                    "id SERIAL PRIMARY KEY, " +
//                    "brand VARCHAR(50), " +
//                    "model VARCHAR(50), " +
//                    "year INTEGER, " +
//                    "price DOUBLE PRECISION, " +
//                    "status VARCHAR(20), " +
//                    "description TEXT" +
//                    ")";
//            try (Statement statement = connection.createStatement()) {
//                statement.execute(createTableSQL);
//            }
//        }
//    }
//
//    @Test
//    @DisplayName("Save a car and find it by ID")
//    public void testSaveAndFindById() {
//        Car car = new Car();
//        car.setBrand("Toyota");
//        car.setModel("Camry");
//        car.setYear(2020);
//        car.setPrice(30000.00);
//        car.setStatus(CarStatus.AVAILABLE);
//        car.setDescription("A reliable car.");
//
//        carRepository.save(car);
//        assertTrue(car.getId() > 0);
//
//        Car foundCar = carRepository.findById(car.getId());
//        assertNotNull(foundCar);
//        assertEquals("Toyota", foundCar.getBrand());
//    }
//
//    @Test
//    @DisplayName("Update a car details")
//    public void testUpdate() {
//        Car car = new Car();
//        car.setBrand("Honda");
//        car.setModel("Civic");
//        car.setYear(2018);
//        car.setPrice(20000.00);
//        car.setStatus(CarStatus.AVAILABLE);
//        car.setDescription("Compact and efficient.");
//
//        carRepository.save(car);
//
//        car.setPrice(19000.00);
//        carRepository.save(car);
//
//        Car updatedCar = carRepository.findById(car.getId());
//        assertEquals(19000.00, updatedCar.getPrice());
//    }
//
//    @Test
//    @DisplayName("Delete a car by ID")
//    public void testDelete() {
//        Car car = new Car();
//        car.setBrand("Ford");
//        car.setModel("Mustang");
//        car.setYear(2021);
//        car.setPrice(55000.00);
//        car.setStatus(CarStatus.AVAILABLE);
//        car.setDescription("Powerful sports car.");
//
//        carRepository.save(car);
//        int carId = car.getId();
//
//        carRepository.delete(carId);
//        Car deletedCar = carRepository.findById(carId);
//        assertNull(deletedCar);
//    }
//
//    @Test
//    @DisplayName("Retrieve all cars from the database")
//    public void testFindAll() {
//        Car car1 = new Car();
//        car1.setBrand("Tesla");
//        car1.setModel("Model S");
//        car1.setYear(2021);
//        car1.setPrice(80000.00);
//        car1.setStatus(CarStatus.AVAILABLE);
//        car1.setDescription("Electric luxury car.");
//
//        Car car2 = new Car();
//        car2.setBrand("BMW");
//        car2.setModel("X5");
//        car2.setYear(2019);
//        car2.setPrice(60000.00);
//        car2.setStatus(CarStatus.AVAILABLE);
//        car2.setDescription("Luxury SUV.");
//
//        carRepository.save(car1);
//        carRepository.save(car2);
//
//        List<Car> cars = carRepository.findAll();
//        assertEquals(2, cars.size());
//    }
//}