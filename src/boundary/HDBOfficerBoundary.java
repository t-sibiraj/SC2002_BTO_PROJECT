package boundary;

import control.HDBOfficerControl;
import java.util.Scanner;
import model.HDBOfficer;
import repo.ApplicantRepo;
import repo.BTOProjectRepo;
import repo.HDBOfficerRepo;

/**
 * The {@code HDBOfficerBoundary} class handles the user interface for HDB officers.
 * It allows officers to perform both applicant-related tasks and officer-specific functions,
 * such as viewing eligible projects, applying or withdrawing, handling enquiries,
 * registering to manage a project, and managing project-related tasks.
 */
public class HDBOfficerBoundary {

    /** Scanner object for reading user input from the console. */
    private final Scanner sc = new Scanner(System.in);
    /** Controller that manages logic and actions related to HDB officers. */
    private HDBOfficerControl controller = new HDBOfficerControl();

    /**
     * Constructs a new {@code HDBOfficerBoundary} and initializes the officer controller
     * with the necessary repositories for applicants, projects, and officers.
     *
     * @param applicantRepo The repository managing applicant data.
     * @param projectRepo   The repository managing BTO project data.
     * @param officerRepo   The repository managing HDB officer data.
     */
    public HDBOfficerBoundary(ApplicantRepo applicantRepo, BTOProjectRepo projectRepo, HDBOfficerRepo officerRepo){
        this.controller.init(applicantRepo, projectRepo, officerRepo);
    }

    /**
     * Displays the main menu for an HDB officer and handles their selected actions.
     * This method continuously loops until the user chooses to log out.
     *
     * @param user The {@code HDBOfficer} currently logged in.
     */
    public void showOfficerMenu(HDBOfficer user) {
        int choice = -1;
        System.out.println("\nWelcome, " + user.getName() + "! (HDB Officer)");

        while (true) {
            System.out.println("\n== Officer Menu ==");
            System.out.println("1. View Eligible Projects (as Applicant)");
            System.out.println("2. Apply for a Project (not handling)");
            System.out.println("3. View My Application");
            System.out.println("4. Request Withdrawal");
            System.out.println("5. Submit Enquiry");
            System.out.println("6. View/Edit/Delete Enquiries");
            System.out.println("7. Register to Handle Project");
            System.out.println("8. View My Registration Status");
            System.out.println("9. View Project I’m Handling");
            System.out.println("10. View/Reply to Enquiries (My Project Only)");
            System.out.println("11. Book Flat for Applicant");
            System.out.println("12. Generate Booking Receipt");
            System.out.println("13. Update Password");
            System.out.println("14. Logout");
            System.out.print("\nEnter your choice: ");

            if (sc.hasNextInt()) {
                choice = sc.nextInt();
                sc.nextLine(); // consume newline

                switch (choice) {
                    case 1 -> controller.viewEligibleProjects(user);
                    case 2 -> controller.handleSubmitApplication(user);
                    case 3 -> controller.viewApplication(user);
                    case 4 -> controller.handleWithdrawApplication(user);
                    case 5 -> controller.handleSubmitEnquiry(user);
                    case 6 -> controller.handleEditEnquiry(user);
                    case 7 -> controller.handleSubmitRegistration(user);
                    case 8 -> controller.handleViewRegistrationStatus(user);
                    case 9 -> controller.viewProjectHandling(user);
                    case 10 -> controller.viewAndReplyEnquiries(user);
                    case 11 -> controller.handleFlatBooking(user);
                    case 12 -> controller.generateBookingReceipt(user);
                    case 13 -> {
                        controller.updatePassword(user);
                        return;
                    }
                    case 14 -> {
                        System.out.println("Logging out...");
                        return;
                    }
                    default -> System.out.println("Invalid choice. Try again.");
                }
            } else {
                System.out.println("Please enter a valid number.");
                sc.nextLine();
            }
        }
    }
}