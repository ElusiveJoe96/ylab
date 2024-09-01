package ru.ylab.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.ylab.domain.enums.CarStatus;

/**
 * Data Transfer Object (DTO) for representing a car.
 * <p>
 * This class is used to transfer car-related data between different layers of the application.
 * It encapsulates the properties of a car including its brand, model, year of manufacture,
 * price, status, and a description.
 * </p>
 *
 * @see CarStatus
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CarDTO {

    /**
     * The brand of the car.
     * <p>
     * For example: "Toyota", "Ford", etc.
     * </p>
     */
    private String brand;

    /**
     * The model of the car.
     * <p>
     * For example: "Corolla", "Mustang", etc.
     * </p>
     */
    private String model;

    /**
     * The year the car was manufactured.
     * <p>
     * The year should be a valid year representation, such as 2020.
     * </p>
     */
    private int year;

    /**
     * The price of the car.
     * <p>
     * This should represent the car's price in the appropriate currency.
     * </p>
     */
    private double price;

    /**
     * The status of the car.
     * <p>
     * This indicates the current condition or availability of the car.
     * Possible values are defined in {@link CarStatus}.
     * </p>
     */
    private CarStatus status;

    /**
     * A brief description of the car.
     * <p>
     * This may include additional details or characteristics of the car.
     * </p>
     */
    private String description;
}