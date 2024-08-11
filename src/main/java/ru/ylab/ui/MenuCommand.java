package ru.ylab.ui;

import java.util.Scanner;

public interface MenuCommand {
    void execute(int choice, Scanner scanner, int userId);
    void showMenu();
    default void exitApplication() {
        System.out.println("Exiting application...");
        System.exit(0);
    }
}
