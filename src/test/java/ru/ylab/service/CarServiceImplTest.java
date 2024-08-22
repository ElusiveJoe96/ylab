//package ru.ylab.service;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import ru.ylab.audit.AuditService;
//import ru.ylab.domain.enums.Role;
//import ru.ylab.domain.model.Car;
//import ru.ylab.domain.enums.CarStatus;
//import ru.ylab.domain.model.User;
//import ru.ylab.repository.CarRepository;
//import ru.ylab.service.implementation.CarServiceImpl;
//
//import java.util.Arrays;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//public class CarServiceImplTest {
//    @Mock
//    private CarRepository carRepository;
//
//    @InjectMocks
//    private CarServiceImpl carService;
//
//    @BeforeEach
//    public void setUp() {
//        MockitoAnnotations.openMocks(this);
//        AuditService.loggedInUser = new User(1, "John Doe", "john.doe@example.com",
//                "password123", Role.CLIENT, "123-456-7890");
//    }
//
//
//
//    @Test
//    @DisplayName("View all cars and verify the repository method is called")
//    public void testViewAllCars() {
//        List<Car> cars = Arrays.asList(
//                new Car(1, "Toyota", "Camry", 2022,
//                        30000, CarStatus.AVAILABLE, "A reliable car"),
//                new Car(2, "Honda", "Accord", 2023,
//                        32000, CarStatus.SOLD, "A sporty car")
//        );
//        when(carRepository.findAll()).thenReturn(cars);
//
//        carService.viewAllCars();
//
//        verify(carRepository).findAll();
//    }
//
//    @Test
//    @DisplayName("Get cars by brand and verify the results")
//    public void testGetCarsByBrand() {
//        List<Car> cars = List.of(
//                new Car(1, "Toyota", "Camry", 2022,
//                        30000, CarStatus.AVAILABLE, "A reliable car")
//        );
//        when(carRepository.findByBrand("Toyota")).thenReturn(cars);
//
//        List<Car> result = carService.getCarsByBrand("Toyota");
//
//        assertEquals(1, result.size());
//        assertEquals("Toyota", result.get(0).getBrand());
//    }
//
//    @Test
//    @DisplayName("Get cars by model and verify the results")
//    public void testGetCarsByModel() {
//        List<Car> cars = List.of(
//                new Car(1, "Toyota", "Camry", 2022,
//                        30000, CarStatus.AVAILABLE, "A reliable car")
//        );
//        when(carRepository.findByModel("Camry")).thenReturn(cars);
//
//        List<Car> result = carService.getCarsByModel("Camry");
//
//        assertEquals(1, result.size());
//        assertEquals("Camry", result.get(0).getModel());
//    }
//
//    @Test
//    @DisplayName("Get cars by year and verify the results")
//    public void testGetCarsByYear() {
//        List<Car> cars = List.of(
//                new Car(1, "Toyota", "Camry", 2022,
//                        30000, CarStatus.AVAILABLE, "A reliable car")
//        );
//        when(carRepository.findByYear(2022)).thenReturn(cars);
//
//        List<Car> result = carService.getCarsByYear(2022);
//
//        assertEquals(1, result.size());
//        assertEquals(2022, result.get(0).getYear());
//    }
//
//    @Test
//    @DisplayName("Get cars by status and verify the results")
//    public void testGetCarsByStatus() {
//        List<Car> cars = List.of(
//                new Car(1, "Toyota", "Camry", 2022,
//                        30000, CarStatus.AVAILABLE, "A reliable car")
//        );
//        when(carRepository.findByStatus(CarStatus.AVAILABLE)).thenReturn(cars);
//
//        List<Car> result = carService.getCarsByStatus(CarStatus.AVAILABLE);
//
//        assertEquals(1, result.size());
//        assertEquals(CarStatus.AVAILABLE, result.get(0).getStatus());
//    }
//
//    @Test
//    @DisplayName("Get car by ID and verify the result")
//    public void testGetCarById() {
//        Car car = new Car(1, "Toyota", "Camry", 2022,
//                30000, CarStatus.AVAILABLE, "A reliable car");
//        when(carRepository.findById(1)).thenReturn(car);
//
//        Car result = carService.getCarById(1);
//
//        assertNotNull(result);
//        assertEquals(1, result.getId());
//    }
//}
