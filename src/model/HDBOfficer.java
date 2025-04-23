package model;

import enums.*;
import java.util.*;

/**
 * Represents an HDB Officer who is both an applicant and a project handler.
 * Inherits functionality from {@code Applicant} and adds features for handling
 * BTO projects, replying to enquiries, booking flats, and generating receipts.
 */
public class HDBOfficer extends Applicant {

    /** The BTO project currently assigned to the officer. */
    private BTOProject assignedProject;

    /** The officer's registration for handling a project. */
    private OfficerRegistration registration;


    /**
     * Constructs an HDBOfficer with the given personal details.
     *
     * @param name      Officer's name.
     * @param nric      Officer's NRIC.
     * @param age       Officer's age.
     * @param isMarried Officer's marital status.
     * @param password  Login password.
     */
    public HDBOfficer(String name, String nric, int age, boolean isMarried, String password) {
        super(name, nric, age, isMarried, password);
    }

    /**
     * Prompts user for input and creates an HDBOfficer instance.
     *
     * @return A new HDBOfficer object.
     */
    public static HDBOfficer createUser() {
        User baseUser = util.UserMaker.createUserFromInput();
        return new HDBOfficer(
            baseUser.getName(),
            baseUser.getNric(),
            baseUser.getAge(),
            baseUser.isMarried(),
            baseUser.getPassword()
        );
    }

    /**
     * Returns the project currently assigned to this officer.
     *
     * @return The assigned BTO project.
     */
    public BTOProject getProject() {
        return this.assignedProject;
    }

    /**
     * Returns the name of the assigned project.
     *
     * @return Name of the assigned project.
     */
    public String getAssignedProjectName(){
        return this.assignedProject.getName();
    }

    /**
     * Checks if the officer is currently handling a project.
     *
     * @return True if handling a project, false otherwise.
     */
    public boolean isHandlingProject() {
        return this.assignedProject != null;
    }

    /**
     * Returns the officer's current registration.
     *
     * @return The current OfficerRegistration.
     */
    public OfficerRegistration getCurrentOfficerRegistrationFromHDBOfficer(){
        return registration;
    }

    /**
     * Gets the officer's registration object.
     *
     * @return The OfficerRegistration object.
     */
    public OfficerRegistration getRegistration() {
        return this.registration;
    }

    /**
     * Assigns a project to this officer.
     *
     * @param project The project to assign.
     */
    public void setAssignedProject(BTOProject project) {
        this.assignedProject = project;
    }

    /**
     * Sets the officer's registration.
     *
     * @param registration The registration to set.
     */
    public void setOfficerRegistrationToHDBOfficer(OfficerRegistration registration){
        this.registration = registration;
    }

    /**
     * Books a flat for the given applicant, ensuring flat availability and eligibility.
     *
     * @param applicant The applicant booking the flat.
     */
    public void bookFlat(Applicant applicant) {
        BTOApplication app = applicant.getApplication();

        if (!app.getApplicationStatus().equals(ApplicationStatus.SUCCESSFUL)) {
            System.out.println("Applicant is not eligible to book a flat.");
            return;
        }

        FlatType flatType = app.getFlatType();

        if (!assignedProject.hasFlatAvailable(flatType)) {
            System.out.println("No more flats of selected type available.");
            return;
        }

        assignedProject.decrementFlatCount(flatType);
        app.createFlatBooking(flatType);
        app.setStatus(ApplicationStatus.BOOKED);
        System.out.println("Flat booked successfully.");
    }

    /**
     * Submits a registration for the officer to a specific project.
     *
     * @param registration The registration request.
     */
    public void submitRegistration(OfficerRegistration registration) {
        this.registration = registration;  // Store the registration in officer
        registration.getProject().addRegistration(registration);  // Link to the project
    }

    /**
     * Checks the status of the officer's submitted registration.
     *
     * @return A string indicating the registration status.
     */
    public String checkRegistrationStatus() {
        if (registration == null) {
            return "No registration submitted.";
        }

        return "Registration Status: " + registration.getStatus();
    }



    /**
     * Replies to an enquiry associated with the officer's assigned project.
     *
     * @param enquiry The enquiry to respond to.
     * @param reply   The reply message.
     */
    public void replyToEnquiry(Enquiry enquiry, String reply) {
        if (assignedProject != null && assignedProject.getEnquiries().contains(enquiry)) {
            enquiry.setReply(reply);
            System.out.println("Reply submitted.");
        } else {
            System.out.println("This enquiry does not belong to your assigned project.");
        }
    }

    /**
     * Generates and displays a flat booking receipt for the given applicant.
     *
     * @param applicant The applicant who booked the flat.
     */
    public void generateBookingReceipt(Applicant applicant) {
        BTOApplication app = applicant.getApplication();

        if (!app.getApplicationStatus().equals(ApplicationStatus.BOOKED)) {
            System.out.println("No flat has been booked.");
            return;
        }

        System.out.println("=== Booking Receipt ===");
        System.out.println("Name: " + applicant.getName());
        System.out.println("NRIC: " + applicant.getNric());
        System.out.println("Age: " + applicant.getAge());
        System.out.println("Marital Status: " + (applicant.isMarried() ? "Married" : "Single"));
        System.out.println("Flat Type: " + app.getFlatType());
        System.out.println("Project: " + assignedProject.getName());
        System.out.println("Date Booked: " + app.getFlatBooking().getBookingDate());
    }



    // OTHER MISC FUNCTIONS
    /**
     * Validates whether the officer is eligible to register for the given project.
     * Ensures no overlap with other project registrations or existing applications.
     *
     * @param targetProject The project the officer wants to register for.
     * @param allProjects   List of all existing projects.
     * @return True if eligible, false otherwise.
     */
    public boolean isEligibleForRegistration(BTOProject targetProject, List<BTOProject> allProjects) {
        // Officer cannot register for a project they've already applied for
        if (this.getApplication() != null && this.getApplication().getProject().equals(targetProject)) {
            return false;
        }

        // Cannot be registered to another project within overlapping application period
        for (BTOProject project : allProjects) {
            if (project == targetProject) continue;
            for (OfficerRegistration reg : project.getOfficerRegistrations()) {
                if (reg.getOfficer().equals(this) &&
                    project.getAppOpenDate().isBefore(targetProject.getAppCloseDate().plusDays(1)) &&
                    project.getAppCloseDate().isAfter(targetProject.getAppOpenDate().minusDays(1))) {
                    return false;
                }
            }
        }
        return true;
    }

}