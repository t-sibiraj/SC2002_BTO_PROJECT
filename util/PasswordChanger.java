package util;

import java.util.Scanner;
import model.User;

public class PasswordChanger {
    private static final Scanner sc = new Scanner(System.in);

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
