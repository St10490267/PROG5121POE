package prog5121poe;

public class Message {

    private String messagePayload;
    private boolean messageSent;
    private boolean messageReceived;
    private boolean messageRead;

    public Message(String messagePayload) {
        this.messagePayload = messagePayload;
        this.messageSent = false;
        this.messageReceived = false;
        this.messageRead = false;
    }

    // ----- ACTIONS -----
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

    // ----- SETTERS -----
    public void setMessagePayload(String messagePayload) {
        this.messagePayload = messagePayload;
    }
}