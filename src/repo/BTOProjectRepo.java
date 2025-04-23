package repo;

import enums.RegistrationStatus;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import model.*;

/**
 * Repository class that manages BTO projects in the system.
 * Handles project creation, editing, deletion, filtering, and CSV persistence.
 */
public class BTOProjectRepo implements IRepo {

    /** List of all BTO projects currently managed. */
    private List<BTOProject> projects;

    /** Repository holding all HDB managers, used for linking during load. */
    private HDBManagerRepo managerRepo;

    /** Repository holding all HDB officers, used for linking during load. */
    private HDBOfficerRepo officerRepo;
 
    /**
     * Constructs a new BTOProjectRepo with manager and officer dependencies.
     *
     * @param managerRepo The repository of HDB managers.
     * @param officerRepo The repository of HDB officers.
     */
    public BTOProjectRepo(HDBManagerRepo managerRepo, HDBOfficerRepo officerRepo){
        this.projects = new ArrayList<>();
        this.managerRepo = managerRepo;
        this.officerRepo = officerRepo;
    }


    /**
     * Returns all loaded BTO projects.
     *
     * @return List of {@link BTOProject} instances.
     */
    public List<BTOProject> getProjects() {
        return projects;
    }

    /**
     * Adds a BTO project to the repository.
     *
     * @param project The project to add.
     */
    public void addUser(BTOProject project) {
        projects.add(project);
    }


    /**
     * Prompts input and creates a new BTO project linked to the given manager.
     *
     * @param manager The manager responsible for this project.
     */
    public void createProject(HDBManager manager){
        String neighborhood, projectName;
        int twoRoomNo, threeRoomNo, twoRoomPrice, threeRoomPrice, noAvailableOffice;
        LocalDate appOpenDate, appCloseDate;
        Scanner sc = new Scanner(System.in);

        System.out.print("Please enter the project name: ");
        projectName = sc.nextLine().trim();

        System.out.print("Please enter neighborhood: ");
        neighborhood = sc.nextLine().trim();

        System.out.print("Enter number of two room flats: ");
        twoRoomNo = sc.nextInt();

        System.out.print("Enter number of three room flats: ");
        threeRoomNo = sc.nextInt();

        System.out.print("Enter price of two room flats: ");
        twoRoomPrice = sc.nextInt();

        System.out.print("Enter price of three room flats: ");
        threeRoomPrice = sc.nextInt();

        sc.nextLine();

        System.out.print("Enter opening date for application (yyyy-mm-dd): ");
        appOpenDate = LocalDate.parse(sc.nextLine().trim());

        System.out.print("Enter closing date for application (yyyy-mm-dd): ");
        appCloseDate = LocalDate.parse(sc.nextLine().trim());

        System.out.print("Enter number of available HDB Officer Slots: ");
        noAvailableOffice = sc.nextInt();

        sc.nextLine();

        System.out.print("Enter The Officer Names: ");
        String officerAssignedAsString = sc.nextLine().trim();


        BTOProject newProject = new BTOProject(projectName, neighborhood, twoRoomNo, threeRoomNo, twoRoomPrice, threeRoomPrice, appOpenDate, appCloseDate, noAvailableOffice, manager, officerAssignedAsString);
        projects.add(newProject);
    }


    /**
     * Retrieves a BTO project by its name (case-insensitive).
     *
     * @param projectName The name of the project.
     * @return The matching project or null if not found.
     */
    public BTOProject getProject(String projectName) {
        for (BTOProject project : projects) {
            if (project.getName().equalsIgnoreCase(projectName)) {
                return project;
            }
        }
        System.out.println("Project not found");
        return null;
    }


    /**
     * Prints all projects to the console, including their details.
     */
    public void printAllProjects() {
        if (projects.isEmpty()) {
            System.out.println("No BTO projects available.");
            return;
        }

        //System.out.println("=== List of All BTO Projects ===");
        for (BTOProject project : filterProject()) {
            System.out.println(project);
            System.out.println("------------------------------------------");
        }
    }


