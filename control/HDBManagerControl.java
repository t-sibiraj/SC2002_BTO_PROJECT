// You can refer to the OfficerControl code for similar functionality.
// I have commented out a few imports because my VSCode auto-removes unused imports. 
// So, please uncomment them when necessary.

// In the main program, the repositories are first initialized and the same instances are used in all control classes.

// Note that:
// - Each Applicant object has its own Application and list of Enquiries.
// - Each HDBOfficer has their own Application, Enquiries, and OfficerRegistration.
// - Each BTOProject stores all (not "own") Applications, Enquiries, and OfficerRegistrations submitted for that project.

// Therefore, when creating a new Application, it must be added to the BTOProject object (via the ProjectRepo)
// **in addition to** setting it in the Applicant object (via the ApplicantRepo).

// For example:
// Enquiry enquiry = new Enquiry(applicant, selectedProject, message);
// applicant.addEnquiry(enquiry);
// selectedProject.addEnquiry(enquiry);

// If you look closely, it’s the same Enquiry object stored in both the applicant and project.
// So, if you update the Enquiry via the ApplicantRepo, the changes will automatically be reflected in the ProjectRepo too.
// This same behavior applies to Application objects as well.

// In OfficerControl, for instance, it retrieves an Application from the ProjectRepo,
// and once it sets the status to BOOKED, the corresponding object in the ApplicantRepo also reflects the update,
// because they point to the same object in memory.

// At the HDBManagerControl level, most of the objects that need to be added to repos will already be added elsewhere.
// One of the few things you might need to add here is a **new Project** to the ProjectRepo.
// Other than that, most entities (Applications, Registrations, Enquiries) are already handled.

// As long as your function modifies a shared object (e.g., Application or Enquiry),
// those changes will be reflected across all repos automatically.

// Here’s what you should prioritize (in order of importance):
// 1. Complete the control/HDBManagerControl and model/HDBManagerclass (add more menu options if needed).
// 1.1 You might have to add or modify some functionalities of repo/BTOProjectRepo also to achive 1
// 2. Write the coding report and perform testing for the HDB Manager functionality only
// 3. Implement input validation (basic checks) (I have already have a few checks in place for null).
//    - Go through each file and check any Scanner input lines.
//    - Add basic input validation there.
//    - To reduce code duplication, you can create utility methods for validation inside the `util` package.



// I WILL LATER ADD DOCUMENTATION COMMENTS FOR THINGS THAT ARE ALREADY CODED SO FAR. FOR THE THINGS THAT YOU WILL BE CODING JUST ADD THE DOC COMMENTS YOURSELF
// ALSO PUSH YOUR CHANGES TO THE GITHUB REPO REGULARY SO THAT ITS EASIER TO RESOLVE IF THERE IS A CONFLICT
// IF YOU GOT ANY QUESTIONS JUST FEEL FREE TO MESSAGE ME


package control;

import java.util.Scanner;
import model.*;
import repo.*;
import util.*;
// import enums.*;

// import java.util.List;
// import java.util.ArrayList;
// import java.util.List;
// import java.util.stream.Collectors;

public class HDBManagerControl {

    private static BTOProjectRepo projectRepo;
    private static ApplicantRepo applicantRepo;
    private static HDBOfficerRepo officerRepo;

    public static void init(ApplicantRepo aRepo, BTOProjectRepo pRepo, HDBOfficerRepo oRepo) {
        projectRepo = pRepo;
        applicantRepo = aRepo;
        officerRepo = oRepo;
    }

    private static final Scanner sc = new Scanner(System.in);

    // 1. Create New Project
    public static void handleCreateProject(HDBManager manager) {
        // Prompt for input fields and create BTOProject
        // Enforce: no overlapping project periods for same manager
    }

    // 2. Edit Project
    public static void handleEditProject(HDBManager manager) {
        // Select from manager's projects
        // Update editable fields
    }

    // 3. Delete Project
    public static void handleDeleteProject(HDBManager manager) {
        // Select and remove project created by this manager
    }

    // 4. Toggle Visibility
    public static void toggleProjectVisibility(HDBManager manager) {
        // Toggle visibility for one of their projects
    }

    // 5. View All Projects
    public static void viewAllProjects() {
        // List all projects regardless of visibility or owner
    }

    // 6. View Only My Projects
    public static void viewMyProjects(HDBManager manager) {
        // Filter projectRepo by manager.getNric()
    }

    // 7. View Officer Registrations
    public static void viewOfficerRegistrations(HDBManager manager) {
        // Show all officer registrations related to manager's projects
    }

    // 8. Approve/Reject Officer Registration
    public static void handleUpdateRegistration(HDBManager manager) {
        // Approve or reject a registration
        // On approval, assign officer to project & decrease available officer slots
    }

    // 9. View Applications to My Project
    public static void viewApplications(HDBManager manager) {
        // List applications to manager's projects
    }

    // 10. Approve/Reject Application
    public static void handleUpdateApplication(HDBManager manager) {
        // Approve/reject application based on flat supply
        // Update project flat counts
    }

    // 11. Process Withdrawal Requests
    public static void handleProcessWithdrawal(HDBManager manager) {
        // Show withdrawal requests for their projects
        // Allow manager to approve/reject
    }

    // 12. View All Enquiries (all projects)
    public static void viewAllEnquiries() {
        // Show enquiries across all projects
    }

    // 13. Reply to Enquiries (My Project Only)
    public static void replyToEnquiries(HDBManager manager) {
        // Allow manager to reply only to enquiries on their own projects
    }

    // 14. Generate Report (with filters)
    public static void generateReports(HDBManager manager) {
        // Generate and display applicant reports (e.g. filter by marital status, flat type, etc.)
    }

    // 15. Change Password
    public static void changePassword(HDBManager manager) {
        PasswordChanger.updatePassword(manager);
    }
}