// File: test/ApplicationTest.java
import model.*;
import enums.*;
import org.junit.jupiter.api.*;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ApplicationTest {

    private BTOProject visibleProject, hiddenProject;
    private Applicant marriedApplicant, singleYoungApplicant, singleOldApplicant;
    
    @BeforeEach
    void setUp() {
        visibleProject = new BTOProject("SkyVille", "Tampines", 10, 5, 300000, 400000,
                LocalDate.now().minusDays(1), LocalDate.now().plusDays(5),
                2, new HDBManager("Mark One", "S1234567A", 40, true, "adminpass"), "Daniel");

        hiddenProject = new BTOProject("HiddenHeights", "Bedok", 10, 5, 300000, 400000,
                LocalDate.now().minusDays(1), LocalDate.now().plusDays(5),
                2, new HDBManager("Mark Two", "S1234567B", 42, true, "adminpass2"), "");
        hiddenProject.toggleVisibility();

        marriedApplicant = new Applicant("John Smith", "S3456789A", 30, true, "password123");
        singleOldApplicant = new Applicant("Lily Tan", "S4567890B", 36, false, "password123");
        singleYoungApplicant = new Applicant("Tom Lee", "S5678901C", 25, false, "password123");
    }

    @Test
    void testProjectVisibilityBasedOnUserGroup() {
        assertTrue(visibleProject.isVisible());
        assertFalse(hiddenProject.isVisible());
    }

    @Test
    void testValidProjectApplication() {
        // Eligible applicant should be able to apply
        FlatType chosenType = FlatType.TWOROOM;
        BTOApplication app = new BTOApplication(marriedApplicant, visibleProject, chosenType);
        marriedApplicant.setApplication(app);
        visibleProject.addApplication(app);

        assertEquals(ApplicationStatus.PENDING, app.getApplicationStatus());
        assertEquals(1, visibleProject.getApplications().size());
    }

    @Test
    void testApplicationStatusViewAfterToggleOff() {
        // Application should still be viewable even if project is hidden later
        FlatType chosenType = FlatType.TWOROOM;
        BTOApplication app = new BTOApplication(singleOldApplicant, visibleProject, chosenType);
        singleOldApplicant.setApplication(app);
        visibleProject.addApplication(app);
        visibleProject.toggleVisibility();  // now hidden

        assertEquals("SkyVille", singleOldApplicant.getApplication().getProject().getName());
        assertEquals(ApplicationStatus.PENDING, singleOldApplicant.getApplication().getApplicationStatus());
    }

    @Test
    void testSingleFlatBookingRestriction() {
        BTOApplication app = new BTOApplication(marriedApplicant, visibleProject, FlatType.TWOROOM);
        HDBOfficer officer = new HDBOfficer("Daniel", "S1234567B", 42, true, "adminpass2");
        marriedApplicant.setApplication(app);
        app.setStatus(ApplicationStatus.SUCCESSFUL);

        officer.setAssignedProject(visibleProject);
        officer.bookFlat(marriedApplicant);

        // Try booking again
        assertThrows(IllegalStateException.class, () -> {
            officer.bookFlat(marriedApplicant);
        });

        assertEquals(ApplicationStatus.BOOKED, app.getApplicationStatus());
        assertNotNull(app.getFlatBooking());
    }
}