package control;

import enums.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import model.*;
import repo.BTOProjectRepo;
import util.*;

/**
 * Control class that contains all the business logic for Applicant‑related
 * actions. Boundary classes handle input/output; this class does the work.
 * 
 * Responsibilities include handling application submission, withdrawal,
 * enquiry management, and other applicant-specific actions.
 */
public class ApplicantControl {

    /** Repository containing all BTO project data. Used for filtering and lookup. */
    private BTOProjectRepo projectRepo;

    /** Scanner object for receiving user input. Shared across control methods. */
    private final Scanner sc = new Scanner(System.in);

    /**
     * Initializes the control class by linking it to a BTO project repository.
     * This method should be called from the main application entry point.
     *
     * @param pRepo The BTO project repository to bind to this control class.
     */
    public void init(BTOProjectRepo pRepo) {
        projectRepo = pRepo;
    }

    /**
    * Displays a list of BTO projects the given applicant is eligible to apply for.
    *
    * Eligibility is based on marital status and age:
    * - Married applicants must be at least 21 years old
    * - Unmarried applicants must be at least 35 years old
    *
    * The method filters out invisible projects and prints the names,
    * available flat types, prices, and application periods of eligible ones.
    *
    * @param applicant The applicant whose eligibility is being evaluated.
    */
    public void viewEligibleProjects(Applicant applicant) {
        List<BTOProject> allProjects = projectRepo.getProjects();
        boolean found = false;

        System.out.println("\n== Eligible BTO Projects ==");

        for (BTOProject project : allProjects) {
            if (!project.isVisible()) continue;

            boolean isMarried = applicant.isMarried();
            int age = applicant.getAge();

            // Eligibility check
            if ((isMarried && age >= 21) || (!isMarried && age >= 35)) {
                found = true;
                System.out.println("- " + project.getName() + " (" + project.getNeighborhood() + ")");

                // Show flats based on rules
                if (isMarried) {
                    System.out.println("  • 2-Room: " + project.getTwoRoomNo() + " units ($" + project.getTwoRoomPrice() + ")");
                    System.out.println("  • 3-Room: " + project.getThreeRoomNo() + " units ($" + project.getThreeRoomPrice() + ")");
                } else {
                    System.out.println("  • 2-Room: " + project.getTwoRoomNo() + " units ($" + project.getTwoRoomPrice() + ")");
                }

                System.out.println("  • Application Period: " + project.getAppOpenDate() + " to " + project.getAppCloseDate());
                System.out.println();
            }
        }

        if (!found) {
            System.out.println("No eligible projects available at the moment.");
        }
    }



    /**
    * Handles the full flow of submitting a BTO application for the given applicant.
    *
    * This includes:
    * - Checking if the applicant already has an active application
    * - Prompting for a project name and validating its existence and visibility
    * - Evaluating the applicant's eligibility based on marital status and age
    * - Displaying eligible flat types and processing the applicant's choice
    * - Creating and saving the application if all conditions are met
    *
    * @param applicant The applicant attempting to submit a new application.
    */
    public void handleSubmitApplication(Applicant applicant) {
        // 1. Check if user already has an application
        if (applicant.getApplication() != null) {
            ApplicationStatus currentStatus = applicant.getApplication().getApplicationStatus();
            if (currentStatus ==  ApplicationStatus.PENDING  || currentStatus == ApplicationStatus.SUCCESSFUL || currentStatus == ApplicationStatus.BOOKED) {
                System.out.println("You have already applied for a project. Cannot apply again.");
                return;
            }
        }
        // 2. Ask for project name
        System.out.print("Enter the name of the project you wish to apply for: ");
        String projectName = sc.nextLine().trim();
        BTOProject project = projectRepo.getProject(projectName);

        if (project == null) {
            System.out.println("Project not found.");
            return;
        }

        // 3. Check project visibility
        if (!project.isVisible() || !project.isActive()) {//not visible or not active
            System.out.println("This project is not currently open for applications.");
            return;
        }

        // 4. Check eligibility and list only allowed flat types
        boolean isMarried = applicant.isMarried();
        int age = applicant.getAge();
        List<FlatType> eligibleTypes = new ArrayList<>();

        if (isMarried && age >= 21) {
            if (project.getTwoRoomNo() > 0) eligibleTypes.add(FlatType.TWOROOM);
            if (project.getThreeRoomNo() > 0) eligibleTypes.add(FlatType.THREEROOM);
        } else if (!isMarried && age >= 35) {
            if (project.getTwoRoomNo() > 0) eligibleTypes.add(FlatType.TWOROOM);
        } else {
            System.out.println("You do not meet the age or marital requirements to apply.");
            return;
        }

        if (eligibleTypes.isEmpty()) {
            System.out.println("No eligible flat types available in this project.");
            return;
        }

        // 5. Let user choose from eligible types
        System.out.println("Available Flat Types:");
        for (int i = 0; i < eligibleTypes.size(); i++) {
            FlatType ft = eligibleTypes.get(i);
            String display = ft == FlatType.TWOROOM ?
                    "2-Room ($" + project.getTwoRoomPrice() + ")" :
                    "3-Room ($" + project.getThreeRoomPrice() + ")";
            System.out.println("  " + (i + 1) + ". " + display);
        }

        System.out.print("Enter your flat type choice (1 to " + eligibleTypes.size() + "): ");
        int choice = sc.nextInt();
        sc.nextLine(); 

        if (choice < 1 || choice > eligibleTypes.size()) {
            System.out.println("Invalid choice.");
            return;
        }

        FlatType selectedFlatType = eligibleTypes.get(choice - 1);

        // 6. All checks passed — Create application
        BTOApplication application = new BTOApplication(applicant, project, selectedFlatType);
        applicant.setApplication(application);
        project.addApplication(application);

        System.out.println("Application submitted successfully!");
    }


