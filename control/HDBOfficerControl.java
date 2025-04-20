package control;


import enums.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import model.*;
import repo.*;
import util.*;

public class HDBOfficerControl {

    private static ApplicantRepo applicantRepo;
    private static BTOProjectRepo projectRepo;
    private static HDBOfficerRepo hdbOfficerRepo;

    private static final Scanner sc = new Scanner(System.in);

    /** ENSURE TO CALL FROM MAIN */
    public static void init(ApplicantRepo aRepo, BTOProjectRepo pRepo, HDBOfficerRepo officerRepo) {
        applicantRepo = aRepo;
        projectRepo = pRepo;
        hdbOfficerRepo = officerRepo;
    }

    // 1. View Eligible Projects (as Applicant)
    public static void viewEligibleProjects(HDBOfficer officer) {
        List<BTOProject> allProjects = projectRepo.getProjects();
        boolean found = false;

        System.out.println("\n== Eligible BTO Projects ==");

        for (BTOProject project : allProjects) {
            if (!project.isVisible()) continue;

            if (officer.isHandlingProject() && project.getName().equalsIgnoreCase(officer.getAssignedProjectName())){
                continue;
            }
            

            boolean isMarried = officer.isMarried();
            int age = officer.getAge();

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

    // 2. Apply for a Project (not handling)
    public static void handleSubmitApplication(HDBOfficer officer) {
        // Allow officer to apply for a project they are not managing
        // Prevent re-application or application to project they registered for
        Scanner sc = new Scanner(System.in);

        // 1. Check if user already has an application
        // System.out.println("CHECK NULL"); //comment
        // System.out.println(applicant.getApplication()); //comment
        if (officer.getApplication() != null) {
            ApplicationStatus currentStatus = officer.getApplication().getApplicationStatus();
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

        // 3. Prevent applying to a project the officer is managing
        if (officer.isHandlingProject() &&
            project.getName().equalsIgnoreCase(officer.getAssignedProjectName())) {
            System.out.println("You are currently handling this project and cannot apply to it.");
            return;
        }

        // 4. Check project visibility
        if (!project.isVisible()) {
            System.out.println("This project is not currently open for applications.");
            return;
        }

        // 4. Check eligibility and list only allowed flat types
        boolean isMarried = officer.isMarried();
        int age = officer.getAge();
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
        sc.nextLine(); // consume newline

        if (choice < 1 || choice > eligibleTypes.size()) {
            System.out.println("Invalid choice.");
            return;
        }

        FlatType selectedFlatType = eligibleTypes.get(choice - 1);

        // 6. All checks passed — Create application
        BTOApplication application = new BTOApplication(officer, project, selectedFlatType);
        officer.setApplication(application);
        project.addApplication(application);

        System.out.println("Application submitted successfully!");
    }

    // ──────────────────────────────────────────────────────────────
    // 3. View Current Application
    // ──────────────────────────────────────────────────────────────
    public static void viewApplication(HDBOfficer officer) {
        BTOApplication app = officer.getApplication();
        if (app == null) {
            System.out.println("You have not applied for any project yet.");
            return;
        }

        System.out.println("=== My BTO Application Summary ===");
        System.out.println("Project Name       : " + app.getProject().getName());
        System.out.println("Neighborhood       : " + app.getProject().getNeighborhood());
        System.out.println("Flat Type Applied  : " + app.getFlatType());
        System.out.println("Application Status : " + app.getApplicationStatus());

        // Optional: If flat is booked, show booking date
        if (app.getApplicationStatus() == ApplicationStatus.BOOKED && app.getFlatBooking() != null) {
            System.out.println("Flat booked on  : " + app.getFlatBooking().getBookingDate());
        }

        System.out.println("==================================");
        // applicantRepo.saveToCSV("data/ApplicantList.csv");
        // projectRepo.saveToCSV("data/ProjectList.csv");
    }

    // ──────────────────────────────────────────────────────────────
    // 4. Withdraw Application
    // ──────────────────────────────────────────────────────────────
    public static void handleWithdrawApplication(HDBOfficer officer) {
        Scanner sc = new Scanner(System.in);
        BTOApplication app = officer.getApplication();

        if (app == null) {
            System.out.println("You have not applied for any project yet.");
            return;
        }

        if (app.getApplicationStatus() == ApplicationStatus.WITHDRAWN) {
            System.out.println("You have already submitted a withdrawal request.");
            return;
        }
        
        System.out.print("Are you sure you want to request withdrawal? (yes/no): ");
        String confirm = sc.nextLine().trim().toLowerCase();

        if (confirm.equals("yes") || confirm.equals("y")) {
            officer.withdrawApplication();


            System.out.println("Withdrawal request submitted.");
        } else {
            System.out.println("Withdrawal cancelled.");
        }
    }

    // ──────────────────────────────────────────────────────────────
    // 5. Submit Enquiry
    // ──────────────────────────────────────────────────────────────
    public static void handleSubmitEnquiry(HDBOfficer officer) {
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
        
        Enquiry enquiry = new Enquiry(officer, selectedProject, message);
        officer.addEnquiry(enquiry);
        selectedProject.addEnquiry(enquiry);

}


    // ──────────────────────────────────────────────────────────────
    // 6. View / Edit / Delete Enquiries
    // ──────────────────────────────────────────────────────────────
    public static void handleEditEnquiry(HDBOfficer officer) {
        Scanner sc = new Scanner(System.in);

        List<Enquiry> enquiries = officer.getEnquiries();
        if (enquiries.isEmpty()) {
            System.out.println("You have no enquiries to manage.");
            return;
        }

        // Display all enquiries with indexes
        officer.viewEnquiries();

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
                    officer.editEnquiry(choice, newMessage);
                }
            }

            case "d" -> {
                if (selected.isReplied()) {
                    System.out.println("This enquiry has already been replied to and cannot be deleted.");
                } else {
                    officer.deleteEnquiry(choice);
                }
            }

            default -> System.out.println("Invalid choice. Operation cancelled.");
        }

    }

