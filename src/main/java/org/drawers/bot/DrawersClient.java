package org.drawers.bot;

import com.drawers.dao.MqttChatMessage;
import org.drawers.bot.listener.DrawersMessageListener;
import org.drawers.bot.mqtt.DrawersBot;

import java.util.concurrent.TimeUnit;

/**
 * Created by nishant.pathak on 09/04/16.
 */
public class DrawersClient implements DrawersMessageListener {

    protected DrawersBot bot;
    protected DrawersClient client;

    public DrawersClient(String clientId, String password) {
        bot = new DrawersBot(clientId, password, this);
    }

    protected void startBot() {
        bot.start();
        try {
            bot.getExecutorService().awaitTermination(100000000l, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onConnected() {

    }

    @Override
    public MqttChatMessage processMessageAndReply(MqttChatMessage message) {
        return null;
    }
}
