package org.drawers.bot.dao;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.util.Arrays;
import java.util.HashSet;

/**
 * Created by nishant.pathak on 09/04/16.
 */
public class MqttChatMessage {
    @SerializedName("i")
    private String messageId;
    @SerializedName("m")
    private String message;
    @SerializedName("s")
    private String senderUid;

    @SerializedName("c")
    private ChatConstant.ChatType chatType;
    @SerializedName("d")
    private boolean deliveryReceipt;

    public String getSenderUid() {
        return senderUid;
    }

    public String getMessage() {
        return message;
    }

    public MqttChatMessage( String messageId, String message, String senderUid, ChatConstant.ChatType chatType, boolean deliveryReceipt) {
        this.messageId = messageId;
        this.message = message;
        this.senderUid = senderUid;
        this.chatType = chatType;
        this.deliveryReceipt = deliveryReceipt;
    }
    private static Gson gson = new Gson();

    public static MqttChatMessage fromString(String json) {
        return gson.fromJson(json, MqttChatMessage.class);
    }

    public static String toJson(MqttChatMessage chatMessage) {
        return gson.toJson(chatMessage);
    }



    public static class ChatConstant {
        public enum ChatType {
            FILE(0),
            IMAGE(1),
            VIDEO(2),
            TEXT(3),
            CONTACT(4),
            MAP(5),
            NOTIFICATION(6),
            CALL(7),
            QA(8);

            int pos;
            ChatType(int p) {
                pos = p;
            }

            public boolean isMediaMessage() {
                return pos <= 2;
            }

            public boolean isTextMessage() {
                return pos == 3;
            }
        }


        static HashSet<ChatType> mType = new HashSet<>(Arrays.asList(ChatType.values()));

        static public boolean validType(ChatType type) {
            return mType.contains(type);
        }

    }


}
