
// UNCOMMENT EVETYHING - SIBI


package boundary;

import control.HDBManagerControl;
import java.util.Scanner;
import model.HDBManager;


public class HDBManagerBoundary {

    private static final Scanner sc = new Scanner(System.in);

    public static void showManagerMenu(HDBManager user) {
        int choice = -1;
        System.out.println("\nWelcome, " + user.getName() + "! (HDB Manager)");

        while (choice != 15) {
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
                    case 1:
                        HDBManagerControl.handleCreateProject(user);
                        break;
                    case 2:
                        HDBManagerControl.handleEditProject(user);
                        break;
                    case 3:
                        HDBManagerControl.handleDeleteProject(user);
                        break;
                    case 4:
                        HDBManagerControl.toggleProjectVisibility(user);
                        break;
                    case 5:
                        HDBManagerControl.viewAllProjects();
                        break;
                    case 6:
                        HDBManagerControl.viewMyProjects(user);
                        break;
                    case 7:
                        HDBManagerControl.viewOfficerRegistrations(user);
                        break;
                    case 8:
                        HDBManagerControl.handleUpdateRegistration(user);
                        break;
                    case 9:
                        HDBManagerControl.viewApplications(user);
                        break;
                    case 10:
                        HDBManagerControl.handleUpdateApplication(user);
                        break;
                    case 11:
                        HDBManagerControl.handleProcessWithdrawal(user);
                        break;
                    case 12:
                        HDBManagerControl.viewAllEnquiries();
                        break;
                    case 13:
                        HDBManagerControl.replyToEnquiries(user);
                        break;
                    case 14:
                        HDBManagerControl.generateReports(user);
                        break;
                    case 15:
                        HDBManagerControl.changePassword(user);
                    case 16:
                        System.out.println("Logging out...");
                        return;
                    default:
                        System.out.println("Invalid choice. Try again.");
                }
            } else {
                System.out.println("Please enter a valid number.");
                sc.nextLine(); 
            }
        }
    }
}