package com.tr.hsyn.telefonrehberi.main.code.comment;


import org.jetbrains.annotations.NotNull;


/**
 * Yorumcu.
 *
 * @param <T> Konunun türü
 */
public interface Commentator<T> {

    /**
     * Konuyu yorumla.
     *
     * @param subject konu
     * @return yorum
     */
    @NotNull
    CharSequence commentate(@NotNull T subject);
}
