package ru.ylab.input;

import ru.ylab.domain.enums.CarStatus;
import ru.ylab.service.CarServiceImpl;
import ru.ylab.util.ValidationUtil;

import java.util.Scanner;

public class CarController {

    private final CarServiceImpl carServiceImpl;

    public CarController(CarServiceImpl carServiceImpl) {
        this.carServiceImpl = carServiceImpl;
    }

    public void addCar(Scanner scanner) {
        System.out.print("Enter brand: ");
        String brand = scanner.nextLine();
        System.out.print("Enter model: ");
        String model = scanner.nextLine();
        System.out.print("Enter year: ");
        int year = ValidationUtil.getValidInt(scanner);
        System.out.print("Enter price: ");
        double price = ValidationUtil.getValidDouble(scanner);
        System.out.print("Enter description: ");
        String description = scanner.nextLine();

        carServiceImpl.addCar(brand, model, year, price, description);
    }




    public void updateCar(Scanner scanner) {
        System.out.print("Enter car ID to update: ");
        int carId = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Enter new brand: ");
        String brand = scanner.nextLine();
        System.out.print("Enter new model: ");
        String model = scanner.nextLine();
        System.out.print("Enter new year: ");
        int year = ValidationUtil.getValidInt(scanner);
        scanner.nextLine();
        System.out.print("Enter new price: ");
        double price = ValidationUtil.getValidDouble(scanner);
        scanner.nextLine();
        System.out.print("Enter new status (AVAILABLE, SOLD): ");
        CarStatus status = CarStatus.valueOf(scanner.nextLine().toUpperCase());
        System.out.print("Enter new description: ");
        String description = scanner.nextLine();

        carServiceImpl.updateCar(carId, brand, model, year, price, status, description);
    }

    public void deleteCar(Scanner scanner) {
        System.out.print("Enter car ID to delete: ");
        int carId = scanner.nextInt();
        scanner.nextLine();

        carServiceImpl.deleteCar(carId);
    }

    public void viewAllCars() {
        carServiceImpl.getAllCars();
    }


}
