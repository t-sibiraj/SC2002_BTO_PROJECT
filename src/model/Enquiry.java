package model;

import java.time.LocalDateTime;

/**
 * Represents an enquiry submitted by an applicant regarding a specific BTO project.
 * Contains the enquiry message, a reply (if any), and the timestamp of creation.
 */
public class Enquiry {

    /** The applicant who submitted the enquiry. */
    private Applicant applicant;

    /** The BTO project the enquiry is about. */
    private BTOProject project;

    /** The content of the enquiry message. */
    private String message;

    /** The reply to the enquiry, if any. */
    private String reply;

    /** The timestamp when the enquiry was created. */
    private LocalDateTime timestamp;

    /** Flag indicating whether the enquiry has been replied to. */
    private boolean isReplied;

    /**
     * Constructs an Enquiry object with the specified applicant, project, and message.
     * Initializes the timestamp to the current time and marks the enquiry as not replied.
     *
     * @param applicant The applicant who submitted the enquiry
     * @param project The BTO project related to the enquiry
     * @param message The message content of the enquiry
     */
    public Enquiry(Applicant applicant, BTOProject project, String message) {
        this.applicant = applicant;
        this.project = project;
        this.message = message;
        this.reply = null;
        this.timestamp = LocalDateTime.now();
        this.isReplied = false;
    }

    /**
     * Returns the applicant who submitted the enquiry.
     *
     * @return the applicant who made the enquiry
     */
    public Applicant getApplicant() {
        return applicant;
    }

    /**
     * Returns the BTO project that the enquiry is related to.
     *
     * @return the BTO project in question
     */
    public BTOProject getProject() {
        return project;
    }

    /**
     * Returns the message content of the enquiry.
     *
     * @return the enquiry message
     */
    public String getMessage() {
        return message;
    }

    /**
     * Returns the reply message to the enquiry, if any.
     *
     * @return the reply to the enquiry, or null if not replied
     */
    public String getReply() {
        return reply;
    }

    /**
     * Returns the timestamp indicating when the enquiry was created.
     *
     * @return the creation timestamp of the enquiry
     */
    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    /**
     * Indicates whether the enquiry has been replied to.
     *
     * @return true if a reply has been provided; false otherwise
     */
    public boolean isReplied() {
        return this.isReplied;
    }

    /**
     * Sets a new message for the enquiry.
     *
     * @param message The new enquiry message
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * Sets the reply for the enquiry and marks it as replied.
     *
     * @param reply The reply message
     */
    public void setReply(String reply) {
        this.reply = reply;
        this.isReplied = true;
    }

    /**
     * Returns a formatted string representation of the enquiry.
     *
     * @return Formatted enquiry details including applicant, project, message, reply, and timestamp
     */
    @Override
    public String toString() {
        return "From: " + applicant.getName() +
            "\nProject: " + project.getName() +
            "\nMessage: " + message +
            "\nReply: " + (isReplied() ? reply : "No reply yet") +
            "\nDate: " + timestamp;
    }

}