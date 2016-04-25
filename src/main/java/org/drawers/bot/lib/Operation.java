package org.drawers.bot.lib;

/**
 * Created by harshit on 27/1/16.
 */
public interface Operation {

    /*
     * Do not override this method
     */
     default Response operate(DrawersBotString body) {
        if(validateAndParse(body)) {
            return operateInternal(body);
        } else {
            throw new RuntimeException("Invalid argument not supported");
        }
    }

    Response operateInternal(DrawersBotString body);
    boolean validateAndParse(DrawersBotString botString);
}
