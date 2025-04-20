package model;

import enums.FlatType;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class BTOProject {

    // ======================
    // Fields
    // ======================
    private String name;
    private String neighborhood;
    private int twoRoomNo;
    private int threeRoomNo;
    private int twoRoomPrice;
    private int threeRoomPrice;
    private boolean visible;
    private LocalDate appOpenDate;
    private LocalDate appCloseDate;
    private int noAvailableOffice;
    private HDBManager hdbManager;
    private String hdbManagerName;
    private String officerAssignedAsString;


    private List<BTOApplication> applications;
    private List<Enquiry> enquiries;
    private List<OfficerRegistration> officerRegistrations;

    // ======================
    // Constructor
    // ======================
    public BTOProject(String name, String neighborhood, int twoRoomNo, int threeRoomNo,
                      int twoRoomPrice, int threeRoomPrice,
                      LocalDate appopenDate, LocalDate appcloseDate, int noAvailableOffice, HDBManager hdbManager,String officerAssignedAsString) {
        this.name = name;
        this.neighborhood = neighborhood;
        this.twoRoomNo = twoRoomNo;
        this.threeRoomNo = threeRoomNo;
        this.twoRoomPrice = twoRoomPrice;
        this.threeRoomPrice = threeRoomPrice;
        this.visible = true;
        this.appOpenDate = appopenDate;
        this.appCloseDate = appcloseDate;
        this.noAvailableOffice = noAvailableOffice;

        this.hdbManager = hdbManager;
        this.hdbManagerName  = this.hdbManager.getName();

        this.applications = new ArrayList<>();
        this.enquiries = new ArrayList<>();
        this.officerRegistrations = new ArrayList<>();

        // this.officerAssignedAsString = officerRegistrations.stream().map(reg -> reg.getOfficer().getName()).collect(Collectors.joining(","));
    }

    // ======================
    // Getters
    // ======================
    public String getName() {
        return name;
    }

    public String getNeighborhood() {
        return neighborhood;
    }

    public int getTwoRoomNo() {
        return twoRoomNo;
    }

    public int getThreeRoomNo() {
        return threeRoomNo;
    }

    public int getTwoRoomPrice() {
        return twoRoomPrice;
    }

    public int getThreeRoomPrice() {
        return threeRoomPrice;
    }

    public boolean isVisible() {
        return this.visible;
    }

    public LocalDate getAppOpenDate() {
        return appOpenDate;
    }

    public LocalDate getAppCloseDate() {
        return appCloseDate;
    }

    public int getNoAvailableOffice() {
        return noAvailableOffice;
    }

    public List<BTOApplication> getApplications() {
        return applications;
    }

    public List<Enquiry> getEnquiries() {
        return enquiries;
    }

    public List<OfficerRegistration> getOfficerRegistrations() {
        return officerRegistrations;
    }

    public String getHDBManagerName() {
        return hdbManagerName;
    }

    public String getOfficerAssignedAsString() {
        return officerRegistrations.stream()
                .map(r -> r.getOfficer().getName())
                .distinct() //eliminate duplicate displyaing of offciers in csv
                .collect(Collectors.joining(",")); 
    }

    // ======================
    // Setters
    // ======================
    public void setNeighborhood(String neighborhood) {
        this.neighborhood = neighborhood;
    }

    public void setTwoRoomNo(int twoRoomNo) {
        this.twoRoomNo = twoRoomNo;
    }

    public void setThreeRoomNo(int threeRoomNo) {
        this.threeRoomNo = threeRoomNo;
    }

    public void setTwoRoomPrice(int twoRoomPrice) {
        this.twoRoomPrice = twoRoomPrice;
    }

    public void setThreeRoomPrice(int threeRoomPrice) {
        this.threeRoomPrice = threeRoomPrice;
    }

    public void setAppOpenDate(LocalDate appOpenDate) {
        this.appOpenDate = appOpenDate;
    }

    public void setAppCloseDate(LocalDate appCloseDate) {
        this.appCloseDate = appCloseDate;
    }

    public void setNoAvailableOffice(int noAvailableOffice) {
        this.noAvailableOffice = noAvailableOffice;
    }


    // ======================
    // Updaters
    // ======================
    // Add enquiry to enquiries list
    public void addEnquiry(Enquiry e) {
        this.enquiries.add(e);
        System.out.println("Enquiry successfully added");
    }

    public void addApplications(BTOApplication a) {
        applications.add(a);
    }

    public void addRegistration(OfficerRegistration reg) {
        boolean exists = officerRegistrations
                        .stream()
                        .anyMatch(r -> r.getOfficer().equals(reg.getOfficer()));
        if (!exists) {
            officerRegistrations.add(reg);
        }
    }

    public void updateOfficerAssignedString() {
        this.officerAssignedAsString = officerRegistrations.stream()
            .map(reg -> reg.getOfficer().getName())
            .collect(Collectors.joining(","));
    }

    // Toggle visibility between true and false
    public void toggleVisibility() {
        this.visible = !this.visible;
    }

    // Check if project is currently active (between open and close date inclusive)
    public boolean isActive() {
        LocalDate now = LocalDate.now();
        return (now.isAfter(this.appOpenDate) || now.isEqual(this.appOpenDate)) &&
               (now.isBefore(appCloseDate) || now.isEqual(appCloseDate));
    }

    // Check if a flat type is available
    public boolean hasFlatAvailable(FlatType flatType) {
        return switch (flatType) {
            case TWOROOM -> twoRoomNo > 0;
            case THREEROOM -> threeRoomNo > 0;
        };
    }

    // Decrement flat count after booking
    public void decrementFlatCount(FlatType flatType) {
        switch (flatType) {
            case TWOROOM -> this.twoRoomNo--;
            case THREEROOM -> this.threeRoomNo--;
        }
    }


    public void addApplication(BTOApplication application) {
        if (application == null) {
            System.out.println("Cannot add null application.");
            return;
        }

        this.applications.add(application);
        System.out.println("Application added to project: " + this.name);
    }


    // ======================
    // Factory Method
    // ======================
    @Override
    public String toString() {
        return """
            Project Name        : %s
            Neighborhood        : %s
            2-Room Units        : %d units at $%d
            3-Room Units        : %d units at $%d
            Application Period  : %s to %s
            Visibility          : %s
            Manager             : %s
            Officer Slots       : %d
            Officers Assigned   : %s
            ------------------------------------------
            """.formatted(
                name,
                neighborhood,
                twoRoomNo, twoRoomPrice,
                threeRoomNo, threeRoomPrice,
                appOpenDate, appCloseDate,
                visible ? "Visible" : "Hidden",
                hdbManagerName,
                noAvailableOffice,
                officerAssignedAsString
            );
    }
}
