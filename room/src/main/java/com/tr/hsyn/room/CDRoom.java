package com.tr.hsyn.room;


import com.tr.hsyn.execution.DelayedRunner;

import org.jetbrains.annotations.NotNull;


public class CDRoom extends RedRoom implements DigiRoom {

    private boolean       loopDelay;
    private DelayedRunner delayedRunner;

    public CDRoom(long interval) {

        super(interval);
    }

    @Override
    public boolean enterTheRoom(@NotNull Runnable onExit) {

        if (!loopDelay) return super.enterTheRoom(onExit);

        if (delayedRunner == null) delayedRunner = DelayedRunner.newRunner(onExit);

        if (isGateOpen()) {

            try {return true;}
            finally {

                closeTheGate();
                delayedRunner.run(getInterval());
            }
        }
        else {

            //- Süreyi sıfırla
            delayedRunner.cancel();
            delayedRunner.run(getInterval());
        }

        return false;
    }

    @Override
    public DigiRoom loopDelay() {

        this.loopDelay = true;
        return this;
    }
}
