package ru.ylab.service.implementation;

import ru.ylab.audit.AuditService;
import ru.ylab.domain.model.Car;
import ru.ylab.domain.enums.CarStatus;
import ru.ylab.repository.CarRepository;
import ru.ylab.service.CarService;
import ru.ylab.util.ValidationUtil;

import java.util.List;
import java.util.Scanner;

public class CarServiceImpl implements CarService {
    private final CarRepository carRepository;
    private final AuditService auditService;

    public CarServiceImpl(CarRepository carRepository, AuditService auditService) {
        this.carRepository = carRepository;
        this.auditService = auditService;
    }

    public void addCar(Scanner scanner) {
        Car car = getCarDetails(scanner);
        carRepository.save(car);
        System.out.println("Car added successfully.");
        auditService.logAction(AuditService.loggedInUser.getId(), "ADD_CAR", "Added car: " + car);
    }

    public Car getCarDetails(Scanner scanner) {
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

        return new Car(0, brand, model, year, price, CarStatus.AVAILABLE, description);
    }

    public void updateCar(Scanner scanner) {
        System.out.print("Enter car ID to update: ");
        int carId = ValidationUtil.getValidInt(scanner);
        Car car = carRepository.findById(carId);
        if(car != null) {
            Car newCarInfo = getCarDetailsToUpdate(scanner);
            car.setBrand(newCarInfo.getBrand());
            car.setModel(newCarInfo.getModel());
            //TODO need change year?
            car.setYear(newCarInfo.getYear());
            car.setPrice(newCarInfo.getPrice());
            car.setStatus(newCarInfo.getStatus());
            car.setDescription(newCarInfo.getDescription());

            carRepository.save(car);
            System.out.println("Car updated successfully.");
            auditService.logAction(AuditService.loggedInUser.getId(), "UPDATE_CAR", "Updated car: " + car);
        } else {
            System.out.println("Car not found");
        }
    }


    public Car getCarDetailsToUpdate(Scanner scanner) {
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

        System.out.print("Enter new description: ");
        String description = scanner.nextLine();

        System.out.print("Enter new status (AVAILABLE, SOLD): ");
        CarStatus status = ValidationUtil.getValidEnumValue(scanner, CarStatus.class);

        return new Car(0, brand, model, year, price, status, description);
    }


    public void deleteCar(Scanner scanner) {
        System.out.print("Enter car ID to delete: ");
        int carId = scanner.nextInt();
        scanner.nextLine();

        if(carRepository.findById(carId) != null) {
            carRepository.delete(carId);
            System.out.println("Car deleted successfully.");
            auditService.logAction(AuditService.loggedInUser.getId(), "DELETE_CAR", "Deleted car with ID: " + carId);
        } else {
            System.out.println("Car not found");
            return;
        }
    }

    public void getAllCars() {
        List<Car> cars = carRepository.findAll();
        if (cars.isEmpty()) {
            System.out.println("No cars available.");
        } else {
            cars.forEach(car -> System.out.println(
                    car.getId() + ": " + car.getBrand() + " " + car.getModel() +
                            ", Year: " + car.getYear() + ", Price: " + car.getPrice() +
                            ", Status: " + car.getStatus() + ", Description: " + car.getDescription()
            ));
        }
    }

    public List<Car> getCarsByBrand(String brand) {
        return carRepository.findByBrand(brand);
    }

    public List<Car> getCarsByModel(String model) {
        return carRepository.findByModel(model);
    }

    public List<Car> getCarsByYear(int year) {
        return carRepository.findByYear(year);
    }

    public List<Car> getCarsByStatus(CarStatus status) {
        return carRepository.findByStatus(status);
    }

    public Car getCarById(int carId) {
        return carRepository.findById(carId);
    }
}
