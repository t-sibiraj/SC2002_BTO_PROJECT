import java.util.List;
import java.util.Scanner;

public class ApplicantRepo implements IUserRepo{
    private List<Applicant> applicants;

    /*
     * prompts user for age and marriage status
     * creates and add user to applicants list
     */
    @Override
    public void createUser(){

        Scanner sc = new Scanner(System.in);

        System.out.print("Enter age:");
        int age = sc.nextInt();

        System.out.print("Are you married(Y/N): ");
        String input = sc.next();
        boolean isMarried = false;
        if (input.equalsIgnoreCase("Y"))
            isMarried = true;

        Applicant applicant = new Applicant(age, isMarried);
        applicants.add(applicant);
    }

    /*
     * deletes user with corresponding id from array
     * prints "user not found" if such user is not in array
     */
    @Override
    public void deleteUser(int id){
        applicants.removeIf(applicant -> {
            if(applicant.getId() == id){
                System.out.println("User deleted");
                return true;
            }
            else{
                 
                return false;
            }
        });
    }
    
    /*
     * returns user with corresponding id
     * returns null if not found
     */
    @Override
    public User getUser(int id){
        for (Applicant applicant : applicants){
            if (applicant.getId() == id){
                return applicant;
            }
        }

        System.out.println("No applicant with such ID found");
        return null;
    }
}