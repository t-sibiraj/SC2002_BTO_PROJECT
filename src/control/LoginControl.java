package control;

import java.util.*;
import repo.*;
import model.Applicant;
import model.HDBManager;
import model.HDBOfficer;
import model.User;
import boundary.*;

public class LoginControl {
    private List<IUserRepo<? extends User>> userRepos;

    public LoginControl(List<IUserRepo<? extends User>> userRepos) {
        this.userRepos = userRepos;
    }

    private User login(String nric, String password) {
        User user = null;
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
        if (user instanceof HDBOfficer) {
            HDBOfficerBoundary.showOfficerMenu((HDBOfficer) user);
        } else if (user instanceof Applicant) {
            ApplicantBoundary.showApplicantMenu((Applicant) user);
        } else if (user instanceof HDBManager) {
            HDBManagerBoundary.showManagerMenu((HDBManager) user);
        }
    }
}
