package com.tr.hsyn.bool;


import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;


/**
 * <h1>Any</h1>
 * <p>
 * Taban sınıfı.
 *
 * @param <T>
 * @author hsyn 13 Haziran 2022 Pazartesi 21:05
 */
public interface Any<T> {

    @Nullable
    T getObject();

    default boolean isNull() {

        return getObject() == null;
    }

    default boolean isNotNull() {

        return getObject() != null;
    }

    default Any<T> ifNotNull(Runnable runnable) {

        if (isNotNull()) runnable.run();
        return this;
    }

    default Any<T> ifNotNull(Consumer<T> consumer) {

        if (isNotNull()) consumer.accept(getObject());
        return this;
    }

    default Any<T> ifNull(Runnable runnable) {

        if (isNull()) runnable.run();
        return this;
    }

    default Any<T> orElse(Any<T> value) {

        return isNull() ? value : this;
    }

    default Any<T> orElse(Any<T> value, Consumer<Any<T>> consumer) {

        if (isNotNull()) consumer.accept(this);
        else consumer.accept(value);

        return this;
    }

}
