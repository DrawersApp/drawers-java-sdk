package org.drawers.bot;

import org.drawers.bot.dto.DrawersMessage;
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

    protected void initializeBot(String clientId, String password) {
        DrawersClient client = new DrawersClient(clientId, password);
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
    public DrawersMessage processMessageAndReply(DrawersMessage message) {
        return null;
    }
}
