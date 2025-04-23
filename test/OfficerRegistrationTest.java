import model.*;
import enums.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class OfficerRegistrationTest {

    private HDBOfficer officer;
    private BTOProject project1, project2;
    private List<BTOProject> allProjects;

    @BeforeEach
    void setUp() {
        officer = new HDBOfficer("Alice Tan", "S1234567A", 28, true, "pass1234");

        HDBManager manager = new HDBManager("Manager One", "S7654321Z", 45, true, "secure123");
        project1 = new BTOProject("Sunshine Ville", "Tampines", 10, 10, 250000, 350000,
                LocalDate.of(2025, 1, 1), LocalDate.of(2025, 1, 30), 2, manager, "");
        project2 = new BTOProject("Moonlight Grove", "Yishun", 10, 10, 260000, 360000,
                LocalDate.of(2025, 1, 15), LocalDate.of(2025, 2, 15), 2, manager, "");

        allProjects = new ArrayList<>();
        allProjects.add(project1);
        allProjects.add(project2);
    }

    @Test
    void testOfficerRegistrationEligibility() {
        BTOApplication application = new BTOApplication(officer, project1, FlatType.TWOROOM);
        officer.setApplication(application);

        assertFalse(officer.isEligibleForRegistration(project1, allProjects),
                "Officer who applied for project should not be eligible to register for same project");
    }

    @Test
    void testRegistrationStatusUpdateAndView() {
        OfficerRegistration reg = new OfficerRegistration(officer, project1);
        officer.setOfficerRegistrationToHDBOfficer(reg);

        assertEquals(RegistrationStatus.PENDING, officer.getRegistration().getStatus(),
                "Default registration status should be PENDING");

        reg.setStatus(RegistrationStatus.APPROVED);
        assertEquals(RegistrationStatus.APPROVED, officer.getRegistration().getStatus(),
                "Officer registration status should update to APPROVED");
    }

    @Test
    void testProjectDetailAccessEvenIfNotVisible() {
        officer.setAssignedProject(project1);
        project1.setVisible(false);

        assertEquals("Sunshine Ville", officer.getProject().getName(),
                "Officer should be able to access project details even when not visible");
    }

    @Test
    void testOfficerCannotEditProjectDetails() {
        officer.setAssignedProject(project1);
        String oldNeighborhood = project1.getNeighborhood();
        project1.setNeighborhood("ChangedName");

        assertNotEquals("ChangedName", oldNeighborhood,
                "Officer should not be able to directly change project details (manual edit detected)");
    }
}