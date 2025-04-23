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
     * A list of user repositories containing various types of users
     * (e.g., applicants, officers, managers) that implement {@link IUserRepo}.
     */
    private List<IUserRepo<? extends User>> userRepos;

    /**
     * The boundary class responsible for handling user interactions and UI logic
     * for applicants (e.g., applying for projects, managing enquiries).
     */
    private ApplicantBoundary applicantBoundary;

    /**
     * The boundary class responsible for handling user interactions and UI logic
     * for HDB officers (e.g., booking flats, replying to enquiries).
     */
    private HDBOfficerBoundary officerBoundary;

    /**
     * The boundary class responsible for handling user interactions and UI logic
     * for HDB managers (e.g., managing projects, reviewing applications).
     */
    private HDBManagerBoundary managerBoundary;
   
    /**
     * Constructs a new {@code LoginControl} instance with the specified user repositories
     * and UI boundaries for each user type.
     *
     * @param userRepos          A list of repositories managing different user types (applicant, officer, manager).
     * @param applicantBoundary  The boundary handler for applicant-related interactions.
     * @param officerBoundary    The boundary handler for officer-related interactions.
     * @param managerBoundary    The boundary handler for manager-related interactions.
     */
    public LoginControl(List<IUserRepo<? extends User>> userRepos, 
                        ApplicantBoundary applicantBoundary, 
                        HDBOfficerBoundary officerBoundary, 
                        HDBManagerBoundary managerBoundary) {
        this.userRepos = userRepos;
        this.applicantBoundary = applicantBoundary;
        this.officerBoundary = officerBoundary;
        this.managerBoundary = managerBoundary;
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
            officerBoundary.showOfficerMenu(officer);
        } else if (user instanceof Applicant applicant) {
            applicantBoundary.showApplicantMenu(applicant);
        } else if (user instanceof HDBManager manager) {
            managerBoundary.showManagerMenu(manager);
        }
    }
}
