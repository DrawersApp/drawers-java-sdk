package org.drawers.bot;

import org.drawers.bot.listener.ConnectionStateListener;
import org.drawers.bot.mqtt.DrawersBot;

import java.util.concurrent.TimeUnit;

/**
 * Created by nishant.pathak on 09/04/16.
 */
public class DrawersClient {

    protected DrawersBot bot;
    protected DrawersClient client;

    public DrawersClient(String clientId, String password) {
        // Create a bot instance.
        bot = new DrawersBot(clientId, password, new ConnectionStateListener() {
            @Override
            public void onConnected() {
                System.out.println("Connected to server");
            }

            @Override
            public void onConnectionLost() {
                System.out.println("Disconnected from server");
            }
        });
    }

    protected void startBot() {
        // Start the bot
        bot.start();
        try {
            // Block the main thread
            bot.getExecutorService().awaitTermination(100000000l, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
