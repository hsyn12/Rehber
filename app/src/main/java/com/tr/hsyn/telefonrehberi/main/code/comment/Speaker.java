package com.tr.hsyn.telefonrehberi.main.code.comment;


import android.app.Activity;

import org.jetbrains.annotations.NotNull;


public interface Speaker<T> {

    @NotNull
    default CharSequence commentate(
            @NotNull Activity activity,
            @NotNull T subject) {

        return null;
    }
}