    /**
     * Displays a summary of the applicant's current BTO application.
     *
     * If the applicant has no application, a message is shown.
     * Otherwise, the method prints the project name, neighborhood,
     * flat type applied for, application status, and booking date
     * if the application has been marked as booked.
     *
     * @param applicant The applicant whose application is to be viewed.
     */
    public void viewApplication(Applicant applicant) {
        BTOApplication app = applicant.getApplication();
        if (app == null) {
            System.out.println("You have not applied for any project yet.");
            return;
        }

        System.out.println("=== My BTO Application Summary ===");
        System.out.println("Project Name       : " + app.getProject().getName());
        System.out.println("Neighborhood       : " + app.getProject().getNeighborhood());
        System.out.println("Flat Type Applied  : " + app.getFlatType());
        System.out.println("Application Status : " + app.getApplicationStatus());

        if (app.getApplicationStatus() == ApplicationStatus.BOOKED && app.getFlatBooking() != null) {
            System.out.println("Flat booked on  : " + app.getFlatBooking().getBookingDate());
        }

        System.out.println("==================================");
    }

    /**
     * Allows the applicant to submit a withdrawal request for their current BTO application.
     *
     * The method performs the following checks:
     * - If the applicant has not applied for any project
     * - If the application is already marked as withdrawn
     * - If a withdrawal request has already been submitted
     *
     * If none of these conditions block the process, the user is prompted to confirm
     * the withdrawal request. If confirmed, the request is submitted.
     *
     * @param applicant The applicant attempting to withdraw their application.
     */
    public void handleWithdrawApplication(Applicant applicant) {
        BTOApplication app = applicant.getApplication();

        if (app == null) {
            System.out.println("You have not applied for any project yet.");
            return;
        }

        if (app.getApplicationStatus() == ApplicationStatus.WITHDRAWN){
            System.out.println("You have already withdrawn.");
            return;
        }

        if (app.hasRequestedWithdraw()) {
            System.out.println("You have already submitted a withdrawal request.");
            return;
        }
        
        System.out.print("Are you sure you want to request withdrawal? (yes/no): ");
        String confirm = sc.nextLine().trim().toLowerCase();

        if (confirm.equals("yes") || confirm.equals("y")) {
            app.withdraw();
        } else {
            System.out.println("Withdrawal cancelled.");
        }
    }


