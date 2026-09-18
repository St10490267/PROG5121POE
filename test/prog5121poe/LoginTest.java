package prog5121poe;

import org.junit.Test;
import static org.junit.Assert.*;

public class LoginTest {

    @Test
    public void testLogin_Correct() {
        Registration reg = new Registration();
        reg.registerUser("Kb_12", "Ch@nge123", "+27821234567");
        Login login = new Login(reg);

        boolean result = login.loginUser("Kb_12", "Ch@nge123");
        assertTrue(result);
    }

    @Test
    public void testLogin_Incorrect_WrongPassword() {
        Registration reg = new Registration();
        reg.registerUser("Kb_12", "Ch@nge123", "+27821234567");
        Login login = new Login(reg);

        boolean result = login.loginUser("Kb_12", "WrongPass1");
        assertFalse(result);
    }

    @Test
    public void testLoginStatusMessage_Correct() {
        Registration reg = new Registration();
        reg.registerUser("Kb_12", "Ch@nge123", "+27821234567");
        Login login = new Login(reg);

        String result = login.returnLoginStatus("Kb_12", "Ch@nge123");
        assertEquals("Welcome Kb_12, it is great to see you again.", result);
    }

    @Test
    public void testLoginStatusMessage_Incorrect() {
        Registration reg = new Registration();
        reg.registerUser("Kb_12", "Ch@nge123", "+27821234567");
        Login login = new Login(reg);

        String result = login.returnLoginStatus("Kb_12", "WrongPass1");
        assertEquals("Username or password incorrect, please try again.", result);
    }
}