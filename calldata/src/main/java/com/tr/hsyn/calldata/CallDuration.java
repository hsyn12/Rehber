package com.tr.hsyn.calldata;


public interface CallDuration {

    int getDuration();

    default boolean isSpoken() {

        return getDuration() != 0;
    }
}
