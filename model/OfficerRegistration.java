package model;

import enums.*;

public class OfficerRegistration {

    // ======================
    // Fields
    // ======================
    private RegistrationStatus status;
    private BTOProject project;
    private HDBOfficer officer;

    // ======================
    // Constructor
    // ======================
    public OfficerRegistration(HDBOfficer officer, BTOProject project) {
        if (officer == null || project == null) {
            throw new IllegalArgumentException("Officer and Project must not be null.");
        }

        this.officer = officer;
        this.project = project;
        this.status = RegistrationStatus.PENDING; // CHANGE THIS BACK TO PENDING REMEMBER

        project.addRegistration(this); // automatically add to project
    }

    // ======================
    // Getters
    // ======================
    public RegistrationStatus getStatus() {
        return status;
    }

    public HDBOfficer getOfficer() {
        return officer;
    }

    public BTOProject getProject() {
        return project;
    }

    // ======================
    // Setters
    // ======================
    public void setStatus(RegistrationStatus status) {
        if (status == null) {
            throw new IllegalArgumentException("Status cannot be null.");
        }
        this.status = status;
    }

    // ======================
    // Factory Method
    // ======================
    @Override
    public String toString() {
        return "Officer: " + officer.getName() +
               " | Project: " + project.getName() +
               " | Status: " + status;
    }
}