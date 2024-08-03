package ru.ylab.util;

import ru.ylab.domain.enums.Role;

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

    public static Role getValidRole(Scanner scanner) {
        while (true) {
            try {
                String input = scanner.nextLine().toUpperCase();
                return Role.valueOf(input);
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid role. Please enter one of the following: ADMIN, MANAGER, CLIENT.");
            }
        }
    }
}
