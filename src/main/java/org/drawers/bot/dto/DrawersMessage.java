package org.drawers.bot.dto;

import org.drawers.bot.dao.MqttChatMessage;

/**
 * Created by nishant.pathak on 08/04/16.
 */
public class DrawersMessage {
    String sender;
    String message;
    MqttChatMessage.ChatConstant.ChatType chatType;

    public DrawersMessage(String sender, String message) {
        this.sender = sender;
        this.message = message;
        this.chatType = MqttChatMessage.ChatConstant.ChatType.TEXT;
    }

    public DrawersMessage(String sender, String message, MqttChatMessage.ChatConstant.ChatType chatType) {
        this.sender = sender;
        this.message = message;
        this.chatType = chatType;
    }

    public MqttChatMessage.ChatConstant.ChatType getChatType() {
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
