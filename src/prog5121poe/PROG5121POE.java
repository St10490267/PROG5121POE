package prog5121poe;

public class PROG5121POE {

    public static void main(String[] args) {

        // ----- TEST REGISTRATION -----
        Registration reg = new Registration();
        String regResult = reg.registerUser("Kb_12", "Ch@nge123", "+27821234567");
        System.out.println(regResult);
        System.out.println();

        // ----- TEST LOGIN -----
        Login login = new Login(reg);
        String loginResult = login.returnLoginStatus("Kb_12", "Ch@nge123");
        System.out.println(loginResult);
        System.out.println();

        // ----- TEST MESSAGE -----
        Message msg = new Message("Hi, testing my chat app!");
        msg.sendMessage();
        msg.receiveMessage();
        msg.readMessage();

        System.out.println("Message payload: " + msg.getMessagePayload());
        System.out.println("Sent: " + msg.isMessageSent());
        System.out.println("Received: " + msg.isMessageReceived());
        System.out.println("Read: " + msg.isMessageRead());
    }
}