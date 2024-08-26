package ru.ylab.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.ylab.domain.dto.CarDTO;
import ru.ylab.domain.dto.mapper.CarMapper;
import ru.ylab.domain.model.Car;
import ru.ylab.domain.enums.CarStatus;
import ru.ylab.repository.CarRepository;
import ru.ylab.service.implementation.CarServiceImpl;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CarServiceImplTest {
    @Mock
    private CarRepository carRepository;

    @Mock
    private CarMapper carMapper;

    @InjectMocks
    private CarServiceImpl carService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("View all cars and verify the repository method is called")
    public void testViewAllCars() {
        List<Car> cars = Arrays.asList(
                new Car(1, "Toyota", "Camry", 2022, 30000, CarStatus.AVAILABLE, "A reliable car"),
                new Car(2, "Honda", "Accord", 2023, 32000, CarStatus.SOLD, "A sporty car")
        );
        when(carRepository.findAll()).thenReturn(cars);
        when(carMapper.toDTO(any(Car.class))).thenAnswer(invocation -> {
            Car car = invocation.getArgument(0);
            return new CarDTO(car.getId(), car.getBrand(), car.getModel());
        });

        List<CarDTO> carDTOs = carService.getAllCars();

        assertEquals(2, carDTOs.size());
        verify(carRepository).findAll();
    }


    @Test
    @DisplayName("Get car by ID and verify the result")
    public void testGetCarById() {
        Car car = new Car(1, "Toyota", "Camry", 2022, 30000, CarStatus.AVAILABLE, "A reliable car");
        when(carRepository.findById(1)).thenReturn(car);
        when(carMapper.toDTO(any(Car.class))).thenReturn(new CarDTO(1, "Toyota", "Camry"));

        CarDTO result = carService.getCarById(1);

        assertNotNull(result);
        assertEquals(1, result.getId());
    }
}
