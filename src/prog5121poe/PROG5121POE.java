package prog5121poe;

import java.util.Scanner;

public class PROG5121POE {

    // ----- PART 3 ARRAYS (NO HARD-CODING) -----
    public static String[] sentMessages = new String[0];
    public static String[] disregardedMessages = new String[0];
    public static String[] storedMessages = new String[0];
    public static String[] storedSenders = new String[0];
    public static String[] storedRecipients = new String[0];
    public static String[] storedMessageIDs = new String[0];
    public static String[] storedMessageHashes = new String[0];
    public static String[] messageHashes = new String[0];
    public static String[] messageIDs = new String[0];

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // ----- PART 1: REGISTRATION -----
        Registration reg = new Registration();
        String regResult;
        do {
            System.out.print("Enter username (must contain '_' and be <= 5 chars): ");
            String username = scanner.nextLine();

            System.out.print("Enter password (min 8 chars, 1 capital, 1 number, 1 special char): ");
            String password = scanner.nextLine();

            System.out.print("Enter South African cell phone number (+27xxxxxxxxx): ");
            String cellphone = scanner.nextLine();

            regResult = reg.registerUser(username, password, cellphone);
            System.out.println(regResult);
        } while (!regResult.startsWith("Username successfully captured."));

        // ----- PART 1: LOGIN -----
        Login login = new Login(reg);
        boolean isLoggedIn = false;
        while (!isLoggedIn) {
            System.out.print("\nLogin - Enter username: ");
            String loginUser = scanner.nextLine();

            System.out.print("Login - Enter password: ");
            String loginPass = scanner.nextLine();

            System.out.println(login.returnLoginStatus(loginUser, loginPass));
            isLoggedIn = login.loginUser(loginUser, loginPass);
        }

        String currentSender = reg.getUsername();
        refreshStoredArraysFromJson();

        // ----- PART 2 & 3: QUICKCHAT MENU -----
        System.out.println("\nWelcome to QuickChat.");

        int choice = 0;
        while (choice != 3) {
            System.out.println("\nPlease select an option:");
            System.out.println("1) Send Messages");
            System.out.println("2) Show recently sent messages");
            System.out.println("3) Quit");
            System.out.println("4) Stored Messages");
            System.out.print("Choice: ");

            if (scanner.hasNextInt()) {
                choice = scanner.nextInt();
                scanner.nextLine();
            } else {
                scanner.nextLine();
                System.out.println("Invalid input. Please enter 1, 2, 3, or 4.");
                continue;
            }

            switch (choice) {
                case 1:
                    System.out.print("How many messages do you wish to enter? ");
                    int numMessages = scanner.nextInt();
                    scanner.nextLine();

                    String[] tempSent = new String[numMessages];
                    String[] tempDisregarded = new String[numMessages];
                    String[] tempHashes = new String[numMessages];
                    String[] tempIDs = new String[numMessages];

                    int sentCount = 0;
                    int disregardedCount = 0;
                    int totalTrackedCount = 0;

                    for (int i = 0; i < numMessages; i++) {
                        System.out.println("\n--- Message " + (i + 1) + " of " + numMessages + " ---");

                        String recipient;
                        while (true) {
                            System.out.print("Enter recipient cell number (+27xxxxxxxxx): ");
                            recipient = scanner.nextLine();
                            Message tempCheck = new Message(currentSender, recipient, "", i + 1);
                            if (tempCheck.checkRecipientCell() == 1) {
                                break;
                            }
                            System.out.println("Cell phone number is incorrectly formatted or does not contain an international code. Please correct the number and try again.");
                        }

                        String payload;
                        Message msg;
                        while (true) {
                            System.out.print("Enter message (max 250 characters): ");
                            payload = scanner.nextLine();
                            msg = new Message(currentSender, recipient, payload, i + 1);
                            String lengthStatus = msg.checkMessageLength();
                            System.out.println(lengthStatus);
                            if (lengthStatus.equals("Message ready to send.")) {
                                break;
                            }
                        }

                        System.out.println("Choose an action for this message:");
                        System.out.println("1) Send Message");
                        System.out.println("2) Disregard Message");
                        System.out.println("3) Store Message to send later");
                        System.out.print("Action: ");
                        int action = scanner.nextInt();
                        scanner.nextLine();

                        String actionResult = msg.sentMessage(action);
                        System.out.println(actionResult);

                        if (action == 1) {
                            tempSent[sentCount++] = msg.getMessagePayload();
                            tempHashes[totalTrackedCount] = msg.getMessageHash();
                            tempIDs[totalTrackedCount] = msg.getMessageID();
                            totalTrackedCount++;
                        } else if (action == 2) {
                            tempDisregarded[disregardedCount++] = msg.getMessagePayload();
                        } else if (action == 3) {
                            tempHashes[totalTrackedCount] = msg.getMessageHash();
                            tempIDs[totalTrackedCount] = msg.getMessageID();
                            totalTrackedCount++;
                        }
                    }

                    sentMessages = appendToArray(sentMessages, tempSent, sentCount);
                    disregardedMessages = appendToArray(disregardedMessages, tempDisregarded, disregardedCount);
                    messageHashes = appendToArray(messageHashes, tempHashes, totalTrackedCount);
                    messageIDs = appendToArray(messageIDs, tempIDs, totalTrackedCount);
                    refreshStoredArraysFromJson();
                    break;

                case 2:
                    System.out.println("Coming Soon.");
                    break;

                case 3:
                    System.out.println("Exiting QuickChat. Goodbye!");
                    break;

                case 4:
                    handleStoredMessagesMenu(scanner);
                    break;

                default:
                    System.out.println("Invalid option. Please select 1, 2, 3, or 4.");
            }
        }

