package ru.ylab.service.implementation;

import org.springframework.stereotype.Service;
import ru.ylab.domain.dto.CarDTO;
import ru.ylab.domain.dto.mapper.CarMapper;
import ru.ylab.domain.model.Car;
import ru.ylab.repository.CarRepository;
import ru.ylab.service.CarService;
import ru.ylab.util.ResourceNotFoundException;

import java.util.List;
import java.util.Optional;

@Service
public class CarServiceImpl implements CarService {

    private final CarRepository carRepository;
    private final CarMapper carMapper = CarMapper.INSTANCE;

    public CarServiceImpl(CarRepository carRepository) {
        this.carRepository = carRepository;
    }

    @Override
    public List<CarDTO> getAllCars() {
        List<Car> cars = carRepository.findAll();
        return cars.stream()
                .map(carMapper::toDTO)
                .toList();
    }

    @Override
    public CarDTO getCarById(int id) {
        Car car = carRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Car with ID " + id + " not found."));
        return carMapper.toDTO(car);
    }

    @Override
    public CarDTO addCar(CarDTO carDTO) {
        Optional<Car> existingCar = carRepository.findByModelAndYear(carDTO.getModel(), carDTO.getYear());
        if (existingCar.isPresent()) {
            throw new IllegalArgumentException("Car with model " + carDTO.getModel() + " and year " + carDTO.getYear() + " already exists.");
        }
        Car car = carMapper.toEntity(carDTO);
        carRepository.save(car);
        return carMapper.toDTO(car);
    }

    @Override
    public CarDTO updateCar(int id, CarDTO carDTO) {
        Car existingCar = carRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Car with ID " + id + " not found."));

        carMapper.updateEntityFromDTO(carDTO, existingCar);
        carRepository.save(existingCar);
        return carMapper.toDTO(existingCar);
    }

    @Override
    public void deleteCar(int id) {
        Car existingCar = carRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Car with ID " + id + " not found."));
        carRepository.delete(existingCar.getId());
    }
}
