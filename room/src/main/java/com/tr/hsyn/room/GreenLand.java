package com.tr.hsyn.room;


import com.tr.hsyn.execution.Runny;


/**
 * Belirtilen bir süre sonunda otomatik çıkış yapmayı sağlar.
 */
public class GreenLand extends BaseRoom implements TimedRoom {

    private final long interval;

    public GreenLand(long interval) {

        this.interval = interval;
    }

    @Override
    public boolean enter() {

        if (super.enter()) {

            try {return true;}
            finally {Runny.run(this::exit, interval);}
        }

        return false;
    }

    @Override
    public long getInterval() {

        return interval;
    }
}
