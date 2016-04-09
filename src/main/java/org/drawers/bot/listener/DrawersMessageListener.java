package org.drawers.bot.listener;

import org.drawers.bot.dto.DrawersMessage;

/**
 * Created by nishant.pathak on 08/04/16.
 */
public interface DrawersMessageListener {
    public DrawersMessage processMessageAndReply(DrawersMessage message);
}
