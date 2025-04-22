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
     * @return The applicant who submitted the enquiry
     */
    public Applicant getApplicant() {
        return applicant;
    }

    /**
     * @return The BTO project the enquiry is about
     */
    public BTOProject getProject() {
        return project;
    }

    /**
     * @return The enquiry message
     */
    public String getMessage() {
        return message;
    }

    /**
     * @return The reply to the enquiry, if any
     */
    public String getReply() {
        return reply;
    }

    /**
     * @return The timestamp when the enquiry was created
     */
    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    /**
     * @return True if the enquiry has been replied to, false otherwise
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