package ru.ylab.service;

import ru.ylab.audit.AuditService;
import ru.ylab.domain.enums.Role;
import ru.ylab.domain.model.User;
import ru.ylab.input.AuditController;
import ru.ylab.input.CarController;
import ru.ylab.input.OrderController;
import ru.ylab.input.UserController;
import ru.ylab.repository.CarRepository;
import ru.ylab.repository.OrderRepository;
import ru.ylab.repository.UserRepository;

import java.util.Optional;
import java.util.Scanner;

public class MenuServiceImpl {

    private final UserController userController;
    private final CarController carController;
    private final OrderController orderController;
    private final AuditController auditController;


    public MenuServiceImpl() {
        UserRepository userRepository = new UserRepository();
        CarRepository carRepository = new CarRepository();
        OrderRepository orderRepository = new OrderRepository();

        AuditService auditService = new AuditService();
        UserServiceImpl userServiceImpl = new UserServiceImpl(userRepository, auditService);
        OrderServiceImpl orderServiceImpl = new OrderServiceImpl(orderRepository, auditService);
        CarServiceImpl carServiceImpl = new CarServiceImpl(carRepository, auditService);


        userController = new UserController(userServiceImpl);
        carController = new CarController(carServiceImpl);
        orderController = new OrderController(orderServiceImpl);
        auditController = new AuditController(auditService);
    }

    public void showMenu(Role role) {
        switch (role) {
            case ADMIN -> showAdminMenu();
            case MANAGER -> showManagerMenu();
            case CLIENT -> showClientMenu();
            default -> showGuestMenu();
        }
    }

    private void showGuestMenu() {
        System.out.println("1. Register\n2. Login\n3. Exit");
    }

    private void showAdminMenu() {
        System.out.println("""
                1. View all users
                2. Update user
                3. View all cars
                4. Change Role
                5. Add car
                6. Update car
                7. Delete car
                8. Create order
                9. Update order status
                10. Show logs
                0. Exit
                """);


    }

    private void showManagerMenu() {
        System.out.println("""
                1. Add car
                2. View users
                3. Update car
                4. Delete car
                5. Create order
                6. Update order status
                0. Exit
                """);
    }

    private void showClientMenu() {
        System.out.println("""
                1. View cars
                2. View orders
                3. View my info
                0. Exit
                """);
    }

    public void executeMenuOption(Role role, int choice, Scanner scanner) {
        Optional<User> currentUser = Optional.ofNullable(AuditService.loggedInUser);
        int userId = currentUser.map(User::getId).orElse(-1);

        switch (role) {
            case ADMIN -> adminActions(choice, scanner, userId);
            case MANAGER -> managerActions(choice, scanner, userId);
            case CLIENT -> clientActions(choice);
            default -> guestActions(choice, scanner);
        }
    }

    private void adminActions(int choice, Scanner scanner, int userId) {
        switch (choice) {
            case 1 -> userController.viewAllUsers();
            case 2 -> userController.updateUser(scanner);
            case 3 -> carController.viewAllCars();
            case 4 -> userController.setUserRole(scanner);
            case 5 -> carController.addCar(scanner);
            case 6 -> carController.updateCar(scanner);
            case 7 -> carController.deleteCar(scanner);
            case 8 -> orderController.createOrder(scanner, userId);
            case 9 -> orderController.updateOrderStatus(scanner);
            case 10 -> auditController.viewLogs();
            case 0 -> exitApplication();
            default -> System.out.println("Invalid option. Please try again.");
        }
    }

    private void managerActions(int choice, Scanner scanner, int userId) {
        switch (choice) {
            case 1 -> carController.addCar(scanner);
            case 2 -> userController.viewAllUsers();
            case 3 -> carController.updateCar(scanner);
            case 4 -> carController.deleteCar(scanner);
            case 5 -> orderController.createOrder(scanner, userId);
            case 6 -> orderController.updateOrderStatus(scanner);
            case 0 -> exitApplication();
            default -> System.out.println("Invalid option. Please try again.");
        }
    }

    private void clientActions(int choice) {
        switch (choice) {
            case 1 -> carController.viewAllCars();
            case 2 -> orderController.viewMyOrders(AuditService.loggedInUser.getId());
            case 3 -> userController.showMyInfo();
            case 0 -> exitApplication();
            default -> System.out.println("Invalid option. Please try again.");
        }
    }
    private void guestActions(int choice, Scanner scanner) {
        switch (choice) {
            case 1 -> userController.registerUser(scanner);
            case 2 -> {
                boolean isSuccess = userController.loginUser(scanner);
                if (isSuccess) {
                    Role role = getCurrentUserRole();
                    showMenu(role);
                } else {
                    System.out.println("Login failed. Try again.");

                }
            }
            case 3 -> exitApplication();
            default -> System.out.println("Invalid option. Please try again.");
        }
    }

    public Role getCurrentUserRole() {
        return Optional.ofNullable(AuditService.loggedInUser)
                .map(User::getRole)
                .orElse(Role.GUEST);
    }

    private void exitApplication() {
        System.out.println("Exiting application...");
        System.exit(0);
    }
}
