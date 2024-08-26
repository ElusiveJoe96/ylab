package ru.ylab.service;

import ru.ylab.domain.dto.CarDTO;

import java.util.List;

public interface CarService {
    void addCar(CarDTO carDTO);
    boolean updateCar(CarDTO carDTO);
    boolean deleteCar(int carId);
    CarDTO getCarById(int carId);
    List<CarDTO> getAllCars();
}
