 package model;

import enums.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Represents an HDB Manager user in the system.
 * Responsible for managing BTO projects, approving registrations,
 * handling applications, generating reports, and responding to enquiries.
 */
public class HDBManager extends User{

    /** List of BTO projects created and managed by this manager. */
    private List<BTOProject> projects;

    /** Report object that stores booking information and supports filtering. */
    private Report report;


    /**
     * Constructs a new HDBManager with the given personal details and an empty project list.
     * Also initializes a new report object for the manager.
     *
     * @param name      Manager's name.
     * @param nric      Manager's NRIC.
     * @param age       Manager's age.
     * @param isMarried Marital status.
     * @param password  Login password.
     */
    public HDBManager(String name, String nric, int age, boolean isMarried, String password) {
        super(name, nric, age, isMarried, password);
        projects = new ArrayList<>();
        report = new Report(this);
    }


    /**
     * Prompts user for input and creates a new HDBManager instance.
     *
     * @return A new HDBManager created from user input.
     */
    public static HDBManager createUser() {
        User baseUser = util.UserMaker.createUserFromInput();
        return new HDBManager(
            baseUser.getName(),
            baseUser.getNric(),
            baseUser.getAge(),
            baseUser.isMarried(),
            baseUser.getPassword()
        );
    }

    /**
     * Returns the list of BTO projects managed by this manager.
     *
     * @return List of managed BTO projects.
     */
    public List<BTOProject> getprojects() { return this.projects; }

    /**
     * Checks if this manager has a report object initialized.
     *
     * @return True if a report exists, false otherwise.
     */
    public boolean hasReports() { return this.report != null; }



    /**
     * Adds a new project to the manager's list.
     *
     * @param p The BTO project to add.
     */
    public void addProject(BTOProject p){
        projects.add(p);
    }


    /**
     * Toggles the visibility of the given project.
     *
     * @param p The project to toggle visibility for.
     */
    public void toggleProjectVisibility(BTOProject p){
        p.toggleVisibility();
    }

    /**
     * Updates the status of an officer registration.
     *
     * @param approved True to approve, false to reject.
     * @param r        The registration to update.
     */
    public void updateRegistration(boolean approved, OfficerRegistration r){
        if(approved)
            r.setStatus(RegistrationStatus.APPROVED);
        else
            r.setStatus(RegistrationStatus.REJECTED);
    }

    /**
     * Updates the status of a BTO application.
     *
     * @param approved True to approve, false to reject.
     * @param a        The application to update.
     */
    public void updateApplication(boolean approved, BTOApplication a){
        if(approved)
            a.setStatus(ApplicationStatus.SUCCESSFUL);
        else
            a.setStatus(ApplicationStatus.UNSUCCESSSFUL);
    }


    /**
     * Displays a list of all projects managed by this user.
     */
    public void viewProjects() {
        if (projects.isEmpty()) {
            System.out.println("You do not have any projects.");
            return;
        }

        for (int i = 0; i < projects.size(); i++) {
            System.out.println("[" + (i + 1) + "]\n" + projects.get(i).toString() + "\n");
        }
    }

    /**
     * Updates the report object with a new one.
     *
     * @param report The report to associate with this manager.
     */
    public void updateReport(Report report){
        this.report = report;
    }

    /**
     * Filters and displays report entries based on the selected filter.
     * 
     * Filters:
     * 1 - No filter (all entries)
     * 2 - Married applicants
     * 3 - Unmarried applicants
     * 4 - Applicants with 2-room flat bookings
     * 5 - Applicants with 3-room flat bookings
     *
     * @param filter The filter number to apply.
     */
    public void viewReport(int filter) {//filter by marital status, flat type, etc.
        String filtered = switch(filter){
            case 1 -> report.toString();
            case 2 -> {//married
                yield report.getInfo().entrySet().stream()
                        .filter(e -> e.getKey().isMarried())
                        .map(e -> e.getKey().getName() + "\n" + e.getValue().toString())
                        .collect(Collectors.joining("/n"));
            }

            case 3 -> {//not married
                yield report.getInfo().entrySet().stream()
                        .filter(e -> !e.getKey().isMarried())
                        .map(e -> e.getKey().getName() + "\n" + e.getValue().toString())
                        .collect(Collectors.joining("/n"));
            }

            case 4 -> {//2 room flats
                yield report.getInfo().entrySet().stream()
                        .filter(e -> e.getValue().getFlatType() == FlatType.TWOROOM)
                        .map(e -> e.getKey().getName() + "\n" + e.getValue().toString())
                        .collect(Collectors.joining("/n"));
            }

            case 5 -> {//3 room flats
                yield report.getInfo().entrySet().stream()
                        .filter(e -> e.getValue().getFlatType() == FlatType.THREEROOM)
                        .map(e -> e.getKey().getName() + "\n" + e.getValue().toString())
                        .collect(Collectors.joining("/n"));
            }

            default -> "invalid filter";
        };
        
        if (filtered.equals(""))
            System.out.println("No reports");
        else
            System.out.println(filtered);
    }


    /**
     * Returns filtered report data as a string instead of printing it.
     * Used for testing report content based on filters.
     *
     * @param filter The filter number (1-5).
     * @return The filtered report string or "No reports".
     */
    public String getReportString(int filter) {
        String filtered = switch(filter){
            case 1 -> report.toString();
            case 2 -> report.getInfo().entrySet().stream()
                    .filter(e -> e.getKey().isMarried())
                    .map(e -> e.getKey().getName() + "\n" + e.getValue().toString())
                    .collect(Collectors.joining("/n"));
            case 3 -> report.getInfo().entrySet().stream()
                    .filter(e -> !e.getKey().isMarried())
                    .map(e -> e.getKey().getName() + "\n" + e.getValue().toString())
                    .collect(Collectors.joining("/n"));
            case 4 -> report.getInfo().entrySet().stream()
                    .filter(e -> e.getValue().getFlatType() == FlatType.TWOROOM)
                    .map(e -> e.getKey().getName() + "\n" + e.getValue().toString())
                    .collect(Collectors.joining("/n"));
            case 5 -> report.getInfo().entrySet().stream()
                    .filter(e -> e.getValue().getFlatType() == FlatType.THREEROOM)
                    .map(e -> e.getKey().getName() + "\n" + e.getValue().toString())
                    .collect(Collectors.joining("/n"));
            default -> "invalid filter";
        };

        return filtered.isEmpty() ? "No reports" : filtered;
    }
    /**
     * Populates the manager's report with all bookings from managed projects.
     */
    public void generateReports() {
        for (BTOProject project : projects){
            for(FlatBooking booking : project.getBookings()){
                report.addInfo(booking);
            }
        }
    }

    /**
     * Replies to a submitted enquiry if it belongs to one of the manager's projects.
     *
     * @param enquiry The enquiry to reply to.
     * @param reply   The reply message to submit.
     */
    public void replyToEnquiry(Enquiry enquiry, String reply) {
        if (!projects.isEmpty() && projects.contains(enquiry.getProject())) {
            enquiry.setReply(reply);
            System.out.println("Reply submitted.");
        } else {
            System.out.println("This enquiry does not belong to your managed projects.");
        }
    }
}
