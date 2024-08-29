package ru.ylab.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.ylab.domain.dto.CarDTO;
import ru.ylab.domain.enums.CarStatus;
import ru.ylab.service.CarService;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static org.mockito.Mockito.*;

public class CarControllerTest {

    private CarController carController;
    private CarService carService;

    private CarDTO car1;
    private CarDTO car2;

    @BeforeEach
    public void setUp() {
        carService = Mockito.mock(CarService.class);
        carController = new CarController(carService);

        car1 = new CarDTO("Brand1", "Model1",
                2020, 20000.0, CarStatus.AVAILABLE, "Description1");
        car2 = new CarDTO("Brand2", "Model2",
                2021, 30000.0, CarStatus.SOLD, "Description2");
    }

    @Test
    @DisplayName("getAllCars should return a list of cars")
    public void getAllCars_ShouldReturnCarList() {
        when(carService.getAllCars()).thenReturn(Arrays.asList(car1, car2));

        ResponseEntity<List<CarDTO>> response = carController.getAllCars();

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(2, Objects.requireNonNull(response.getBody()).size());
        Assertions.assertEquals(car1, response.getBody().get(0));
        Assertions.assertEquals(car2, response.getBody().get(1));
        verify(carService, times(1)).getAllCars();
    }

    @Test
    @DisplayName("getCarById should return a car when found")
    public void getCarById_ShouldReturnCar_WhenFound() {
        when(carService.getCarById(1)).thenReturn(car1);

        ResponseEntity<CarDTO> response = carController.getCarById(1);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(car1, response.getBody());
        verify(carService, times(1)).getCarById(1);
    }

    @Test
    @DisplayName("getCarById should return 404 when the car is not found")
    public void getCarById_ShouldReturnNotFound_WhenCarNotFound() {
        when(carService.getCarById(1)).thenReturn(null);

        ResponseEntity<CarDTO> response = carController.getCarById(1);

        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        Assertions.assertNull(response.getBody());
        verify(carService, times(1)).getCarById(1);
    }

    @Test
    @DisplayName("addCar should return the created car")
    public void addCar_ShouldReturnCreatedCar() {
        when(carService.addCar(any(CarDTO.class))).thenReturn(car1);

        ResponseEntity<CarDTO> response = carController.addCar(car1);

        Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());
        Assertions.assertEquals(car1, response.getBody());
        verify(carService, times(1)).addCar(car1);
    }

    @Test
    @DisplayName("updateCar should return the updated car when it exists")
    public void updateCar_ShouldReturnUpdatedCar_WhenCarExists() {
        when(carService.updateCar(eq(1), any(CarDTO.class))).thenReturn(car1);

        ResponseEntity<CarDTO> response = carController.updateCar(1, car1);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(car1, response.getBody());
        verify(carService, times(1)).updateCar(eq(1), any(CarDTO.class));
    }

    @Test
    @DisplayName("updateCar should return 404 when the car does not exist")
    public void updateCar_ShouldReturnNotFound_WhenCarDoesNotExist() {
        when(carService.updateCar(eq(1), any(CarDTO.class))).thenReturn(null);

        ResponseEntity<CarDTO> response = carController.updateCar(1, car1);

        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        Assertions.assertNull(response.getBody());
    }

    @Test
    @DisplayName("deleteCar should return 204 No Content when the car is deleted")
    public void deleteCar_ShouldReturnNoContent() {
        doNothing().when(carService).deleteCar(1);

        ResponseEntity<Void> response = carController.deleteCar(1);

        Assertions.assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(carService, times(1)).deleteCar(1);
    }
}