    // ──────────────────────────────────────────────────────────────
    // 7. Register to Handle Project
    // ──────────────────────────────────────────────────────────────
    public static void handleSubmitRegistration(HDBOfficer officer) {
        // 1. Already handling a project?
        if (officer.isHandlingProject()) {
            System.out.println("You are already handling a project: " + officer.getProject().getName());
            return;
        }

        // 2. Ask for project name first
        System.out.print("Enter the name of the project you wish to register as an officer for: ");
        String projectName = sc.nextLine().trim();
        BTOProject project = projectRepo.getProject(projectName);

        if (project == null) {
            System.out.println("Project not found.");
            return;
        }

        // 3. Check if already applied to this specific project
        if (officer.getApplication() != null &&
            officer.getApplication().getProject().getName().equalsIgnoreCase(projectName)) {
            System.out.println("You have already applied for this project and cannot register as an officer for it.");
            return;
        }

        // 4. Check if already registered for this project
        OfficerRegistration existing = officer.getRegistration();
        if (existing != null &&
            existing.getProject().getName().equalsIgnoreCase(projectName)) {
            System.out.println("You have already registered for this project.");
            return;
        }

        // 5. Check for overlapping application periods
        for (HDBOfficer officerInRepo : hdbOfficerRepo.getAllOfficers()) {
            OfficerRegistration reg = officerInRepo.getRegistration();
            if (reg == null) continue;

            if (officerInRepo.getNric().equals(officer.getNric()) &&
                reg.getStatus() != RegistrationStatus.REJECTED &&
                datesOverlap(project.getAppOpenDate(), project.getAppCloseDate(),
                            reg.getProject().getAppOpenDate(), reg.getProject().getAppCloseDate())) {

                System.out.println("You already have an officer registration that overlaps with this project's application period.");
                return;
            }
        }

        // 6. All checks passed – submit registration
        OfficerRegistration newReg = new OfficerRegistration(officer, project);
        officer.setOfficerRegistrationToHDBOfficer(newReg);
        // officer.setAssignedProject(project);
        project.addRegistration(newReg);

        System.out.println("Officer registration submitted successfully. Awaiting manager approval.");
    }

    // ──────────────────────────────────────────────────────────────
    // 8 - View the status of the registration 
    // ──────────────────────────────────────────────────────────────
    public static void handleViewRegistrationStatus(HDBOfficer officer) {
        OfficerRegistration registration = officer.getRegistration();

        if (registration == null) {
            System.out.println("You have not submitted any officer registration yet.");
            return;
        }

        System.out.println("=== Officer Registration Status ===");
        System.out.println("Project Name       : " + registration.getProject().getName());
        System.out.println("Neighborhood       : " + registration.getProject().getNeighborhood());
        System.out.println("Application Period : " +
            registration.getProject().getAppOpenDate() + " to " +
            registration.getProject().getAppCloseDate());
        System.out.println("Current Status     : " + registration.getStatus());
    }
    
    // ──────────────────────────────────────────────────────────────
    // 9. View Project I’m Handling (even if not visible)
    // ──────────────────────────────────────────────────────────────
    public static void viewProjectHandling(HDBOfficer officer) {
        if (!officer.isHandlingProject()) {
            System.out.println("You are not currently handling any project.");
            return;
        }

        BTOProject project = officer.getProject();

        System.out.println("=== Project You're Handling ===");
        System.out.println("Project Name       : " + project.getName());
        System.out.println("Neighborhood       : " + project.getNeighborhood());
        System.out.println("Application Period : " + project.getAppOpenDate() + " to " + project.getAppCloseDate());
        System.out.println("Project Visibility : " + (project.isVisible() ? "Visible to public" : "Hidden (not public)"));
        System.out.println("Available Flats:");
        System.out.println("  • 2-Room: " + project.getTwoRoomNo() + " units ($" + project.getTwoRoomPrice() + ")");
        System.out.println("  • 3-Room: " + project.getThreeRoomNo() + " units ($" + project.getThreeRoomPrice() + ")");
        System.out.println("===================================");
    }

