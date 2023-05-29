package com.tr.hsyn.telefonrehberi.main.call.cast;


import androidx.annotation.Nullable;


/**
 * Üretici.
 *
 * @param <T> Üretilen nesne türü
 */
public interface Generator<T> {

    @Nullable
    T generate();
}
