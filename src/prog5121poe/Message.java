package prog5121poe;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Message {

    public static class StoredRecord {
        public final String sender;
        public final String recipient;
        public final String messageID;
        public final String messageHash;
        public final String message;

        public StoredRecord(String sender, String recipient, String messageID, String messageHash, String message) {
            this.sender = sender;
            this.recipient = recipient;
            this.messageID = messageID;
            this.messageHash = messageHash;
            this.message = message;
        }
    }

    private String messageID;
    private int numMessagesSent;
    private String sender;
    private String recipient;
    private String messagePayload;
    private String messageHash;
    private boolean messageSent;
    private boolean messageReceived;
    private boolean messageRead;

    private static final String JSON_FILE_PATH = "storedMessages.json";

    public Message(String recipient, String messagePayload, int messageNumber) {
        this("SystemUser", recipient, messagePayload, messageNumber);
    }

    public Message(String sender, String recipient, String messagePayload, int messageNumber) {
        this.messageID = generateMessageID();
        this.sender = (sender == null || sender.trim().isEmpty()) ? "UnknownSender" : sender;
        this.recipient = recipient;
        this.messagePayload = messagePayload;
        this.numMessagesSent = messageNumber;
        this.messageHash = createMessageHash();
        this.messageSent = false;
        this.messageReceived = false;
        this.messageRead = false;
    }

    // ----- GENERATE 10-DIGIT MESSAGE ID -----
    public final String generateMessageID() {
        Random random = new Random();
        long tenDigitNumber = 1000000000L + (long) (random.nextDouble() * 9000000000L);
        return String.valueOf(tenDigitNumber);
    }

    // ----- VALIDATION CHECKS -----
    public boolean checkMessageID() {
        return messageID != null && messageID.length() <= 10;
    }

    public int checkRecipientCell() {
        if (recipient != null && Pattern.matches("^\\+27[0-9]{9}$", recipient)) {
            return 1;
        }
        return 0;
    }

    public String checkMessageLength() {
        if (messagePayload != null && messagePayload.length() <= 250) {
            return "Message ready to send.";
        }
        int excess = (messagePayload == null ? 0 : messagePayload.length()) - 250;
        return "Message exceeds 250 characters by " + excess + ", please reduce size.";
    }

    // ----- CREATE MESSAGE HASH -----
    public final String createMessageHash() {
        String idPrefix = messageID.substring(0, 2);
        String trimmed = messagePayload == null ? "" : messagePayload.trim();
        if (trimmed.isEmpty()) {
            return (idPrefix + ":" + numMessagesSent + ":EMPTY").toUpperCase();
        }
        String[] words = trimmed.split("\\s+");
        String firstWord = words[0].replaceAll("[^a-zA-Z0-9]", "");
        String lastWord = words[words.length - 1].replaceAll("[^a-zA-Z0-9]", "");
        return (idPrefix + ":" + numMessagesSent + ":" + firstWord + lastWord).toUpperCase();
    }

    // ----- SEND / DISREGARD / STORE ACTIONS -----
    public String sentMessage(int option) {
        switch (option) {
            case 1:
                this.messageSent = true;
                return "Message successfully sent.";
            case 2:
                return "Press 0 to delete message.";
            case 3:
                storeMessageToJson();
                return "Message successfully stored.";
            default:
                return "Invalid option selected.";
        }
    }

    // ----- SAVE MESSAGE TO JSON FILE -----
    public void storeMessageToJson() {
        List<StoredRecord> records = new ArrayList<>(List.of(readStoredRecordsFromJson()));
        records.add(new StoredRecord(sender, recipient, messageID, messageHash, messagePayload));
        writeStoredRecordsToJson(records);
    }

    // ----- READ ALL STORED RECORDS FROM JSON FILE -----
    public static StoredRecord[] readStoredRecordsFromJson() {
        List<StoredRecord> records = new ArrayList<>();
        File file = new File(JSON_FILE_PATH);

        if (!file.exists()) {
            return new StoredRecord[0];
        }

        try {
            String content = new String(Files.readAllBytes(Paths.get(JSON_FILE_PATH)));
            Matcher objMatcher = Pattern.compile("\\{[^}]*\\}").matcher(content);
            while (objMatcher.find()) {
                String obj = objMatcher.group();
                String s = extractJsonField(obj, "sender", "UnknownSender");
                String r = extractJsonField(obj, "recipient", "");
                String id = extractJsonField(obj, "messageID", "");
                String h = extractJsonField(obj, "messageHash", "");
                String m = extractJsonField(obj, "message", "");
                records.add(new StoredRecord(s, r, id, h, m));
            }
        } catch (IOException e) {
            System.out.println("Error loading stored messages from JSON: " + e.getMessage());
        }

        return records.toArray(new StoredRecord[0]);
    }

    // ----- READ STORED MESSAGES INTO A STRING ARRAY -----
    public static String[] readStoredMessagesFromJson() {
        StoredRecord[] records = readStoredRecordsFromJson();
        String[] messages = new String[records.length];
        for (int i = 0; i < records.length; i++) {
            messages[i] = records[i].message;
        }
        return messages;
    }

    // ----- DELETE A STORED MESSAGE BY HASH -----
    public static boolean deleteStoredMessageByHash(String targetHash) {
        if (targetHash == null || targetHash.trim().isEmpty()) {
            return false;
        }
        StoredRecord[] existing = readStoredRecordsFromJson();
        List<StoredRecord> remaining = new ArrayList<>();
        boolean deleted = false;

        for (StoredRecord rec : existing) {
            if (!deleted && rec.messageHash != null && rec.messageHash.equalsIgnoreCase(targetHash.trim())) {
                deleted = true;
            } else {
                remaining.add(rec);
            }
        }

        if (deleted) {
            writeStoredRecordsToJson(remaining);
        }
        return deleted;
    }

    private static void writeStoredRecordsToJson(List<StoredRecord> records) {
        StringBuilder jsonArray = new StringBuilder("[\n");
        for (int i = 0; i < records.size(); i++) {
            StoredRecord rec = records.get(i);
            jsonArray.append(String.format(
                    "  {\n    \"sender\": \"%s\",\n    \"recipient\": \"%s\",\n    \"messageID\": \"%s\",\n    \"messageHash\": \"%s\",\n    \"message\": \"%s\"\n  }",
                    escapeJson(rec.sender),
                    escapeJson(rec.recipient),
                    escapeJson(rec.messageID),
                    escapeJson(rec.messageHash),
                    escapeJson(rec.message)
            ));
            if (i < records.size() - 1) {
                jsonArray.append(",");
            }
            jsonArray.append("\n");
        }
        jsonArray.append("]");

        try (FileWriter writer = new FileWriter(JSON_FILE_PATH)) {
            writer.write(jsonArray.toString());
        } catch (IOException e) {
            System.out.println("Error writing JSON file: " + e.getMessage());
        }
    }

    private static String extractJsonField(String jsonObject, String key, String defaultValue) {
        Matcher m = Pattern.compile("\"" + Pattern.quote(key) + "\"\\s*:\\s*\"((?:\\\\.|[^\"\\\\])*)\"").matcher(jsonObject);
        if (m.find()) {
            return unescapeJson(m.group(1));
        }
        return defaultValue;
    }

    private static String escapeJson(String value) {
        if (value == null) {
            return "";
        }
        return value.replace("\\", "\\\\").replace("\"", "\\\"");
    }

    private static String unescapeJson(String value) {
        if (value == null) {
            return "";
        }
        return value.replace("\\\"", "\"").replace("\\\\", "\\");
    }

    // ----- STATUS ACTIONS -----
    public void sendMessage() {
        this.messageSent = true;
    }

    public void receiveMessage() {
        this.messageReceived = true;
    }

    public void readMessage() {
        this.messageRead = true;
    }

    // ----- GETTERS -----
    public String getMessageID() {
        return messageID;
    }

    public String getMessageHash() {
        return messageHash;
    }

    public String getSender() {
        return sender;
    }

    public String getRecipient() {
        return recipient;
    }

    public String getMessagePayload() {
        return messagePayload;
    }

    public boolean isMessageSent() {
        return messageSent;
    }

    public boolean isMessageReceived() {
        return messageReceived;
    }

    public boolean isMessageRead() {
        return messageRead;
    }

    public void setMessagePayload(String messagePayload) {
        this.messagePayload = messagePayload;
    }
}
