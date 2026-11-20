package prog5121poe;

import org.junit.Test;
import static org.junit.Assert.*;

public class StoredMessagesTest {

    // Test Data from Part 3 Item 3 (Messages 1 - 5)
    private static final String MSG_1_RECIP = "+27834557896";
    private static final String MSG_1_TEXT  = "Did you get the cake?";

    private static final String MSG_2_RECIP = "+27838884567";
    private static final String MSG_2_TEXT  = "Where are you? You are late! I have asked you to be on time.";

    private static final String MSG_3_RECIP = "+27834484567";
    private static final String MSG_3_TEXT  = "Yohoooo, I am at your gate.";

    private static final String MSG_4_DEV   = "0838884567";
    private static final String MSG_4_TEXT  = "It is dinner time !";

    private static final String MSG_5_RECIP = "+27838884567";
    private static final String MSG_5_TEXT  = "Ok, I am leaving without you.";

    @Test
    public void testSentMessagesArrayPopulated() {
        String[] sentOnly = new String[]{MSG_1_TEXT, MSG_4_TEXT};
        String actual = PROG5121POE.getSentMessagesSummary(sentOnly);
        assertEquals(MSG_1_TEXT + ", " + MSG_4_TEXT, actual);
    }

    @Test
    public void testDisplayLongestMessage() {
        String[] messages1to4 = new String[]{MSG_1_TEXT, MSG_2_TEXT, MSG_3_TEXT, MSG_4_TEXT};
        String actual = PROG5121POE.findLongestMessage(messages1to4);
        assertEquals(MSG_2_TEXT, actual);
    }

    @Test
    public void testSearchForMessageID() {
        Message msg4 = new Message("Kb_12", MSG_4_DEV, MSG_4_TEXT, 4);
        String[] ids = new String[]{"1000000001", "1000000002", "1000000003", msg4.getMessageID()};
        String[] devs = new String[]{MSG_1_RECIP, MSG_2_RECIP, MSG_3_RECIP, MSG_4_DEV};
        String[] msgs = new String[]{MSG_1_TEXT, MSG_2_TEXT, MSG_3_TEXT, MSG_4_TEXT};

        String actual = PROG5121POE.searchMessageTextByID(ids, devs, msgs, MSG_4_DEV);
        assertEquals(MSG_4_TEXT, actual);
    }

    @Test
    public void testSearchAllMessagesForRecipient() {
        String[] recipients = new String[]{MSG_1_RECIP, MSG_2_RECIP, MSG_3_RECIP, MSG_4_DEV, MSG_5_RECIP};
        String[] messages   = new String[]{MSG_1_TEXT, MSG_2_TEXT, MSG_3_TEXT, MSG_4_TEXT, MSG_5_TEXT};

        String actual = PROG5121POE.searchMessagesByRecipientOrDeveloper(recipients, messages, "+27838884567");
        assertEquals(MSG_2_TEXT + " " + MSG_5_TEXT, actual);
    }

    @Test
    public void testDeleteMessageUsingMessageHash() {
        Message msg2 = new Message("Kb_12", MSG_2_RECIP, MSG_2_TEXT, 2);
        msg2.storeMessageToJson();

        String[] hashes   = new String[]{"H1", msg2.getMessageHash()};
        String[] messages = new String[]{MSG_1_TEXT, MSG_2_TEXT};

        String actual = PROG5121POE.deleteMessageByHashWithConfirmation(hashes, messages, msg2.getMessageHash());
        assertEquals("Message: \"" + MSG_2_TEXT + "\" successfully deleted.", actual);
    }

    @Test
    public void testDisplayReport() {
        Message msg1 = new Message("Kb_12", MSG_1_RECIP, MSG_1_TEXT, 1);
        Message msg4 = new Message("Kb_12", MSG_4_DEV, MSG_4_TEXT, 4);

        String[] hashes     = new String[]{msg1.getMessageHash(), msg4.getMessageHash()};
        String[] recipients = new String[]{MSG_1_RECIP, MSG_4_DEV};
        String[] messages   = new String[]{MSG_1_TEXT, MSG_4_TEXT};

        String expected = "Message Hash: " + msg1.getMessageHash() + " | Recipient: " + MSG_1_RECIP + " | Message: " + MSG_1_TEXT + "\n"
                        + "Message Hash: " + msg4.getMessageHash() + " | Recipient: " + MSG_4_DEV + " | Message: " + MSG_4_TEXT;

        String actual = PROG5121POE.buildSentMessagesReport(hashes, recipients, messages);
        assertEquals(expected, actual);
    }
}
