package com.tr.hsyn.telefonrehberi.main.call.activity.random.listener;


import org.jetbrains.annotations.NotNull;


public interface ProgressListener<T> {

    void onProgress(@NotNull T value, int currentProgress, int total);
}
