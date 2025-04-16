public class HDBOfficer extends Applicant{
    private BTOProject assignedProject;

    public HDBOfficer(int age, boolean isMarried) {
        super(age, isMarried);
    }

    public void bookFlat(Applicant a){
        a.getApplication().createFlatBooking();
    }

    public void setRegistration(OfficerRegistration r){
        assignedProject.addRegistration(r);
    }

    //TODO figure this out
    public String checkRegistrationStatus(){
        //this.assignedProject.getRegistrations();
    }
}
   
