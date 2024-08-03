package ru.ylab.domain.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.ylab.domain.enums.CarStatus;

@Getter
@Setter
@EqualsAndHashCode
@ToString
public class Car {
    private int id;
    private String brand;
    private String model;
    private int year;
    private double price;
    private CarStatus status;
    private String description;

    public Car(int id, String brand, String model, int year, double price, CarStatus status, String description) {
        this.id = id;
        this.brand = brand;
        this.model = model;
        this.year = year;
        this.price = price;
        this.status = status;
        this.description = description;
    }

}
