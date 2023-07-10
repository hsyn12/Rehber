package com.tr.hsyn.execution;


import com.tr.hsyn.execution.annotation.OnBackground;
import com.tr.hsyn.execution.annotation.OnMain;
import com.tr.hsyn.executors.Executors;
import com.tr.hsyn.executors.MainExecutor;
import com.tr.hsyn.executors.MinExecutor;
import com.tr.hsyn.executors.NormExecutor;
import com.tr.hsyn.executors.Scheduler;
import com.tr.hsyn.reflection.Clazz;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Method;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

import java9.util.concurrent.CompletableFuture;


/**
 * Provides methods to work on a background or foreground thread.
 */
public enum Runny {
	;
	
	/**
	 * Executes a method.
	 *
	 * @param methodOwner method owner
	 * @param method      method
	 * @param args        method args
	 * @see OnMain
	 * @see OnBackground
	 */
	public static void run(@Nullable Object methodOwner, @NotNull Method method, Object... args) {
		
		OnMain       onMain = method.getAnnotation(OnMain.class);
		OnBackground onBack = method.getAnnotation(OnBackground.class);
		Runnable     work   = () -> Clazz.invoke(methodOwner, method, args);
		
		if (onMain != null && onBack != null)
			throw new IllegalArgumentException("Cannot use both @OnMain and @OnBackground : " + method);
		
		else if (onMain != null) run(work, true);
		else if (onBack != null) run(work, false);
		else work.run();
	}
	
	/**
	 * Executes a method.
	 *
	 * @param methodOwner method owner
	 * @param methodName  method name
	 * @param args        method args
	 */
	public static void run(@Nullable Object methodOwner, @NotNull String methodName, Object... args) {
		
		if (methodOwner != null) {
			
			Method method = Clazz.findMethod(methodOwner.getClass(), methodName, args);
			
			if (method != null) run(methodOwner, method, args);
		}
	}
	
	/**
	 * Executes a runnable.
	 *
	 * @param runnable runnable
	 * @param onMain   {@code true} if UI Thread, {@code false} background thread
	 * @param delay    delay in milliseconds
	 * @param reason   reason
	 */
	public static void run(@NotNull Runnable runnable, boolean onMain, long delay, @Nullable String reason) {
		
		if (onMain) MainExecutor.run(runnable, delay);
		else NormExecutor.run(runnable, delay);
	}
	
	/**
	 * Executes a runnable.
	 *
	 * @param runnable runnable
	 * @param onMain   {@code true} if UI Thread, {@code false} background thread
	 * @param delay    delay in milliseconds
	 */
	public static void run(@NotNull Runnable runnable, boolean onMain, long delay) {
		
		run(runnable, onMain, delay, null);
	}
	
	/**
	 * Executes a runnable.
	 *
	 * @param runnable runnable
	 * @param onMain   {@code true} if UI Thread, {@code false} background thread
	 */
	public static void run(@NotNull Runnable runnable, boolean onMain) {
		
		run(runnable, onMain, 0, null);
	}
	
	/**
	 * Executes a runnable.
	 *
	 * @param runnable runnable
	 * @param delay    delay in milliseconds
	 */
	public static void run(@NotNull Runnable runnable, long delay) {
		
		run(runnable, true, delay, null);
	}
	
	/**
	 * Executes the runnable on UI Thread.
	 *
	 * @param runnable runnable
	 */
	public static void run(@NotNull Runnable runnable) {
		
		run(runnable, true, 0, null);
	}
	
	/**
	 * Cancels the runnable.
	 *
	 * @param runnable     runnable
	 * @param isBackground the thread that runnable belongs to
	 */
	public static void cancel(@NotNull Runnable runnable, boolean isBackground) {
		
		if (isBackground) Scheduler.removeFromNorm(runnable);
		else MainExecutor.HOLDER.getMainExecutor().cancel(runnable);
	}
	
	/**
	 * Executes the callable.
	 *
	 * @param delay       delay in milliseconds
	 * @param minPriority {@code true} if low priority, {@code false} normal priority
	 * @param callable    callable
	 * @param <R>         return type
	 * @return CompletableFuture
	 */
	@NotNull
	public static <R> CompletableFuture<R> complete(long delay, boolean minPriority, @NotNull Callable<R> callable) {
		
		return CompletableFuture.supplyAsync(() -> {
			
			try {
				return minPriority ? Scheduler.scheduleMin(delay, callable).get() : Scheduler.scheduleNorm(delay, callable).get();
			}
			catch (InterruptedException | ExecutionException e) {e.printStackTrace();}
			
			return null;
		}, Executors.MIN_PRIORITY_EXECUTOR);
	}
	
	/**
	 * Executes a callable.
	 *
	 * @param minPriority {@code true} if low priority, {@code false} normal priority
	 * @param callable    callable
	 * @param <R>         return type
	 * @return CompletableFuture
	 */
	@NotNull
	public static <R> CompletableFuture<R> complete(boolean minPriority, @NotNull Callable<R> callable) {
		
		return complete(0L, minPriority, callable);
	}
	
	/**
	 * Executes a callable.
	 *
	 * @param delay    delay in milliseconds
	 * @param callable callable
	 * @param <R>      return type
	 * @return CompletableFuture
	 */
	@NotNull
	public static <R> CompletableFuture<R> complete(long delay, @NotNull Callable<R> callable) {
		
		return complete(delay, false, callable);
	}
	