    /**
     * Edits project attributes such as flat counts, prices, dates, and officer slots.
     *
     * @param projectName The name of the project to edit.
     */
    public void editProject(String projectName) {
        BTOProject project = null;
        for (BTOProject p : projects) {
            if (p.getName().equalsIgnoreCase(projectName)) {
                project = p;
                break;
            }
        }

        if (project == null) {
            System.out.println("Project not found");
            return;
        }

        System.out.println("Selected Project:");
        System.out.println(project);
        int choice;
        Scanner sc = new Scanner(System.in);

        do {
            System.out.print("""
                    1. Neighborhood
                    2. Number of two room flats
                    3. Number of three room flats
                    4. Price of two room flats
                    5. Price of three room flats
                    6. Opening date for applications
                    7. Closing date for applications
                    8. Number of available HDB Officer Slots
                    9. Exit
                    Choose which to change:
                    """);

            while (!sc.hasNextInt()) {
                System.out.print("Please enter a valid number: ");
                sc.next();
            }
            choice = sc.nextInt();
            sc.nextLine(); // consume newline

            switch (choice) {
                case 1 -> {
                    System.out.print("Enter new neighborhood: ");
                    project.setNeighborhood(sc.nextLine().trim());
                }
                case 2 -> {
                    System.out.print("Enter new number of two room flats: ");
                    project.setTwoRoomNo(sc.nextInt());
                    sc.nextLine();
                }
                case 3 -> {
                    System.out.print("Enter new number of three room flats: ");
                    project.setThreeRoomNo(sc.nextInt());
                    sc.nextLine();
                }
                case 4 -> {
                    System.out.print("Enter new price of two room flats: ");
                    project.setTwoRoomPrice(sc.nextInt());
                    sc.nextLine();
                }
                case 5 -> {
                    System.out.print("Enter new price of three room flats: ");
                    project.setThreeRoomPrice(sc.nextInt());
                    sc.nextLine();
                }
                case 6 -> {
                    System.out.print("Enter new opening date for applications (yyyy-mm-dd): ");
                    project.setAppOpenDate(LocalDate.parse(sc.nextLine().trim()));
                }
                case 7 -> {
                    System.out.print("Enter new closing date for applications (yyyy-mm-dd): ");
                    project.setAppCloseDate(LocalDate.parse(sc.nextLine().trim()));
                }
                case 8 -> {
                    System.out.print("Enter new number of available HDB Officer Slots: ");
                    int numSlots = sc.nextInt();
                    sc.nextLine();
                    if (numSlots >= 0)
                        project.setNoAvailableOffice(numSlots);
                    else
                        System.out.println("Error: Negative number entered!");
                }
                case 9 -> System.out.println("Returning to menu...");
                default -> System.out.println("Invalid choice. Try again.");
            }
            System.out.println("");
        } while (choice != 9);
    }

    /**
     * Deletes a project by name.
     *
     * @param projectName The name of the project to delete.
     */
    public void deleteProject(String projectName){
        projects.removeIf(project -> project.getName().equals(projectName));
    }

