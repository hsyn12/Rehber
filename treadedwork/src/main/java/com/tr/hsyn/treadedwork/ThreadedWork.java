package com.tr.hsyn.treadedwork;

import com.tr.hsyn.execution.Runny;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.Callable;

import java9.util.concurrent.CompletableFuture;


public interface ThreadedWork {

    default void workOnBackground(@NotNull Runnable work) {

        Runny.run(work, false);
    }

    default void workOnMain(@NotNull Runnable work) {

        Runny.run(work);
    }

    default <R> CompletableFuture<R> completeWork(@NotNull Callable<R> callable) {

        return Runny.complete(callable);
    }

}
