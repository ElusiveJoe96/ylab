package ru.ylab.service.implementation;

import ru.ylab.audit.AuditService;
import ru.ylab.domain.model.User;
import ru.ylab.domain.enums.Role;
import ru.ylab.repository.UserRepository;
import ru.ylab.service.UserService;
import ru.ylab.util.ValidationUtil;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final AuditService auditService;

    public UserServiceImpl(UserRepository userRepository, AuditService auditService) {
        this.userRepository = userRepository;
        this.auditService = auditService;
    }

    public void registerUser(Scanner scanner) {
        User user = getUserDetails(scanner);

        if (!ValidationUtil.isValidEmail(user.getEmail())) {
            System.out.println("Invalid email format");
            return;
        }
        if (!ValidationUtil.isNonEmpty(user.getName()) || !ValidationUtil.isNonEmpty(user.getPassword())) {
            System.out.println("Name and password cannot be empty");
            return;
        }

        userRepository.save(user);
        System.out.println("User registered successfully.");
        auditService.logAction(user.getId(), "REGISTER_USER", "Registered user: " + user);
    }

    public Optional<User> login(Scanner scanner) {
        System.out.print("Enter email: ");
        String email = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (user.getPassword().equals(password)) {
                AuditService.loggedInUser = user;
                System.out.println("Login successful. Welcome " + user.getName() + "!");
                auditService.logAction(user.getId(), "LOGIN", "User logged in: " + email);
                return Optional.of(user);
            }
        }
        System.out.println("Invalid email or password.");
        return Optional.empty();
    }

    public void logout() {
        if (AuditService.loggedInUser != null) {
            auditService.logAction(AuditService.loggedInUser.getId(), "LOGOUT",
                    "User logged out: " + AuditService.loggedInUser.getEmail());
            AuditService.loggedInUser = null;
            System.out.println("Logout successful.");
        } else {
            System.out.println("No user is currently logged in.");
        }
    }

    public void updateUser(Scanner scanner) {
        System.out.print("Enter user ID to update: ");
        int userId = scanner.nextInt();
        scanner.nextLine();
        Optional<User> user = userRepository.findById(userId);

        user.ifPresentOrElse(existingUser -> {
            User newUserInfo = gerUserDetailsToUpdate(scanner);
            existingUser.setName(newUserInfo.getName());
            existingUser.setEmail(newUserInfo.getEmail());
            existingUser.setPassword(newUserInfo.getPassword());
            existingUser.setRole(newUserInfo.getRole());
            existingUser.setContactInfo(newUserInfo.getContactInfo());
            userRepository.save(existingUser);

            System.out.println("User updated successfully.");
            auditService.logAction(AuditService.loggedInUser.getId(), "UPDATE_USER", "Updated user: " + existingUser);
        },
                () -> System.out.println("User not found"));
    }

    public User getUserDetails(Scanner scanner) {
        System.out.print("Enter name: ");
        String name = scanner.nextLine();

        System.out.print("Enter email: ");
        String email = scanner.nextLine();

        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        System.out.print("Enter contact info: ");
        String contactInfo = scanner.nextLine();
        return new User(0, name, email, password, Role.CLIENT, contactInfo);
    }

    public User gerUserDetailsToUpdate(Scanner scanner) {
        System.out.print("Enter new name (leave empty to keep current): ");
        String name = scanner.nextLine();

        System.out.print("Enter new email (leave empty to keep current): ");
        String email = scanner.nextLine();

        System.out.print("Enter new password (leave empty to keep current): ");
        String password = scanner.nextLine();

        System.out.print("Enter new role (ADMIN, MANAGER, CLIENT) (leave empty to keep current): ");
        String roleInput = scanner.nextLine();

        Role role = ValidationUtil.getValidEnumValue(scanner, Role.class);
        System.out.print("Enter new contact info (leave empty to keep current): ");
        String contactInfo = scanner.nextLine();

        return new User(0, name, email, password, role, contactInfo);
    }

    public void deleteUser(int userId) {
        Optional<User> userToDelete = userRepository.findById(userId);

        if (userToDelete.isPresent()) {
            userRepository.delete(userId);
            auditService.logAction(AuditService.loggedInUser.getId(), "DELETE_USER",
                    "Deleted user with ID: " + userId);
            System.out.println("User with ID " + userId + " has been deleted.");
        } else {
            System.out.println("User with ID " + userId + " not found.");
        }
    }

    public void getAllUsers() {
        List<User> users = userRepository.findAll();
        if (users.isEmpty()) {
            System.out.println("No users available.");
        } else {
            users.forEach(user -> System.out.println(user.getId() + ": " + user.getName() +
                    ", Role: " + user.getRole() + ", Contact Info: " + user.getContactInfo()));
        }
    }

    public void updateUserRole(Scanner scanner) {
        System.out.print("Enter user ID to update role: ");
        int userId = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Enter new role (ADMIN, MANAGER, CLIENT): ");
        Role newRole = Role.valueOf(scanner.nextLine().toUpperCase());


        Optional<User> optionalUser = userRepository.findById(userId);
        optionalUser.ifPresent(user -> {
            user.setRole(newRole);
            userRepository.save(user);
            System.out.println("User role updated successfully.");
            auditService.logAction(AuditService.loggedInUser.getId(), "UPDATE_USER_ROLE",
                    "Updated role for user ID: " + userId + " to " + newRole);
        });
    }

    public void viewMyInfo() {
        User loggedInUser = AuditService.loggedInUser;
        if (loggedInUser != null) {
            System.out.println("ID: " + loggedInUser.getId());
            System.out.println("Name: " + loggedInUser.getName());
            System.out.println("Email: " + loggedInUser.getEmail());
            System.out.println("Role: " + loggedInUser.getRole());
            System.out.println("Contact Info: " + loggedInUser.getContactInfo());
        } else {
            System.out.println("No user is logged in.");
        }
    }

    public Optional<User> getUserById(int userId) {
        return userRepository.findById(userId);
    }

    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public List<User> getUsersByRole(Role role) {
        return userRepository.findByRole(role);
    }
}
