package com.tr.hsyn.treadedwork;


import com.tr.hsyn.execution.Runny;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.Callable;

import java9.util.concurrent.CompletableFuture;


/**
 * Provides ready to use methods for the background and foreground work.
 */
public interface Threaded {
	
	/**
	 * Executes the work on the background.
	 *
	 * @param backgroundWork the work to execute.
	 */
	default void onBackground(@NotNull Runnable backgroundWork) {
		
		Runny.run(backgroundWork, false);
	}
	
	/**
	 * Executes the work on the foreground.
	 *
	 * @param uiWork the work to execute.
	 */
	default void onMain(@NotNull Runnable uiWork) {
		
		Runny.run(uiWork);
	}
	
	/**
	 * Executes the work on the background.
	 *
	 * @param callable the work to execute.
	 * @param <R>      the return type
	 * @return a {@link CompletableFuture}
	 */
	default <R> CompletableFuture<R> completeWork(@NotNull Callable<R> callable) {
		
		return Runny.complete(callable);
	}
	
}
