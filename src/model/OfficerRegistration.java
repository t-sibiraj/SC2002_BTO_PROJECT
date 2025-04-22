package model;

import enums.*;

/**
 * Represents a registration made by an HDB officer to manage a BTO project.
 * This class stores the status of the registration, the associated project,
 * and the officer who applied.
 */
public class OfficerRegistration {

    /** The current status of the officer's registration. */
    private RegistrationStatus status;

    /** The BTO project the officer is registering to manage. */
    private BTOProject project;

    /** The HDB officer who submitted the registration. */
    private HDBOfficer officer;

    /**
     * Constructs a new {@code OfficerRegistration} with default status PENDING.
     * Automatically adds the registration to the specified project.
     *
     * @param officer The officer submitting the registration.
     * @param project The project for which the officer is registering.
     * @throws IllegalArgumentException if either the officer or project is null.
     */
    public OfficerRegistration(HDBOfficer officer, BTOProject project) {
        if (officer == null || project == null) {
            throw new IllegalArgumentException("Officer and Project must not be null.");
        }

        this.officer = officer;
        this.project = project;
        this.status = RegistrationStatus.PENDING;

        project.addRegistration(this);
    }

    /**
     * Returns the current status of this registration.
     *
     * @return The registration status.
     */
    public RegistrationStatus getStatus() {
        return status;
    }

    /**
     * Returns the officer who submitted the registration.
     *
     * @return The {@code HDBOfficer}.
     */
    public HDBOfficer getOfficer() {
        return officer;
    }

    /**
     * Returns the project this registration is for.
     *
     * @return The {@code BTOProject}.
     */
    public BTOProject getProject() {
        return project;
    }

    /**
     * Updates the status of this registration.
     *
     * @param status The new registration status.
     * @throws IllegalArgumentException if the status is null.
     */
    public void setStatus(RegistrationStatus status) {
        if (status == null) {
            throw new IllegalArgumentException("Status cannot be null.");
        }
        this.status = status;
    }

    /**
     * Returns a string representation of this registration.
     *
     * @return A formatted string with officer name, project name, and status.
     */
    @Override
    public String toString() {
        return "Officer: " + officer.getName() +
               " | Project: " + project.getName() +
               " | Status: " + status;
    }
}