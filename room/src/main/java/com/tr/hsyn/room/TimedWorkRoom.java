package com.tr.hsyn.room;


import com.tr.hsyn.gate.Room;

import org.jetbrains.annotations.NotNull;


/**
 * Hem otomatik çıkış yapmaya hem de çıkışta bir işlem çalıştırılmasına imkan sağlar.
 * Kapının durumu izlenebilir değildir.
 *
 * @see Room
 * @see TimedRoom
 * @see ObservableRoom
 * @see WorkRoom
 */
public interface TimedWorkRoom extends TimedRoom, WorkRoom {

    @NotNull
    static TimedWorkRoom createRoom(long stayInDuration) {

        return new Confessional(stayInDuration);
    }
}
