package repo;

import java.util.ArrayList;
import java.util.List;
import model.*;

public class ApplicantRepo implements IUserRepo {
    private List<Applicant> applicants = new ArrayList<>();


    // ======================
    // ALL THE CREATE, DELETE , GET AND ADD METHODS
    // ======================
    @Override
    public void createUser() {
        Applicant newApplicant = Applicant.createUser();
        if (getUser(newApplicant.getNric()) != null) {
            System.out.println("An applicant with this NRIC already exists");
            return;
        }
        applicants.add(newApplicant);
        System.out.println("Applicant registered successfully");
    }

   
    @Override
    public void deleteUser(String nric) {
        boolean removed = applicants.removeIf(applicant -> applicant.getNric().equalsIgnoreCase(nric));
        if (removed) {
            System.out.println("User deleted");
        } else {
            System.out.println("User not found");
        }
    }

   
    @Override
    public Applicant getUser(String nric) {
        for (Applicant applicant : applicants) {
            if (applicant.getNric().equalsIgnoreCase(nric)) {
                return applicant;
            }
        }
        //System.out.println("No applicant with such NRIC found"); Unsafe, shows up even if login fail
        return null;
    }

    public void addUser(Applicant applicant) {
        applicants.add(applicant);
    }

   
    public List<Applicant> getAllApplicants() {
        return applicants;
    }


    // ======================
    // PRINT METHOD
    // ======================
    public void printAllApplicants() {
    if (applicants.isEmpty()) {
        System.out.println("No applicants available.");
        return;
    }

    System.out.println("=== List of All Applicants ===");
    for (Applicant applicant : applicants) {
        System.out.println(applicant);
        System.out.println("----------------------------------");
    }
}


    // ======================
    // CSV METHODS
    // ======================
    @Override
    public void saveToCSV(String fileName) {
        try (java.io.PrintWriter writer = new java.io.PrintWriter(new java.io.File(fileName))) {
            writer.println("Name,NRIC,Age,MaritalStatus,Password");
            for (Applicant a : applicants) {
                writer.printf("%s,%s,%d,%s,%s%n",
                    a.getName(),
                    a.getNric(),
                    a.getAge(),
                    a.isMarried() ? "Married" : "Single",
                    a.getPassword()
                );
            }
            System.out.println("Applicant data saved to " + fileName);
        } catch (Exception e) {
            System.out.println("Failed to save applicants to CSV: " + e.getMessage());
        }
    }


    @Override
    public void loadFromCSV(String fileName) {
        try (java.util.Scanner sc = new java.util.Scanner(new java.io.File(fileName))) {
            if (sc.hasNextLine()) sc.nextLine(); // Skip header
            while (sc.hasNextLine()) {
                String[] tokens = sc.nextLine().split(",");
                if (tokens.length == 5) {
                    String name = tokens[0].trim();
                    String nric = tokens[1].trim();
                    int age = Integer.parseInt(tokens[2].trim());
                    boolean isMarried = tokens[3].trim().equalsIgnoreCase("Married");
                    String password = tokens[4].trim();

                    Applicant applicant = new Applicant(name, nric, age, isMarried, password);
                    applicants.add(applicant);
                }
            }
            System.out.println("Applicants loaded from " + fileName);
        } catch (Exception e) {
            System.out.println("Failed to load applicants from CSV: " + e.getMessage());
        }
    }

}