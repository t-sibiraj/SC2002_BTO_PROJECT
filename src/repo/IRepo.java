package repo;

public interface IRepo {
    public void saveToCSV(String filename);
    public void loadFromCSV(String filename);
}
