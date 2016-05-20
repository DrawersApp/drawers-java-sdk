package org.drawers.bot.listener;

import com.drawers.dao.MqttChatMessage;

/**
 * Created by nishant.pathak on 08/04/16.
 */
public interface DrawersMessageListener {
    public MqttChatMessage processMessageAndReply(MqttChatMessage message);
}
