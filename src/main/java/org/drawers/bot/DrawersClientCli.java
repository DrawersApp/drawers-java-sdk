package org.drawers.bot;

import org.drawers.bot.dto.DrawersMessage;
import org.drawers.bot.listener.DrawersMessageListener;
import org.drawers.bot.mqtt.DrawersBot;
import org.drawers.bot.util.SendMail;

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
        String adminEmail;

        if (args.length != 3) {
            System.out.println("Usage: java DrawersClientCli <admin-email-id> <clientId> <password>");
            return;
        } else {
            clientId = args[0];
            password = args[1];
            adminEmail = args[2];
        }

        DrawersClientCli client = new DrawersClientCli(clientId, password);
        client.startBot();
        SendMail.getInstance().setAdminEmail(adminEmail);
        SendMail.getInstance().sendMail("Welcome to Drawers Bot",
                "Your bot is up and running now.");
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
