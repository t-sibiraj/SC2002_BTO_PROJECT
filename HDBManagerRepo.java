import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class HDBManagerRepo implements IUserRepo{
    private List<HDBManager> managers;

    public HDBManagerRepo() {
        this.managers = new ArrayList<>();
    }

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
            
        HDBManager newManager = new HDBManager(age, isMarried); 
        managers.add(newManager);
    }

    @Override
    public void deleteUser(int id){
        managers.removeIf(manager -> {
            if(manager.getId() == id){
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
    public HDBManager getUser(int id){
        for (HDBManager manager : managers){
            if(manager.getId() == id){
                return manager;
            }
        }

        System.out.println("No manager with such ID found");
        return null;
    }
}
