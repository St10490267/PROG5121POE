package prog5121poe;

import org.junit.Test;
import static org.junit.Assert.*;

public class RegistrationTest {

    // ----- USERNAME TESTS -----
    @Test
    public void testUsername_Correct() {
        Registration reg = new Registration();
        boolean result = reg.checkUsername("Kb_12");
        assertTrue(result);
    }

    @Test
    public void testUsername_Incorrect_NoUnderscore() {
        Registration reg = new Registration();
        boolean result = reg.checkUsername("Kobus1");
        assertFalse(result);
    }

    @Test
    public void testUsername_Incorrect_TooLong() {
        Registration reg = new Registration();
        boolean result = reg.checkUsername("Kb_1234");
        assertFalse(result);
    }

    // ----- PASSWORD TESTS -----
    @Test
    public void testPassword_Correct() {
        Registration reg = new Registration();
        boolean result = reg.checkPasswordComplexity("Ch@nge123");
        assertTrue(result);
    }

    @Test
    public void testPassword_Incorrect_NoSpecialChar() {
        Registration reg = new Registration();
        boolean result = reg.checkPasswordComplexity("Change123");
        assertFalse(result);
    }

    @Test
    public void testPassword_Incorrect_TooShort() {
        Registration reg = new Registration();
        boolean result = reg.checkPasswordComplexity("C@1a");
        assertFalse(result);
    }

    // ----- CELLPHONE TESTS -----
    @Test
    public void testCellphone_Correct() {
        Registration reg = new Registration();
        boolean result = reg.checkCellPhoneNumber("+27821234567");
        assertTrue(result);
    }

    @Test
    public void testCellphone_Incorrect_NoCountryCode() {
        Registration reg = new Registration();
        boolean result = reg.checkCellPhoneNumber("0821234567");
        assertFalse(result);
    }

    @Test
    public void testCellphone_Incorrect_TooManyDigits() {
        Registration reg = new Registration();
        boolean result = reg.checkCellPhoneNumber("+278212345678");
        assertFalse(result);
    }
}