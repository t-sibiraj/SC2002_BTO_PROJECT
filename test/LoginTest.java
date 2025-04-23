import model.User;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class LoginTest {

    @Test
    void testValidLogin() {
        User user = new User("Alice", "S1234567A", 30, false, "password123");
        assertTrue(user.checkPassword("password123"), "Valid login should succeed.");
    }

    @Test
    void testRejectInvalidNricFormat() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new User("Test", "1234567A", 30, false, "password123");
        });

        assertTrue(exception.getMessage().contains("Invalid NRIC format"));
    }


    @Test
    void testEmptyPasswordFails() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new User("Charlie", "T1234567B", 25, false, "");
        });
        assertEquals("Invalid password. Must be at least 8 characters long and contain only letters and digits.", exception.getMessage());
    }


    @Test
    void testPasswordChangeFunctionality() {
        User user = new User("Eve", "S1234567Z", 30, false, "initialPass1");
        assertTrue(user.checkPassword("initialPass1"), "Initial password should be valid");
        user.setPassword("newSecure8");
        assertTrue(user.checkPassword("newSecure8"), "New password should be valid");
        assertFalse(user.checkPassword("initialPass1"), "Old password should no longer work");
    }
}