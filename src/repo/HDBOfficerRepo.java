package repo;

import java.util.ArrayList;
import java.util.List;
import model.HDBOfficer;

/**
 * Repository class that manages {@link HDBOfficer} accounts.
 * Provides methods to create, delete, retrieve officers, and load/save data to CSV files.
 */
public class HDBOfficerRepo implements IUserRepo<HDBOfficer> {

    /** Internal list storing all the HDB officers. */
    private List<HDBOfficer> officers = new ArrayList<>();



    /**
     * Adds an {@link HDBOfficer} to the repository.
     *
     * @param officer the officer to add
     */
    @Override
    public void addUser(HDBOfficer officer) {
        officers.add(officer);
    }

    /**
     * Prompts user input to create a new officer and adds them to the repository.
     * Ensures no duplicate NRIC exists.
     */
    @Override
    public void createUser() {
        HDBOfficer officer = HDBOfficer.createUser();
        if (getUser(officer.getNric()) != null) {
            System.out.println("An officer with this NRIC already exists.");
            return;
        }
        officers.add(officer);
        System.out.println("HDB Officer registered successfully!");
    }

    /**
     * Deletes an officer based on their NRIC.
     *
     * @param nric the NRIC of the officer to delete
     */
    @Override
    public void deleteUser(String nric) {
        boolean removed = officers.removeIf(officer -> officer.getNric().equalsIgnoreCase(nric));
        if (removed) {
            System.out.println("User deleted");
        } else {
            System.out.println("User not found");
        }
    }


    /**
     * Retrieves an officer using their NRIC.
     *
     * @param nric the NRIC to search for
     * @return the {@link HDBOfficer} if found, otherwise {@code null}
     */
    @Override
    public HDBOfficer getUser(String nric) {
        for (HDBOfficer officer : officers) {
            if (officer.getNric().equalsIgnoreCase(nric)) {
                return officer;
            }
        }
        //System.out.println("No officer with such NRIC found"); Unsafe, shows up even if login fail
        return null;
    }


    /**
     * Gets the list of all registered officers.
     *
     * @return a list of {@link HDBOfficer} objects
     */
    public List<HDBOfficer> getAllOfficers() {
        return officers;
    }


    /**
     * Saves all officers to a CSV file.
     *
     * @param fileName the file path to save the CSV
     */
    @Override
    public void saveToCSV(String fileName) {
        try (java.io.PrintWriter writer = new java.io.PrintWriter(new java.io.File(fileName))) {
            writer.println("Name,NRIC,Age,MaritalStatus,Password");  // CSV header
            for (HDBOfficer officer : officers) {
                writer.printf("%s,%s,%d,%s,%s%n",
                    officer.getName(),
                    officer.getNric(),
                    officer.getAge(),
                    officer.isMarried() ? "Married" : "Single",
                    officer.getPassword()
                );
            }
            System.out.println("Officers saved to " + fileName);
        } catch (Exception e) {
            System.out.println("Error saving officers to CSV: " + e.getMessage());
        }
    }

    /**
     * Loads officer data from a CSV file.
     *
     * @param fileName the path to the CSV file
     */
    @Override
    public void loadFromCSV(String fileName) {
        try (java.util.Scanner sc = new java.util.Scanner(new java.io.File(fileName))) {
            if (sc.hasNextLine()) sc.nextLine(); // skip header
            while (sc.hasNextLine()) {
                String[] fields = sc.nextLine().split(",");
                if (fields.length == 5) {
                    String name = fields[0].trim();
                    String nric = fields[1].trim();
                    int age = Integer.parseInt(fields[2].trim());
                    boolean isMarried = Boolean.parseBoolean(fields[3].trim());
                    String password = fields[4].trim();

                    HDBOfficer officer = new HDBOfficer(name, nric, age, isMarried, password);
                    officers.add(officer);
                }
            }
            System.out.println("Officers loaded from " + fileName);
        } catch (Exception e) {
            System.out.println("Error loading officers from CSV: " + e.getMessage());
        }
    }


    /**
     * Displays all officers in the repository in a readable format.
     */
    public void printAllOfficers() {
        if (officers.isEmpty()) {
            System.out.println("No HDB Officers available.");
            return;
        }

        System.out.println("=== List of All HDB Officers ===");
        for (HDBOfficer officer : officers) {
            System.out.println(officer);
            System.out.println("----------------------------------");
        }
    }
}