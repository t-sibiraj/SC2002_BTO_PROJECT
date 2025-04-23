package control;

import boundary.*;
import java.util.*;
import model.Applicant;
import model.HDBManager;
import model.HDBOfficer;
import model.User;
import repo.*;

/**
 * Control class that handles user authentication and post-login routing
 * based on user role (Applicant, HDBOfficer, or HDBManager).
 */
public class LoginControl {
    /**
     * List of user repositories representing all different user types.
     */
    private List<IUserRepo<? extends User>> userRepos;

    /**
     * Constructs a new LoginControl instance with the given user repositories.
     *
     * @param userRepos A list of repositories for different user types.
     */
    public LoginControl(List<IUserRepo<? extends User>> userRepos) {
        this.userRepos = userRepos;
    }

    /**
     * Attempts to log in a user by matching NRIC and password against
     * each user repository.
     *
     * @param nric     The NRIC of the user attempting to log in.
     * @param password The password provided by the user.
     * @return The authenticated User object if login is successful, null otherwise.
     */
    private User login(String nric, String password) {
        User user;
        for (IUserRepo<? extends User> userRepo : userRepos) {
            user = userRepo.getUser(nric);
            if (user != null) {
                if (user.checkPassword(password))
                    return user;
                else
                    return null;
            }
        }
        return null;
    }

    /**
     * Handles the login flow. If authentication is successful, directs the user
     * to the appropriate boundary interface based on their user type.
     *
     * @param nric     The NRIC entered by the user.
     * @param password The password entered by the user.
     */
    public void handleLogin(String nric, String password) {
        User user = login(nric, password);
        if (user == null) {
            System.out.println("Invalid NRIC or password.");
            return;
        }
        System.out.println("Login successful!");
        if (user instanceof HDBOfficer officer) {
            HDBOfficerBoundary officerBoundary = new HDBOfficerBoundary();
            officerBoundary.showOfficerMenu(officer);
        } else if (user instanceof Applicant applicant) {
            ApplicantBoundary applicantBoundary = new ApplicantBoundary();
            applicantBoundary.showApplicantMenu(applicant);
        } else if (user instanceof HDBManager manager) {
            HDBManagerBoundary managerBoundary = new HDBManagerBoundary();
            managerBoundary.showManagerMenu(manager);
        }
    }
}
