package prog5121poe;

import java.util.Scanner;

public class PROG5121POE {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        Registration reg = new Registration();

        System.out.println("===== REGISTER =====");
        while (true) {
            System.out.print("Enter username: ");
            String username = sc.nextLine();
            System.out.print("Enter password: ");
            String password = sc.nextLine();
            System.out.print("Enter cellphone number (e.g. +27821234567): ");
            String cell = sc.nextLine();

            String result = reg.registerUser(username, password, cell);
            System.out.println(result);
            if (result.startsWith("Username successfully captured")) {
                break;
            }
            System.out.println("Please try again.\n");
        }

        System.out.println("\n===== LOGIN =====");
        Login login = new Login(reg);
        while (true) {
            System.out.print("Enter username: ");
            String user = sc.nextLine();
            System.out.print("Enter password: ");
            String pass = sc.nextLine();

            System.out.println(login.returnLoginStatus(user, pass));
            if (login.loginUser(user, pass)) {
                break;
            }
        }

        System.out.println("\nWelcome to QuickChat.");

        int maxMessages = 0;
        while (maxMessages <= 0) {
            System.out.print("How many messages do you wish to enter? ");
            try {
                maxMessages = Integer.parseInt(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
            if (maxMessages <= 0) {
                System.out.println("The number must be greater than 0.");
            }
        }

        int messagesEntered = 0;
        boolean running = true;

        while (running) {
            System.out.println("\n===== MENU =====");
            System.out.println("1) Send Messages");
            System.out.println("2) Show recently sent messages");
            System.out.println("3) Quit");
            System.out.print("Choose an option: ");
            String option = sc.nextLine().trim();

            switch (option) {
                case "1":
                    if (messagesEntered >= maxMessages) {
                        System.out.println("You have already entered all " + maxMessages + " messages.");
                        break;
                    }

                    while (messagesEntered < maxMessages) {
                        System.out.println("\n--- Message " + (messagesEntered + 1) + " of " + maxMessages + " ---");

                        String recipient;
                        while (true) {
                            System.out.print("Enter recipient cell number (with international code): ");
                            recipient = sc.nextLine().trim();
                            Message check = new Message(messagesEntered, recipient, "x");
                            String cellResult = check.checkRecipientCell();
                            System.out.println(cellResult);
                            if (cellResult.startsWith("Cell phone number successfully")) {
                                break;
                            }
                        }

                        String text;
                        while (true) {
                            System.out.print("Enter your message: ");
                            text = sc.nextLine();
                            if (Message.isMessageLengthValid(text) && !text.trim().isEmpty()) {
                                System.out.println("Message sent");
                                break;
                            }
                            System.out.println("Please enter a message of less than 250 characters.");
                        }

                        Message msg = new Message(messagesEntered, recipient, text);

                        System.out.println("1) Send   2) Discard   3) Store");
                        System.out.print("What would you like to do with this message? ");
                        int choice;
                        try {
                            choice = Integer.parseInt(sc.nextLine().trim());
                        } catch (NumberFormatException e) {
                            choice = 0;
                        }

                        System.out.println(msg.sentMessage(choice));

                        if (choice == 1) {
                            System.out.println("\n" + msg.getDetails());
                        }
                        messagesEntered++;
                    }

                    System.out.println("Total number of messages sent: " + Message.returnTotalMessages());
                    break;

                case "2":
                    System.out.println("Coming Soon.");
                    break;

                case "3":
                    System.out.println("Total number of messages sent: " + Message.returnTotalMessages());
                    System.out.println("Goodbye!");
                    running = false;
                    break;

                default:
                    System.out.println("Invalid option, please choose 1, 2 or 3.");
            }
        }
    }
}