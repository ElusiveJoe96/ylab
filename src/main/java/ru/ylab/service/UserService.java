package ru.ylab.service;

import ru.ylab.domain.enums.Role;
import ru.ylab.domain.model.User;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public interface UserService {
    void registerUser(String name, String email, String password, Role role, String contactInfo);
    User getUserDetails(Scanner scanner);
    Optional<User> login(String email, String password);
    void updateUser(int userId, String name, String email, String password, Role role, String contactInfo);
    void deleteUser(int userId);
    void getAllUsers();
    void updateUserRole(int userId, Role newRole);
    void viewMyInfo();
    Optional<User> getUserById(int userId);
    Optional<User> getUserByEmail(String email);
    List<User> getUsersByRole(Role role);
}