    /**
     * Handles the process of submitting an enquiry about a visible BTO project.
     *
     * The method displays a list of all visible projects, prompts the applicant to select one,
     * and allows them to enter an enquiry message. If the input is valid, a new enquiry is created
     * and added to both the applicant and the project.
     *
     * Validation includes:
     * - Checking for available visible projects
     * - Validating project selection input
     * - Ensuring the enquiry message is not empty
     *
     * @param applicant The applicant submitting the enquiry.
     */
    public void handleSubmitEnquiry(Applicant applicant) {
        List<BTOProject> visibleProjects;
        visibleProjects = projectRepo.getProjects().stream()
                .filter(BTOProject::isVisible)
                .collect(Collectors.toList());

        if (visibleProjects.isEmpty()) {
            System.out.println("No visible projects available to submit enquiries.");
            return;
        }

        System.out.println("=== Available Projects ===");
        for (int i = 0; i < visibleProjects.size(); i++) {
            System.out.println((i + 1) + ". " + visibleProjects.get(i).getName());
        }

        System.out.print("Enter the number of the project you want to enquire about: ");
        int choice;
        try {
            choice = Integer.parseInt(sc.nextLine().trim()) - 1;
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a number.");
            return;
        }

        if (choice < 0 || choice >= visibleProjects.size()) {
            System.out.println("Invalid project choice.");
            return;
        }

        BTOProject selectedProject = visibleProjects.get(choice);

        System.out.print("Enter your enquiry message: ");
        String message = sc.nextLine().trim();

        if (message.isEmpty()) {
            System.out.println("Enquiry message cannot be empty.");
            return;
        }

        Enquiry enquiry = new Enquiry(applicant, selectedProject, message);
        applicant.addEnquiry(enquiry);
        selectedProject.addEnquiry(enquiry);

}

    /**
     * Allows the applicant to view, edit, or delete their submitted enquiries.
     *
     * The method displays all current enquiries and prompts the applicant to choose one.
     * Based on their selection, they can either:
     * - Edit the enquiry message (if it hasn't been replied to)
     * - Delete the enquiry (if it hasn't been replied to)
     *
     * Validation includes:
     * - Ensuring the applicant has at least one enquiry
     * - Validating the selected index
     * - Preventing modifications to replied enquiries
     *
     * @param applicant The applicant managing their enquiries.
     */
    public void handleEditEnquiry(Applicant applicant) {
        List<Enquiry> enquiries = applicant.getEnquiries();
        if (enquiries.isEmpty()) {
            System.out.println("You have no enquiries to manage.");
            return;
        }

        // Display all enquiries with indexes
        applicant.viewEnquiries();

        System.out.print("Enter the number of the enquiry you want to edit/delete (0 to cancel): ");
        int choice;
        try {
            choice = Integer.parseInt(sc.nextLine()) - 1;
        } catch (NumberFormatException e) {
            System.out.println("Invalid input.");
            return;
        }

        if (choice == -1) {
            System.out.println("Operation cancelled.");
            return;
        }

        if (choice < 0 || choice >= enquiries.size()) {
            System.out.println("Invalid enquiry number.");
            return;
        }

        Enquiry selected = enquiries.get(choice);
        System.out.println("\nSelected Enquiry:");
        System.out.println(selected);

        System.out.print("Would you like to (E)dit or (D)elete this enquiry? ");
        String action = sc.nextLine().trim().toLowerCase();

        switch (action) {
            case "e" -> {
                if (selected.isReplied()) {
                    System.out.println("This enquiry has already been replied to and cannot be edited.");
                } else {
                    System.out.print("Enter new message: ");
                    String newMessage = sc.nextLine().trim();
                    applicant.editEnquiry(choice, newMessage);
                }
            }

            case "d" -> {
                if (selected.isReplied()) {
                    System.out.println("This enquiry has already been replied to and cannot be deleted.");
                } else {
                    applicant.deleteEnquiry(choice);
                }
            }

            default -> System.out.println("Invalid choice. Operation cancelled.");
        }

    }

    /**
     * Invokes the password update flow for the given applicant.
     *
     * Delegates the password changing logic to the PasswordChanger utility class.
     *
     * @param applicant The applicant whose password will be updated.
     */
    public void updatePassword(Applicant applicant) {
        PasswordChanger.updatePassword(applicant);
    }

    // //UNUSED?
    // private FlatType promptFlatType() {
    //     System.out.print("Choose flat type (1 = 2‑Room, 2 = 3‑Room): ");
    //     int choice = sc.nextInt(); sc.nextLine();
    //     return (choice == 1) ? FlatType.TWOROOM : FlatType.THREEROOM;
    // }
}