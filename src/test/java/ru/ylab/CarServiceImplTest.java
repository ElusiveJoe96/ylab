package ru.ylab;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.ylab.audit.AuditService;
import ru.ylab.domain.enums.CarStatus;
import ru.ylab.domain.enums.Role;
import ru.ylab.domain.model.Car;
import ru.ylab.domain.model.User;
import ru.ylab.repository.CarRepository;
import ru.ylab.service.implementation.CarServiceImpl;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class CarServiceImplTest {
    @Mock
    private CarRepository carRepository;

    @Mock
    private AuditService auditService;

    @InjectMocks
    private CarServiceImpl carServiceImpl;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        AuditService.loggedInUser = new User(1, "testUser",
                "main@mail.ru", "pass", Role.ADMIN, "contactInfo");
    }

//    @Test
//    void testAddCar() {
//        carServiceImpl.addCar("Toyota", "Corolla", 2022, 20000, "A reliable car");
//
//        verify(carRepository).save(any(Car.class));
//    }

//    @Test
//    void testUpdateCar() {
//        Car existingCar = new Car(1, "Toyota", "Corolla",
//                2022, 20000, CarStatus.AVAILABLE, "A reliable car");
//        when(carRepository.findById(1)).thenReturn(existingCar);
//
//        carServiceImpl.updateCar(1, "Honda", "Civic",
//                2023, 22000, CarStatus.SOLD, "A sporty car");
//
//        verify(carRepository).save(existingCar);
//    }

//    @Test
//    void testDeleteCar() {
//        when(carRepository.findById(1)).thenReturn(new Car(1, "Toyota", "Corolla", 2022, 20000, CarStatus.AVAILABLE, "A reliable car"));
//
//        carServiceImpl.deleteCar(1);
//
//        verify(carRepository).delete(1);
//        verify(auditService).logAction(1, "DELETE_CAR", "Deleted car with ID: 1");
//    }

    @Test
    void testGetAllCars() {
        List<Car> cars = Arrays.asList(
                new Car(1, "Toyota", "Corolla",
                        2022, 20000, CarStatus.AVAILABLE, "A reliable car"),
                new Car(2, "Honda", "Civic",
                        2023, 22000, CarStatus.SOLD, "A sporty car")
        );
        when(carRepository.findAll()).thenReturn(cars);

        carServiceImpl.getAllCars();
    }

    @Test
    void testGetCarsByBrand() {
        List<Car> cars = List.of(
                new Car(1, "Toyota", "Corolla", 2022,
                        20000, CarStatus.AVAILABLE, "A reliable car")
        );
        when(carRepository.findByBrand("Toyota")).thenReturn(cars);

        List<Car> result = carServiceImpl.getCarsByBrand("Toyota");

        verify(carRepository).findByBrand("Toyota");
        assert result.equals(cars);
    }

    @Test
    void testGetCarsByModel() {
        List<Car> cars = List.of(
                new Car(1, "Toyota", "Corolla", 2022,
                        20000, CarStatus.AVAILABLE, "A reliable car")
        );
        when(carRepository.findByModel("Corolla")).thenReturn(cars);

        List<Car> result = carServiceImpl.getCarsByModel("Corolla");

        verify(carRepository).findByModel("Corolla");
        assert result.equals(cars);
    }

    @Test
    void testGetCarsByYear() {
        List<Car> cars = List.of(
                new Car(1, "Toyota", "Corolla", 2022,
                        20000, CarStatus.AVAILABLE, "A reliable car")
        );
        when(carRepository.findByYear(2022)).thenReturn(cars);

        List<Car> result = carServiceImpl.getCarsByYear(2022);

        verify(carRepository).findByYear(2022);
        assert result.equals(cars);
    }

    @Test
    void testGetCarsByStatus() {
        List<Car> cars = List.of(
                new Car(1, "Toyota", "Corolla", 2022,
                        20000, CarStatus.AVAILABLE, "A reliable car")
        );
        when(carRepository.findByStatus(CarStatus.AVAILABLE)).thenReturn(cars);

        List<Car> result = carServiceImpl.getCarsByStatus(CarStatus.AVAILABLE);

        verify(carRepository).findByStatus(CarStatus.AVAILABLE);
        assert result.equals(cars);
    }

    @Test
    void testGetCarById() {
        Car car = new Car(1, "Toyota", "Corolla", 2022,
                20000, CarStatus.AVAILABLE, "A reliable car");
        when(carRepository.findById(1)).thenReturn(car);

        Car result = carServiceImpl.getCarById(1);

        verify(carRepository).findById(1);
        assert result.equals(car);
    }
}
