package util;

import java.util.Scanner;
import model.User;

/**
 * A utility class to create {@code User} objects based on console input.
 */
public class UserMaker {

    /**
     * Prompts the user via the console to enter user details, and creates a {@code User} object.
     *
     * @return A new {@code User} instance populated with input values.
     */
    public static User createUserFromInput() {
        Scanner sc = new Scanner(System.in);

        System.out.print("Enter name: ");
        String name = sc.nextLine();

        System.out.print("Enter NRIC: ");
        String nric = sc.nextLine();

        System.out.print("Enter password: ");
        String password = sc.nextLine();

        System.out.print("Enter age: ");
        int age = Integer.parseInt(sc.nextLine());

        System.out.print("Are you married? (yes/no): ");
        String maritalInput = sc.nextLine().trim().toLowerCase();
        boolean isMarried = maritalInput.equals("yes") || maritalInput.equals("y");

        return new User(nric, name, age, isMarried, password);
    }
}