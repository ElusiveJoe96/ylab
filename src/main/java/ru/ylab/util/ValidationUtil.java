package ru.ylab.util;

import ru.ylab.domain.dto.OrderDTO;
import ru.ylab.domain.dto.UserDTO;

import java.util.EnumSet;
import java.util.Scanner;
import java.util.regex.Pattern;

public class ValidationUtil {
    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");

    public static boolean isValidEmail(String email) {
        return EMAIL_PATTERN.matcher(email).matches();
    }

    public static boolean isNonEmpty(String str) {
        return str != null && !str.trim().isEmpty();
    }

    public static void validateUserDTO(UserDTO userDTO) {
        if (!isNonEmpty(userDTO.getName())) {
            throw new IllegalArgumentException("Name cannot be empty");
        }
        if (!isValidEmail(userDTO.getEmail())) {
            throw new IllegalArgumentException("Invalid email format");
        }
        if (!isNonEmpty(userDTO.getPassword())) {
            throw new IllegalArgumentException("Password cannot be empty");
        }
        if (!isNonEmpty(userDTO.getContactInfo())) {
            throw new IllegalArgumentException("Contact info cannot be empty");
        }
    }


    public static void validateOrderDTO(OrderDTO orderDTO) {
        if (orderDTO.getCarId() <= 0) {
            throw new IllegalArgumentException("Invalid car ID.");
        }
        if (orderDTO.getUserId() <= 0) {
            throw new IllegalArgumentException("Invalid user ID.");
        }
        if (orderDTO.getOrderDate() == null) {
            throw new IllegalArgumentException("Order date must not be null.");
        }
        if (orderDTO.getStatus() == null) {
            throw new IllegalArgumentException("Order status must not be null.");
        }
        if (orderDTO.getType() == null) {
            throw new IllegalArgumentException("Order type must not be null.");
        }
    }


    public static int getValidInt(Scanner scanner) {
        while (!scanner.hasNextInt()) {
            System.out.println("Invalid input. Please enter a valid integer.");
            scanner.next();
        }
        return scanner.nextInt();
    }

    public static double getValidDouble(Scanner scanner) {
        while (!scanner.hasNextDouble()) {
            System.out.println("Invalid input. Please enter a valid number.");
            scanner.next();
        }
        return scanner.nextDouble();
    }

    public static <T extends Enum<T>> T getValidEnumValue(Scanner scanner, Class<T> enumClass) {
        while (true) {
            try {
                String input = scanner.nextLine().toUpperCase();
                return Enum.valueOf(enumClass, input);
            } catch (IllegalArgumentException e) {
                String enumTypes = String.join(", ", EnumSet.allOf(enumClass).stream()
                        .map(Enum::name)
                        .toArray(String[]::new));
                System.out.println("Invalid " + enumClass.getSimpleName()
                        + ". Please enter one of the following: " + enumTypes + ".");
            }
        }
    }
}
