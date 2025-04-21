 package model;

import enums.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class HDBManager extends User{
    // ======================
    // Fields
    // ======================
    private List<BTOProject> projects = new ArrayList<>();
    private Report report;

    // ======================
    // Constructor
    // ======================
    public HDBManager(String name, String nric, int age, boolean isMarried, String password) {
        super(name, nric, age, isMarried, password);
        report = new Report(this);
    }

    // ======================
    // Factory Method
    // ======================
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

    // ======================
    // Getters
    // ======================

    public List<BTOProject> getprojects() { return this.projects; }
    public boolean hasReports() { return this.report != null; }

    // ADD IF YOU NEED ANY


    // ======================
    // Setters
    // ======================
    public void addProject(BTOProject p){
        projects.add(p);
    }


    // ======================
    // Updaters or Other Menthods
    // ======================

    public void processWithdrawal(){
        if (!this.projects.isEmpty()){
            List<BTOApplication> applications;

            for (BTOProject project : projects){//for each project in projects
                applications= project.getApplications();//get project applications

                for (BTOApplication application : applications){//for each application
                    if(application.hasRequestedWithdraw())
                        application.withdraw();
                }
            }    
        }
        else
            System.out.println("No project to withdraw");       
    }

    /*
     * toggles project visibility
     */
    public void toggleProjectVisibility(BTOProject p){
        p.toggleVisibility();
    }

    public void updateRegistration(boolean approved, OfficerRegistration r){
        if(approved)
            r.setStatus(RegistrationStatus.APPROVED);
        else
            r.setStatus(RegistrationStatus.REJECTED);
    }

    public void updateApplication(boolean approved, BTOApplication a){
        if(approved)
            a.setStatus(ApplicationStatus.SUCCESSFUL);
        else
            a.setStatus(ApplicationStatus.UNSUCCESSSFUL);
    }

    public void viewProjects() {
        if (projects.isEmpty()) {
            System.out.println("You do not have any projects.");
            return;
        }

        for (int i = 0; i < projects.size(); i++) {
            System.out.println("[" + (i + 1) + "]\n" + projects.get(i).toString() + "\n");
        }
    }

    public void updateReport(Report report){
        this.report = report;
    }

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

    public void generateReports() {
        for (BTOProject project : projects){
            for(FlatBooking booking : project.getBookings()){
                report.addInfo(booking);
            }
        }
    }

    public void replyToEnquiry(Enquiry enquiry, String reply) {
        if (!projects.isEmpty() && projects.contains(enquiry.getProject())) {
            enquiry.setReply(reply);
            System.out.println("Reply submitted.");
        } else {
            System.out.println("This enquiry does not belong to your managed projects.");
        }
    }

    // ======================
    // Factory Method
    // ======================
}
