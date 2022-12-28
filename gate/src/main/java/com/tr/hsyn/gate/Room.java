package com.tr.hsyn.gate;


import org.jetbrains.annotations.NotNull;


/**
 * Oda.
 * Giriş-çıkış yapılan bir yer.
 */
public interface Room {

    /**
     * Yeni bir oda oluşturur.
     *
     * @return Room
     */
    @NotNull
    static Room createRoom() {

        return new Base();
    }
    
    /**
     * Yeni bir oda oluşturur.
     *
     * @param gate Oda için kullanılacak olan kapı
     * @return Room
     */
    @NotNull
    static Room createRoom(@NotNull Gate gate) {

        return new Base(gate);
    }

    /**
     * Odaya gir.
     *
     * @return Giriş başarılı ise {@code true}, değilse {@code false}
     */
    boolean enter();

    /**
     * Odadan çık.
     */
    void exit();
}