    /**
     * Filters projects based on neighborhood or available flat types.
     *
     * @return A filtered list of {@link BTOProject} instances, or all if none match.
     */
    public List<BTOProject> filterProject() {
        Scanner sc = new Scanner(System.in);
        List<BTOProject> filteredProjects = new ArrayList<>();

        projects.sort((p1, p2) -> p1.getNeighborhood().compareToIgnoreCase(p2.getNeighborhood()));

        System.out.println("Filter by:");
        System.out.println("1. No Filter");
        System.out.println("2. Neighbourhood");
        System.out.println("3. Flat Type");
        System.out.print("Enter choice: ");
        String choice = sc.nextLine().trim();

        switch (choice) {
            case "2" -> {
                System.out.print("Enter neighborhood name: ");
                String input = sc.nextLine().trim().toLowerCase();
                for (BTOProject project : projects) {
                    if (project.getNeighborhood().toLowerCase().contains(input)) {
                        filteredProjects.add(project);
                    }
                }
            }

            case "3" -> {
                System.out.println("a. Two Room Flats");
                System.out.println("b. Three Room Flats");
                System.out.print("Enter choice: ");
                String input = sc.nextLine().trim().toLowerCase();

                if (input.equals("a")) {
                    for (BTOProject project : projects) {
                        if (project.getTwoRoomNo() > 0) {
                            filteredProjects.add(project);
                        }
                    }
                } else if (input.equals("b")) {
                    for (BTOProject project : projects) {
                        if (project.getThreeRoomNo() > 0) {
                            filteredProjects.add(project);
                        }
                    }
                } else {
                    System.out.println("Invalid flat type option.");
                }
            }

            default -> {
                System.out.println("\nNo filter applied.");
                System.out.println("=== List of All BTO Projects ===");
                return projects;
            }
        }

        if (filteredProjects.isEmpty()) {
            System.out.println("\n=== No projects matched your criteria. Showing all ===");
            return projects;
        }

        System.out.println("\n=== List of Filtered BTO Projects ===");
        return filteredProjects;
    }

    /**
     * Filters BTO projects based on the given criteria type and value.
     *
     * Supported filter types:
     * - "neighborhood": Filters projects whose neighborhood names contain the given value (case-insensitive).
     * - "flat": Filters projects based on flat availability:
     *     - "tworoom": Projects with at least one 2-room flat available.
     *     - "threeroom": Projects with at least one 3-room flat available.
     *
     * @param type  The type of filter to apply ("neighborhood" or "flat").
     * @param value The value to filter by (e.g., "Yishun" for neighborhood, "tworoom" or "threeroom" for flat type).
     * @return A list of BTOProject instances that match the filter criteria.
     */
    public List<BTOProject> filterProjectByCriteria(String type, String value) {
        List<BTOProject> filteredProjects = new ArrayList<>();

        switch (type.toLowerCase()) {
            case "neighborhood" -> {
                for (BTOProject project : projects) {
                    if (project.getNeighborhood().toLowerCase().contains(value.toLowerCase())) {
                        filteredProjects.add(project);
                    }
                }
            }

            case "flat" -> {
                switch (value.toLowerCase()) {
                    case "tworoom" -> {
                        for (BTOProject project : projects) {
                            if (project.getTwoRoomNo() > 0) {
                                filteredProjects.add(project);
                            }
                        }
                    }

                    case "threeroom" -> {
                        for (BTOProject project : projects) {
                            if (project.getThreeRoomNo() > 0) {
                                filteredProjects.add(project);
                            }
                        }
                    }

                    default -> System.out.println("Invalid flat type filter value: " + value);
                }
            }

            default -> System.out.println("Invalid filter type: " + type);
        }

        return filteredProjects;
    }


