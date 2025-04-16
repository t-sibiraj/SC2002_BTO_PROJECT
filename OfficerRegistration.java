public class OfficerRegistration {
    RegistrationStatus status;
    BTOProject project;
    HDBOfficer officer;

    public OfficerRegistration(HDBOfficer o, BTOProject p){
        this.project = p;
        this.officer = o;

        this.project.addRegistration(this);
    }
    
    /*
     * getter function
     */
    public RegistrationStatus getStatus(){ return this.status; }

    public HDBOfficer getOfficer(){ return this.officer; }

    /*
     * setter function
     */
    public void setStatus(RegistrationStatus status) {
        this.status = status;
    }
}
