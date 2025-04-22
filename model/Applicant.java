package model;

import java.util.ArrayList;
import java.util.List;

public class Applicant extends User {

    // ======================
    // Fields
    // ======================
    private BTOApplication application;
    private List<Enquiry> enquiries;

    // ======================
    // Constructor
    // ======================
    public Applicant(String name, String nric, int age, boolean isMarried, String password) {
        super(name, nric, age, isMarried,password);
        this.enquiries = new ArrayList<>();
    }

    // ======================
    // Getters
    // ======================
    public BTOApplication getApplication() {
        return this.application;
    }

    public List<Enquiry> getEnquiries() {
        return this.enquiries;
    }


    // ======================
    // Setters
    // ======================
    public void setApplication(BTOApplication application) {
        this.application = application;
    }

    // ======================
    // Other Methods
    // ======================

    // ──────────────────────────────────────────────────────────────
    // ENQUIRY RELATED METHODS
    // ──────────────────────────────────────────────────────────────
    public void addEnquiry(Enquiry enquiry) {
        this.enquiries.add(enquiry);
    }

    public void submitEnquiry(BTOProject project, String message) {
        Enquiry enquiry = new Enquiry(this, project, message);
        addEnquiry(enquiry);       
        System.out.println("Enquiry submitted successfully.");
    }

    public void viewEnquiries() {
        if (enquiries.isEmpty()) {
            System.out.println("You have not submitted any enquiries.");
            return;
        }

        for (int i = 0; i < enquiries.size(); i++) {
            System.out.println("[" + (i + 1) + "]\n" + enquiries.get(i).toString() + "\n");
        }
    }

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
        enquiry.getProject().getEnquiries().remove(enquiry);  // also remove from project //REMEMBER
        System.out.println("Enquiry deleted successfully.");
    }


    // ======================
    // Factory Method
    // ======================

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