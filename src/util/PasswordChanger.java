package util;

import java.util.Scanner;
import model.User;

/**
 * A utility class that provides functionality to update a user's password.
 */
public class PasswordChanger {

    /** Scanner object for user input. */
    private static final Scanner sc = new Scanner(System.in);

    /**
     * Prompts the user to update their password.
     * Verifies the current password, and ensures the new password is confirmed correctly
     * before updating the user's password.
     *
     * @param user The {@code User} whose password is to be updated.
     */
    public static void updatePassword(User user) {
        System.out.print("Enter current password: ");
        String currentPassword = sc.nextLine();

        if (!user.getPassword().equals(currentPassword)) {
            System.out.println("Incorrect current password.");
            return;
        }

        System.out.print("Enter new password: ");
        String newPassword = sc.nextLine();
        System.out.print("Confirm new password: ");
        String confirmPassword = sc.nextLine();

        if (!newPassword.equals(confirmPassword)) {
            System.out.println("Passwords do not match.");
            return;
        }

        user.setPassword(newPassword);
        System.out.println("Password updated successfully.");
    }
}