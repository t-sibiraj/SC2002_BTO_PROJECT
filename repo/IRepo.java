package repo;

/**
 * An interface representing a generic repository that supports
 * saving to and loading from a CSV file.
 */
public interface IRepo {

    /**
     * Saves the current state of the repository to a CSV file.
     *
     * @param filename The name of the file to save to.
     */
    public void saveToCSV(String filename);

    /**
     * Loads data into the repository from a CSV file.
     *
     * @param filename The name of the file to load from.
     */
    public void loadFromCSV(String filename);
}