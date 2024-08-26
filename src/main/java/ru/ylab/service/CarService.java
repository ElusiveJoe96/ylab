package ru.ylab.service;

import ru.ylab.domain.dto.CarDTO;

import java.util.List;

public interface CarService {
    List<CarDTO> getAllCars();
    CarDTO getCarById(int id);
    CarDTO addCar(CarDTO carDTO);
    CarDTO updateCar(int id, CarDTO carDTO);
    void deleteCar(int id);
}
