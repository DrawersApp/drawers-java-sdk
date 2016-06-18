package org.drawers.bot.mqtt;

import com.drawers.dao.MqttChatMessage;
import com.drawers.dao.mqttinterface.PublisherImpl;
import com.drawers.dao.packets.MqttChat;
import com.drawers.dao.packets.MqttProviderManager;
import com.drawers.dao.packets.ParseFailedException;
import com.drawers.dao.packets.SubscribeOthers;
import org.drawers.bot.crypto.DrawersCryptoEngine;
import org.drawers.bot.listener.DrawersMessageListener;
import org.drawers.bot.util.SendMail;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by nishant.pathak on 08/04/16.
 */
public final class DrawersBot implements MqttCallback, PublisherImpl {

    private String clientId;
    private String password;
    private MqttClient mqttClient;
    private static final DrawersCryptoEngine DRAWERS_CRYPTO_ENGINE = new DrawersCryptoEngine();
    private ExecutorService executorService = Executors.newFixedThreadPool(DEFAULT_THREAD_COUNT);


    private static final String SERVER_URL = "mqtt-server.sandwitch.in";
    private static final String PROTOCOL = "tcp://";
    private static final String PROTOCOL_SECURE = "ssl://";
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

    private boolean isStartFailed;
    private Exception exception;

    public void start() {
        isStartFailed = false;
        try {
            MemoryPersistence persistence = new MemoryPersistence();
            mqttClient = new MqttClient(getServerUrl(), clientId, persistence);
            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setCleanSession(false);
            connOpts.setConnectionTimeout(10);
            connOpts.setUserName(clientId);
            connOpts.setPassword(password.toCharArray());

            System.out.println("Connecting to broker");
            mqttClient.connect(connOpts);
            System.out.println("connected");
            mqttClient.setCallback(this);
            mqttClient.subscribe(clientId + SubscribeOthers.OTHERS_NAMESPACE, MqttChat.QOS);

            messageListener.onConnected();

        } catch (Exception ex) {
            ex.printStackTrace();
            System.err.println("Will retry after 10 sec.");
            isStartFailed = true;
            exception = ex;
        } finally {
            try {
                if (isStartFailed) {
                    System.out.println("sleeping zzzz");
                    Thread.sleep(10000L);
                    start();
                    SendMail.getInstance().sendMail(DrawersBot.class.getPackage().getName() + " restart failed",
                            "Weather changed this time, Check below crash log for more details" +
                            "Contact harshit.bangar@gmail.com, nishantpathak.cse@gmail.com in " +
                            "case of any help\n" + "Stack trace:\n"  + Arrays.deepToString(exception.getStackTrace()));
                }
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
        System.out.println("message arrived");
        executorService.submit(() -> {
            try {
                // Decrypt message
                String incomingMessage = new String(message.getPayload());
                String decryptedMessage = DRAWERS_CRYPTO_ENGINE.aesDecrypt(incomingMessage);
                message.setPayload(decryptedMessage.getBytes());

                // invoke listeners
                MqttProviderManager.getInstanceFor(this).dispatch(message, topic, this);
            } catch (ParseFailedException e) {
                e.printStackTrace();
            }
        });
    }

    Set<MqttChatMessage> messageHistory = Collections.newSetFromMap(new LinkedHashMap<MqttChatMessage, Boolean>(){
        protected boolean removeEldestEntry(Map.Entry<MqttChatMessage, Boolean> eldest) {
            return size() > MAX_MESSAGE_HISTORY;
        }
    });

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
        System.out.println("Delivery Complete: " + token.toString());
    }

    @Override
    public IMqttDeliveryToken publish(String topic, MqttMessage message,
                                      String invocationContext, String activityToken) {
        try {
            // Encrypt message
            String replyMessage = new String(message.getPayload());
            String encryptedMessage = DRAWERS_CRYPTO_ENGINE.aesEncrypt(replyMessage);
            message.setPayload(encryptedMessage.getBytes());

            // Publish message
            mqttClient.publish(topic, message);
        } catch (MqttException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void subscribe(final String topic, final int qos,
                          String invocationContext, String activityToken) {
        try {
            mqttClient.subscribe(topic, qos);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void unsubscribe(final String topic, String invocationContext,
                            String activityToken) {
        try {
            mqttClient.unsubscribe(topic);
        } catch (MqttException e) {
            e.printStackTrace();
        }

    }
}
