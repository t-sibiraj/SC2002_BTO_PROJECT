package control;

import enums.ApplicationStatus;
import enums.RegistrationStatus;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import model.*;
import repo.*;
import util.*;

/**
 * Control class that handles all business logic related to HDB Manager actions.
 * This includes managing BTO projects, approving officer registrations,
 * handling application decisions, and generating reports.
 */
public class HDBManagerControl {

    /** Repository containing all BTO project data accessible to the manager. */
    private BTOProjectRepo projectRepo;

    /** Scanner for reading user input during manager interactions. */
    private final Scanner sc = new Scanner(System.in);

    /**
     * Initializes the control class by linking it to a BTO project repository.
     * This must be called before invoking any manager-related operations.
     *
     * @param pRepo The project repository to associate with this controller.
     */
    public void init(BTOProjectRepo pRepo) {
        projectRepo = pRepo;
    }

    /**
     * Handles the creation of a new BTO project by the manager.
     * Delegates the creation logic to the project repository.
     *
     * @param manager The HDB manager creating the project.
     */
    public void handleCreateProject(HDBManager manager) {
        // Prompt for input fields and create BTOProject
        // Enforce: no overlapping project periods for same manager
        projectRepo.createProject(manager);
    }

    /**
     * Allows the manager to select and edit one of their existing projects.
     * Displays the list of projects, validates the selection,
     * and delegates the editing logic to the project repository.
     *
     * @param manager The HDB manager editing their project.
     */
    public void handleEditProject(HDBManager manager) {
        // Select from manager's projects
        // Update editable fields

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

    /**
     * Allows the manager to delete one of their projects.
     * Displays the list of projects, validates the selection,
     * and removes the selected project via the repository.
     *
     * @param manager The HDB manager deleting their project.
     */
    public void handleDeleteProject(HDBManager manager) {
        // Select and remove project created by this manager

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


    /**
     * Toggles the visibility of a project managed by the HDB manager.
     * Displays the manager's projects and updates the visibility of the selected one.
     *
     * @param manager The HDB manager toggling project visibility.
     */
    public void toggleProjectVisibility(HDBManager manager) {
        // Toggle visibility for one of their projects

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

    /**
     * Displays all BTO projects available in the system, regardless of visibility or owner.
     */
    public void viewAllProjects() {
        // List all projects regardless of visibility or owner
        projectRepo.printAllProjects();
    }

    /**
     * Displays only the projects created by the given HDB manager.
     *
     * @param manager The manager whose projects are to be viewed.
     */
    public void viewMyProjects(HDBManager manager) {
        // Filter projectRepo by manager.getNric()
        manager.viewProjects();
    }


    /**
     * Displays all officer registration requests associated with the manager's projects.
     *
     * @param manager The manager reviewing officer registrations.
     */
    public void viewOfficerRegistrations(HDBManager manager) {
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


    /**
     * Handles the approval or rejection of pending officer registration requests
     * for the manager's projects. On approval, assigns the officer to the project
     * and decrements the number of available officer slots.
     *
     * @param manager The HDB manager processing officer registrations.
     */
    public void handleUpdateRegistration(HDBManager manager) {
        // Approve or reject a registration
        // On approval, assign officer to project & decrease available officer slots

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


    /**
     * Displays all applications submitted to the projects managed by the given manager.
     *
     * @param manager The manager viewing submitted applications.
     */
    public void viewApplications(HDBManager manager) {
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


    /**
     * Allows the manager to approve or reject pending BTO applications.
     * Approval is based on flat availability, and approved applications
     * will decrement the available flat count.
     *
     * @param manager The manager updating the application statuses.
     */
    public void handleUpdateApplication(HDBManager manager) {
        // Approve/reject application based on flat supply
        // Update project flat counts

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


    /**
     * Processes withdrawal requests submitted by applicants for the manager's projects.
     * The manager may approve or reject each request.
     *
     * @param manager The manager handling withdrawal requests.
     */
    public void handleProcessWithdrawal(HDBManager manager) {
        // Show withdrawal requests for their projects
        // Allow manager to approve/reject

        if(manager.getprojects().isEmpty()){
            System.out.println("No Withdrawals to show");
        }

        List<BTOApplication> withdrawals = new ArrayList<>();
        for(BTOProject project : manager.getprojects()){//for each project
            for(BTOApplication application : project.getApplications()){//for each project application
                if(application.hasRequestedWithdraw()){//if application requested for withdrawal
                    withdrawals.add(application);
                }
            }
        }

        System.out.println("=== Pending Withdrawals ===");
        for (int i = 0; i < withdrawals.size(); i++) {
            System.out.println((i + 1) + ". " + withdrawals.toString());
        }

        System.out.print("Enter the number of the withdrawals you want to approve/reject (0 to cancel): ");
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

        if (choice < 0 || choice > withdrawals.size()) {
            System.out.println("Invalid application number.");
            return;
        }

        BTOApplication selected = withdrawals.get(choice -1);
        System.out.println("\nSelected withdrawals:");
        System.out.println(selected);
        
        System.out.print("Would you like to (A)pprove or (R)eject this withdrawals? ");
        String action = sc.nextLine().trim().toLowerCase();

        switch (action) {
            case "a" -> {
                selected.withdraw();
            }
            case "r" -> {
                selected.rejectWithdraw();
            }
            default -> System.out.println("Invalid choice. Operation cancelled.");
        }
    }


    /**
     * Displays all enquiries submitted across all projects in the system,
     * regardless of project ownership or visibility.
     */
    public void viewAllEnquiries() {
        // Show enquiries across all projects
        System.out.println("=== List of All Enquiries ===");
        for (BTOProject project : projectRepo.getProjects()){
            for (Enquiry enquiry : project.getEnquiries()){
                System.out.println(enquiry.toString());
            }
        }
    }


    /**
     * Allows the manager to reply to pending enquiries related to their own projects.
     * Only unreplied enquiries can be selected and responded to.
     *
     * @param manager The manager replying to their project's enquiries.
     */
    public void replyToEnquiries(HDBManager manager) {
        // Allow manager to reply only to enquiries on their own projects
        List<BTOProject> projects = manager.getprojects();
        List<Enquiry> enquiries = new ArrayList<>();

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


    /**
     * Generates and displays reports related to applicant bookings.
     * The manager can choose to apply filters such as marital status or flat type.
     *
     * @param manager The manager generating and filtering reports.
     */
    public void generateReports(HDBManager manager) {
        // Generate and display applicant reports (e.g. filter by marital status, flat type, etc.)

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


    /**
     * Allows the manager to update their password by invoking the password utility.
     *
     * @param manager The manager whose password will be changed.
     */
    public void changePassword(HDBManager manager) {
        PasswordChanger.updatePassword(manager);
    }
}