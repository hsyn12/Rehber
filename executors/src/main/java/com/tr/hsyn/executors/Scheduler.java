package com.tr.hsyn.executors;


import static com.tr.hsyn.executors.Executors.MIN_PRIORITY_EXECUTOR;
import static com.tr.hsyn.executors.Executors.NORM_PRIORITY_EXECUTOR;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.Callable;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;


public interface Scheduler {
	
	@NotNull
	static <T> ScheduledFuture<?> scheduleNorm(long delay, Runnable runnable) {
		
		return NORM_PRIORITY_EXECUTOR.schedule(runnable, delay, TimeUnit.MILLISECONDS);
	}
	
	@NotNull
	static <T> ScheduledFuture<T> scheduleNorm(long delay, Callable<T> callable) {
		
		return NORM_PRIORITY_EXECUTOR.schedule(callable, delay, TimeUnit.MILLISECONDS);
	}
	
	@NotNull
	static <T> ScheduledFuture<?> schedule(long delay, Runnable runnable) {
		
		return MIN_PRIORITY_EXECUTOR.schedule(runnable, delay, TimeUnit.MILLISECONDS);
	}
	
	@NotNull
	static <T> ScheduledFuture<T> schedule(long delay, Callable<T> callable) {
		
		return MIN_PRIORITY_EXECUTOR.schedule(callable, delay, TimeUnit.MILLISECONDS);
	}
	
	static boolean removeFromNorm(Runnable runnable) {
		
		return NORM_PRIORITY_EXECUTOR.remove(runnable);
	}
	
	static boolean removeFromMin(Runnable runnable) {
		
		return MIN_PRIORITY_EXECUTOR.remove(runnable);
	}
}
