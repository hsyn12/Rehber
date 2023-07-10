package com.tr.hsyn.executors;


import static com.tr.hsyn.executors.Executors.MIN_PRIORITY_EXECUTOR;
import static com.tr.hsyn.executors.Executors.NORM_PRIORITY_EXECUTOR;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.Callable;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;


/**
 * Defines static methods to schedule the works.<br>
 */
public interface Scheduler {
	
	/**
	 * Schedules the works.
	 *
	 * @param delay       delay in milliseconds
	 * @param minPriority whether to execute the works on low-priority threads
	 * @param runnable    runnable
	 * @param <T>         return type
	 * @return scheduled future
	 */
	@NotNull
	static <T> ScheduledFuture<?> schedule(long delay, boolean minPriority, Runnable runnable) {
		
		if (minPriority)
			return MIN_PRIORITY_EXECUTOR.schedule(runnable, delay, TimeUnit.MILLISECONDS);
		return NORM_PRIORITY_EXECUTOR.schedule(runnable, delay, TimeUnit.MILLISECONDS);
	}
	
	/**
	 * Schedules the works.
	 *
	 * @param delay       delay in milliseconds
	 * @param minPriority whether to execute the works on low-priority threads
	 * @param callable    callable
	 * @param <T>         return type
	 * @return scheduled future
	 */
	@NotNull
	static <T> ScheduledFuture<T> schedule(long delay, boolean minPriority, Callable<T> callable) {
		
		if (minPriority)
			return MIN_PRIORITY_EXECUTOR.schedule(callable, delay, TimeUnit.MILLISECONDS);
		return NORM_PRIORITY_EXECUTOR.schedule(callable, delay, TimeUnit.MILLISECONDS);
	}
	
	/**
	 * Schedules the works on normal-priority threads.<br>
	 *
	 * @param delay    delay in milliseconds
	 * @param runnable runnable
	 * @param <T>      return type
	 * @return scheduled future
	 */
	@NotNull
	static <T> ScheduledFuture<?> scheduleNorm(long delay, Runnable runnable) {
		
		return schedule(delay, false, runnable);
	}
	
	/**
	 * Schedules the works on normal-priority threads.<br>
	 *
	 * @param delay    delay in milliseconds
	 * @param callable callable
	 * @param <T>      return type
	 * @return scheduled future
	 */
	@NotNull
	static <T> ScheduledFuture<T> scheduleNorm(long delay, Callable<T> callable) {
		
		return schedule(delay, false, callable);
	}
	
	/**
	 * Schedules the works on low-priority threads.<br>
	 *
	 * @param delay    delay in milliseconds
	 * @param runnable runnable
	 * @param <T>      return type
	 * @return scheduled future
	 */
	@NotNull
	static <T> ScheduledFuture<?> scheduleMin(long delay, Runnable runnable) {
		
		return schedule(delay, true, runnable);
	}
	
	/**
	 * Schedules the works on low-priority threads.<br>
	 *
	 * @param delay    delay in milliseconds
	 * @param callable callable
	 * @param <T>      return type
	 * @return scheduled future
	 */
	@NotNull
	static <T> ScheduledFuture<T> scheduleMin(long delay, Callable<T> callable) {
		
		return schedule(delay, true, callable);
	}
	
	/**
	 * Removes the works.
	 *
	 * @param runnable runnable
	 * @return {@code true} if removed
	 */
	static boolean removeFromNorm(Runnable runnable) {
		
		return NORM_PRIORITY_EXECUTOR.remove(runnable);
	}
	
	/**
	 * Removes the works.
	 *
	 * @param runnable runnable
	 * @return {@code true} if removed
	 */
	static boolean removeFromMin(Runnable runnable) {
		
		return MIN_PRIORITY_EXECUTOR.remove(runnable);
	}
}
