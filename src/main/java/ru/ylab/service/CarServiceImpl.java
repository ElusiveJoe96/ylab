package ru.ylab.service;

import ru.ylab.audit.AuditService;
import ru.ylab.domain.model.Car;
import ru.ylab.domain.enums.CarStatus;
import ru.ylab.repository.CarRepository;

import java.util.List;

public class CarServiceImpl implements CarService {
    private final CarRepository carRepository;
    private final AuditService auditService;

    public CarServiceImpl(CarRepository carRepository, AuditService auditService) {
        this.carRepository = carRepository;
        this.auditService = auditService;
    }

    public void addCar(String brand, String model, int year, double price, String description) {
        Car car = new Car(0, brand, model, year, price, CarStatus.AVAILABLE, description);
        carRepository.save(car);
        System.out.println("Car added successfully.");
        auditService.logAction(AuditService.loggedInUser.getId(), "ADD_CAR", "Added car: " + car);
    }

    public void updateCar(int carId, String brand, String model, int year, double price, CarStatus status, String description) {
        Car car = carRepository.findById(carId);
        if(car != null) {
            car.setBrand(brand);
            car.setModel(model);
            car.setYear(year);
            car.setPrice(price);
            car.setStatus(status);
            car.setDescription(description);
            carRepository.save(car);
            System.out.println("Car updated successfully.");
            auditService.logAction(AuditService.loggedInUser.getId(), "UPDATE_CAR", "Updated car: " + car);
        } else {
            System.out.println("Car not found");
        }
    }

    public void deleteCar(int carId) {
        if(carRepository.findById(carId) != null) {
            carRepository.delete(carId);
            System.out.println("Car deleted successfully.");
        } else {
            System.out.println("Car not found");
            return;
        }
        auditService.logAction(AuditService.loggedInUser.getId(), "DELETE_CAR", "Deleted car with ID: " + carId);
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
