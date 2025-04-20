package boundary;

import control.HDBOfficerControl;
import java.util.Scanner;
import model.HDBOfficer;

public class HDBOfficerBoundary {

    private static final Scanner sc = new Scanner(System.in);

    public static void showOfficerMenu(HDBOfficer user) {
        int choice = -1;
        System.out.println("\nWelcome, " + user.getName() + "! (HDB Officer)");

        while (choice != 9) {
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
                    case 1:
                        HDBOfficerControl.viewEligibleProjects(user);
                        break;
                    case 2:
                        HDBOfficerControl.handleSubmitApplication(user);
                        break;
                    case 3:
                        HDBOfficerControl.viewApplication(user);
                        break;
                    case 4:
                        HDBOfficerControl.handleWithdrawApplication(user);
                        break;
                    case 5:
                        HDBOfficerControl.handleSubmitEnquiry(user);
                        break;
                    case 6:
                        HDBOfficerControl.handleEditEnquiry(user);
                        break;
                    case 7:
                        HDBOfficerControl.handleSubmitRegistration(user);
                        break;
                    case 8:
                        HDBOfficerControl.handleViewRegistrationStatus(user);
                        break;
                    case 9:
                        HDBOfficerControl.viewProjectHandling(user);
                        break;
                    case 10:
                        HDBOfficerControl.viewAndReplyEnquiries(user);
                        break;
                    case 11:
                        HDBOfficerControl.handleFlatBooking(user);
                        break;
                    case 12:
                        HDBOfficerControl.generateBookingReceipt(user);
                        break;
                    case 13:
                        HDBOfficerControl.updatePassword(user);
                    case 14:
                        System.out.println("✅ Logging out...");
                        return;
                    default:
                        System.out.println("Invalid choice. Try again.");
                }
            } else {
                System.out.println("Please enter a valid number.");
                sc.nextLine(); // clear input
            }
        }
    }
}