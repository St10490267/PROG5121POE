package prog5121poe;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class MessageTest {

    @Before
    public void setUp() {
        Message.clearMessages();
    }

    // ----- TEST DATA: MESSAGE 1 (Send) -----
    @Test
    public void testMessage1_RecipientValid() {
        Message m = new Message("0012345678", 0, "+27718693002",
                "Hi Mike, can you join us for dinner tonight?");
        assertEquals("Cell phone number successfully captured.", m.checkRecipientCell());
    }

    @Test
    public void testMessage1_LengthValid() {
        assertTrue(Message.isMessageLengthValid("Hi Mike, can you join us for dinner tonight?"));
    }

    @Test
    public void testMessage1_Hash() {
        Message m = new Message("0012345678", 0, "+27718693002",
                "Hi Mike, can you join us for dinner tonight?");
        assertEquals("00:0:HITONIGHT", m.getMessageHash());
    }

    @Test
    public void testMessage1_IdValid() {
        Message m = new Message("0012345678", 0, "+27718693002",
                "Hi Mike, can you join us for dinner tonight?");
        assertTrue(m.checkMessageID());
    }

    @Test
    public void testMessage1_Send() {
        Message m = new Message("0012345678", 0, "+27718693002",
                "Hi Mike, can you join us for dinner tonight?");
        assertEquals("Message successfully sent.", m.sentMessage(1));
    }

    // ----- TEST DATA: MESSAGE 2 (Discard) -----
    @Test
    public void testMessage2_RecipientInvalid() {
        Message m = new Message("0098765432", 1, "08575975889",
                "Hi Keegan, did you receive the payment?");
        assertEquals("Cell phone number is incorrectly formatted or does not contain an international code. Please correct the number and try again.",
                m.checkRecipientCell());
    }

    @Test
    public void testMessage2_Hash() {
        Message m = new Message("0098765432", 1, "08575975889",
                "Hi Keegan, did you receive the payment?");
        assertEquals("00:1:HIPAYMENT", m.getMessageHash());
    }

    @Test
    public void testMessage2_Discard() {
        Message m = new Message("0098765432", 1, "08575975889",
                "Hi Keegan, did you receive the payment?");
        assertEquals("Press 0 to delete message.", m.sentMessage(2));
    }

    // ----- LENGTH CHECK -----
    @Test
    public void testMessageTooLong() {
        String longText = "a".repeat(251);
        assertFalse(Message.isMessageLengthValid(longText));
    }

    // ----- TOTAL SENT (Message 1 sent, Message 2 discarded = 1) -----
    @Test
    public void testTotalMessagesSent() {
        Message m1 = new Message("0012345678", 0, "+27718693002",
                "Hi Mike, can you join us for dinner tonight?");
        Message m2 = new Message("0098765432", 1, "08575975889",
                "Hi Keegan, did you receive the payment?");
        m1.sentMessage(1);
        m2.sentMessage(2);
        assertEquals(1, Message.returnTotalMessages());
    }

    @Test
    public void testPrintMessages_ContainsSentMessage() {
        Message m1 = new Message("0012345678", 0, "+27718693002",
                "Hi Mike, can you join us for dinner tonight?");
        m1.sentMessage(1);
        assertTrue(Message.printMessages().contains("00:0:HITONIGHT"));
    }
}