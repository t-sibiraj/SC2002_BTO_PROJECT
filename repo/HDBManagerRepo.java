package repo;

import java.util.ArrayList;
import java.util.List;
import model.HDBManager;

public class HDBManagerRepo implements IUserRepo {
    private List<HDBManager> managers = new ArrayList<>();

    public void addUser(HDBManager manager) {
        managers.add(manager);
    }

    // ======================
    // ALL THE CREATE, DELETE , GET AND ADD METHODS
    // ======================
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

    @Override
    public void deleteUser(String nric) {
        boolean removed = managers.removeIf(manager -> manager.getNric().equalsIgnoreCase(nric));
        if (removed) {
            System.out.println("User deleted");
        } else {
            System.out.println("User not found");
        }
    }

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

    public List<HDBManager> getAllManagers() {
        return managers;
    }


    // ======================
    // CSV METHODS
    // ======================
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

    // ======================
    // PRINT METHOD
    // ======================
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