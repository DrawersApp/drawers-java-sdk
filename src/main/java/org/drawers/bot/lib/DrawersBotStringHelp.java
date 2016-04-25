package org.drawers.bot.lib;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

/**
 * Help send when user connects for the first time.
 */
public class DrawersBotStringHelp {
    public List<DrawersBotString> getDrawersBotStrings() {
        return drawersBotStrings;
    }

    private List<DrawersBotString> drawersBotStrings;

    public static DrawersBotStringHelp getDrawersBotStringHelp() {
        return drawersBotStringHelp;
    }

    private static DrawersBotStringHelp drawersBotStringHelp = new DrawersBotStringHelp();

    private DrawersBotStringHelp() {
        drawersBotStrings = new ArrayList<>();
    }

    public String toJsonString() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    public static DrawersBotStringHelp fromString(String json) {
        Gson gson = new Gson();
        return gson.fromJson(json, DrawersBotStringHelp.class);
    }
}
