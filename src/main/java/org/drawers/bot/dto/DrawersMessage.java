package org.drawers.bot.dto;


import com.drawers.dao.ChatConstant;

/**
 * Created by nishant.pathak on 08/04/16.
 */
public class DrawersMessage {
    String sender;
    String message;
    ChatConstant.ChatType chatType;

    public DrawersMessage(String sender, String message) {
        this.sender = sender;
        this.message = message;
        this.chatType = ChatConstant.ChatType.TEXT;
    }

    public DrawersMessage(String sender, String message, ChatConstant.ChatType chatType) {
        this.sender = sender;
        this.message = message;
        this.chatType = chatType;
    }

    public ChatConstant.ChatType getChatType() {
        return chatType;
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
