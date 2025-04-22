package boundary;

import control.LoginControl;
import java.util.*;

/**
 * The {@code LoginBoundary} class provides the user interface for login operations.
 * It prompts the user for NRIC and password, and delegates the login logic to the {@code LoginControl}.
 */
public class LoginBoundary {

    /** Scanner object for reading user input from the console. */
    private Scanner sc;
    
    /**
     * Constructs a {@code LoginBoundary} and initializes the input scanner.
     */
    public LoginBoundary() {
        sc = new Scanner(System.in);
    }

    /**
     * Prompts the user for their NRIC and password, and invokes the login handler.
     *
     * @param loginControl The {@code LoginControl} that processes login logic.
     */
    public void promptLogin(LoginControl loginControl) {
        System.out.print("Enter NRIC: ");
        String nric = sc.nextLine().trim();
        System.out.print("Enter password: ");
        String password = sc.nextLine().trim();
        loginControl.handleLogin(nric, password);
    }
}
