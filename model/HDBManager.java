 package model;
 
 public class HDBManager extends User{
    // ======================
    // Fields
    // ======================
    private BTOProject project;

    // ======================
    // Constructor
    // ======================
    public HDBManager(String name, String nric, int age, boolean isMarried, String password) {
        super(name, nric, age, isMarried, password);
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

    // ADD IF YOU NEED ANY


    // ======================
    // Setters
    // ======================
    public void setProject(BTOProject p){
        this.project = p;
    }

    public void processWithdrawal(){//TODO implement application withdrawal
        if (this.project!=null){
            
        }
            
    }

    // ======================
    // Updaters or Other Menthods
    // ======================

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


    // ======================
    // Factory Method
    // ======================
}
