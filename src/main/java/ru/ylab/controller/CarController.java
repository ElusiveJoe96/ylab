package ru.ylab.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.ylab.domain.dto.CarDTO;
import ru.ylab.service.CarService;

import java.util.List;


@RestController
@RequestMapping("/cars")

@RequiredArgsConstructor
public class CarController {
    private final CarService carService;


    @GetMapping
    public ResponseEntity<List<CarDTO>> getAllCars() {
        List<CarDTO> cars = carService.getAllCars();
        return ResponseEntity.ok(cars);
    }


    @GetMapping("/{id}")
    public ResponseEntity<CarDTO> getCarById(@PathVariable("id") int id) {
        CarDTO car = carService.getCarById(id);
        if (car != null) {
            return ResponseEntity.ok(car);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }


    @PostMapping
    public ResponseEntity<CarDTO> addCar(@RequestBody CarDTO carDTO) {
        CarDTO newCar = carService.addCar(carDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(newCar);
    }


    @PutMapping("/{id}")
    public ResponseEntity<CarDTO> updateCar(@PathVariable("id") int id, @RequestBody CarDTO carDTO) {
        CarDTO updatedCar = carService.updateCar(id, carDTO);
        if (updatedCar != null) {
            return ResponseEntity.ok(updatedCar);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCar(@PathVariable("id") int id) {
        carService.deleteCar(id);
        return ResponseEntity.noContent().build();
    }
}
