package model;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents an applicant user in the BTO system. 
 * An applicant can submit, view, edit, and delete enquiries, and apply for BTO projects.
 */
public class Applicant extends User {

    /** The BTO application submitted by the applicant. */
    private BTOApplication application;

    /** A list of enquiries made by the applicant. */
    private List<Enquiry> enquiries;

    /**
     * Constructs an {@code Applicant} with the given personal details.
     *
     * @param name The applicant's name.
     * @param nric The applicant's NRIC.
     * @param age The applicant's age.
     * @param isMarried The marital status of the applicant.
     * @param password The applicant's password.
     */
    public Applicant(String name, String nric, int age, boolean isMarried, String password) {
        super(name, nric, age, isMarried,password);
        this.enquiries = new ArrayList<>();
    }

    /**
     * Returns the BTO application associated with the applicant.
     *
     * @return The {@code BTOApplication} object.
     */
    public BTOApplication getApplication() {
        return this.application;
    }

    /**
     * Returns the list of enquiries submitted by the applicant.
     *
     * @return A list of {@code Enquiry} objects.
     */
    public List<Enquiry> getEnquiries() {
        return this.enquiries;
    }

    /**
     * Sets the BTO application for the applicant.
     *
     * @param application The BTO application to assign.
     */
    public void setApplication(BTOApplication application) {
        this.application = application;
    }

    /**
     * Adds an enquiry to the applicant's enquiry list.
     *
     * @param enquiry The enquiry to add.
     */
    public void addEnquiry(Enquiry enquiry) {
        this.enquiries.add(enquiry);
    }

    /**
     * Creates and submits a new enquiry to a BTO project.
     *
     * @param project The project the enquiry is for.
     * @param message The enquiry message.
     */
    public void submitEnquiry(BTOProject project, String message) {
        Enquiry enquiry = new Enquiry(this, project, message);
        addEnquiry(enquiry);       
        System.out.println("Enquiry submitted successfully.");
    }

    /**
     * Displays all enquiries submitted by the applicant.
     */
    public void viewEnquiries() {
        if (enquiries.isEmpty()) {
            System.out.println("You have not submitted any enquiries.");
            return;
        }

        for (int i = 0; i < enquiries.size(); i++) {
            System.out.println("[" + (i + 1) + "]\n" + enquiries.get(i).toString() + "\n");
        }
    }

    /**
     * Edits an enquiry's message if it has not yet been replied to.
     *
     * @param index The index of the enquiry to edit.
     * @param newMessage The updated message.
     */
    public void editEnquiry(int index, String newMessage) {
        if (index < 0 || index >= enquiries.size()) {
            System.out.println("Invalid enquiry index.");
            return;
        }

        Enquiry enquiry = enquiries.get(index);

        if (enquiry.isReplied()) {
            System.out.println("Cannot edit enquiry after it has been replied to.");
            return;
        }

        enquiry.setMessage(newMessage);
        System.out.println("Enquiry updated successfully.");
    }

    /**
     * Deletes an enquiry if it has not yet been replied to.
     *
     * @param index The index of the enquiry to delete.
     */
    public void deleteEnquiry(int index) {
        if (index < 0 || index >= enquiries.size()) {
            System.out.println("Invalid enquiry index.");
            return;
        }

        Enquiry enquiry = enquiries.get(index);

        if (enquiry.isReplied()) {
            System.out.println("Cannot delete enquiry after it has been replied to.");
            return;
        }

        enquiries.remove(index);
        enquiry.getProject().getEnquiries().remove(enquiry);  // also remove from project
        System.out.println("Enquiry deleted successfully.");
    }

    /**
     * Factory method to create an {@code Applicant} based on user input.
     *
     * @return A new {@code Applicant} object with populated fields.
     */
    public static Applicant createUser() {
        User newUser = util.UserMaker.createUserFromInput();
        return new Applicant(
            newUser.getName(),
            newUser.getNric(),
            newUser.getAge(),
            newUser.isMarried(),
            newUser.getPassword()
        );
    }
}