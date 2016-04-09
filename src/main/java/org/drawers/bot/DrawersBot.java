package org.drawers.bot;

import org.drawers.bot.crypto.DrawersCryptoEngine;
import org.drawers.bot.dao.MqttChatMessage;
import org.drawers.bot.dto.DrawersMessage;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by nishant.pathak on 08/04/16.
 */
public final class DrawersBot implements MqttCallback {

    private String clientId;
    private String password;
    private MqttClient mqttClient;
    private static final DrawersCryptoEngine DRAWERS_CRYPTO_ENGINE = new DrawersCryptoEngine();
    private ExecutorService executorService = Executors.newFixedThreadPool(DEFAULT_THREAD_COUNT);


    private static final String SERVER_URL = "mqtt-lb.sandwitch.in";
    private static final String PROTOCOL = "tcp://";
    private static final String PROTOCOL_SECURE = "ssl://";
    private static final String MESSAGES_OTHERS = "/o/m";
    private static final int SERVER_PORT = 80;
    private static final int SERVER_SECURE_PORT = 443;
    private static final int DEFAULT_THREAD_COUNT = 5;

    private DrawersMessageListener messageListener;

    public DrawersBot(String clientId, String password, DrawersClient messageListener) {
        this.clientId = clientId;
        this.password = password;
        this.messageListener = messageListener;

    }

    private static String getServerUrl() {
        return PROTOCOL + SERVER_URL + ":" + String.valueOf(SERVER_PORT);
    }

    private static String getSecureServerUrl() {
        return PROTOCOL_SECURE + SERVER_URL + ":" + String.valueOf(SERVER_SECURE_PORT);
    }


    public ExecutorService getExecutorService() {
        return executorService;
    }

    public void start() {
        MemoryPersistence persistence = new MemoryPersistence();
        try {
            mqttClient = new MqttClient(getServerUrl(), clientId, persistence);
            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setCleanSession(false);
            connOpts.setConnectionTimeout(10);

            System.out.println("Connecting to broker");
            mqttClient.connect();
            System.out.println("connected");

            mqttClient.subscribe(clientId + MESSAGES_OTHERS);
            System.out.println("subscribed");

            mqttClient.setCallback(this);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void connectionLost(Throwable cause) {
        System.out.println("Connection lost due to:");
        cause.printStackTrace();
        start();
    }

    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        executorService.submit(() -> messageArrivedInternal(topic, message));
    }

    private void messageArrivedInternal(String topic, MqttMessage message) {
        try {
            String incomingMessage = new String(message.getPayload());
            String decryptedMessage = DRAWERS_CRYPTO_ENGINE.aesDecrypt(incomingMessage);
            MqttChatMessage chatMessage = MqttChatMessage.fromString(decryptedMessage);
            DrawersMessage reply = messageListener.processMessageAndReply(new DrawersMessage(topic, URLDecoder.decode(chatMessage.getMessage(), "UTF-8")));

            MqttChatMessage replyChatMessage = new MqttChatMessage(UUID.randomUUID().toString(), URLEncoder.encode(reply.getMessage(), "UTF-8"),
                    mqttClient.getClientId(), MqttChatMessage.ChatConstant.ChatType.TEXT, false);

            String encryptedMessage = DRAWERS_CRYPTO_ENGINE.aesEncrypt(MqttChatMessage.toJson(replyChatMessage));
            MqttMessage mqttMessage = new MqttMessage();
            mqttMessage.setPayload(encryptedMessage.getBytes());
            mqttMessage.setQos(1);
            mqttClient.publish(chatMessage.getSenderUid() + MESSAGES_OTHERS, mqttMessage);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
        System.out.println("Delivery Complete: " + token.toString());
    }
}
