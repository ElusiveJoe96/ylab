package ru.ylab.service;

import ru.ylab.domain.dto.CarDTO;

import java.util.List;

/**
 * Service interface for managing cars.
 * Provides methods for retrieving, adding, updating, and deleting cars.
 */
public interface CarService {

    /**
     * Retrieves all cars.
     *
     * @return a list of all cars as {@link CarDTO}.
     */
    List<CarDTO> getAllCars();

    /**
     * Retrieves a car by its unique identifier.
     *
     * @param id the unique identifier of the car.
     * @return the car as {@link CarDTO}, or {@code null} if no car is found with the given ID.
     */
    CarDTO getCarById(int id);

    /**
     * Adds a new car to the system.
     *
     * @param carDTO the car to be added, represented as {@link CarDTO}.
     * @return the created car as {@link CarDTO}.
     */
    CarDTO addCar(CarDTO carDTO);

    /**
     * Updates an existing car.
     *
     * @param id the unique identifier of the car to be updated.
     * @param carDTO the updated car data, represented as {@link CarDTO}.
     * @return the updated car as {@link CarDTO}, or {@code null} if no car is found with the given ID.
     */
    CarDTO updateCar(int id, CarDTO carDTO);

    /**
     * Deletes a car by its unique identifier.
     *
     * @param id the unique identifier of the car to be deleted.
     */
    void deleteCar(int id);
}
