package org.drawers.bot.listener;

/**
 * Created by nishant.pathak on 08/04/16.
 */
public interface ConnectionStateListener {
    void onConnected();
    void onConnectionLost();
}
