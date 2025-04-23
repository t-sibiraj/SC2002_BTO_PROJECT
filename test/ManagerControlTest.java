import model.*;
import enums.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class ManagerControlTest {

    private HDBManager manager;
    private HDBOfficer officer;
    private BTOProject project;
    private BTOApplication application;

    @BeforeEach
    void setUp() {
        manager = new HDBManager("Alice", "S1234567A", 40, true, "adminpass");
        officer = new HDBOfficer("Bob", "S9876543Z", 30, false, "officerpass");
        project = new BTOProject(
                "Serangoon Heights", "Serangoon", 5, 3, 300000, 400000,
                LocalDate.now().minusDays(5), LocalDate.now().plusDays(5), 2, manager, "");

        OfficerRegistration registration = new OfficerRegistration(officer, project);
        project.addRegistration(registration);
        officer.setOfficerRegistrationToHDBOfficer(registration);

        application = new BTOApplication(officer, project, FlatType.TWOROOM);
        project.addApplication(application);
        officer.setApplication(application);
    }

    @Test
    void testManagerCanApproveOfficerRegistration() {
        OfficerRegistration registration = officer.getRegistration();
        assertEquals(RegistrationStatus.PENDING, registration.getStatus());

        manager.updateRegistration(true, registration);
        assertEquals(RegistrationStatus.APPROVED, registration.getStatus());
    }

    @Test
    void testManagerCanRejectOfficerRegistration() {
        OfficerRegistration registration = officer.getRegistration();
        assertEquals(RegistrationStatus.PENDING, registration.getStatus());

        manager.updateRegistration(false, registration);
        assertEquals(RegistrationStatus.REJECTED, registration.getStatus());
    }

    @Test
    void testApproveBTOApplication() {
        assertEquals(ApplicationStatus.PENDING, application.getApplicationStatus());

        manager.updateApplication(true, application);
        assertEquals(ApplicationStatus.SUCCESSFUL, application.getApplicationStatus());
    }

    @Test
    void testRejectBTOApplication() {
        assertEquals(ApplicationStatus.PENDING, application.getApplicationStatus());

        manager.updateApplication(false, application);
        assertEquals(ApplicationStatus.UNSUCCESSSFUL, application.getApplicationStatus());
    }
}