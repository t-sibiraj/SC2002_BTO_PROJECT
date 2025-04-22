// File: control/ApplicantControl.java
package control;

import enums.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import model.*;
import repo.ApplicantRepo;
import repo.BTOProjectRepo;
import util.*;

/**
 *  Control class that contains all the business logic for Applicant‑related
 *  actions.  Boundary classes handle input/output; this class does the work.
 */
public class ApplicantControl {

    private static ApplicantRepo applicantRepo;
    private static BTOProjectRepo projectRepo;

    private static final Scanner sc = new Scanner(System.in);

    /** ENSURE TO CALL FROM MAIN */
    public static void init(ApplicantRepo aRepo, BTOProjectRepo pRepo) {
        applicantRepo = aRepo;
        projectRepo   = pRepo;
    }

    // ──────────────────────────────────────────────────────────────
    // 1. View Eligible Projects
    // ──────────────────────────────────────────────────────────────
    public static void viewEligibleProjects(Applicant applicant) {
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



    // ──────────────────────────────────────────────────────────────
    // 2. Submit Application
    // ──────────────────────────────────────────────────────────────
    public static void handleSubmitApplication(Applicant applicant) {
        Scanner sc = new Scanner(System.in);

        // 1. Check if user already has an application
        // System.out.println("CHECK NULL"); //comment
        // System.out.println(applicant.getApplication()); //comment
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
        if (!project.isVisible()) {
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

    // ──────────────────────────────────────────────────────────────
    // 3. View Current Application
    // ──────────────────────────────────────────────────────────────
    public static void viewApplication(Applicant applicant) {
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

    // ──────────────────────────────────────────────────────────────
    // 4. Withdraw Application
    // ──────────────────────────────────────────────────────────────
    public static void handleWithdrawApplication(Applicant applicant) {
        Scanner sc = new Scanner(System.in);
        BTOApplication app = applicant.getApplication();

        if (app == null) {
            System.out.println("You have not applied for any project yet.");
            return;
        }

        if (app.getApplicationStatus() == ApplicationStatus.WITHDRAWN) {
            System.out.println("You have already submitted a withdrawal request.");
            return;
        }

        if (app.getApplicationStatus() == ApplicationStatus.BOOKED) {
            System.out.println("You have already booked a flat. Withdrawal is no longer allowed.");
            return;
        }
        
        System.out.print("Are you sure you want to request withdrawal? (yes/no): ");
        String confirm = sc.nextLine().trim().toLowerCase();

        if (confirm.equals("yes") || confirm.equals("y")) {
            applicant.withdrawApplication();

            System.out.println("Withdrawal request submitted.");
        } else {
            System.out.println("Withdrawal cancelled.");
        }
    }


    // ──────────────────────────────────────────────────────────────
    // 5. Submit Enquiry
    // ──────────────────────────────────────────────────────────────
    public static void handleSubmitEnquiry(Applicant applicant) {
        Scanner sc = new Scanner(System.in);
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
    // ──────────────────────────────────────────────────────────────
    // 6. View / Edit / Delete Enquiries
    // ──────────────────────────────────────────────────────────────
    public static void handleEditEnquiry(Applicant applicant) {
        Scanner sc = new Scanner(System.in);

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

    public static void updatePassword(Applicant applicant) {
        PasswordChanger.updatePassword(applicant);
    }

    private static FlatType promptFlatType() {
        System.out.print("Choose flat type (1 = 2‑Room, 2 = 3‑Room): ");
        int choice = sc.nextInt(); sc.nextLine();
        return (choice == 1) ? FlatType.TWOROOM : FlatType.THREEROOM;
    }
}