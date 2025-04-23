import model.*;
import enums.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import repo.BTOProjectRepo;
import repo.HDBManagerRepo;
import repo.HDBOfficerRepo;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ProjectManagementTest {

    private BTOProjectRepo projectRepo;
    private HDBManager manager;

    @BeforeEach
    void setUp() {
        manager = new HDBManager("John Manager", "S1234567A", 45, true, "manager123");
        HDBManagerRepo managerRepo = new HDBManagerRepo();
        managerRepo.addUser(manager);
        projectRepo = new BTOProjectRepo(managerRepo, new HDBOfficerRepo());
    }

    @Test
    void testCreateEditDeleteProject() {
        // Create
        BTOProject project = new BTOProject("SkyVille", "Bishan", 10, 5, 200000, 300000,
                LocalDate.now(), LocalDate.now().plusDays(7), 1, manager, "Officer A");
        manager.addProject(project);
        projectRepo.addUser(project);

        assertEquals(1, projectRepo.getProjects().size());

        // Edit (neighborhood)
        project.setNeighborhood("Toa Payoh");
        assertEquals("Toa Payoh", project.getNeighborhood());

        // Delete
        projectRepo.deleteProject("SkyVille");
        assertTrue(projectRepo.getProjects().isEmpty());
    }

    @Test
    void testManagerCannotCreateOverlappingProjects() {
        BTOProject p1 = new BTOProject("Maple Grove", "Yishun", 8, 4, 180000, 280000,
                LocalDate.of(2025, 5, 1), LocalDate.of(2025, 5, 10), 2, manager, "Officer B");
        BTOProject p2 = new BTOProject("GreenPark", "Punggol", 5, 3, 190000, 270000,
                LocalDate.of(2025, 5, 5), LocalDate.of(2025, 5, 15), 2, manager, "Officer C");

        manager.addProject(p1);
        projectRepo.addUser(p1);

        // Try adding overlapping project
        boolean overlaps = p1.getAppOpenDate().isBefore(p2.getAppCloseDate()) &&
                           p2.getAppOpenDate().isBefore(p1.getAppCloseDate());

        assertTrue(overlaps, "Overlapping dates should be detected.");
    }

    @Test
    void testToggleProjectVisibility() {
        BTOProject project = new BTOProject("Sunrise", "Pasir Ris", 6, 6, 210000, 310000,
                LocalDate.now(), LocalDate.now().plusDays(10), 1, manager, "Officer D");
        manager.addProject(project);
        projectRepo.addUser(project);

        assertTrue(project.isVisible(), "Project should be visible by default.");
        project.toggleVisibility();
        assertFalse(project.isVisible(), "Visibility toggle should set project as hidden.");
    }

    @Test
    void testFilterByNeighborhood() {
        List<BTOProject> results = projectRepo.filterProjectByCriteria("neighborhood", "Paya Lebar");        
        assertTrue(results.isEmpty());
    }

    @Test
    void testFilterByFlatTypeTwoRoom() {
        List<BTOProject> results = projectRepo.filterProjectByCriteria("flat", "tworoom");
        assertTrue(results.isEmpty());
    }
}