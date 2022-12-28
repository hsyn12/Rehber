package com.tr.hsyn.room;


import org.jetbrains.annotations.NotNull;


/**
 * Üst sınıflara ek olarak,
 * odada iken içeri girme talebi olursa otomatik çıkış süresini sıfırlayıp
 * baştan saymaya başlamasını sağlayan bir yapı sunar.
 *
 * @see #loopDelay()
 */
public interface DigiRoom extends ObservableTimedWorkRoom {

    @NotNull
    static DigiRoom createRoom(long duration) {

        return new CDRoom(duration);
    }

    /**
     * Bu çağrıdan sonra, oda dolu iken her içeri girme talebi otomatik çıkış süresinin sıfırlanıp
     * baştan saymaya başlamasına sebep olur.
     * Yani çıkış geçikmiş olur.
     *
     * @return digi room
     */
    DigiRoom loopDelay();
}
