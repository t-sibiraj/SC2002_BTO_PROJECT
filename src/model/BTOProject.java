package model;

import enums.FlatType;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Represents a Build-To-Order (BTO) housing project with properties like flat types,
 * application period, assigned manager, and officer registrations.
 */
public class BTOProject {

    /** Name of the project. */
    private String name;

    /** Neighborhood where the project is located. */
    private String neighborhood;

    /** Number of available 2-room flats. */
    private int twoRoomNo;

    /** Number of available 3-room flats. */
    private int threeRoomNo;

    /** Price of 2-room flats. */
    private int twoRoomPrice;

    /** Price of 3-room flats. */
    private int threeRoomPrice;

    /** Visibility status of the project to applicants. */
    private boolean visible;

    /** Start date for application submission. */
    private LocalDate appOpenDate;

    /** End date for application submission. */
    private LocalDate appCloseDate;

    /** Number of available officer slots for handling the project. */
    private int noAvailableOffice;

    /** Manager responsible for this project. */
    private HDBManager hdbManager;

    /** Name of the manager as a string. */
    private String hdbManagerName;

    /** String containing names of assigned officers. */
    private String officerAssignedAsString;

    /** List of applications submitted to this project. */
    private List<BTOApplication> applications;

    /** List of enquiries submitted for this project. */
    private List<Enquiry> enquiries;

    /** List of officer registrations for handling this project. */
    private List<OfficerRegistration> officerRegistrations;

    /** List of bookings made for this project. */
    private List<FlatBooking> bookings;

    /**
     * Constructs a new BTOProject with the specified details.
     *
     * @param name Project name
     * @param neighborhood Project neighborhood
     * @param twoRoomNo Number of 2-room flats
     * @param threeRoomNo Number of 3-room flats
     * @param twoRoomPrice Price of 2-room flats
     * @param threeRoomPrice Price of 3-room flats
     * @param appopenDate Application start date
     * @param appcloseDate Application end date
     * @param noAvailableOffice Number of officer slots available
     * @param hdbManager Manager handling the project
     * @param officerAssignedAsString Comma-separated string of assigned officer names
     */
    public BTOProject(String name, String neighborhood, int twoRoomNo, int threeRoomNo,
                      int twoRoomPrice, int threeRoomPrice,
                      LocalDate appopenDate, LocalDate appcloseDate, int noAvailableOffice, HDBManager hdbManager, String officerAsString) {
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
        this.bookings = new ArrayList<>();

        this.officerAssignedAsString = officerAsString;
        if (officerAsString.isEmpty()) {
            this.officerAssignedAsString = officerRegistrations.stream().map(reg -> reg.getOfficer().getName()).collect(Collectors.joining(","));
        }
        this.hdbManager.addProject(this);
    }

    /** @return the name of the project */
    public String getName() { return name; }

    /** @return the neighborhood of the project */
    public String getNeighborhood() { return neighborhood; }

    /** @return the number of 2-room flats */
    public int getTwoRoomNo() { return twoRoomNo; }

    /** @return the number of 3-room flats */
    public int getThreeRoomNo() { return threeRoomNo; }

    /** @return the price of 2-room flats */
    public int getTwoRoomPrice() { return twoRoomPrice; }

    /** @return the price of 3-room flats */
    public int getThreeRoomPrice() { return threeRoomPrice; }

    /** @return whether the project is visible to applicants */
    public boolean isVisible() { return this.visible; }

    /** @return the start date for application submission */
    public LocalDate getAppOpenDate() { return appOpenDate; }

    /** @return the end date for application submission */
    public LocalDate getAppCloseDate() { return appCloseDate; }

    /** @return the number of available officer slots */
    public int getNoAvailableOffice() { return noAvailableOffice; }

    /** @return the list of applications submitted */
    public List<BTOApplication> getApplications() { return applications; }

    /** @return the list of enquiries submitted */
    public List<Enquiry> getEnquiries() { return enquiries; }

    /** @return the list of officer registrations */
    public List<OfficerRegistration> getOfficerRegistrations() { return officerRegistrations; }

    /** @return the name of the HDB manager */
    public String getHDBManagerName() { return hdbManagerName; }

    /** @return a comma-separated string of assigned officer names */
    public String getOfficerAssignedAsString() {
        return officerRegistrations.stream()
                .map(r -> r.getOfficer().getName())
                .distinct()
                .collect(Collectors.joining(","));
    }

