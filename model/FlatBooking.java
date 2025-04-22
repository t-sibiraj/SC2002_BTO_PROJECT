package model;

import enums.FlatType;
import java.time.LocalDateTime;

/**
 * Represents a flat booking associated with a BTO application.
 * Contains details about the flat type, booking date, resolution status,
 * the linked BTO project, and the original application.
 */
public class FlatBooking {

    /** The type of flat being booked. */
    private FlatType flatType;

    /** The BTO project this booking belongs to. */
    private BTOProject project;

    /** The date and time when the booking was made. */
    private LocalDateTime bookingDate;

    /** Whether the booking has been resolved (e.g., confirmed or completed). */
    private boolean isResolved;

    /** The BTO application associated with this booking. */
    private BTOApplication application;

    /**
     * Constructs a new FlatBooking and links it to the associated project.
     *
     * @param flatType The type of flat booked.
     * @param application The application related to this booking.
     */
    public FlatBooking(FlatType flatType, BTOApplication application) {
        this.flatType = flatType;
        this.project = application.getProject();
        this.bookingDate = LocalDateTime.now();
        this.isResolved = false;
        this.application = application;
        project.addBooking(this);
    }

    /**
     * @return The type of flat booked.
     */
    public FlatType getFlatType() {
        return flatType;
    }

    /**
     * @return The BTO project related to the booking.
     */
    public BTOProject getProject() {
        return project;
    }

    /**
     * @return The date and time when the booking was made.
     */
    public LocalDateTime getBookingDate() {
        return bookingDate;
    }

    /**
     * @return True if the booking is resolved, false otherwise.
     */
    public boolean isResolved() {
        return isResolved;
    }

    /**
     * @return The application associated with this booking.
     */
    public BTOApplication getApplication(){
        return application;
    }

    /**
     * Sets whether the booking has been resolved.
     *
     * @param resolved True to mark as resolved, false otherwise.
     */
    public void setResolved(boolean resolved) {
        this.isResolved = resolved;
    }

    /**
     * Returns a string representation of the booking.
     *
     * @return A formatted string with booking details.
     */
    @Override
    public String toString() {
        return "Flat Type: " + flatType +
               "\nProject: " + project.getName() +
               "\nBooking Date: " + bookingDate +
               "\nResolved: " + (isResolved ? "Yes" : "No");
    }
}