package repo;
import model.*;

public interface IUserRepo{
    public void createUser();
    public void deleteUser(String nric);
    public User getUser(String nric);

    public void saveToCSV(String fileName);
    public void loadFromCSV(String fileName);
}