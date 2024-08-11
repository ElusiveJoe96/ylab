package ru.ylab.service;

import ru.ylab.domain.enums.CarStatus;
import ru.ylab.domain.model.Car;

import java.util.List;
import java.util.Scanner;

public interface CarService {
    void addCar(Scanner scanner);
    void updateCar(Scanner scanner);
    void deleteCar(Scanner scanner);
    void viewAllCars();
    List<Car> getCarsByBrand(String brand);
    List<Car> getCarsByModel(String model);
    List<Car> getCarsByYear(int year);
    List<Car> getCarsByStatus(CarStatus status);
    Car getCarById(int carId);
}
