package com.tr.hsyn.holder;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


public interface Holder<T> {

    @NotNull
    static <T> Holder<T> newHolder() {

        return new Holo<>();
    }

    @NotNull
    static <T> Holder<T> newHolder(T obj) {

        return new Holo<>(obj);
    }

    @Nullable T get();

    void set(@Nullable T t);
}
