package boundary;

import control.HDBManagerControl;
import java.util.Scanner;
import model.HDBManager;
import repo.BTOProjectRepo;

/**
 * The {@code HDBManagerBoundary} class provides the user interface for HDB managers.
 * It allows managers to create, edit, and manage projects, view and handle applications,
 * manage officer registrations, reply to enquiries, generate reports, and update passwords.
 */
public class HDBManagerBoundary {

    /** Scanner object for reading user input from the console. */
    private final Scanner sc = new Scanner(System.in);

    /** Controller that manages the logic and operations for the HDB manager. */
    private HDBManagerControl controller = new HDBManagerControl();

    /**
     * Constructs a new {@code HDBManagerBoundary} and initializes the controller
     * with the provided BTO project repository.
     *
     * @param projectRepo The repository containing BTO project data to be managed.
     */
    public HDBManagerBoundary(BTOProjectRepo projectRepo){
        this.controller.init(projectRepo);
    }

    /**
     * Displays the main menu for an HDB manager and handles the selection of options.
     * This method loops continuously until the user chooses to log out.
     *
     * @param user The {@code HDBManager} who is currently logged in.
     */
    public void showManagerMenu(HDBManager user) {
        int choice = -1;
        System.out.println("\nWelcome, " + user.getName() + "! (HDB Manager)");

        while (true) {
            System.out.println("\n== Manager Menu ==");
            System.out.println("1. Create New Project");
            System.out.println("2. Edit My Project");
            System.out.println("3. Delete My Project");
            System.out.println("4. Toggle Project Visibility");
            System.out.println("5. View All Projects");
            System.out.println("6. View My Projects Only");
            System.out.println("7. View Officer Registrations");
            System.out.println("8. Approve/Reject Officer Registration");
            System.out.println("9. View Applications to My Project");
            System.out.println("10. Approve/Reject Application");
            System.out.println("11. Process Withdrawal Requests");
            System.out.println("12. View All Enquiries");
            System.out.println("13. Reply to Enquiries (My Project Only)");
            System.out.println("14. Generate Reports (with Filters)");
            System.out.println("15. Change Passsword");
            System.out.println("16. Logout");
            System.out.print("\nEnter your choice: ");

            if (sc.hasNextInt()) {
                choice = sc.nextInt();
                sc.nextLine(); // consume newline

                switch (choice) {
                    case 1 -> controller.handleCreateProject(user);
                    case 2 -> controller.handleEditProject(user);
                    case 3 -> controller.handleDeleteProject(user);
                    case 4 -> controller.toggleProjectVisibility(user);
                    case 5 -> controller.viewAllProjects();
                    case 6 -> controller.viewMyProjects(user);
                    case 7 -> controller.viewOfficerRegistrations(user);
                    case 8 -> controller.handleUpdateRegistration(user);
                    case 9 -> controller.viewApplications(user);
                    case 10 -> controller.handleUpdateApplication(user);
                    case 11 -> controller.handleProcessWithdrawal(user);
                    case 12 -> controller.viewAllEnquiries();
                    case 13 -> controller.replyToEnquiries(user);
                    case 14 -> controller.generateReports(user);
                    case 15 -> {
                        controller.changePassword(user);
                        return;
                    }
                    case 16 -> {
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