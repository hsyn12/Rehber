package com.tr.hsyn.executors;


import static com.tr.hsyn.executors.Executors.NORM_PRIORITY_EXECUTOR;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;


/**
 * Normal (system default) priority thread executor.
 */
public interface NormExecutor extends ExExecutor {
	
	/**
	 * Executes the command.
	 *
	 * @param command Runnable
	 */
	@Override
	default void execute(@NotNull Runnable command) {
		
		run(command);
	}
	
	/**
	 * Executes the command.
	 *
	 * @param command Runnable
	 * @param delay   Delay in milliseconds
	 */
	@Override
	default void execute(@NotNull Runnable command, long delay) {
		
		run(command, delay);
	}
	
	/**
	 * Executes the command with normal-priority.
	 *
	 * @param command Runnable to execute
	 */
	static void run(@NotNull Runnable command) {
		
		NORM_PRIORITY_EXECUTOR.execute(command);
	}
	
	/**
	 * Executes the command with normal-priority.
	 *
	 * @param command Runnable to execute
	 * @param delay   Delay in milliseconds
	 */
	static void run(@NotNull Runnable command, long delay) {
		
		if (delay > 0) NORM_PRIORITY_EXECUTOR.schedule(command, delay, TimeUnit.MILLISECONDS);
		else run(command);
	}
	
}
