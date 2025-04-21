package model;

import enums.*;


public class BTOApplication{

    // ======================
    // Fields
    // ======================
    private Applicant applicant;             
    private BTOProject project;              
    private FlatType flatType;              
    private ApplicationStatus status;       
    private boolean wantWithdraw;       
    private FlatBooking flatBooking;          

    // ======================
    // Constructor
    // ======================
    public BTOApplication(Applicant applicant, BTOProject project, FlatType flatType) {
        this.applicant = applicant;
        this.project = project;
        this.flatType = flatType;
        this.status = ApplicationStatus.SUCCESSFUL;  //CHANGE IT BACK PENDING REMEMBER
        this.wantWithdraw = false;

        this.applicant.setApplication(this);
    }

    // ======================
    // Getters
    // ======================

    public Applicant getApplicant() {
        return applicant;
    }

    public BTOProject getProject() {
        return project;
    }

    public FlatType getFlatType(){
        return flatType;
    } 
    public boolean hasRequestedWithdraw() {
        return wantWithdraw;
    }

    public ApplicationStatus getApplicationStatus() {
        return this.status;
    }

    public FlatBooking getFlatBooking() {
        return this.flatBooking;
    }

    // ======================
    // Setters
    // ======================
    public void setStatus(ApplicationStatus applicationStatus){
        this.status = applicationStatus;
        System.out.println("Status changed successfully");
    }
    
    // ======================
    // Other Methods
    // ======================
    public void withdraw() {
        if (status == ApplicationStatus.BOOKED) {
            System.out.println("Cannot withdraw after flat is booked.");
        } else if (wantWithdraw) {
            System.out.println("Withdrawal already requested.");
        } else {
            this.wantWithdraw = true;
            System.out.println("Withdrawal request submitted successfully.");
        }
    }

    // ======================
    // Factory Method
    // ======================
    public void createFlatBooking(FlatType flatType) {
        this.flatBooking = new FlatBooking(flatType, this);  
    }

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