package com.tr.hsyn.room;


import com.tr.hsyn.execution.Runny;
import com.tr.hsyn.gate.Room;


/**
 * Odadan çıkıldığında bir çıkış işlemi çalıştırılmasına imkan verir.
 * Kapı durumu izlenebilir değildir, izlenmek isteniyorsa {@link ObservableRoom} kullanılmalıdır.
 *
 * @see Room
 * @see TimedRoom
 * @see ObservableRoom
 * @see WorkRoom
 */
public class Confessional extends GreenLand implements TimedWorkRoom {

    public Confessional(long interval) {

        super(interval);
    }

    @Override
    public boolean enterTheRoom(Runnable onExit) {

        if (enter()) {

            try {return true;}
            finally {Runny.run(onExit, getInterval());}
        }

        return false;
    }
}
