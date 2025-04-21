// You can refer to the OfficerControl code for similar functionality.
// I have commented out a few imports because my VSCode auto-removes unused imports. 
// So, please uncomment them when necessary.

// In the main program, the repositories are first initialized and the same instances are used in all control classes.

// Note that:
// - Each Applicant object has its own Application and list of Enquiries.
// - Each HDBOfficer has their own Application, Enquiries, and OfficerRegistration.
// - Each BTOProject stores all (not "own") Applications, Enquiries, and OfficerRegistrations submitted for that project.

// Therefore, when creating a new Application, it must be added to the BTOProject object (via the ProjectRepo)
// **in addition to** setting it in the Applicant object (via the ApplicantRepo).

// For example:
// Enquiry enquiry = new Enquiry(applicant, selectedProject, message);
// applicant.addEnquiry(enquiry);
// selectedProject.addEnquiry(enquiry);

// If you look closely, it’s the same Enquiry object stored in both the applicant and project.
// So, if you update the Enquiry via the ApplicantRepo, the changes will automatically be reflected in the ProjectRepo too.
// This same behavior applies to Application objects as well.

// In OfficerControl, for instance, it retrieves an Application from the ProjectRepo,
// and once it sets the status to BOOKED, the corresponding object in the ApplicantRepo also reflects the update,
// because they point to the same object in memory.

// At the HDBManagerControl level, most of the objects that need to be added to repos will already be added elsewhere.
// One of the few things you might need to add here is a **new Project** to the ProjectRepo.
// Other than that, most entities (Applications, Registrations, Enquiries) are already handled.

// As long as your function modifies a shared object (e.g., Application or Enquiry),
// those changes will be reflected across all repos automatically.

// Here’s what you should prioritize (in order of importance):
// 1. Complete the control/HDBManagerControl and model/HDBManagerclass (add more menu options if needed).
// 1.1 You might have to add or modify some functionalities of repo/BTOProjectRepo also to achive 1
// 2. Write the coding report and perform testing for the HDB Manager functionality only
// 3. Implement input validation (basic checks) (I have already have a few checks in place for null).
//    - Go through each file and check any Scanner input lines.
//    - Add basic input validation there.
//    - To reduce code duplication, you can create utility methods for validation inside the `util` package.



// I WILL LATER ADD DOCUMENTATION COMMENTS FOR THINGS THAT ARE ALREADY CODED SO FAR. FOR THE THINGS THAT YOU WILL BE CODING JUST ADD THE DOC COMMENTS YOURSELF
// ALSO PUSH YOUR CHANGES TO THE GITHUB REPO REGULARY SO THAT ITS EASIER TO RESOLVE IF THERE IS A CONFLICT
// IF YOU GOT ANY QUESTIONS JUST FEEL FREE TO MESSAGE ME


package control;

import enums.ApplicationStatus;
import enums.RegistrationStatus;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import model.*;
import repo.*;
import util.*;
// import enums.*;

// import java.util.List;
// import java.util.ArrayList;
// import java.util.List;
// import java.util.stream.Collectors;

public class HDBManagerControl {

    private static BTOProjectRepo projectRepo;
    private static ApplicantRepo applicantRepo;
    private static HDBOfficerRepo officerRepo;

    public static void init(ApplicantRepo aRepo, BTOProjectRepo pRepo, HDBOfficerRepo oRepo) {
        projectRepo = pRepo;
        applicantRepo = aRepo;
        officerRepo = oRepo;
    }

    // 1. Create New Project
    public static void handleCreateProject(HDBManager manager) {
        // Prompt for input fields and create BTOProject
        // Enforce: no overlapping project periods for same manager
        projectRepo.createProject(manager);
    }

