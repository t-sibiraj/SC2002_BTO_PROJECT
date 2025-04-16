import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class BTOProject {
    private int id;
    private String neighborhood;
    private int twoRoomNo;
    private int threeRoomNo;
    private int twoRoomPrice;
    private int threeRoomPrice;
    private boolean visible;
    private LocalDate appOpenDate;
    private LocalDate appCloseDate;
    private int noAvailableOffice;
    
    //is this right? double check
    private List<BTOApplication> applications;
    private List<Enquiry> enquiries;
    private List<OfficerRegistration> officerRegistrations;

    /*
     * constructor
     */
    public BTOProject(int id, String neighborhood, int twoRoomNo, int threeRoomNo, 
    int twoRoomPrice, int threeRoomPrice,
    LocalDate appopenDate, LocalDate appcloseDate, int noAvailableOffice){
        this.id = id;
        this.neighborhood = neighborhood;
        this.twoRoomNo = twoRoomNo;
        this.threeRoomNo = threeRoomNo;
        this.twoRoomPrice = twoRoomPrice;
        this.threeRoomPrice = threeRoomPrice;
        this.appOpenDate = appopenDate;
        this.appCloseDate = appcloseDate;
        this.noAvailableOffice = noAvailableOffice;

        this.applications = new ArrayList<>();
        this.enquiries = new ArrayList<>();
        this.officerRegistrations = new ArrayList<>();
    }

    /*
     * getter functions
     */
    public int getId(){ return this.id; }

    public String getNeighborhood(){ return this.neighborhood; }

    public int getTwoRoomNo(){ return twoRoomNo; }

    public int getThreeRoomNo(){ return threeRoomNo; }

    public int getNoAvailableOffice(){ return this.noAvailableOffice; }

    public List<OfficerRegistration> getRegistrations(){ return this.officerRegistrations; }

    /*
     * setter functions
     */
    public void setNeighborhood(String neighborhood){ this.neighborhood = neighborhood; }

    public void setTwoRoomNo(int twoRoomNo){ this.twoRoomNo = twoRoomNo; }

    public void setThreeRoomNo(int threeRoomNo){ this.threeRoomNo = threeRoomNo; }

    public void setTwoRoomPrice(int twoRoomPrice){ this.twoRoomPrice = twoRoomPrice; }

    public void setThreeRoomPrice(int threeRoomPrice){ this.threeRoomPrice = threeRoomPrice; }

    public void setAppOpenDate(LocalDate appOpenDate){ this.appOpenDate = appOpenDate; }

    public void setAppCloseDate(LocalDate appCloseDate){ this.appCloseDate = appCloseDate; }

    public void setNoAvailableOffice(int noAvailableOffice){ this.noAvailableOffice = noAvailableOffice; }

    

    /*
     * toggles visibility between true and false
     */
    public void toggleVisibility(){
        this.visible = !this.visible;
    }
    /*
     * prints BTO project details
     * TODO check if function is even needed
     */
    public void displayInfo(){
        if (this.visible){
            System.out.println("Details about project");
            System.out.println("Neighborhood:" + this.neighborhood);
            System.out.println("Number of two room flats: " + this.twoRoomNo);
            System.out.println("Number of three room flats: " + this.threeRoomNo);
            System.out.println("Price of two room flats: " + this.twoRoomPrice);
            System.out.println("Price of three room flats: " + this.threeRoomPrice);
            System.out.println("Opening date for applications: " + appOpenDate);
            System.out.println("Closing date for applications: " + appCloseDate);
        }
        else
            System.out.println("Project not found");
    }

    /*
     * add enquiry to enquiries list
     */
    public void addEnquiry(Enquiry e){
        this.enquiries.add(e);
        System.out.println("Enquiry successfully added");
    }

    /*
     * check if project is currently active
     * return true if between opening and closing date (inclusive)
     */
    public boolean isActive(){
        LocalDate now = LocalDate.now();
        return (now.isAfter(this.appOpenDate) || now.isEqual(this.appOpenDate)) && (now.isBefore(appCloseDate) || now.isEqual(appCloseDate));
    }

    public void addApplications(BTOApplication a){
        applications.add(a);
    }

    public void addRegistration(OfficerRegistration r){
        officerRegistrations.add(r);
    }
}
