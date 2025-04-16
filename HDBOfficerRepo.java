
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class HDBOfficerRepo implements IUserRepo{
   private List<HDBOfficer> officers = new ArrayList<>();

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
            
        HDBOfficer newOfficer = new HDBOfficer(age, isMarried);
        officers.add(newOfficer);
    }

    @Override
    public void deleteUser(int id){
        officers.removeIf(officer -> {
            if(officer.getId() == id){
                System.out.println("User deleted");
                return true;
            }
            else{
                System.out.println("User not found");
                return false;
            }
        });
    }

    @Override
    public HDBOfficer getUser(int id){
        for (HDBOfficer officer : officers){
            if(officer.getId() == id){
                return officer;
            }
        }

        System.out.println("No applicant with such ID found");
        return null;
    }
}
