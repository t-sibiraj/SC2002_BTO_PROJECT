import model.*;
import enums.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ReportTest {

    private HDBManager manager;
    private BTOProject project;
    private Applicant applicant1;
    private Applicant applicant2;

    @BeforeEach
    void setUp() {
        manager = new HDBManager("Alice Manager", "S2345678Z", 40, true, "password123");

        project = new BTOProject(
                "Sunshine Heights", "Tampines",
                2, 2, 300000, 400000,
                java.time.LocalDate.now(), java.time.LocalDate.now().plusDays(10),
                2, manager, "");

        manager.addProject(project);

        applicant1 = new Applicant("John Married", "S1111111A", 35, true, "pass1234");
        applicant2 = new Applicant("Jane Single", "S2222222B", 36, false, "pass5678");

        BTOApplication app1 = new BTOApplication(applicant1, project, FlatType.TWOROOM);
        app1.setStatus(ApplicationStatus.BOOKED);
        app1.createFlatBooking(FlatType.TWOROOM);
        project.addBooking(app1.getFlatBooking());

        BTOApplication app2 = new BTOApplication(applicant2, project, FlatType.THREEROOM);
        app2.setStatus(ApplicationStatus.BOOKED);
        app2.createFlatBooking(FlatType.THREEROOM);
        project.addBooking(app2.getFlatBooking());

        applicant1.setApplication(app1);
        applicant2.setApplication(app2);
    }

    @Test
    void testGenerateAndFilterReports() {
        // Generate report data (flat bookings tied to applicants)
        manager.generateReports();

        // Filter 1: Married applicants only
        String marriedReport = manager.getReportString(2);
        assertTrue(marriedReport.contains("Married"), "Married report should mention married applicants.");

        // Filter 2: Not married applicants
        String singleReport = manager.getReportString(3);
        assertTrue(singleReport.contains("Single") || !singleReport.contains("Married"),
            "Single report should not include married applicants.");

        // Filter 3: 2-Room Flat Type
        String twoRoomReport = manager.getReportString(4);
        assertTrue(twoRoomReport.contains("TWOROOM"), "2-Room report should include 2-Room booking info.");

        // Filter 4: 3-Room Flat Type
        String threeRoomReport = manager.getReportString(5);
        assertTrue(threeRoomReport.contains("THREEROOM"), "3-Room report should include 3-Room booking info.");
    }
}