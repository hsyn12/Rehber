package com.tr.hsyn.room;


import com.tr.hsyn.gate.Room;


/**
 * Odadan çıkıldığında bir çıkış işlemi çalıştırılmasına imkan verir.
 */
public interface WorkRoom extends Room {

    /**
     * Odaya girmek için telepte bulunur.
     *
     * @param onExit Odaya girilirse, çıkarken çalıştırılacak iş
     * @return Odaya girildiyse {@code true}, aksi halde {@code false}
     */
    boolean enterTheRoom(Runnable onExit);

}
