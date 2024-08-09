package ru.ylab.ui;

import ru.ylab.audit.AuditService;
import ru.ylab.domain.enums.Role;
import ru.ylab.domain.model.User;
import ru.ylab.controller.*;
import ru.ylab.repository.*;
import ru.ylab.service.implementation.*;

import java.util.Optional;
import java.util.Scanner;

public class MainMenu {

    private final MenuCommand adminMenu;
    private final MenuCommand managerMenu;
    private final MenuCommand clientMenu;
    private final MenuCommand guestMenu;

    public MainMenu() {
        UserRepository userRepository = new UserRepository();
        CarRepository carRepository = new CarRepository();
        OrderRepository orderRepository = new OrderRepository();

        AuditService auditService = new AuditService();
        UserServiceImpl userServiceImpl = new UserServiceImpl(userRepository, auditService);
        OrderServiceImpl orderServiceImpl = new OrderServiceImpl(orderRepository, auditService);
        CarServiceImpl carServiceImpl = new CarServiceImpl(carRepository, auditService);


        UserController userController = new UserController(userServiceImpl);
        CarController carController = new CarController(carServiceImpl);
        OrderController orderController = new OrderController(orderServiceImpl);
        AuditController auditController = new AuditController(auditService);

        adminMenu = new AdminMenuCommand(userController, carController, orderController, auditController);
        managerMenu = new ManagerMenuCommand(userController, carController, orderController);
        clientMenu = new ClientMenuCommand(carController, orderController, userController);
        guestMenu = new GuestMenuCommand(userController);


        User admin = new User(0, "admin", "admin@mail.ru", "1", Role.ADMIN, "1");
        userRepository.save(admin);
    }


    public void executeMenu(Role role, int choice, Scanner scanner) {
        Optional<User> currentUser = Optional.ofNullable(AuditService.loggedInUser);
        int userId = currentUser.map(User::getId).orElse(-1);

        MenuCommand command = switch (role) {
            case ADMIN -> adminMenu;
            case MANAGER -> managerMenu;
            case CLIENT -> clientMenu;
            default -> guestMenu;
        };
        command.execute(choice, scanner, userId);
    }


    public void showMenu(Role role) {
        MenuCommand command = switch (role) {
            case ADMIN -> adminMenu;
            case MANAGER -> managerMenu;
            case CLIENT -> clientMenu;
            default -> guestMenu;
        };
          command.showMenu();
    }


    public Role getCurrentUserRole() {
        return Optional.ofNullable(AuditService.loggedInUser)
                .map(User::getRole)
                .orElse(Role.GUEST);
    }

}