    // ──────────────────────────────────────────────────────────────
    // 10. View/Reply to Enquiries (My Project Only)
    // ──────────────────────────────────────────────────────────────
    public static void viewAndReplyEnquiries(HDBOfficer officer) {
        if (!officer.isHandlingProject()) {
            System.out.println("You are not assigned to any project.");
            return;
        }

        BTOProject project = officer.getProject();
        List<Enquiry> enquiries = project.getEnquiries();

        if (enquiries.isEmpty()) {
            System.out.println("📭 No enquiries have been submitted for this project.");
            return;
        }

        System.out.println("=== Enquiries for Project: " + project.getName() + " ===");

        // Show all enquiries
        for (int i = 0; i < enquiries.size(); i++) {
            Enquiry e = enquiries.get(i);
            System.out.println("\n[" + (i + 1) + "]");
            System.out.println("From     : " + e.getApplicant().getName());
            System.out.println("Message  : " + e.getMessage());
            System.out.println("Status   : " + (e.isReplied() ? "Replied" : "Not Replied"));
            if (e.isReplied()) {
                System.out.println("Reply    : " + e.getReply());
            }
        }

        System.out.print("\nEnter the enquiry number to reply to (0 to cancel): ");
        int choice;
        try {
            choice = Integer.parseInt(sc.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Invalid input.");
            return;
        }

        if (choice == 0) {
            System.out.println("Operation cancelled.");
            return;
        }

        if (choice < 1 || choice > enquiries.size()) {
            System.out.println("Invalid enquiry number.");
            return;
        }

        Enquiry selected = enquiries.get(choice - 1);

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

        officer.replyToEnquiry(selected, reply); // Handles logic and confirmation
    }

    // ──────────────────────────────────────────────────────────────
    // 11. Book Flat for Applicant
    // ──────────────────────────────────────────────────────────────
    public static void handleFlatBooking(HDBOfficer user) {
        if (!user.isHandlingProject()) {
            System.out.println("You are not currently assigned to any project.");
            return;
        }

        System.out.print("Enter the NRIC of the applicant: ");
        String nric = sc.nextLine().trim();
        Applicant applicant = applicantRepo.getUser(nric);

        if (applicant == null) {
            System.out.println("Applicant not found.");
            return;
        }

        BTOApplication app = applicant.getApplication();

        if (app == null) {
            System.out.println("This applicant has not applied for any project.");
            return;
        }

        // Ensure the officer is handling the same project the applicant applied for
        if (!app.getProject().getName().equalsIgnoreCase(user.getProject().getName())) {
            System.out.println("This applicant did not apply to your assigned project.");
            return;
        }

        if (app.getApplicationStatus() != ApplicationStatus.SUCCESSFUL) {
            System.out.println("The applicant’s application is not marked as SUCCESSFUL.");
            return;
        }

        FlatType type = app.getFlatType();

        if (!user.getProject().hasFlatAvailable(type)) {
            System.out.println("No more flats available for the selected flat type.");
            return;
        }

        // Proceed with booking
        user.bookFlat(applicant);  // call the alreadu existing method
    }

    // ──────────────────────────────────────────────────────────────
    // 12 - Generate Boooking Reciept
    // ──────────────────────────────────────────────────────────────
    public static void generateBookingReceipt(HDBOfficer user) {
        if (!user.isHandlingProject()) {
            System.out.println("You are not currently assigned to any project.");
            return;
        }

        System.out.print("Enter the NRIC of the applicant: ");
        String nric = sc.nextLine().trim();
        Applicant applicant = applicantRepo.getUser(nric);

        if (applicant == null) {
            System.out.println("Applicant not found.");
            return;
        }

        BTOApplication app = applicant.getApplication();

        if (app == null) {
            System.out.println("This applicant has not applied for any project.");
            return;
        }

        if (!app.getProject().getName().equalsIgnoreCase(user.getProject().getName())) {
            System.out.println("This applicant did not apply to your assigned project.");
            return;
        }

        if (app.getApplicationStatus() != ApplicationStatus.BOOKED) {
            System.out.println("This applicant has not booked a flat yet.");
            return;
        }

        // Everything is valid — print the receipt
        user.generateBookingReceipt(applicant);
    }

    // ──────────────────────────────────────────────────────────────
    // 13. Update Password 
    // ──────────────────────────────────────────────────────────────
    public static void updatePassword(HDBOfficer user) {
        PasswordChanger.updatePassword(user);
    }

    private static boolean datesOverlap(LocalDate start1, LocalDate end1, LocalDate start2, LocalDate end2) {
        return !(end1.isBefore(start2) || end2.isBefore(start1));
    }
}