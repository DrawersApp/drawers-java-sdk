package org.drawers.bot.dto;

/**
 * Created by nishant.pathak on 08/04/16.
 */
public class DrawersMessage {
    String sender;
    String message;

    public DrawersMessage(String sender, String message) {
        this.sender = sender;
        this.message = message;
    }

    public String getSender() {
        return sender;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "DrawersMessage{" +
                "sender='" + sender + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
