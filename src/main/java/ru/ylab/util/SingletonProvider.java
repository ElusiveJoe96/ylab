package ru.ylab.util;

import ru.ylab.config.DatabaseConfig;
import ru.ylab.repository.CarRepository;
import ru.ylab.repository.OrderRepository;
import ru.ylab.repository.UserRepository;
import ru.ylab.service.CarService;
import ru.ylab.service.OrderService;
import ru.ylab.service.UserService;
import ru.ylab.service.implementation.CarServiceImpl;
import ru.ylab.service.implementation.OrderServiceImpl;
import ru.ylab.service.implementation.UserServiceImpl;

public class SingletonProvider {
    private static final DatabaseConfig DATABASE_CONFIG = new DatabaseConfig();

    private static UserRepository userRepository;
    private static CarRepository carRepository;
    private static OrderRepository orderRepository;

    private static UserService userService;
    private static CarService carService;
    private static OrderService orderService;

    private SingletonProvider() {}

    public static UserRepository getUserRepository() {
        if (userRepository == null) {
            userRepository = new UserRepository(DATABASE_CONFIG);
        }
        return userRepository;
    }

    public static CarRepository getCarRepository() {
        if (carRepository == null) {
            carRepository = new CarRepository(DATABASE_CONFIG);
        }
        return carRepository;
    }

    public static OrderRepository getOrderRepository() {
        if (orderRepository == null) {
            orderRepository = new OrderRepository(DATABASE_CONFIG);
        }
        return orderRepository;
    }

    public static UserService getUserService() {
        if (userService == null) {
            userService = new UserServiceImpl(getUserRepository());
        }
        return userService;
    }

    public static CarService getCarService() {
        if (carService == null) {
            carService = new CarServiceImpl(getCarRepository());
        }
        return carService;
    }

    public static OrderService getOrderService() {
        if (orderService == null) {
            orderService = new OrderServiceImpl(getOrderRepository());
        }
        return orderService;
    }
}
