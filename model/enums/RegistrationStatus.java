package enums;

/**
 * The {@code RegistrationStatus} enum defines the possible statuses
 * for an HDB officer's registration to manage a project.
 */
public enum RegistrationStatus {

    /** The registration has been submitted and is awaiting review. */
    PENDING,

    /** The registration has been approved. */
    APPROVED,

    /** The registration has been rejected. */
    REJECTED
}