	/**
	 * Executes the callable with normal priority.
	 *
	 * @param callable callable
	 * @param <R>      return type
	 * @return CompletableFuture
	 */
	@NotNull
	public static <R> CompletableFuture<R> complete(@NotNull Callable<R> callable) {
		
		return complete(0L, false, callable);
	}
	
	/**
	 * Executes the runnable.
	 *
	 * @param delay       delay in milliseconds
	 * @param minPriority {@code true} if low priority, {@code false} normal priority
	 * @param runnable    runnable
	 * @return CompletableFuture
	 */
	@NotNull
	public static CompletableFuture<Void> complete(long delay, boolean minPriority, @NotNull Runnable runnable) {
		
		if (minPriority) {
			
			return CompletableFuture.runAsync(() -> MinExecutor.run(runnable, delay));
		}
		return CompletableFuture.runAsync(() -> NormExecutor.run(runnable, delay));
	}
	
	/**
	 * Executes the runnable.
	 *
	 * @param runnable runnable
	 * @return CompletableFuture
	 */
	@NotNull
	public static CompletableFuture<Void> complete(@NotNull Runnable runnable) {
		
		return complete(0, false, runnable);
	}
	
	/**
	 * Executes the work.
	 *
	 * @param execution execution
	 */
	public static void run(@NotNull Execution execution) {
		
		if (execution.runnable != null) {
			
			if (!execution.onMain) {
				
				Scheduler.schedule(execution.delay, execution.minPriority, execution.runnable);
			}
			else {
				
				run(execution.runnable, true, execution.delay, execution.reason);
			}
		}
	}
	
	/**
	 * @return a new builder for execution
	 */
	@NotNull
	public static Execution.Builder builder() {
		
		return new Execution.Builder();
	}
	
	/**
	 * Work information.
	 */
	static class Execution {
		
		private final String   reason;
		private final long     delay;
		private final Runnable runnable;
		private final boolean  onMain;
		private final boolean  minPriority;
		
		/**
		 * Constructor of {@link Execution}.
		 *
		 * @param builder the builder
		 */
		private Execution(@NotNull Builder builder) {
			
			reason      = builder.reason;
			delay       = builder.delay;
			runnable    = builder.runnable;
			onMain      = builder.onMain;
			minPriority = builder.minPriority;
		}
		
		/**
		 * @return {@code true} if this work has delay
		 */
		public boolean hasDelay() {
			
			return delay > 0L;
		}
		
		/**
		 * @return {@code true} if this work has low-priority
		 */
		public boolean isMinPriority() {
			
			return minPriority;
		}
		
		/**
		 * @return the reason to this work
		 */
		public String getReason() {
			
			return reason;
		}
		
		/**
		 * @return the delay
		 */
		public long getDelay() {
			
			return delay;
		}
		
		/**
		 * @return the runnable to this work
		 */
		public Runnable getRunnable() {
			
			return runnable;
		}
		
		/**
		 * Executes this work
		 */
		public void run() {
			
			Runny.run(this);
		}
		
		/**
		 * The builder for {@link Execution}.
		 */
		public static final class Builder {
			
			private String   reason;
			private long     delay;
			private Runnable runnable;
			private boolean  onMain;
			private boolean  minPriority;
			
			/**
			 * Constructor of {@link Execution.Builder}.
			 */
			public Builder() {}
			
			/**
			 * Creates a new builder from {@link Execution}.
			 *
			 * @param copy execution
			 */
			public Builder(@NotNull Execution copy) {
				
				this.reason   = copy.getReason();
				this.delay    = copy.getDelay();
				this.runnable = copy.getRunnable();
				onMain        = copy.onMain;
			}
			
			/**
			 * Sets the priority for low.
			 *
			 * @return builder to chain
			 */
			public Builder minPriority() {
				
				this.minPriority = true;
				return this;
			}
			
			/**
			 * Sets the priority.
			 *
			 * @param min {@code true} if low priority, {@code false} normal priority
			 * @return builder to chain
			 */
			public Builder minPriority(boolean min) {
				
				this.minPriority = min;
				return this;
			}
			
			/**
			 * Sets the UI Thread.
			 *
			 * @return builder to chain
			 */
			@NotNull
			public Builder onMain() {
				
				this.onMain = true;
				return this;
			}
			
			/**
			 * Sets the reason.
			 *
			 * @param reason the reason
			 * @return builder to chain
			 */
			@NotNull
			public Builder reason(@NotNull String reason) {
				
				this.reason = reason;
				return this;
			}
			
			/**
			 * Sets the delay.
			 *
			 * @param delay the delay
			 * @return builder to chain
			 */
			@NotNull
			public Builder delay(long delay) {
				
				this.delay = delay;
				return this;
			}
			
			/**
			 * Sets the runnable.
			 *
			 * @param runnable the runnable
			 * @return builder to chain
			 */
			@NotNull
			public Builder runnable(@NotNull Runnable runnable) {
				
				this.runnable = runnable;
				return this;
			}
			
			/**
			 * Builds the execution.
			 *
			 * @return execution
			 */
			@NotNull
			public Execution build() {
				
				return new Execution(this);
			}
		}
	}
	
}