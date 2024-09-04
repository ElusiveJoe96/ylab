package ru.ylab.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.ylab.domain.dto.CarDTO;
import ru.ylab.domain.enums.CarStatus;
import ru.ylab.service.CarService;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(CarController.class)
public class CarControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CarService carService;

    private final CarDTO car1 = new CarDTO("Brand1", "Model1", 2020, 20000.0, CarStatus.AVAILABLE, "Description1");
    private final CarDTO car2 = new CarDTO("Brand2", "Model2", 2021, 30000.0, CarStatus.SOLD, "Description2");

    @Test
    @DisplayName("getAllCars should return a list of cars")
    public void getAllCars_ShouldReturnCarList() throws Exception {
        Mockito.when(carService.getAllCars()).thenReturn(Arrays.asList(car1, car2));

        mockMvc.perform(get("/cars"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].brand").value(car1.getBrand()))
                .andExpect(jsonPath("$[1].brand").value(car2.getBrand()));
    }

    @Test
    @DisplayName("getCarById should return a car when found")
    public void getCarById_ShouldReturnCar_WhenFound() throws Exception {
        Mockito.when(carService.getCarById(1)).thenReturn(car1);

        mockMvc.perform(get("/cars/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.brand").value(car1.getBrand()));
    }

    @Test
    @DisplayName("getCarById should return 404 when the car is not found")
    public void getCarById_ShouldReturnNotFound_WhenCarNotFound() throws Exception {
        Mockito.when(carService.getCarById(1)).thenReturn(null);

        mockMvc.perform(get("/cars/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("addCar should return the created car")
    public void addCar_ShouldReturnCreatedCar() throws Exception {
        Mockito.when(carService.addCar(any(CarDTO.class))).thenReturn(car1);

        mockMvc.perform(post("/cars")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"brand\": \"Brand1\", \"model\": \"Model1\", \"year\": 2020, \"price\": 20000.0, \"status\": \"AVAILABLE\", \"description\": \"Description1\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.brand").value(car1.getBrand()));
    }

    @Test
    @DisplayName("updateCar should return the updated car when it exists")
    public void updateCar_ShouldReturnUpdatedCar_WhenCarExists() throws Exception {
        Mockito.when(carService.updateCar(eq(1), any(CarDTO.class))).thenReturn(car1);

        mockMvc.perform(put("/cars/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"brand\": \"Brand1\", \"model\": \"Model1\", \"year\": 2020, \"price\": 20000.0, \"status\": \"AVAILABLE\", \"description\": \"Description1\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.brand").value(car1.getBrand()));
    }

    @Test
    @DisplayName("updateCar should return 404 when the car does not exist")
    public void updateCar_ShouldReturnNotFound_WhenCarDoesNotExist() throws Exception {
        Mockito.when(carService.updateCar(eq(1), any(CarDTO.class))).thenReturn(null);

        mockMvc.perform(put("/cars/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"brand\": \"Brand1\", \"model\": \"Model1\", \"year\": 2020, \"price\": 20000.0, \"status\": \"AVAILABLE\", \"description\": \"Description1\"}"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("deleteCar should return 204 No Content when the car is deleted")
    public void deleteCar_ShouldReturnNoContent() throws Exception {
        Mockito.doNothing().when(carService).deleteCar(1);

        mockMvc.perform(delete("/cars/1"))
                .andExpect(status().isNoContent());
    }
}