    /** @return list of bookings for this project */
    public List<FlatBooking> getBookings() { return bookings; }

    /**
     * Sets the neighborhood of the project.
     * @param neighborhood the new neighborhood
     */
    public void setNeighborhood(String neighborhood) { this.neighborhood = neighborhood; }

    /** Sets the number of 2-room flats. */
    public void setTwoRoomNo(int twoRoomNo) { this.twoRoomNo = twoRoomNo; }

    /** Sets the number of 3-room flats. */
    public void setThreeRoomNo(int threeRoomNo) { this.threeRoomNo = threeRoomNo; }

    /** Sets the price of 2-room flats. */
    public void setTwoRoomPrice(int twoRoomPrice) { this.twoRoomPrice = twoRoomPrice; }

    /** Sets the price of 3-room flats. */
    public void setThreeRoomPrice(int threeRoomPrice) { this.threeRoomPrice = threeRoomPrice; }

    /** Sets the application open date. */
    public void setAppOpenDate(LocalDate appOpenDate) { this.appOpenDate = appOpenDate; }

    /** Sets the application close date. */
    public void setAppCloseDate(LocalDate appCloseDate) { this.appCloseDate = appCloseDate; }

    /** Sets the number of available officer slots. */
    public void setNoAvailableOffice(int noAvailableOffice) { this.noAvailableOffice = noAvailableOffice; }

    /** Sets the visibility of the project */
    public void setVisible(boolean visibility) {this.visible = visibility; }

    
    /**
     * Adds a new enquiry to the project.
     * @param e the enquiry to add
     */
    public void addEnquiry(Enquiry e) {
        this.enquiries.add(e);
        System.out.println("Enquiry successfully added");
    }

    /**
     * Adds a new application to the project.
     * @param a the application to add
     */
    public void addApplications(BTOApplication a) {
        applications.add(a);
    }

    /**
     * Adds a new officer registration if the officer isn't already registered.
     * @param reg the officer registration
     */
    public void addRegistration(OfficerRegistration reg) {
        boolean exists = officerRegistrations.stream()
                .anyMatch(r -> r.getOfficer().equals(reg.getOfficer()));
        if (!exists) {
            officerRegistrations.add(reg);
        }
    }

    /** Updates the string of assigned officer names. */
    public void updateOfficerAssignedString() {
        this.officerAssignedAsString = officerRegistrations.stream()
            .map(reg -> reg.getOfficer().getName())
            .collect(Collectors.joining(","));
    }

    /** Toggles the visibility status of the project. */
    public void toggleVisibility() {
        this.visible = !this.visible;
    }

    /**
     * Checks whether the project is currently active.
     * @return true if the current date is within the application period
     */
    public boolean isActive() {
        LocalDate now = LocalDate.now();
        return (now.isAfter(this.appOpenDate) || now.isEqual(this.appOpenDate)) &&
               (now.isBefore(appCloseDate) || now.isEqual(appCloseDate));
    }

    /**
     * Checks if flats of a specified type are available.
     * @param flatType the type of flat to check
     * @return true if available
     */
    public boolean hasFlatAvailable(FlatType flatType) {
        return switch (flatType) {
            case TWOROOM -> twoRoomNo > 0;
            case THREEROOM -> threeRoomNo > 0;
        };
    }

    /**
     * Decreases the count of the specified flat type by one.
     * @param flatType the type of flat
     */
    public void decrementFlatCount(FlatType flatType) {
        switch (flatType) {
            case TWOROOM -> this.twoRoomNo--;
            case THREEROOM -> this.threeRoomNo--;
        }
    }

    /**
     * Adds an application to the project if it's not null.
     * @param application the application to add
     */
    public void addApplication(BTOApplication application) {
        if (application == null) {
            System.out.println("Cannot add null application.");
            return;
        }
        this.applications.add(application);
        System.out.println("Application added to project: " + this.name);
    }

    /** Decreases the number of available officer slots by one. */
    public void decrementAvailableOffice() {
        this.noAvailableOffice--;
    }

    /**
     * Adds a new flat booking to the project.
     * @param booking the booking to add
     */
    public void addBooking(FlatBooking booking) {
        this.bookings.add(booking);
    }

    /**
     * Returns a formatted string describing the project.
     * @return project details
     */
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