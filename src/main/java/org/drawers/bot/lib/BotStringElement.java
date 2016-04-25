package org.drawers.bot.lib;

import java.util.List;

/**
 * Created by harshit on 25/1/16.
 */
public class BotStringElement {
    public BotStringType getType() {
        return type;
    }

    public String getPlaceHolder() {
        return placeHolder;
    }

    public String getText() {
        return text;
    }

    public void setType(BotStringType type) {
        this.type = type;
    }

    public void setPlaceHolder(String placeHolder) {
        this.placeHolder = placeHolder;
    }

    public void setText(String text) {
        this.text = text;
    }
    public List<String> getDefaults() {
        return defaults;
    }

    public void setDefaults(List<String> defaults) {
        this.defaults = defaults;
    }

    private String placeHolder;
    private String text;
    private BotStringType type;

    private List<String> defaults; // Useful for %l

    public BotStringElement(BotStringType type, String placeHolder, String text, List<String> defaults) {
        this.placeHolder = placeHolder;
        this.text = text;
        this.type = type;
        this.defaults = defaults;
    }

    public BotStringElement(BotStringType type, String placeHolder, String text) {
        this.type = type;
        this.placeHolder = placeHolder;
        this.text = text;
    }

    public BotStringElement(BotStringType type, String text) {
        this.type = type;
        this.text = text;
        this.placeHolder = text;
    }
}
