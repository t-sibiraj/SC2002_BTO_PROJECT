package boundary;

import java.util.*;
import control.LoginControl;

public class LoginBoundary {
    private Scanner sc;
    
    public LoginBoundary() {
        sc = new Scanner(System.in);
    }

    public void promptLogin(LoginControl loginControl) {
        System.out.print("Enter NRIC: ");
        String nric = sc.nextLine().trim();
        System.out.print("Enter password: ");
        String password = sc.nextLine().trim();
        loginControl.handleLogin(nric, password);
    }
}
