package ru.ylab.service;

import ru.ylab.domain.enums.CarStatus;
import ru.ylab.domain.model.Car;

import java.util.List;

public interface CarService {
    void addCar(String brand, String model, int year, double price, String description);
    void updateCar(int carId, String brand, String model, int year, double price, CarStatus status, String description);
    void deleteCar(int carId);
    void getAllCars();
    List<Car> getCarsByBrand(String brand);
    List<Car> getCarsByModel(String model);
    List<Car> getCarsByYear(int year);
    List<Car> getCarsByStatus(CarStatus status);
    Car getCarById(int carId);
}
