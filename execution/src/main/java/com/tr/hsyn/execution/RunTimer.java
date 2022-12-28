package com.tr.hsyn.execution;


public class RunTimer extends RoadRunner implements DelayedRunner {

    public RunTimer(Runnable runnable) {

        super(runnable);
    }

    @Override
    public void run() {run(0L);}

    @Override
    public void run(long delay) {

        Runny.run(getRunnable(), delay);
    }

    @Override
    public void cancel() {

        Runny.cancel(getRunnable(), false);
    }
}
