package ru.ylab.repository;

import ru.ylab.domain.model.Car;
import ru.ylab.domain.enums.CarStatus;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class CarRepository {
    private final HashMap<Integer, Car> cars = new HashMap<>();
    private int idCounter = 1;

    public void save(Car car) {
        if (car.getId() == 0) {
            car.setId(idCounter++);
        }
        cars.put(car.getId(), car);
    }

    public void delete(int carId) {
        cars.remove(carId);
    }

    public Car findById(int carId) {
        return cars.get(carId);
    }

    public List<Car> findAll() {
        return List.copyOf(cars.values());
    }

    public List<Car> findByBrand(String brand) {
        return cars.values().stream()
                .filter(car -> car.getBrand().equalsIgnoreCase(brand))
                .collect(Collectors.toList());
    }

    public List<Car> findByModel(String model) {
        return cars.values().stream()
                .filter(car -> car.getModel().equalsIgnoreCase(model))
                .collect(Collectors.toList());
    }

    public List<Car> findByYear(int year) {
        return cars.values().stream()
                .filter(car -> car.getYear() == year)
                .collect(Collectors.toList());
    }

    public List<Car> findByPrice(double price) {
        return cars.values().stream()
                .filter(car -> car.getPrice() == price)
                .collect(Collectors.toList());
    }

    public List<Car> findByStatus(CarStatus status) {
        return cars.values().stream()
                .filter(car -> car.getStatus() == status)
                .collect(Collectors.toList());
    }
}
