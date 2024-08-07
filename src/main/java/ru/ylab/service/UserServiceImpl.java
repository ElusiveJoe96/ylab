package ru.ylab.service;

import ru.ylab.audit.AuditService;
import ru.ylab.domain.model.User;
import ru.ylab.domain.enums.Role;
import ru.ylab.repository.UserRepository;
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

    public void registerUser(String name, String email, String password, Role role, String contactInfo) {

        if (!ValidationUtil.isValidEmail(email)) {
            System.out.println("Invalid email format");
            return;
        }
        if (!ValidationUtil.isNonEmpty(name) || !ValidationUtil.isNonEmpty(password)) {
            System.out.println("Name and password cannot be empty");
            return;
        }

        User user = new User(0, name, email, password, role, contactInfo);

        userRepository.save(user);
        System.out.println("User registered successfully.");
        auditService.logAction(user.getId(), "REGISTER_USER", "Registered user: " + user);
    }

    public User getUserDetails(Scanner scanner) {
        System.out.print("Enter role (ADMIN, MANAGER, CLIENT): ");
        Role role = ValidationUtil.getValidRole(scanner);

        System.out.print("Enter name: ");
        String name = scanner.nextLine();
        System.out.print("Enter email: ");
        String email = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();
        System.out.print("Enter contact info: ");
        String contactInfo = scanner.nextLine();
        return new User(0, name, email, password, role, contactInfo);
    }

    public Optional<User> login(String email, String password) {
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (user.getPassword().equals(password)) {
                AuditService.loggedInUser = user;
                auditService.logAction(user.getId(), "LOGIN", "User logged in: " + email);
                System.out.println("Login successful. Welcome " + user.getName() + "!");
                return Optional.of(user);
            }
        }
        System.out.println("Invalid email or password.");
        return Optional.empty();
    }

    public void updateUser(int userId, String name, String email, String password, Role role, String contactInfo) {
        Optional<User> optionalUser = userRepository.findById(userId);
        optionalUser.ifPresent(user -> {
            if (ValidationUtil.isNonEmpty(name)) user.setName(name);
            if (ValidationUtil.isValidEmail(email)) user.setEmail(email);
            if (ValidationUtil.isNonEmpty(password)) user.setPassword(password);
            user.setRole(role);
            user.setContactInfo(contactInfo);
            userRepository.save(user);
            auditService.logAction(AuditService.loggedInUser.getId(), "UPDATE_USER", "Updated user: " + user);
        });
    }

    public void deleteUser(int userId) {
        Optional<User> userToDelete = userRepository.findById(userId);

        if (userToDelete.isPresent()) {
            userRepository.delete(userId);
            auditService.logAction(AuditService.loggedInUser.getId(), "DELETE_USER", "Deleted user with ID: " + userId);
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

    public void updateUserRole(int userId, Role newRole) {
        Optional<User> optionalUser = userRepository.findById(userId);
        optionalUser.ifPresent(user -> {
            user.setRole(newRole);
            userRepository.save(user);
            auditService.logAction(AuditService.loggedInUser.getId(), "UPDATE_USER_ROLE", "Updated role for user ID: " + userId + " to " + newRole);
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
