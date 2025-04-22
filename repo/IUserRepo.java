package repo;
import model.*;

public interface IUserRepo<T extends User> extends IRepo {
    public void createUser();
    public void deleteUser(String nric);
    public T getUser(String nric);
    public void addUser(T user);
}