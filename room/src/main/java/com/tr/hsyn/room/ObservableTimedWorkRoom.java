package com.tr.hsyn.room;


import org.jetbrains.annotations.NotNull;


/**
 * Hem otomatik çıkış yapmaya hem de çıkışta bir işlem çalıştırılmasına imkan sağlar.
 * Ayrıca kapı durumu izlenebilir.
 */
public interface ObservableTimedWorkRoom extends TimedRoom, WorkRoom, ObservableRoom {

    @NotNull
    static ObservableTimedWorkRoom createRoom(long stayInDuration) {

        return new RedRoom(stayInDuration);
    }
}
