package ru.ylab.controller;

import ru.ylab.service.CarService;

import java.util.Scanner;

public class CarController {

    private final CarService carService;

    public CarController(CarService carService) {
        this.carService = carService;
    }

    public void addCar(Scanner scanner) {
        carService.addCar(scanner);
    }

    public void updateCar(Scanner scanner) {
        carService.updateCar(scanner);
    }

    public void deleteCar(Scanner scanner) {
        carService.deleteCar(scanner);
    }

    public void viewAllCars() {
        carService.getAllCars();
    }
}
