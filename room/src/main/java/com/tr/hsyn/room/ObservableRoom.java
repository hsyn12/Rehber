package com.tr.hsyn.room;


import com.tr.hsyn.gate.Room;


/**
 * Kapının açılıp kapandığı durumlarda haber almayı sağlayan bir oda.
 */
public interface ObservableRoom extends Room {

    void addGateObserver(GateObserver gateObserver);

    void removeGateObserver(GateObserver gateObserver);

    /**
     * Kapının durumunu dinleyen sınıf
     */
    interface GateObserver {

        void onGateStateChange(boolean isOpen);
    }

}
