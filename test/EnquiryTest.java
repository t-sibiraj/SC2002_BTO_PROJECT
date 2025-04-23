import model.*;
import enums.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class EnquiryTest {

    private Applicant applicant;
    private HDBOfficer officer;
    private BTOProject project;

    @BeforeEach
    void setUp() {
        applicant = new Applicant("Alice Tan", "S1234567A", 28, false, "Password1");
        officer = new HDBOfficer("Bob Lim", "S7654321Z", 35, true, "Password2");
        project = new BTOProject("Skyline Heights", "Bishan",
                10, 5, 300000, 400000,
                java.time.LocalDate.now(), java.time.LocalDate.now().plusDays(30),
                2, new HDBManager("Manager One", "S1111111A", 50, true, "Password3"),
                "Bob Lim");
    }

    @Test
    void testApplicantSubmitEnquiry() {
        Enquiry enquiry = new Enquiry(applicant, project, "When will application results be out?");
        applicant.addEnquiry(enquiry);
        project.addEnquiry(enquiry);

        List<Enquiry> enquiries = applicant.getEnquiries();
        assertEquals(1, enquiries.size());
        assertEquals("When will application results be out?", enquiries.get(0).getMessage());
    }

    @Test
    void testApplicantEditEnquiry() {
        Enquiry enquiry = new Enquiry(applicant, project, "Initial message");
        applicant.addEnquiry(enquiry);
        project.addEnquiry(enquiry);

        applicant.editEnquiry(0, "Updated enquiry message");
        assertEquals("Updated enquiry message", applicant.getEnquiries().get(0).getMessage());
    }

    @Test
    void testApplicantDeleteEnquiry() {
        Enquiry enquiry = new Enquiry(applicant, project, "Will there be a show flat?");
        applicant.addEnquiry(enquiry);
        project.addEnquiry(enquiry);

        applicant.deleteEnquiry(0);
        assertTrue(applicant.getEnquiries().isEmpty());
    }

    @Test
    void testOfficerReplyToEnquiry() {
        officer.setAssignedProject(project);
        Enquiry enquiry = new Enquiry(applicant, project, "What is the price for 3-Room?");
        project.addEnquiry(enquiry);

        officer.replyToEnquiry(enquiry, "The price is $400,000.");
        assertTrue(enquiry.isReplied());
        assertEquals("The price is $400,000.", enquiry.getReply());
    }

    @Test
    void testOfficerCannotReplyIfNotAssignedToProject() {
        Enquiry enquiry = new Enquiry(applicant, project, "Is parking included?");
        project.addEnquiry(enquiry);

        officer.replyToEnquiry(enquiry, "Yes, parking is available.");
        assertFalse(enquiry.isReplied(), "Officer should not be able to reply if not assigned to project");
    }
}