import control.*;
import java.util.Scanner;
import model.*;
import repo.*;

public class MainApp {
    public static void main(String[] args) {
        ApplicantRepo  applicantRepo = new ApplicantRepo();
        HDBOfficerRepo officerRepo   = new HDBOfficerRepo();
        HDBManagerRepo managerRepo   = new HDBManagerRepo();
        BTOProjectRepo projectRepo   = new BTOProjectRepo();
        ApplicantControl.init(applicantRepo, projectRepo);
        HDBOfficerControl.init(applicantRepo, projectRepo, officerRepo);
        HDBManagerControl.init(applicantRepo, projectRepo, officerRepo);

        applicantRepo.loadFromCSV("data\\ApplicantList.csv");
        officerRepo.loadFromCSV("data\\OfficerList.csv");
        managerRepo.loadFromCSV("data\\ManagerList.csv");
        projectRepo.loadFromCSV("data\\ProjectList.csv", managerRepo, officerRepo);

        
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
                        System.out.print("Enter NRIC: ");
                        String nric = sc.nextLine().trim();

                        System.out.print("Enter password: ");
                        String password = sc.nextLine().trim();

                        User user = null;
                        String role = "";

                        if ((user = applicantRepo.getUser(nric)) != null) {
                            role = "Applicant";
                        } else if ((user = officerRepo.getUser(nric)) != null) {
                            role = "HDB Officer";
                        } else if ((user = managerRepo.getUser(nric)) != null) {
                            role = "HDB Manager";
                        }

                        if (user != null && user.checkPassword(password)) {
                            System.out.println("Login successful!");

                            switch (role) {
                                case "Applicant" -> boundary.ApplicantBoundary.showApplicantMenu((Applicant) user);
                                case "HDB Officer" -> boundary.HDBOfficerBoundary.showOfficerMenu((HDBOfficer) user); 
                                case "HDB Manager" -> boundary.HDBManagerBoundary.showManagerMenu((HDBManager) user);
                            }
                        } else {
                            System.out.println("Invalid NRIC or password.");
                        }
                        break;
                    case 2:
                        System.out.println("Goodbye!");
                        // applicantRepo.printAllApplicants(); //COMMENT
                        applicantRepo.saveToCSV("../data/ApplicantList.csv");
                        officerRepo.saveToCSV("../data/OfficerList.csv");
                        managerRepo.saveToCSV("../data/ManagerList.csv");
                        projectRepo.saveToCSV("../data/ProjectList.csv");
                        
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