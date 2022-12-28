package com.tr.hsyn.execution;


public class SwitchRunner extends RunTimer implements ThreadRunner {

    private boolean onBackground;

    public SwitchRunner(Runnable runnable) {

        super(runnable);
    }

    @Override
    public void run(long delay) {

        Runny.run(getRunnable(), !onBackground, delay);
    }

    @Override
    public void cancel() {

        Runny.cancel(getRunnable(), onBackground);
    }

    @Override
    public ThreadRunner onBackground() {

        return onBackground(true);
    }

    @Override
    public ThreadRunner onBackground(boolean onBackground) {

        this.onBackground = onBackground;
        return this;
    }
}
