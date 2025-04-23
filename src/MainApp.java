import boundary.LoginBoundary;
import control.*;
import java.util.*;
import model.*;
import repo.*;

/**
 * Entry point of the BTO Management System application.
 * This class initializes repositories, loads CSV data, sets up controllers,
 * and starts the login loop for users (Applicants, HDB Officers, and HDB Managers).
 */
public class MainApp {

    /**
     * Main method that launches the BTO Management System.
     * It initializes repositories and controllers, loads user and project data from CSV files,
     * and handles the login menu loop with user interaction.
     *
     * @param args Command-line arguments (not used).
     */
    public static void main(String[] args) {
        ApplicantRepo  applicantRepo = new ApplicantRepo();
        HDBOfficerRepo officerRepo   = new HDBOfficerRepo();
        HDBManagerRepo managerRepo   = new HDBManagerRepo();
        BTOProjectRepo projectRepo   = new BTOProjectRepo(managerRepo, officerRepo);

        List<IUserRepo<? extends User>> userRepos = Arrays.asList(applicantRepo, officerRepo, managerRepo);

        LoginBoundary loginBoundary = new LoginBoundary();
        LoginControl loginControl = new LoginControl(userRepos, applicantRepo, officerRepo, managerRepo, projectRepo);

        applicantRepo.loadFromCSV("data/ApplicantList.csv");
        officerRepo.loadFromCSV("data/OfficerList.csv");
        managerRepo.loadFromCSV("data/ManagerList.csv");
        projectRepo.loadFromCSV("data/ProjectList.csv");

        
        for (HDBOfficer officer : officerRepo.getAllOfficers()){
            if (officer != null){
                for (BTOProject project: projectRepo.getProjects()){
                    if (project != null){
                        if (project.getOfficerAssignedAsString().contains(officer.getName())){
                            officer.setAssignedProject(project);
                            break;
                        }
                    }
                }
            }

        }

        // applicantRepo.printAllApplicants(); //COMMENT
        // officerRepo.printAllOfficers(); //COMMENT
        // managerRepo.printAllManagers(); //COMMENT
        // projectRepo.printAllProjects(); //COMMENT 
        // DataInitializer.initializeProjects(projectRepo);
        
        Scanner sc = new Scanner(System.in);
        int choice = -1;

        while (choice != 2) {
            System.out.println("=== Welcome to BTO Management System ===");
            System.out.println("1. Login");
            System.out.println("2. Exit");
            System.out.print("Enter choice: ");

            if (sc.hasNextInt()) {
                choice = sc.nextInt();
                sc.nextLine();

                switch (choice) {
                    case 1:
                        loginBoundary.promptLogin(loginControl);
                        break;
                    case 2:
                        System.out.println("Goodbye!");
                        // applicantRepo.printAllApplicants(); //COMMENT
                        applicantRepo.saveToCSV("data/ApplicantList.csv");
                        officerRepo.saveToCSV("data/OfficerList.csv");
                        managerRepo.saveToCSV("data/ManagerList.csv");
                        projectRepo.saveToCSV("data/ProjectList.csv");
                        
                        break;
                    default:
                        System.out.println("Invalid option. Try again.");
                }
            } else {
                System.out.println("Please enter a number.");
                sc.next(); 
            }

            System.out.println();
        }

        sc.close();
    }
}