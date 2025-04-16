 public class HDBManager extends User{
    private BTOProject project;

    public HDBManager(int age, boolean isMarried) {
        super(age, isMarried);
    }

    /*
     * setter functions
     */
    public void setProject(BTOProject p){
        this.project = p;
    }

    public void processWithdrawal(){//TODO implement application withdrawal
        if (this.project!=null){
            
        }
            
    }

    /*
     * toggles project visibility
     */
    public void toggleProjectVisibility(){
        this.project.toggleVisibility();
    }

    //TODO figure out registration
    public void updateRegistration(boolean approved){

    }

    public void updateApplication(boolean approved){

    }
}
