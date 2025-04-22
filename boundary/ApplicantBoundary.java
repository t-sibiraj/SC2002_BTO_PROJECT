package boundary;

import control.ApplicantControl;
import java.util.Scanner;
import model.Applicant;

public class ApplicantBoundary {

    private static final Scanner sc = new Scanner(System.in);
    private ApplicantControl controller = new ApplicantControl();

    public void showApplicantMenu(Applicant user) {
        int choice = -1;
        System.out.println("\nWelcome, " + user.getName() + "! (Applicant)");

        while (choice != 7) {
            System.out.println("\n== Applicant Menu ==");
            System.out.println("1. View Eligible Projects");
            System.out.println("2. Apply for a Project");
            System.out.println("3. View My Application");
            System.out.println("4. Request Withdrawal");
            System.out.println("5. Submit Enquiry");
            System.out.println("6. View/Edit/Delete Enquiries");
            System.out.println("7. Update Password");
            System.out.println("8. Logout");
            System.out.print("\nEnter your choice: ");

            if (sc.hasNextInt()) {
                choice = sc.nextInt();
                sc.nextLine();

                switch (choice) {
                    case 1 -> controller.viewEligibleProjects(user);
                    case 2 -> controller.handleSubmitApplication(user);
                    case 3 -> controller.viewApplication(user);
                    case 4 -> controller.handleWithdrawApplication(user);
                    case 5 -> controller.handleSubmitEnquiry(user);
                    case 6 -> controller.handleEditEnquiry(user);
                    case 7 -> controller.updatePassword(user);
                    case 8 -> {
                        System.out.println("✅ Logging out...");
                        return;
                    }
                    default -> System.out.println("Invalid choice. Try again.");
                }
            } else {
                System.out.println("Please enter a valid number.");
                sc.nextLine(); // clear invalid input
            }
        }
    }
}