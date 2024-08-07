package ru.ylab.input;

import ru.ylab.domain.enums.Role;
import ru.ylab.domain.model.User;
import ru.ylab.service.UserServiceImpl;

import java.util.Optional;
import java.util.Scanner;

public class UserController {
    private final UserServiceImpl userServiceImpl;

    public UserController(UserServiceImpl userServiceImpl) {
        this.userServiceImpl = userServiceImpl;
    }

    public void registerUser(Scanner scanner) {
        User details = userServiceImpl.getUserDetails(scanner);
        userServiceImpl.registerUser(details.getName(), details.getEmail(),
                details.getPassword(), details.getRole(), details.getContactInfo());
    }

    public boolean loginUser(Scanner scanner) {
        System.out.print("Enter email: ");
        String email = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        Optional<User> user = userServiceImpl.login(email, password);
        return user.isPresent();
    }

    public void setUserRole(Scanner scanner) {
        System.out.print("Enter user ID to update role: ");
        int userId = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Enter new role (ADMIN, MANAGER, CLIENT): ");
        Role newRole = Role.valueOf(scanner.nextLine().toUpperCase());

        userServiceImpl.updateUserRole(userId, newRole);
        System.out.println("User role updated successfully.");
    }

    public void showMyInfo() {
        userServiceImpl.viewMyInfo();
    }

    public void updateUser(Scanner scanner) {
        System.out.print("Enter user ID to update: ");
        int userId = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Enter new name (leave empty to keep current): ");
        String name = scanner.nextLine();
        System.out.print("Enter new email (leave empty to keep current): ");
        String email = scanner.nextLine();
        System.out.print("Enter new password (leave empty to keep current): ");
        String password = scanner.nextLine();
        System.out.print("Enter new role (ADMIN, MANAGER, CLIENT) (leave empty to keep current): ");
        String roleInput = scanner.nextLine();
        Role role = roleInput.isEmpty() ? null : Role.valueOf(roleInput.toUpperCase());
        System.out.print("Enter new contact info (leave empty to keep current): ");
        String contactInfo = scanner.nextLine();

        userServiceImpl.updateUser(userId, name, email, password, role, contactInfo);
    }

    public void viewAllUsers() {
        userServiceImpl.getAllUsers();
    }


}
