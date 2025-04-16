import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class BTOProjectRepo {
    private List<BTOProject> projects;
    private static int projectCounter = 5830; //random starting value
    private ApplicantRepo applicantRepo;
    private HDBOfficerRepo officerRepo;
    private HDBManagerRepo managerRepo;
    
    BTOProjectRepo(ApplicantRepo a, HDBOfficerRepo o, HDBManagerRepo m){
        this.applicantRepo = a;
        this.officerRepo = o;
        this.managerRepo = m;
    }

    /*
     * create new project and add to project list
     */
    public void createProject(){
        String neighborhood;
        int twoRoomNo, threeRoomNo, twoRoomPrice, threeRoomPrice, noAvailableOffice;
        LocalDate appOpenDate, appCloseDate;
        Scanner sc = new Scanner(System.in);

        System.out.print("Please enter neighborhood:");
        neighborhood = sc.nextLine().trim();

        System.out.print("Enter number of two room flats");
        twoRoomNo = sc.nextInt();

        System.out.print("Enter number of three room flats");
        threeRoomNo = sc.nextInt();

        System.out.print("Enter price of two room flats");
        twoRoomPrice = sc.nextInt();

        System.out.print("Enter price of three room flats");
        threeRoomPrice = sc.nextInt();


        System.out.print("Enter opening date for application (yyyy-mm-dd)");
        appOpenDate = LocalDate.parse(sc.nextLine().trim());

        System.out.print("Enter closing date for application (yyyy-mm-dd)");
        appCloseDate = LocalDate.parse(sc.nextLine().trim());

        System.out.print("Enter number of available HDB Officer Slots: ");
        noAvailableOffice = sc.nextInt();

        BTOProject newProject = new BTOProject(projectCounter++, neighborhood, twoRoomNo, threeRoomNo, twoRoomPrice, threeRoomPrice, appOpenDate, appCloseDate, noAvailableOffice);
        projects.add(newProject);
    }

    /*
     * get BTOProject by projectId
     * returns project
     * return null if not found
     */
    public BTOProject getProject(int projectId){
        for (BTOProject project: projects){
            if (project.getId() == projectId)
                return project;
        }

        System.out.println("Project not found");
        return null;
    }

    /*
     * edits project details
     * TODO check if implemented properly
     */
    public void editProject(int projectId){
        BTOProject project = null;
        for (BTOProject p : projects){
            if (p.getId() == projectId){
                project = p;
                break;
            }
        }

        if (project == null)
            System.out.println("Project not found");
        else {
            project.displayInfo();
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

                choice = sc.nextInt();
                switch(choice){
                    case 1 -> {
                        System.out.print("Enter new neighborhood: ");
                        project.setNeighborhood(sc.nextLine().trim());
                    }

                    case 2 -> {
                        System.out.print("Enter new number of two room flats: ");
                        project.setTwoRoomNo(sc.nextInt());
                    }
                    
                    case 3 -> {
                        System.out.print("Enter new number of three room flats: ");
                        project.setThreeRoomNo(sc.nextInt());
                    }

                    case 4 -> {
                        System.out.print("Enter new price of two room flats: ");
                        project.setTwoRoomPrice(sc.nextInt());
                    }
                    
                    case 5 -> {
                        System.out.print("Enter new price of three room flats: ");
                        project.setThreeRoomPrice(sc.nextInt());
                    }

                    case 6 -> {
                        System.out.print("Enter new opening date for applications(yyyy-mm-dd): ");
                        project.setAppOpenDate(LocalDate.parse(sc.nextLine().trim()));
                    }

                    case 7 -> {
                        System.out.print("Enter new closing date for applications(yyyy-mm-dd): ");
                        project.setAppCloseDate(LocalDate.parse(sc.nextLine().trim()));
                    }

                    case 8 -> {
                        System.out.print("Enter new number of available HDB Officer Slots: ");
                        int numSlots = sc.nextInt();
                        if (numSlots >= 0)
                            project.setNoAvailableOffice(numSlots);
                        else 
                            System.out.println("Error negative number entered!");
                    }
}
            } while (choice != 9);
        }
    }

    /*
     * removes project from list
     * TODO check implementation, and check if this kind of data manipulation allowed
     */
    public void deleteProject(int projectId){
        projects.removeIf(project -> project.getId() == projectId);
    }

    public List<BTOProject> filterProject(){
        Scanner sc = new Scanner(System.in);
        String input;
        List<BTOProject> filteredProjects = new ArrayList<>();

        projects.sort((p1, p2) -> p1.getNeighborhood().compareTo(p2.getNeighborhood()));

        System.out.print("""
                Filter by
                1. Neighborhood
                2. Flat Type
                """);
        switch (sc.nextLine().trim()) {
            case "1" -> {
                System.out.print("Enter neighborhood name: ");
                input = sc.nextLine();
                for(BTOProject project : projects){
                    if(project.getNeighborhood().toLowerCase().contains(input.toLowerCase()))
                        filteredProjects.add(project);
                }
            }

            case "2" -> {
                System.out.println("a. Two Room Flats\nb. Three Room Flats");
                input = sc.nextLine().trim();
                if (input.equals("a"))
                    for(BTOProject project : projects){
                        if(project.getTwoRoomNo()>0)
                            filteredProjects.add(project);
                    }
                else if (input.equals("b"))
                    for(BTOProject project : projects){
                        if(project.getThreeRoomNo()>0)
                            filteredProjects.add(project);
                    }
            }
        }

        if (filteredProjects.isEmpty())
            return projects;
        return filteredProjects;
    }
}
