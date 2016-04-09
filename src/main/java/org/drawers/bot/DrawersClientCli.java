package org.drawers.bot;

import org.drawers.bot.dto.DrawersMessage;
import org.drawers.bot.listener.DrawersMessageListener;
import org.drawers.bot.mqtt.DrawersBot;

import java.util.concurrent.TimeUnit;

/**
 * Created by nishant.pathak on 08/04/16.
 */
public class DrawersClientCli implements DrawersMessageListener {

    private static DrawersBot bot;
    private static DrawersClientCli client;

    public DrawersClientCli(String clientId, String password) {
        bot = new DrawersBot(clientId, password, this);
    }

    public static void main(String [] args) {
        String clientId;
        String password;

        if (args.length != 2) {
            System.out.println("Usage: java DrawersClientCli <clientId> <password>");
            return;
        } else {
            clientId = args[0];
            password = args[1];
        }

        DrawersClientCli client = new DrawersClientCli(clientId, password);
        client.startBot();
    }

    private void startBot() {
        bot.start();
        try {
            bot.getExecutorService().awaitTermination(100000000l, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    @Override
    public DrawersMessage processMessageAndReply(DrawersMessage message) {
        System.out.println("Received new message: " + message);
        return message;

    }
}
