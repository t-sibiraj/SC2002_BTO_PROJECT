import enums.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import model.*;

import java.time.LocalDate;

public class BookingTest {

    private HDBOfficer officer;
    private Applicant applicant;
    private BTOProject project;

    @BeforeEach
    void setUp() {
        officer = new HDBOfficer("Alice Officer", "S1234567A", 35, true, "Password123");
        applicant = new Applicant("Bob Applicant", "S7654321B", 36, false, "SecurePass1");

        project = new BTOProject(
            "Maple Grove", "Punggol",
            1, 1,
            300000, 400000,
            LocalDate.now().minusDays(1), LocalDate.now().plusDays(5),
            1, new HDBManager("Manager Name", "S4567890C", 45, true, "ManagerPass1"),
            "Alice Officer"
        );

        officer.setAssignedProject(project);
        BTOApplication application = new BTOApplication(applicant, project, FlatType.TWOROOM);
        application.setStatus(ApplicationStatus.SUCCESSFUL);
        applicant.setApplication(application);
        project.addApplication(application);
    }

    @Test
    void testFlatBookingUpdatesAvailabilityAndStatus() {
        assertEquals(1, project.getTwoRoomNo());
        officer.bookFlat(applicant);
        assertEquals(0, project.getTwoRoomNo());
        assertEquals(ApplicationStatus.BOOKED, applicant.getApplication().getApplicationStatus());
        assertNotNull(applicant.getApplication().getFlatBooking());
    }

    @Test
    void testGenerateBookingReceipt() {
        officer.bookFlat(applicant);
        assertDoesNotThrow(() -> officer.generateBookingReceipt(applicant));
    }
}