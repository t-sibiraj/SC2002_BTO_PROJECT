package model;

import enums.*;
import java.util.*;

public class HDBOfficer extends Applicant {

    // ======================
    // Fields
    // ======================
    private BTOProject assignedProject;
    private OfficerRegistration registration;

    // ======================
    // Constructor
    // ======================
    public HDBOfficer(String name, String nric, int age, boolean isMarried, String password) {
        super(name, nric, age, isMarried, password);
    }

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

    // ======================
    // Getters
    // ======================

    public BTOProject getProject() {
        return this.assignedProject;
    }

    public String getAssignedProjectName(){
        return this.assignedProject.getName();
    }

    public boolean isHandlingProject() {
        return this.assignedProject != null;
    }

    public OfficerRegistration getCurrentOfficerRegistrationFromHDBOfficer(){
        return registration;
    }

    // ======================
    // Setters
    // ======================

    public void setAssignedProject(BTOProject project) {
        this.assignedProject = project;
    }

    public void setOfficerRegistrationToHDBOfficer(OfficerRegistration registration){
        this.registration = registration;
    }




    // ======================
    // Officer Functionalities
    // ======================




    /**
     * Book a flat for an applicant
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
     * Submit a registration request for this officer
     */
    public void submitRegistration(OfficerRegistration registration) {
        this.registration = registration;  // Store the registration in officer
        registration.getProject().addRegistration(registration);  // Link to the project
    }

    /**
     * Check officer's registration status in the assigned project
     */
    public String checkRegistrationStatus() {
        if (registration == null) {
            return "No registration submitted.";
        }

        return "Registration Status: " + registration.getStatus();
    }

    public OfficerRegistration getRegistration() {
        return this.registration;
    }

    /**
     * View and reply to enquiries related to this officer's project
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
     * Generate receipt for an applicant with a flat booking
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




    // THESE FUNCIONS ARE NEEDED BUT NOT USED 
        /**
     * Validate registration eligibility
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