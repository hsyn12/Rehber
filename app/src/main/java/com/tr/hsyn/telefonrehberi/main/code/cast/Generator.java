package com.tr.hsyn.telefonrehberi.main.code.cast;


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
