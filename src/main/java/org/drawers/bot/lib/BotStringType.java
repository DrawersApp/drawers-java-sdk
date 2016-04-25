package org.drawers.bot.lib;

/**
 * Created by nishant.pathak on 24/04/16.
 */
public enum BotStringType {
    D("DATE"),
    L("LOCATION"),
    T("TIME"),
    S("STRING"),
    U("UNEDITABLE"),
    LI("LIST"),
    I("INTEGER");

    public String getDesc() {
        return desc;
    }

    private String desc;
    BotStringType(String desc) {
        this.desc = desc;
    }
}
