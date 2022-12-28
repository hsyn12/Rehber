package com.tr.hsyn.telefonrehberi.main.activity.call;


import android.graphics.drawable.Drawable;


public class MostCallsItemData {

    private final String   callName;
    private final String   typeName;
    private final Drawable type;
    private final Drawable rank;

    public MostCallsItemData(String callName, String typeName, Drawable type, Drawable rank) {

        this.callName = callName;
        this.typeName = typeName;
        this.type     = type;
        this.rank     = rank;
    }

    public String getCallName() {

        return callName;
    }

    public String getTypeName() {

        return typeName;
    }

    public Drawable getType() {

        return type;
    }

    public Drawable getRank() {

        return rank;
    }

    @Override
    public String toString() {

        return "MostCallsItemData{" +
               "callName='" + callName + '\'' +
               ", typeName='" + typeName + '\'' +
               ", type=" + type +
               ", rank=" + rank +
               '}';
    }
}
