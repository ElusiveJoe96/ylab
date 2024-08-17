package ru.ylab.ui;

import ru.ylab.audit.AuditLogRepository;
import ru.ylab.audit.AuditService;
import ru.ylab.config.DatabaseConfig;
import ru.ylab.config.LiquibaseConfig;
import ru.ylab.domain.enums.Role;
import ru.ylab.domain.model.User;
import ru.ylab.repository.*;
import ru.ylab.service.CarService;
import ru.ylab.service.OrderService;
import ru.ylab.service.UserService;
import ru.ylab.service.implementation.*;

import java.util.Optional;
import java.util.Scanner;

public class MainMenu {
    private final MenuCommand adminMenu;
    private final MenuCommand managerMenu;
    private final MenuCommand clientMenu;
    private final MenuCommand guestMenu;

    public MainMenu() {
        DatabaseConfig databaseConfig = new DatabaseConfig();
        LiquibaseConfig liquibaseConfig = new LiquibaseConfig(databaseConfig);
        liquibaseConfig.runMigrations();

        UserRepository userRepository = new UserRepository(databaseConfig);
        CarRepository carRepository = new CarRepository(databaseConfig);
        OrderRepository orderRepository = new OrderRepository(databaseConfig);
        AuditLogRepository auditLogRepository = new AuditLogRepository(databaseConfig);

        AuditService auditService = new AuditService(auditLogRepository);
        UserService userService = new UserServiceImpl(userRepository, auditService);
        OrderService orderService = new OrderServiceImpl(orderRepository, auditService);
        CarService carService = new CarServiceImpl(carRepository, auditService);

        adminMenu = new AdminMenuCommand(userService, carService, orderService, auditService);
        managerMenu = new ManagerMenuCommand(userService, carService, orderService);
        clientMenu = new ClientMenuCommand(userService, carService, orderService);
        guestMenu = new GuestMenuCommand(userService);
    }

    public void executeMenu(int choice, Scanner scanner) {
        Optional<User> currentUser = Optional.ofNullable(AuditService.loggedInUser);
        int userId = currentUser.map(User::getId).orElse(-1);

        MenuCommand command = switch (getCurrentUserRole()) {
            case ADMIN -> adminMenu;
            case MANAGER -> managerMenu;
            case CLIENT -> clientMenu;
            default -> guestMenu;
        };
        command.execute(choice, scanner, userId);
    }

    public void showMenu() {
        MenuCommand command = switch (getCurrentUserRole()) {
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
