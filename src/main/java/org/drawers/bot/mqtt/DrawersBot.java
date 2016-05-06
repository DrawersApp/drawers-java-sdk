package org.drawers.bot.mqtt;

import com.drawers.dao.MqttChatMessage;
import com.google.gson.Gson;
import org.drawers.bot.crypto.DrawersCryptoEngine;
import org.drawers.bot.dto.DrawersMessage;
import org.drawers.bot.listener.DrawersMessageListener;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.*;
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
    private static final int MAX_MESSAGE_HISTORY = 20000;


    private DrawersMessageListener messageListener;

    public DrawersBot(String clientId, String password, DrawersMessageListener messageListener) {
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
        try {
            MemoryPersistence persistence = new MemoryPersistence();
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
            start();
        } finally {
            try {
                System.err.println("Will retry after 10 sec.");
                Thread.sleep(10000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
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


    Set<MqttChatMessage> messageHistory = Collections.newSetFromMap(new LinkedHashMap<MqttChatMessage, Boolean>(){
        protected boolean removeEldestEntry(Map.Entry<MqttChatMessage, Boolean> eldest) {
            return size() > MAX_MESSAGE_HISTORY;
        }
    });

    private static final Gson gson = new Gson();

    private void messageArrivedInternal(String topic, MqttMessage message) {
        try {
            String incomingMessage = new String(message.getPayload());
            String decryptedMessage = DRAWERS_CRYPTO_ENGINE.aesDecrypt(incomingMessage);
            MqttChatMessage chatMessage = gson.fromJson(decryptedMessage, MqttChatMessage.class);

            if (!messageHistory.add(chatMessage)) {
                return;
            }

            DrawersMessage reply = messageListener.processMessageAndReply(new DrawersMessage(topic, URLDecoder.decode(chatMessage.message, "UTF-8")));

            MqttChatMessage replyChatMessage = new MqttChatMessage(UUID.randomUUID().toString(), URLEncoder.encode(reply.getMessage(), "UTF-8"),
                    mqttClient.getClientId(), reply.getChatType(), false);

            String encryptedMessage = DRAWERS_CRYPTO_ENGINE.aesEncrypt(gson.toJson(replyChatMessage));
            MqttMessage mqttMessage = new MqttMessage();
            mqttMessage.setPayload(encryptedMessage.getBytes());
            mqttMessage.setQos(1);
            mqttClient.publish(chatMessage.senderUid + MESSAGES_OTHERS, mqttMessage);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
        System.out.println("Delivery Complete: " + token.toString());
    }
}
