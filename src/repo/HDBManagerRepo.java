package repo;

import java.util.ArrayList;
import java.util.List;
import model.HDBManager;

/**
 * Repository class for managing {@link HDBManager} users.
 * Supports operations such as create, retrieve, delete, and load/save from CSV.
 */
public class HDBManagerRepo implements IUserRepo<HDBManager> {
    /** Internal list storing all the HDB managers. */
    private List<HDBManager> managers = new ArrayList<>();

    /**
     * Adds an HDB Manager to the repository.
     *
     * @param manager the {@link HDBManager} instance to add
     */
    @Override
    public void addUser(HDBManager manager) {
        managers.add(manager);
    }


    /**
     * Creates a new HDB Manager by prompting user input,
     * and adds to the repository if the NRIC is unique.
     */
    @Override
    public void createUser() {
        HDBManager manager = HDBManager.createUser();
        if (getUser(manager.getNric()) != null) {
            System.out.println("A manager with this NRIC already exists");
            return;
        }
        managers.add(manager);
        System.out.println("HDB Manager registered successfully");
    }

    /**
     * Deletes a manager with the given NRIC from the repository.
     *
     * @param nric the NRIC of the manager to delete
     */
    @Override
    public void deleteUser(String nric) {
        boolean removed = managers.removeIf(manager -> manager.getNric().equalsIgnoreCase(nric));
        if (removed) {
            System.out.println("User deleted");
        } else {
            System.out.println("User not found");
        }
    }

    /**
     * Retrieves an HDB Manager by their NRIC.
     *
     * @param nric the NRIC to search for
     * @return the {@link HDBManager} if found, else {@code null}
     */
    @Override
    public HDBManager getUser(String nric) {
        for (HDBManager manager : managers) {
            if (manager.getNric().equalsIgnoreCase(nric)) {
                return manager;
            }
        }
        //System.out.println("No manager with such NRIC found"); Unsafe, shows up even if login fail
        return null;
    }


    /**
     * Returns a list of all HDB Managers in the repository.
     *
     * @return list of {@link HDBManager}
     */
    public List<HDBManager> getAllManagers() {
        return managers;
    }


    /**
     * Saves the current list of managers to a CSV file.
     *
     * @param fileName the name of the file to write to
     */
    @Override
    public void saveToCSV(String fileName) {
        try (java.io.PrintWriter writer = new java.io.PrintWriter(new java.io.File(fileName))) {
            writer.println("Name,NRIC,Age,MaritalStatus,Password");  // CSV header
            for (HDBManager manager : managers) {
                writer.printf("%s,%s,%d,%s,%s%n",
                    manager.getName(),
                    manager.getNric(),
                    manager.getAge(),
                    manager.isMarried() ? "Married" : "Single",
                    manager.getPassword()
                );
            }
            System.out.println("Managers saved to " + fileName);
        } catch (Exception e) {
            System.out.println("Error saving managers to CSV: " + e.getMessage());
        }
    }

    /**
     * Loads HDB Managers from a CSV file and populates the repository.
     *
     * @param fileName the name of the CSV file to read from
     */
    @Override
    public void loadFromCSV(String fileName) {
        try (java.util.Scanner sc = new java.util.Scanner(new java.io.File(fileName))) {
            if (sc.hasNextLine()) sc.nextLine(); // Skip header
            while (sc.hasNextLine()) {
                String[] fields = sc.nextLine().split(",");
                if (fields.length == 5) {
                    String name = fields[0].trim();
                    String nric = fields[1].trim();
                    int age = Integer.parseInt(fields[2].trim());
                    boolean isMarried = Boolean.parseBoolean(fields[3].trim());
                    String password = fields[4].trim();

                    HDBManager manager = new HDBManager(name, nric, age, isMarried, password);
                    managers.add(manager);
                }
            }
            System.out.println("Managers loaded from " + fileName);
        } catch (Exception e) {
            System.out.println("Error loading managers from CSV: " + e.getMessage());
        }
    }


    /**
     * Prints all HDB Managers currently in the repository to the console.
     */
    public void printAllManagers() {
        if (managers.isEmpty()) {
            System.out.println("No HDB Managers available.");
            return;
        }

        System.out.println("=== List of All HDB Managers ===");
        for (HDBManager manager : managers) {
            System.out.println(manager);
            System.out.println("----------------------------------");
        }
    }
}