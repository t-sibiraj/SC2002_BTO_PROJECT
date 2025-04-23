package model;

import enums.*;

/**
 * Represents an application submitted by an applicant for a BTO project.
 * The application includes the applicant, project details, flat type, status,
 * and flat booking if applicable.
 */
public class BTOApplication {

    /** The applicant who submitted the application. */
    private Applicant applicant;

    /** The BTO project the applicant is applying to. */
    private BTOProject project;

    /** The type of flat the applicant is applying for. */
    private FlatType flatType;

    /** The current status of the application. */
    private ApplicationStatus status;

    /** Whether the applicant has requested to withdraw the application. */
    private boolean wantWithdraw;

    /** The flat booking associated with this application, if booked. */
    private FlatBooking flatBooking;

    /**
     * Constructs a new BTOApplication.
     *
     * @param applicant The applicant submitting the application.
     * @param project The BTO project being applied to.
     * @param flatType The type of flat requested.
     */
    public BTOApplication(Applicant applicant, BTOProject project, FlatType flatType) {
        this.applicant = applicant;
        this.project = project;
        this.flatType = flatType;
        this.status = ApplicationStatus.PENDING;  // CHANGE IT BACK TO PENDING REMEMBER
        this.wantWithdraw = false;
        this.applicant.setApplication(this);
    }


    /**
     * Returns the applicant associated with this application.
     *
     * @return The {@code Applicant} object linked to this application.
     */
    public Applicant getApplicant() {
        return applicant;
    }


    /**
     * Returns the BTO project for which the application was made.
     *
     * @return The {@code BTOProject} linked to this application.
     */
    public BTOProject getProject() {
        return project;
    }


    /**
     * Returns the type of flat the applicant applied for.
     *
     * @return The {@code FlatType} selected in the application.
     */
    public FlatType getFlatType(){
        return flatType;
    }


    /**
     * Checks if a withdrawal has been requested for this application.
     *
     * @return {@code true} if a withdrawal was requested, otherwise {@code false}.
     */
    public boolean hasRequestedWithdraw() {
        return wantWithdraw;
    }


    /**
     * Gets the current application status.
     *
     * @return The {@code ApplicationStatus} of the application.
     */
    public ApplicationStatus getApplicationStatus() {
        return this.status;
    }


    /**
     * Gets the flat booking details associated with this application.
     *
     * @return The {@code FlatBooking} instance if one exists, otherwise {@code null}.
     */
    public FlatBooking getFlatBooking() {
        return this.flatBooking;
    }

    /**
     * Updates the application status.
     *
     * @param applicationStatus The new status to be set.
     */
    public void setStatus(ApplicationStatus applicationStatus){
        this.status = applicationStatus;
        System.out.println("Status changed successfully");
    }

    /**
     * Marks this application as having a withdrawal request.
     */
    public void withdraw() {
        this.wantWithdraw = true;
        this.status = ApplicationStatus.WITHDRAWN;
        System.out.println("Withdrawal request submitted successfully.");
    }

    /**
     * Approves withdrawal request
     */
    public void approveWithdraw() {
        this.status = ApplicationStatus.WITHDRAWN;
        System.out.println("Withdrawal request submitted successfully.");
    }

    /**
     * Cancels a previous withdrawal request.
     */
    public void rejectWithdraw() {
        this.wantWithdraw = false;
        System.out.println("Withdrawal request rejected successfully.");
    }

    /**
     * Creates a flat booking associated with this application.
     *
     * @param flatType The type of flat to book.
     */
    public void createFlatBooking(FlatType flatType) {
        this.flatBooking = new FlatBooking(flatType, this);  
    }

    /**
     * Returns a string representation of the application.
     *
     * @return Formatted application summary.
     */
    @Override
    public String toString() {
        return """
            Applicant Name      : %s
            Project Name        : %s
            Flat Type           : %s
            Application Status  : %s
            Withdrawal Status   : %s
            
            ------------------------------------------
            """.formatted(
                applicant.getName(), project.getName(), flatType.toString().toLowerCase(), 
                status.toString().toLowerCase(), wantWithdraw ? "Submitted" : "None"
            );
    }
}