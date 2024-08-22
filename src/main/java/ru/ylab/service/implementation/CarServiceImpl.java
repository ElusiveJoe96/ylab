package ru.ylab.service.implementation;

import ru.ylab.domain.dto.CarDTO;
import ru.ylab.domain.dto.mapper.CarMapper;
import ru.ylab.domain.model.Car;
import ru.ylab.domain.enums.CarStatus;
import ru.ylab.repository.CarRepository;
import ru.ylab.service.CarService;

import java.util.List;
import java.util.stream.Collectors;

public class CarServiceImpl implements CarService {
    private final CarRepository carRepository;
    private final CarMapper carMapper = CarMapper.INSTANCE;

    public CarServiceImpl(CarRepository carRepository) {
        this.carRepository = carRepository;
    }


    @Override
    public void addCar(CarDTO carDTO) {
        Car car = carMapper.toEntity(carDTO);
        car.setStatus(CarStatus.AVAILABLE);
        carRepository.save(car);
    }

    @Override
    public boolean updateCar(CarDTO carDTO) {
        Car car = carRepository.findById(carDTO.getId());
        if (car != null) {
            carMapper.updateEntityFromDTO(carDTO, car);
            carRepository.save(car);
            return true;
        }
        return false;
    }

    @Override
    public boolean deleteCar(int carId) {
        Car car = carRepository.findById(carId);
        if (car != null) {
            carRepository.delete(carId);
            return true;
        }
        return false;
    }

    @Override
    public CarDTO getCarById(int carId) {
        Car car = carRepository.findById(carId);
        return car != null ? carMapper.toDTO(car) : null;
    }

    @Override
    public List<CarDTO> getAllCars() {
        List<Car> cars = carRepository.findAll();
        return cars.stream()
                .map(carMapper::toDTO)
                .collect(Collectors.toList());
    }
}
