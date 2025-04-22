package model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class UserTest {

    @Test
    void testCheckPasswordSuccess() {
        User user = new User("Alice", "S1234567A", 28, false, "password123");
        assertTrue(user.checkPassword("password123"));
    }

    @Test
    void testCheckPasswordFail() {
        User user = new User("Bob", "S7654321Z", 35, true, "secret");
        assertFalse(user.checkPassword("wrongpass"));
    }

    @Test
    void testPasswordUpdate() {
        User user = new User("Charlie", "S9876543T", 40, false, "init");
        user.setPassword("newpass");
        assertTrue(user.checkPassword("newpass"));
    }
}