        scanner.close();
    }

    // ----- OPTION 4: STORED MESSAGES SUB-MENU (a - f) -----
    public static void handleStoredMessagesMenu(Scanner scanner) {
        refreshStoredArraysFromJson();

        int subChoice = 0;
        while (subChoice != 7) {
            System.out.println("\n--- Stored Messages Menu ---");
            System.out.println("a) 1. Display sender and recipient of all stored messages");
            System.out.println("b) 2. Display the longest stored message");
            System.out.println("c) 3. Search for a message ID and display corresponding recipient and message");
            System.out.println("d) 4. Search for all messages stored for a particular recipient");
            System.out.println("e) 5. Delete a message using the message hash");
            System.out.println("f) 6. Display full report of all stored messages");
            System.out.println("   7. Back to Main Menu");
            System.out.print("Select option (1-7): ");

            if (scanner.hasNextInt()) {
                subChoice = scanner.nextInt();
                scanner.nextLine();
            } else {
                scanner.nextLine();
                System.out.println("Invalid input. Please enter a number from 1 to 7.");
                continue;
            }

            switch (subChoice) {
                case 1:
                    System.out.println(displaySendersAndRecipients());
                    break;
                case 2:
                    System.out.println(displayLongestStoredMessage());
                    break;
                case 3:
                    System.out.print("Enter Message ID to search: ");
                    String searchId = scanner.nextLine();
                    System.out.println(searchByMessageID(searchId));
                    break;
                case 4:
                    System.out.print("Enter Recipient number to search (+27xxxxxxxxx): ");
                    String searchRecipient = scanner.nextLine();
                    System.out.println(searchMessagesByRecipient(searchRecipient));
                    break;
                case 5:
                    System.out.print("Enter Message Hash to delete: ");
                    String hashToDelete = scanner.nextLine();
                    System.out.println(deleteMessageByHash(hashToDelete));
                    break;
                case 6:
                    System.out.println(displayStoredMessagesReport());
                    break;
                case 7:
                    System.out.println("Returning to main menu...");
                    break;
                default:
                    System.out.println("Invalid selection. Choose 1 to 7.");
            }
        }
    }

    // Populates all stored message arrays from JSON (no hard-coding)
    public static void refreshStoredArraysFromJson() {
        Message.StoredRecord[] records = Message.readStoredRecordsFromJson();
        storedSenders = new String[records.length];
        storedRecipients = new String[records.length];
        storedMessageIDs = new String[records.length];
        storedMessageHashes = new String[records.length];
        storedMessages = new String[records.length];

        for (int i = 0; i < records.length; i++) {
            storedSenders[i] = records[i].sender;
            storedRecipients[i] = records[i].recipient;
            storedMessageIDs[i] = records[i].messageID;
            storedMessageHashes[i] = records[i].messageHash;
            storedMessages[i] = records[i].message;
        }
    }

    // a. Display sender and recipient of all stored messages
    public static String displaySendersAndRecipients() {
        refreshStoredArraysFromJson();
        if (storedMessages.length == 0) {
            return "No stored messages found.";
        }
        StringBuilder sb = new StringBuilder("=== Sender & Recipient of All Stored Messages ===\n");
        for (int i = 0; i < storedMessages.length; i++) {
            sb.append((i + 1))
              .append(". Sender: ").append(storedSenders[i])
              .append(" | Recipient: ").append(storedRecipients[i])
              .append("\n");
        }
        return sb.toString().trim();
    }

    // b. Display the longest stored message
    public static String displayLongestStoredMessage() {
        refreshStoredArraysFromJson();
        if (storedMessages.length == 0) {
            return "No stored messages found.";
        }

        int longestIndex = 0;
        int maxLength = storedMessages[0] == null ? 0 : storedMessages[0].length();

        for (int i = 1; i < storedMessages.length; i++) {
            int currentLen = storedMessages[i] == null ? 0 : storedMessages[i].length();
            if (currentLen > maxLength) {
                maxLength = currentLen;
                longestIndex = i;
            }
        }

        return "=== Longest Stored Message ===\n"
                + "Message ID: " + storedMessageIDs[longestIndex] + "\n"
                + "Recipient: " + storedRecipients[longestIndex] + "\n"
                + "Length: " + maxLength + " characters\n"
                + "Message: " + storedMessages[longestIndex];
    }

    // c. Search for a message ID and display corresponding recipient and message
    public static String searchByMessageID(String targetID) {
        refreshStoredArraysFromJson();
        if (targetID == null || targetID.trim().isEmpty()) {
            return "Please provide a valid Message ID.";
        }

        for (int i = 0; i < storedMessageIDs.length; i++) {
            if (storedMessageIDs[i] != null && storedMessageIDs[i].equals(targetID.trim())) {
                return "=== Message Found by ID ===\n"
                        + "Message ID: " + storedMessageIDs[i] + "\n"
                        + "Recipient: " + storedRecipients[i] + "\n"
                        + "Message: " + storedMessages[i];
            }
        }
        return "No stored message found with ID: " + targetID;
    }

    // d. Search for all messages stored for a particular recipient
    public static String searchMessagesByRecipient(String targetRecipient) {
        refreshStoredArraysFromJson();
        if (targetRecipient == null || targetRecipient.trim().isEmpty()) {
            return "Please provide a valid recipient.";
        }

        StringBuilder sb = new StringBuilder();
        int matchCount = 0;

        for (int i = 0; i < storedRecipients.length; i++) {
            if (storedRecipients[i] != null && storedRecipients[i].equalsIgnoreCase(targetRecipient.trim())) {
                matchCount++;
                sb.append(matchCount)
                  .append(". [ID: ").append(storedMessageIDs[i])
                  .append("] Message: ").append(storedMessages[i])
                  .append("\n");
            }
        }

        if (matchCount == 0) {
            return "No stored messages found for recipient: " + targetRecipient;
        }
        return "=== Stored Messages for Recipient: " + targetRecipient + " ===\n" + sb.toString().trim();
    }

    // e. Delete a message using the message hash
    public static String deleteMessageByHash(String targetHash) {
        boolean removed = Message.deleteStoredMessageByHash(targetHash);
        refreshStoredArraysFromJson();
        if (removed) {
            return "Message with hash '" + targetHash + "' has been successfully deleted.";
        }
        return "No stored message found with hash '" + targetHash + "'.";
    }

    // f. Display a report that lists full details of all stored messages
    public static String displayStoredMessagesReport() {
        refreshStoredArraysFromJson();
        if (storedMessages.length == 0) {
            return "No stored messages available for report.";
        }

        StringBuilder sb = new StringBuilder("=== Full Stored Messages Report ===\n");
        for (int i = 0; i < storedMessages.length; i++) {
            sb.append("Record #").append(i + 1).append("\n")
              .append("  Sender:       ").append(storedSenders[i]).append("\n")
              .append("  Recipient:    ").append(storedRecipients[i]).append("\n")
              .append("  Message ID:   ").append(storedMessageIDs[i]).append("\n")
              .append("  Message Hash: ").append(storedMessageHashes[i]).append("\n")
              .append("  Message:      ").append(storedMessages[i]).append("\n")
              .append("----------------------------------------\n");
        }
        return sb.toString().trim();
    }

    // ----- ITEM 4 HELPER METHODS FOR SENT / STORED / ALL MESSAGES -----
    public static String getSentMessagesSummary(String[] sentArray) {
        if (sentArray == null || sentArray.length == 0) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < sentArray.length; i++) {
            sb.append(sentArray[i]);
            if (i < sentArray.length - 1) {
                sb.append(", ");
            }
        }
        return sb.toString();
    }

    public static String findLongestMessage(String[] messages) {
        if (messages == null || messages.length == 0) {
            return "";
        }
        String longest = messages[0] == null ? "" : messages[0];
        for (int i = 1; i < messages.length; i++) {
            if (messages[i] != null && messages[i].length() > longest.length()) {
                longest = messages[i];
            }
        }
        return longest;
    }

    public static String searchMessageTextByID(String[] ids, String[] recipientsOrDevs, String[] messages, String targetID) {
        if (ids == null || messages == null || targetID == null) {
            return "";
        }
        for (int i = 0; i < ids.length; i++) {
            if (targetID.equals(ids[i]) && i < messages.length) {
                return messages[i];
            }
        }
        if (recipientsOrDevs != null) {
            for (int i = 0; i < recipientsOrDevs.length; i++) {
                if (targetID.equals(recipientsOrDevs[i]) && i < messages.length) {
                    return messages[i];
                }
            }
        }
        return "";
    }

    public static String searchMessagesByRecipientOrDeveloper(String[] recipients, String[] messages, String target) {
        if (recipients == null || messages == null || target == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < recipients.length; i++) {
            if (target.equals(recipients[i]) && i < messages.length) {
                if (sb.length() > 0) {
                    sb.append(" ");
                }
                sb.append(messages[i]);
            }
        }
        return sb.toString();
    }

    public static String deleteMessageByHashWithConfirmation(String[] hashes, String[] messages, String targetHash) {
        if (hashes == null || messages == null || targetHash == null) {
            return "";
        }
        for (int i = 0; i < hashes.length; i++) {
            if (targetHash.equals(hashes[i]) && i < messages.length) {
                String deletedText = messages[i];
                hashes[i] = null;
                messages[i] = null;
                Message.deleteStoredMessageByHash(targetHash);
                refreshStoredArraysFromJson();
                return "Message: \"" + deletedText + "\" successfully deleted.";
            }
        }
        return "";
    }

    public static String buildSentMessagesReport(String[] hashes, String[] recipients, String[] messages) {
        StringBuilder sb = new StringBuilder();
        int count = Math.min(hashes.length, Math.min(recipients.length, messages.length));
        for (int i = 0; i < count; i++) {
            if (messages[i] != null) {
                sb.append("Message Hash: ").append(hashes[i])
                  .append(" | Recipient: ").append(recipients[i])
                  .append(" | Message: ").append(messages[i]);
                if (i < count - 1) {
                    sb.append("\n");
                }
            }
        }
        return sb.toString().trim();
    }

    private static String[] appendToArray(String[] original, String[] additions, int countToAdd) {
        String[] result = new String[original.length + countToAdd];
        System.arraycopy(original, 0, result, 0, original.length);
        System.arraycopy(additions, 0, result, original.length, countToAdd);
        return result;
    }
}
