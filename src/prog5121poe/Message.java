package prog5121poe;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class Message {

    // Shared across all messages while the program is running
    private static ArrayList<Message> sentMessages = new ArrayList<>();
    private static ArrayList<Message> storedMessages = new ArrayList<>();
    private static int totalSent = 0;

    private String messageId;
    private int messageNumber;
    private String recipient;
    private String messageText;
    private String messageHash;

    // Constructor that auto-generates the message ID
    public Message(int messageNumber, String recipient, String messageText) {
        this(generateMessageId(), messageNumber, recipient, messageText);
    }

    // Constructor where the ID is supplied (useful for unit tests)
    public Message(String messageId, int messageNumber, String recipient, String messageText) {
        this.messageId = messageId;
        this.messageNumber = messageNumber;
        this.recipient = recipient;
        this.messageText = messageText;
        this.messageHash = createMessageHash();
    }

    // Random ten-digit message ID
    private static String generateMessageId() {
        Random rand = new Random();
        long number = (long) (rand.nextDouble() * 10000000000L);
        return String.format("%010d", number);
    }

    // Message ID must be no more than ten characters
    public boolean checkMessageID() {
        return messageId.length() <= 10;
    }

    // Recipient must start with an international code and be a valid length
    public String checkRecipientCell() {
        if (recipient.matches("^\\+[0-9]{1,3}[0-9]{1,10}$")) {
            return "Cell phone number successfully captured.";
        }
        return "Cell phone number is incorrectly formatted or does not contain an international code. Please correct the number and try again.";
    }

    // Message must not exceed 250 characters
    public static boolean isMessageLengthValid(String text) {
        return text.length() <= 250;
    }

    // Hash = first two digits of ID : message number : FIRSTWORDLASTWORD (all caps)
    public String createMessageHash() {
        String[] words = messageText.trim().split("\\s+");
        String firstWord = words[0].replaceAll("[^A-Za-z0-9]", "");
        String lastWord = words[words.length - 1].replaceAll("[^A-Za-z0-9]", "");
        String firstTwo = messageId.substring(0, 2);
        return (firstTwo + ":" + messageNumber + ":" + firstWord + lastWord).toUpperCase();
    }

    // choice: 1 = Send, 2 = Disregard, 3 = Store
    public String sentMessage(int choice) {
        if (choice == 1) {
            sentMessages.add(this);
            totalSent++;
            return "Message successfully sent.";
        } else if (choice == 2) {
            return "Press 0 to delete message.";
        } else if (choice == 3) {
            storeMessage(this);
            return "Message successfully stored.";
        } else {
            return "Invalid choice.";
        }
    }

    // Returns all messages sent while the program is running
    public static String printMessages() {
        StringBuilder sb = new StringBuilder();
        for (Message m : sentMessages) {
            sb.append(m.getDetails()).append("\n");
        }
        return sb.toString();
    }

    // Total number of messages sent
    public static int returnTotalMessages() {
        return totalSent;
    }

    // Stores messages in a JSON file (messages.json in the project folder)
    public static void storeMessage(Message m) {
        storedMessages.add(m);

        StringBuilder json = new StringBuilder("[\n");
        for (int i = 0; i < storedMessages.size(); i++) {
            Message s = storedMessages.get(i);
            json.append("  {\n");
            json.append("    \"messageID\": \"").append(escape(s.messageId)).append("\",\n");
            json.append("    \"messageHash\": \"").append(escape(s.messageHash)).append("\",\n");
            json.append("    \"recipient\": \"").append(escape(s.recipient)).append("\",\n");
            json.append("    \"message\": \"").append(escape(s.messageText)).append("\"\n");
            json.append("  }");
            if (i < storedMessages.size() - 1) {
                json.append(",");
            }
            json.append("\n");
        }
        json.append("]");

        try (FileWriter writer = new FileWriter("messages.json")) {
            writer.write(json.toString());
        } catch (IOException e) {
            System.out.println("Could not write to JSON file: " + e.getMessage());
        }
    }

    private static String escape(String text) {
        return text.replace("\\", "\\\\").replace("\"", "\\\"");
    }

    // Full details in the required order: ID, Hash, Recipient, Message
    public String getDetails() {
        return "Message ID: " + messageId + "\n"
                + "Message Hash: " + messageHash + "\n"
                + "Recipient: " + recipient + "\n"
                + "Message: " + messageText + "\n";
    }

    // Clears the shared lists (handy between unit tests)
    public static void clearMessages() {
        sentMessages.clear();
        storedMessages.clear();
        totalSent = 0;
    }

    public String getMessageId() { return messageId; }
    public String getMessageHash() { return messageHash; }
    public String getRecipient() { return recipient; }
    public String getMessageText() { return messageText; }
}