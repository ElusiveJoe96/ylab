package ru.ylab.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.ylab.domain.enums.CarStatus;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Car {
    private int id;
    private String brand;
    private String model;
    private int year;
    private double price;
    private CarStatus status;
    private String description;
}
