package com.tr.hsyn.room;


import com.tr.hsyn.execution.Runny;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.atomic.AtomicBoolean;


public class RedRoom extends GreenLand implements ObservableTimedWorkRoom {

    private final AtomicBoolean            gate = new AtomicBoolean(true);
    private       Collection<GateObserver> gateObservers;

    public RedRoom(long interval) {

        super(interval);
    }

    @Override
    public void openTheGate() {

        try {gate.set(true);}
        finally {notifyGateState(true);}
    }

    @Override
    public void closeTheGate() {

        try {gate.set(false);}
        finally {notifyGateState(false);}
    }

    @Override
    public boolean isGateOpen() {

        return gate.get();
    }

    @Override
    public boolean enterTheRoom(@NotNull Runnable onExit) {

        if (enter()) {

            try {return true;}
            finally {Runny.run(onExit, getInterval());}
        }

        return false;
    }

    @Override
    public void exit() {

        openTheGate();
    }

    @Override
    public void addGateObserver(@NotNull GateObserver gateObserver) {

        if (gateObservers == null) gateObservers = new ArrayList<>();

        if (!gateObservers.contains(gateObserver)) gateObservers.add(gateObserver);
    }

    @Override
    public void removeGateObserver(@NotNull GateObserver gateObserver) {

        if (gateObservers == null) return;

        gateObservers.remove(gateObserver);
    }

    private void notifyGateState(boolean open) {

        if (gateObservers != null)
            gateObservers.forEach(o -> o.onGateStateChange(open));
    }
}
