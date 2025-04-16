public class BTOApplication{
    private boolean wantWithdraw;
    ApplicationStatus status;
    FlatBooking flatBooking;
    FlatType flatType;

    public BTOApplication(Applicant a, BTOProject p, FlatType flatType) {
        a.setApplication(this);
        this.flatType = flatType;
        wantWithdraw = false;
        status = ApplicationStatus.PENDING;
    }

    /*
     * setter functions
     */
    public void setStatus(ApplicationStatus s){
        status = s;
        System.out.println("Status changed successfully");
    }
    
    /*
     * getter function
     */
    public FlatType getFlatType(){
        return flatType;
    } 

    /*
    * withdraw BTO Application
    * if withdraw request already sent, prints "Already withdrawing"
    * else if there is no pending request, prints "no pending request to withdraw"
    */
    public void withdraw(){
        if(wantWithdraw)
            System.out.println("Already withdrawing");

        else if(status == ApplicationStatus.PENDING)
            this.wantWithdraw = true;
        else
            System.out.println("No pending request to withdraw");
    }

    /*
     * create new flat booking
     */
    public void createFlatBooking(){
        this.flatBooking  = new FlatBooking(false);
    }
}