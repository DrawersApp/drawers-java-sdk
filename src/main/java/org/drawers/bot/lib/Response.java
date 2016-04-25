package org.drawers.bot.lib;

import com.google.gson.Gson;

/**
 * Created by harshit on 27/1/16.
 */
public interface Response {
    // It just prints object in json format.
    default String toUserString() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
