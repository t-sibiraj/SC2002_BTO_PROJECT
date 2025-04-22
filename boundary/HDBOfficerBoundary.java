package boundary;

import control.HDBOfficerControl;
import java.util.Scanner;
import model.HDBOfficer;

public class HDBOfficerBoundary {

    private final Scanner sc = new Scanner(System.in);
    private HDBOfficerControl controller = new HDBOfficerControl();

    public void showOfficerMenu(HDBOfficer user) {
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
                    case 13 -> controller.updatePassword(user);
                    case 14 -> {
                        System.out.println("✅ Logging out...");
                        return;
                    }
                    default -> System.out.println("Invalid choice. Try again.");
                }
            } else {
                System.out.println("Please enter a valid number.");
                sc.nextLine(); // clear input
            }
        }
    }
}