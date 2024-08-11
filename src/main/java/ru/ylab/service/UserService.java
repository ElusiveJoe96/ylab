package ru.ylab.service;

import ru.ylab.domain.enums.Role;
import ru.ylab.domain.model.User;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public interface UserService {
    void registerUser(Scanner scanner);
    void logout();
    void updateUser(Scanner scanner);
    Optional<User> login(Scanner scanner);
    User getUserDetails(Scanner scanner);
    User gerUserDetailsToUpdate(Scanner scanner);
    void deleteUser(int userId);
    void viewAllUsers();
    void updateUserRole(Scanner scanner);
    void viewMyInfo();
    Optional<User> getUserById(int userId);
    Optional<User> getUserByEmail(String email);
    List<User> getUsersByRole(Role role);
}
