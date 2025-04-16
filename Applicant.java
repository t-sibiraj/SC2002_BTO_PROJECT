import java.util.List;

public class Applicant extends User{
    private BTOApplication application;
    private List<Enquiry> enquiries;

    public Applicant(int age, boolean isMarried) {
        super(age, isMarried);
    }
    public void setApplication(BTOApplication a){
        this.application = a;
    }

    public void withdrawApplication(){
        if(this.application == null)
            throw new IllegalArgumentException("Application not found");
        
        else if(this.application.status == ApplicationStatus.BOOKED)
            throw new IllegalStateException("Cannot withdraw after flat is booked");
        else
         application.withdraw();
    }

    public void addEnquiry(Enquiry e){
        this.enquiries.add(e);
    }

    /*
     * getter function
     */
    public BTOApplication getApplication(){ return this.application; }
}
