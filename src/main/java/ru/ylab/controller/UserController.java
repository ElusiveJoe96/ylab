package ru.ylab.controller;

import ru.ylab.service.UserService;

import java.util.Scanner;

public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    public void registerUser(Scanner scanner) {
        userService.registerUser(scanner);
    }

    public boolean loginUser(Scanner scanner) {
        return userService.login(scanner).isPresent();
    }

    public void setUserRole(Scanner scanner) {
        userService.updateUserRole(scanner);
    }

    public void showMyInfo() {
        userService.viewMyInfo();
    }

    public void updateUser(Scanner scanner) {
        userService.updateUser(scanner);
    }

    public void viewAllUsers() {
        userService.getAllUsers();
    }


}
