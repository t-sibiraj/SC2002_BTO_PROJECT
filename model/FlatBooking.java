package model;

import enums.FlatType;
import java.time.LocalDateTime;

public class FlatBooking {

    // ======================
    // Fields
    // ======================
    private FlatType flatType;         
    private BTOProject project;       
    private LocalDateTime bookingDate;
    private boolean isResolved;       

    // ======================
    // Constructor
    // ======================
    public FlatBooking(FlatType flatType, BTOProject project) {
        this.flatType = flatType;
        this.project = project;
        this.bookingDate = LocalDateTime.now();
        this.isResolved = false;
    }

    // ======================
    // Getters
    // ======================

    public FlatType getFlatType() {
        return flatType;
    }

    public BTOProject getProject() {
        return project;
    }

    public LocalDateTime getBookingDate() {
        return bookingDate;
    }

    public boolean isResolved() {
        return isResolved;
    }

    // ======================
    // Setters
    // ======================

    public void setResolved(boolean resolved) {
        this.isResolved = resolved;
    }

    // ======================
    // Fatory Method
    // ======================

    @Override
    public String toString() {
        return "Flat Type: " + flatType +
               "\nProject: " + project.getName() +
               "\nBooking Date: " + bookingDate +
               "\nResolved: " + (isResolved ? "Yes" : "No");
    }
}