package boundary;

import control.ApplicantControl;
import java.util.Scanner;
import model.Applicant;

public class ApplicantBoundary {

    private static final Scanner sc = new Scanner(System.in);

    public static void showApplicantMenu(Applicant user) {
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
                    case 1:
                        ApplicantControl.viewEligibleProjects(user);
                        break;
                    case 2:
                        ApplicantControl.handleSubmitApplication(user);
                        break;
                    case 3:
                        ApplicantControl.viewApplication(user);
                        break;
                    case 4:
                        ApplicantControl.handleWithdrawApplication(user);
                        break;
                    case 5:
                        ApplicantControl.handleSubmitEnquiry(user);
                        break;
                    case 6:
                        ApplicantControl.handleEditEnquiry(user);
                        break;
                    case 7:
                        ApplicantControl.updatePassword(user);
                        break;
                    case 8:
                        System.out.println("✅ Logging out...");
                        return;
                    default:
                        System.out.println("Invalid choice. Try again.");
                }
            } else {
                System.out.println("Please enter a valid number.");
                sc.nextLine(); // clear invalid input
            }
        }
    }
}