    // 2. Edit Project
    public static void handleEditProject(HDBManager manager) {
        // Select from manager's projects
        // Update editable fields

        Scanner sc = new Scanner(System.in);

        List<BTOProject> projects = manager.getprojects();
        if(projects.isEmpty())
            return;
        manager.viewProjects();

        System.out.print("Enter the number of the project you want to edit (0 to cancel): ");
        int choice;
        try {
            choice = Integer.parseInt(sc.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid input.");
            return;
        }

        if (choice == 0) {
            System.out.println("Operation cancelled.");
            return;
        }

        if (choice < 0 || choice > projects.size()) {
            System.out.println("Invalid project number.");
            return;
        }

        BTOProject selected = projects.get(choice - 1);
        System.out.println("\nSelected project:");
        System.out.println(selected);
        
        projectRepo.editProject(selected.getName());
    }

    // 3. Delete Project
    public static void handleDeleteProject(HDBManager manager) {
        // Select and remove project created by this manager
        Scanner sc = new Scanner(System.in);

        List<BTOProject> projects = manager.getprojects();
        if(projects.isEmpty())
            return;
        manager.viewProjects();

        System.out.print("Enter the number of the project you want to delete (0 to cancel): ");
        int choice;
        try {
            choice = Integer.parseInt(sc.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid input.");
            return;
        }

        if (choice == 0) {
            System.out.println("Operation cancelled.");
            return;
        }

        if (choice < 0 || choice > projects.size()) {
            System.out.println("Invalid project number.");
            return;
        }

        BTOProject selected = projects.get(choice - 1);
        System.out.println("\nSelected project:");
        System.out.println(selected);
        
        projectRepo.deleteProject(selected.getName());
    }

    // 4. Toggle Visibility
    public static void toggleProjectVisibility(HDBManager manager) {
        // Toggle visibility for one of their projects
        Scanner sc = new Scanner(System.in);

        List<BTOProject> projects = manager.getprojects();
        if(projects.isEmpty())
            return;
        manager.viewProjects();

        System.out.print("Enter the number of the project you want to toggle visibility (0 to cancel): ");
        int choice;
        try {
            choice = Integer.parseInt(sc.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid input.");
            return;
        }

        if (choice == 0) {
            System.out.println("Operation cancelled.");
            return;
        }

        if (choice < 0 || choice > projects.size()) {
            System.out.println("Invalid project number.");
            return;
        }

        BTOProject selected = projects.get(choice - 1);
        selected.toggleVisibility();
        System.out.println("\nUpdated project:");
        System.out.println(selected);
    }

    // 5. View All Projects
    public static void viewAllProjects() {
        // List all projects regardless of visibility or owner
        projectRepo.printAllProjects();
    }

    // 6. View Only My Projects
    public static void viewMyProjects(HDBManager manager) {
        // Filter projectRepo by manager.getNric()
        manager.viewProjects();
    }

    // 7. View Officer Registrations
    public static void viewOfficerRegistrations(HDBManager manager) {
        // Show all officer registrations related to manager's projects
        if (manager.getprojects().isEmpty()){
            System.out.println("No projects");
            return;
        }

        System.out.println("=== Officer Registrations ===");
        System.out.println("------------------------------------------");
        for (BTOProject project : manager.getprojects()){
            System.out.println(project.getOfficerRegistrations().toString());
        }
        System.out.println("------------------------------------------");
    }

    // 8. Approve/Reject Officer Registration
    public static void handleUpdateRegistration(HDBManager manager) {
        // Approve or reject a registration
        // On approval, assign officer to project & decrease available officer slots

        Scanner sc = new Scanner(System.in);
        List<OfficerRegistration> registrations = new ArrayList<>();
        
        for (BTOProject projects : manager.getprojects()){
            for (OfficerRegistration registration : projects.getOfficerRegistrations()){
                if (registration.getStatus() == RegistrationStatus.PENDING)
                    registrations.add(registration);
            }
        }

        System.out.println("=== Pending Registrations ===");
        for (int i = 0; i < registrations.size(); i++) {
            System.out.println((i + 1) + ". " + registrations.toString());
        }

        System.out.print("Enter the number of the registration you want to approve/reject (0 to cancel): ");
        int choice;
        try {
            choice = Integer.parseInt(sc.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid input.");
            return;
        }

        if (choice == 0) {
            System.out.println("Operation cancelled.");
            return;
        }

        if (choice < 0 || choice > registrations.size()) {
            System.out.println("Invalid registration number.");
            return;
        }

        OfficerRegistration selected = registrations.get(choice -1);
        System.out.println("\nSelected registration:");
        System.out.println(selected);
        
        System.out.print("Would you like to (A)pprove or (R)eject this registration? ");
        String action = sc.nextLine().trim().toLowerCase();

        switch (action) {
            case "a" -> {
                if (selected.getProject().getNoAvailableOffice()>0){
                    manager.updateRegistration(true, selected);
                    selected.getProject().decrementAvailableOffice();
                    System.out.println("Registration Approved");
                }
                else
                    System.out.println("No office available, approval failed. ");
            }

            case "r" -> {
                manager.updateRegistration(false, selected);
                System.out.println("Registration Rejected");
            }

            default -> System.out.println("Invalid choice. Operation cancelled.");
        }

    }

    // 9. View Applications to My Project
    public static void viewApplications(HDBManager manager) {
        // List applications to manager's projects
        List<BTOProject> projects = manager.getprojects();
        if (projects.isEmpty()){
            System.out.println("No projects to view.");
            return;
        }

        for (BTOProject project : projects){
            System.out.println(project.getApplications().toString());
        }
    }

    // 10. Approve/Reject Application
    public static void handleUpdateApplication(HDBManager manager) {
        // Approve/reject application based on flat supply
        // Update project flat counts
        Scanner sc = new Scanner(System.in);
        List<BTOProject> projects = manager.getprojects();
        List<BTOApplication> applications = new ArrayList<>();

        if (projects.isEmpty()){
            System.out.println("No projects available.");
            return;
        }

        for (BTOProject project : projects){//view all projects
            for (BTOApplication application : project.getApplications()){//for each project get all applications
                if (application.getApplicationStatus() == ApplicationStatus.PENDING)//for each application check status is pending
                    applications.add(application);//add pending applications to list
            }
        }

        System.out.println("=== Pending Applications ===");
        for (int i = 0; i < applications.size(); i++) {
            System.out.println((i + 1) + ". " + applications.toString());
        }

        System.out.print("Enter the number of the application you want to approve/reject (0 to cancel): ");
        int choice;
        try {
            choice = Integer.parseInt(sc.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid input.");
            return;
        }

        if (choice == 0) {
            System.out.println("Operation cancelled.");
            return;
        }

        if (choice < 0 || choice > applications.size()) {
            System.out.println("Invalid application number.");
            return;
        }

        BTOApplication selected = applications.get(choice -1);
        System.out.println("\nSelected application:");
        System.out.println(selected);
        
        System.out.print("Would you like to (A)pprove or (R)eject this application? ");
        String action = sc.nextLine().trim().toLowerCase();

        switch (action) {
            case "a" -> {
                if (selected.getProject().hasFlatAvailable(selected.getFlatType())){
                    manager.updateApplication(true, selected);
                    selected.getProject().decrementFlatCount(selected.getFlatType());
                }
                else
                    System.out.println("No flats available, approval failed. ");
            }

            case "r" -> {
                manager.updateApplication(false, selected);
            }

            default -> System.out.println("Invalid choice. Operation cancelled.");
        }
    }

    // 11. Process Withdrawal Requests
    public static void handleProcessWithdrawal(HDBManager manager) {
        // Show withdrawal requests for their projects
        // Allow manager to approve/reject
        manager.processWithdrawal();
        System.out.println("=== Withdrawal Processed ===");
    }

    // 12. View All Enquiries (all projects)
    public static void viewAllEnquiries() {
        // Show enquiries across all projects
        System.out.println("=== List of All Enquiries ===");
        for (BTOProject project : projectRepo.getProjects()){
            for (Enquiry enquiry : project.getEnquiries()){
                System.out.println(enquiry.toString());
            }
        }
    }

    // 13. Reply to Enquiries (My Project Only)
    public static void replyToEnquiries(HDBManager manager) {
        // Allow manager to reply only to enquiries on their own projects
        List<BTOProject> projects = manager.getprojects();
        List<Enquiry> enquiries = new ArrayList<>();
        Scanner sc = new Scanner(System.in);

        if (projects.isEmpty()){
            System.out.println("You are not managing any projects");
            return;
        }

        for (BTOProject project : projects){//view all projects
            for (Enquiry enquiry : project.getEnquiries()){//for each project get all enquiries
                if (!enquiry.isReplied())//for each enquiry filter non replied enquiries
                    enquiries.add(enquiry);//add enquiry to list
            }
        }

        System.out.println("=== Pending Enquiries ===");
        for (int i = 0; i < enquiries.size(); i++) {
            System.out.println((i + 1) + ". " + enquiries.toString());
        }

        System.out.print("Enter the number of the enquiries you want to reply to (0 to cancel): ");
        int choice;
        try {
            choice = Integer.parseInt(sc.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid input.");
            return;
        }

        if (choice == 0) {
            System.out.println("Operation cancelled.");
            return;
        }

        if (choice < 0 || choice > enquiries.size()) {
            System.out.println("Invalid enquiry number.");
            return;
        }

        Enquiry selected = enquiries.get(choice -1);
        System.out.println("\nSelected enquiry:");
        System.out.println(selected);

        if (selected.isReplied()) {
            System.out.println("This enquiry has already been replied to.");
            return;
        }

        System.out.print("Enter your reply: ");
        String reply = sc.nextLine().trim();

        if (reply.isEmpty()) {
            System.out.println("Reply cannot be empty.");
            return;
        }

        manager.replyToEnquiry(selected, reply);
    }

    // 14. Generate Report (with filters)
    public static void generateReports(HDBManager manager) {
        // Generate and display applicant reports (e.g. filter by marital status, flat type, etc.)
        Scanner sc = new Scanner(System.in);

        manager.generateReports();

        System.out.print("""
            1. No filter
            2. Married
            3. Not Married
            4. Two Room Flats
            5. Three Room Flats
            Enter the number of the filter you want to use (0 to cancel): 
                """);

        int choice;
        try {
            choice = Integer.parseInt(sc.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid input.");
            return;
        }

        if (choice == 0) {
            System.out.println("Operation cancelled.");
            return;
        }

        if (choice < 0 || choice > 5) {
            System.out.println("Invalid option.");
            return;
        }


        manager.viewReport(choice);
    }

    // 15. Change Password
    public static void changePassword(HDBManager manager) {
        PasswordChanger.updatePassword(manager);
    }
}