package control;


import enums.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import model.*;
import repo.*;

/**
 * Control class for handling all business logic specific to HDB Officers.
 * Extends {@code ApplicantControl} to reuse common applicant functionality,
 * while adding features related to project registration, enquiry management,
 * and flat booking for officers.
 */
public class HDBOfficerControl extends ApplicantControl{

    /** Repository containing all registered applicants. Used for cross-referencing and data access. */
    private ApplicantRepo applicantRepo;

    /** Repository managing all BTO project data accessible to officers. */
    private BTOProjectRepo projectRepo;

    /** Repository for managing officer-specific data and persistence. */
    private HDBOfficerRepo hdbOfficerRepo;

    /** Scanner for reading user input in officer-specific workflows. */
    private final Scanner sc = new Scanner(System.in);

    /**
     * Initializes the officer control with access to the applicant, project, and officer repositories.
     * This method must be called from the main application before using any control functionality.
     *
     * @param aRepo       The applicant repository to be used.
     * @param pRepo       The BTO project repository to be used.
     * @param officerRepo The officer repository to be used.
     */
    public void init(ApplicantRepo aRepo, BTOProjectRepo pRepo, HDBOfficerRepo officerRepo) {
        applicantRepo = aRepo;
        projectRepo = pRepo;
        hdbOfficerRepo = officerRepo;
    }


    /**
     * Displays a list of BTO projects the officer is eligible to apply for.
     * Filters out projects the officer is already managing and only shows visible ones.
     * Eligibility is based on marital status and age.
     *
     * @param officer The HDB officer viewing eligible projects.
     */
    public void viewEligibleProjects(HDBOfficer officer) {
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


    /**
     * Handles the process of allowing an HDB officer to apply for a BTO project,
     * ensuring they do not apply to projects they are already managing and that
     * they meet eligibility criteria based on age and marital status.
     *
     * Includes the following checks:
     * - Officer has not already submitted a valid application
     * - The selected project exists, is visible, and is currently active
     * - Officer is not applying to a project they are managing
     * - Officer meets age/marital requirements for flat types
     * - Officer selects a valid flat type for application
     *
     * @param officer The HDB officer submitting the application.
     */
    public void handleSubmitApplication(HDBOfficer officer) {
        // Allow officer to apply for a project they are not managing
        // Prevent re-application or application to project they registered for

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
        if (!project.isVisible() || !project.isActive()) {//not visible or not active
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
        sc.nextLine();

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
    // 4. Withdraw Application
    // 5. Submit Enquiry
    // 6. View / Edit / Delete Enquiries
    // ──────────────────────────────────────────────────────────────
    // avaliable in super class


    /**
     * Handles the officer's registration to manage a BTO project.
     *
     * Performs several validations:
     * - Officer is not already handling another project
     * - Project exists
     * - Officer has not applied to or already registered for the same project
     * - No existing registration overlaps with the new project's application period
     *
     * If all checks pass, a new officer registration is submitted for approval.
     *
     * @param officer The HDB officer attempting to register for a project.
     */
    public void handleSubmitRegistration(HDBOfficer officer) {
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

        OfficerRegistration reg = officer.getRegistration();
        if (reg != null && reg.getStatus() != RegistrationStatus.REJECTED && 
            datesOverlap(project.getAppOpenDate(), project.getAppCloseDate(),
                reg.getProject().getAppOpenDate(), reg.getProject().getAppCloseDate())) {

            System.out.println("You already have an officer registration that overlaps with this project's application period.");
            return;
        }

        // 6. All checks passed – submit registration
        OfficerRegistration newReg = new OfficerRegistration(officer, project);
        officer.setOfficerRegistrationToHDBOfficer(newReg);
        // officer.setAssignedProject(project);
        project.addRegistration(newReg);

        System.out.println("Officer registration submitted successfully. Awaiting manager approval.");
    }


    /**
     * Displays the status of the officer's current registration, if one exists.
     * Shows the project name, neighborhood, application period, and current approval status.
     *
     * @param officer The officer viewing their registration status.
     */
    public void handleViewRegistrationStatus(HDBOfficer officer) {
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
    

    /**
     * Displays the full details of the project currently assigned to the officer.
     * This includes name, neighborhood, application period, visibility,
     * and available flat types with pricing.
     *
     * If the officer is not currently assigned to any project, a message is shown.
     *
     * @param officer The officer viewing the project they are managing.
     */
    public void viewProjectHandling(HDBOfficer officer) {
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


    /**
     * Displays all enquiries submitted for the project the officer is managing,
     * and allows the officer to reply to one that hasn't been responded to yet.
     *
     * Validates officer assignment, enquiry selection, and reply input.
     *
     * @param officer The officer replying to enquiries.
     */
    public void viewAndReplyEnquiries(HDBOfficer officer) {
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


    /**
     * Handles flat booking for an applicant, ensuring the officer is assigned to the
     * same project the applicant applied for, and that the application status is successful.
     *
     * Also checks if flats are still available before proceeding with the booking.
     *
     * @param user The officer performing the flat booking.
     */
    public void handleFlatBooking(HDBOfficer user) {
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


    /**
     * Generates and displays a booking receipt for a successfully booked applicant.
     *
     * Validates that the officer is handling the relevant project and that
     * the applicant has a booked application for that project.
     *
     * @param user The officer generating the receipt.
     */
    public void generateBookingReceipt(HDBOfficer user) {
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

    /**
     * Utility method to check if two date ranges overlap.
     * Used to ensure no conflicting officer registrations across projects.
     *
     * @param start1 Start date of the first range.
     * @param end1   End date of the first range.
     * @param start2 Start date of the second range.
     * @param end2   End date of the second range.
     * @return True if the ranges overlap, false otherwise.
     */
    private boolean datesOverlap(LocalDate start1, LocalDate end1, LocalDate start2, LocalDate end2) {
        return !(end1.isBefore(start2) || end2.isBefore(start1));
    }
}