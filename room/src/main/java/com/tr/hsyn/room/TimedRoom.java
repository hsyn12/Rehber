package com.tr.hsyn.room;


import com.tr.hsyn.gate.Room;

import org.jetbrains.annotations.NotNull;


/**
 * Zaman ayarlı oda.
 * Otomatik kapı olarak da düşünebilirsin, ancak biraz farklı çalışır.
 * Başarılı bir giriş sonrasında, belirtilen süre sonunda otomatik çıkış yapmayı sağlar.
 */
public interface TimedRoom extends Room {

    /**
     * Yeni bir oda oluşturur.
     *
     * @param duration otomatik çıkış süresi
     * @return TimeRoom
     */
    @NotNull
    static TimedRoom createRoom(long duration) {

        return new GreenLand(duration);
    }

    /**
     * @return Girişten sonra otomatik çıkış için geçmesi gereken süre
     */
    long getInterval();
}