    /**
     * Saves all projects to a CSV file.
     *
     * @param fileName The file to save to.
     */
    @Override
    public void saveToCSV(String fileName) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(fileName))) {
        writer.println("Project Name,Neighborhood,Type 1,Number of units for Type 1,Selling price for Type 1,Type 2,Number of units for Type 2,Selling price for Type 2,Application opening date,Application closing date,Manager,Officer Slot,Officer");

        for (BTOProject p : projects) {
            writer.printf("%s,%s,%s,%d,%d,%s,%d,%d,%s,%s,%s,%d,\"%s\"%n",
                p.getName(),
                p.getNeighborhood(),
                "2-Room",
                p.getTwoRoomNo(),
                p.getTwoRoomPrice(),
                "3-Room",
                p.getThreeRoomNo(),
                p.getThreeRoomPrice(),
                p.getAppOpenDate().format(DateTimeFormatter.ofPattern("d/M/yy")),
                p.getAppCloseDate().format(DateTimeFormatter.ofPattern("d/M/yy")),
                p.getHDBManagerName(),
                p.getNoAvailableOffice(),
                p.getOfficerAssignedAsString()
            );
        }

            System.out.println("Projects saved to " + fileName);
        } catch (IOException e) {
            System.out.println("Error saving projects: " + e.getMessage());
        }
    }

    /**
     * Loads BTO projects from a CSV file, including manager and officer registrations.
     *
     * @param fileName The CSV file to read from.
     */
    @Override
    public void loadFromCSV(String fileName) {
        try (Scanner sc = new Scanner(new File(fileName))) {
            if (sc.hasNextLine()) sc.nextLine(); // Skip header

            while (sc.hasNextLine()) {
                String[] tokens = sc.nextLine().split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)", -1);
                if (tokens.length < 13) {
                    System.out.println("Invalid CSV line: " + Arrays.toString(tokens));
                    continue;
                }

                String name = tokens[0].trim();
                String neighborhood = tokens[1].trim();
                int twoRoomNo = Integer.parseInt(tokens[3].trim());
                int twoRoomPrice = Integer.parseInt(tokens[4].trim());
                int threeRoomNo = Integer.parseInt(tokens[6].trim());
                int threeRoomPrice = Integer.parseInt(tokens[7].trim());
                LocalDate appOpenDate = LocalDate.parse(tokens[8].trim(), DateTimeFormatter.ofPattern("d/M/yy"));
                LocalDate appCloseDate = LocalDate.parse(tokens[9].trim(), DateTimeFormatter.ofPattern("d/M/yy"));
                String managerName = tokens[10].trim();
                int officerSlots = Integer.parseInt(tokens[11].trim());
                // System.err.println("TOEKN "+ tokens[12]); //COMMENT
                String officerAssigned = tokens[12].trim().replaceAll("\"", ""); // Remove quotes

                // Find manager object
                HDBManager manager = null;
                for (HDBManager m : managerRepo.getAllManagers()) {
                    // System.out.println("INSIDE FIND"+ m.getName());
                    if (m.getName().equalsIgnoreCase(managerName)) {
                        manager = m;
                        break;
                    }
                }

                if (manager == null) {
                    System.out.println("Manager not found: " + managerName);
                    continue;
                }

                // Create project
                BTOProject project = new BTOProject(name, neighborhood, twoRoomNo, threeRoomNo,
                        twoRoomPrice, threeRoomPrice, appOpenDate, appCloseDate, officerSlots, manager, officerAssigned);

                // Split and attach officer registrations
                // Split and attach officer registrations
                String[] officerNames = officerAssigned.split(",");
                // System.out.println("Officers Assigned"+ officerAssigned); //comment
                // System.out.println("Officer names array: " + java.util.Arrays.toString(officerNames)); //comment
                for (String officerName : officerNames) {
                    officerName = officerName.trim();
                    // System.out.println("Looking for officer name: [" + officerName + "]"); //comment

                    boolean found = false;

                    for (HDBOfficer o : officerRepo.getAllOfficers()) {
                        // System.out.println("Comparing with officer object: [getName() = " + o.getName() + ", getNRIC() = " + o.getNric() + "]"); //comment

                        if (o.getName().equalsIgnoreCase(officerName)) {
                            // System.out.println("Match found for: " + officerName); //comment

                            OfficerRegistration reg = new OfficerRegistration(o, project);
                            reg.setStatus(RegistrationStatus.APPROVED); // Assume approved if already in file
                            project.addRegistration(reg);
                            o.setAssignedProject(project);
                            o.setOfficerRegistrationToHDBOfficer(reg); //REMEMBER
                            // REMEMBER 8 - HERE WE CAN DO VIEW STATUS OF HTE REGISTRATION ALSO

                            found = true;
                            break;
                        }
                    }

                    if (!found) {
                        System.out.println("No match found for officer: " + officerName);
                    }
                }
                project.updateOfficerAssignedString(); 
                projects.add(project);
            }
            System.out.println("Projects loaded from " + fileName);
        } catch (Exception e) {
            System.out.println("Failed to load projects from CSV: " + e.getMessage());
        }
    }

}
