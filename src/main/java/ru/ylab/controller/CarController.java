package ru.ylab.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.ylab.domain.dto.CarDTO;
import ru.ylab.service.CarService;

import java.util.List;


@RestController
@RequestMapping("/cars")
@Tag(name = "Car Controller", description = "API for managing cars")
@RequiredArgsConstructor
public class CarController {
    private final CarService carService;

    @Operation(summary = "Get all cars", description = "Retrieve a list of all cars")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = CarDTO.class))})
    })
    @GetMapping
    public ResponseEntity<List<CarDTO>> getAllCars() {
        List<CarDTO> cars = carService.getAllCars();
        return ResponseEntity.ok(cars);
    }

    @Operation(summary = "Get a car by ID", description = "Retrieve a car by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved car",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = CarDTO.class))}),
            @ApiResponse(responseCode = "404", description = "Car not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<CarDTO> getCarById(@PathVariable("id") int id) {
        CarDTO car = carService.getCarById(id);
        if (car != null) {
            return ResponseEntity.ok(car);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @Operation(summary = "Add a new car", description = "Create a new car entry")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Car successfully created",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = CarDTO.class))})
    })
    @PostMapping
    public ResponseEntity<CarDTO> addCar(@RequestBody CarDTO carDTO) {
        CarDTO newCar = carService.addCar(carDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(newCar);
    }

    @Operation(summary = "Update a car", description = "Update the details of an existing car")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Car successfully updated",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = CarDTO.class))}),
            @ApiResponse(responseCode = "404", description = "Car not found")
    })
    @PutMapping("/{id}")
    public ResponseEntity<CarDTO> updateCar(@PathVariable("id") int id, @RequestBody CarDTO carDTO) {
        CarDTO updatedCar = carService.updateCar(id, carDTO);
        if (updatedCar != null) {
            return ResponseEntity.ok(updatedCar);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @Operation(summary = "Delete a car", description = "Delete a car by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Car successfully deleted"),
            @ApiResponse(responseCode = "404", description = "Car not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCar(@PathVariable("id") int id) {
        carService.deleteCar(id);
        return ResponseEntity.noContent().build();
    }
}
