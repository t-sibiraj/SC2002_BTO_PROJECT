package enums;

/**
 * The {@code ApplicationStatus} enum represents the various states
 * an applicant's project application can be in within the BTO system.
 */
public enum ApplicationStatus {

    /** The application has been submitted but not yet processed. */
    PENDING,

    /** The application has been approved successfully. */
    SUCCESSFUL,

    /** The application was not approved. */
    UNSUCCESSSFUL,

    /** The applicant has booked a flat. */
    BOOKED,

    /** The applicant has withdrawn their application. */
    WITHDRAWN // Having this makes things easier
}