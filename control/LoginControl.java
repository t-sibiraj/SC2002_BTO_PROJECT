package control;

import boundary.*;
import java.util.*;
import model.Applicant;
import model.HDBManager;
import model.HDBOfficer;
import model.User;
import repo.*;

public class LoginControl {
    private List<IUserRepo<? extends User>> userRepos;

    public LoginControl(List<IUserRepo<? extends User>> userRepos) {
        this.userRepos = userRepos;
    }

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
