package model;

import java.time.LocalDateTime;

public class Enquiry {

    // ======================
    // Fields
    // ======================
    private Applicant applicant;     
    private BTOProject project;       
    private String message;          
    private String reply;          
    private LocalDateTime timestamp;
    private boolean isReplied;
    

    // ======================
    // Constructor
    // ======================
    public Enquiry(Applicant applicant, BTOProject project, String message) {
        this.applicant = applicant;
        this.project = project;
        this.message = message;
        this.reply = null;
        this.timestamp = LocalDateTime.now();
        this.isReplied = false;
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

    public String getMessage() {
        return message;
    }

    public String getReply() {
        return reply;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public boolean isReplied() {
        return this.isReplied;
    }

    // ======================
    // Setters
    // ======================
    public void setMessage(String message) {
        this.message = message;
    }

    public void setReply(String reply) {
        this.reply = reply;
        this.isReplied = true;
    }


    // ======================
    // Factory Method
    // ======================
    @Override
    public String toString() {
        return "From: " + applicant.getName() +
            "\nProject: " + project.getName() +
            "\nMessage: " + message +
            "\nReply: " + (isReplied() ? reply : "No reply yet") +
            "\nDate: " + timestamp;
    }

}
