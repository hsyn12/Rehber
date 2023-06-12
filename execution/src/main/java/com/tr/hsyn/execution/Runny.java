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
 * Bu sınıf, iş yürütmek için metotlar sağlar.
 */
public enum Runny {
	;
	
	/**
	 * Metodu, bildirildiği şekilde (ön planda yada arka planda) çalıştırır.
	 * Eğer bir çalışma şekli bildirilmemiş ise metodun çağrıldığı yerden direk çağrılır.
	 *
	 * @param methodOwner Metodun ait olduğu sınıfın örneği (static metotlar için {@code null} olabilir)
	 * @param method      Metot
	 * @param args        Metodun argümanları
	 */
	public static void run(@Nullable Object methodOwner, @NotNull Method method, Object... args) {
		
		OnMain       onMain = method.getAnnotation(OnMain.class);
		OnBackground onBack = method.getAnnotation(OnBackground.class);
		Runnable     work   = () -> Clazz.invoke(methodOwner, method, args);
		
		if (onMain != null && onBack != null)
			throw new IllegalArgumentException("Her iki çalışma modu kullanılamaz");
		else if (onMain != null) MainExecutor.run(work);
		else if (onBack != null) NormExecutor.run(work);
		else work.run();
	}
	
	/**
	 * Verilen isimdeki metodu çalıştır.
	 *
	 * @param methodOwner Method owner
	 * @param methodName  Method name
	 * @param args        Method args
	 */
	public static void run(@Nullable Object methodOwner, @NotNull String methodName, Object... args) {
		
		if (methodOwner != null) {
			
			Method method = Clazz.findMethod(methodOwner.getClass(), methodName, args);
			
			if (method != null) run(methodOwner, method, args);
		}
	}
	
	/**
	 * Verilen işi koştur.
	 *
	 * @param runnable İş
	 * @param onMain   UI Thread mi?
	 * @param delay    Erteleme süresi (milisaniye)
	 * @param reason   İşin amacı
	 */
	public static void run(@NotNull Runnable runnable, boolean onMain, long delay, @Nullable String reason) {
		
		if (onMain) MainExecutor.run(runnable, delay);
		else NormExecutor.run(runnable, delay);
	}
	
	/**
	 * Verilen işi koştur.
	 *
	 * @param runnable İş
	 * @param onMain   UI Thread mi?
	 * @param delay    Erteleme süresi (milisaniye)
	 */
	public static void run(@NotNull Runnable runnable, boolean onMain, long delay) {
		
		run(runnable, onMain, delay, null);
	}
	
	/**
	 * Verilen işi koştur.
	 *
	 * @param runnable İş
	 * @param onMain   UI Thread mi?
	 */
	public static void run(@NotNull Runnable runnable, boolean onMain) {
		
		run(runnable, onMain, 0, null);
	}
	
	/**
	 * Verilen işi ui thread üzerinde koştur.
	 *
	 * @param runnable İş
	 * @param delay    Erteleme süresi (milisaniye)
	 */
	public static void run(@NotNull Runnable runnable, long delay) {
		
		run(runnable, true, delay, null);
	}
	
	/**
	 * Verilen işi ui thread üzerinde koştur.
	 *
	 * @param runnable İş
	 */
	public static void run(@NotNull Runnable runnable) {
		
		run(runnable, true, 0, null);
	}
	
	/**
	 * İşi iptal et.
	 * Eğer iş çalışmaya başlamış ise iptal edilemez.
	 *
	 * @param runnable     İş
	 * @param isBackground İptal edilmek istenen iş için belirtilmiş olan thread
	 */
	public static void cancel(@NotNull Runnable runnable, boolean isBackground) {
		
		if (isBackground) Scheduler.removeFromNorm(runnable);
		else MainExecutor.HOLDER.getMainExecutor().cancel(runnable);
	}
	
	/**
	 * Verilen işi koştur.
	 *
	 * @param delay       Erteleme süresi
	 * @param minPriority Öncelik
	 * @param callable    İş
	 * @param <R>         İş dönüş türü
	 * @return CompletableFuture
	 */
	@NotNull
	public static <R> CompletableFuture<R> complete(long delay, boolean minPriority, @NotNull Callable<R> callable) {
		
		return CompletableFuture.supplyAsync(() -> {
			
			try {
				return minPriority ? Scheduler.schedule(delay, callable).get() : Scheduler.scheduleNorm(delay, callable).get();
			}
			catch (InterruptedException | ExecutionException e) {e.printStackTrace();}
			
			return null;
		}, Executors.MIN_PRIORITY_EXECUTOR);
	}
	
	/**
	 * Verilen işi koştur.
	 *
	 * @param minPriority Öncelik
	 * @param callable    İş
	 * @param <R>         İş dönüş türü
	 * @return CompletableFuture
	 */
	@NotNull
	public static <R> CompletableFuture<R> complete(boolean minPriority, @NotNull Callable<R> callable) {
		
		return complete(0L, minPriority, callable);
	}
	
	/**
	 * Verilen işi koştur.
	 *
	 * @param delay    Erteleme süresi
	 * @param callable İş
	 * @param <R>      İş dönüş türü
	 * @return CompletableFuture
	 */
	@NotNull
	public static <R> CompletableFuture<R> complete(long delay, @NotNull Callable<R> callable) {
		
		return complete(delay, false, callable);
	}
	
	/**
	 * Verilen işi koştur.
	 *
	 * @param callable İş
	 * @param <R>      İş dönüş türü
	 * @return CompletableFuture
	 */
	@NotNull
	public static <R> CompletableFuture<R> complete(@NotNull Callable<R> callable) {
		
		return complete(0L, false, callable);
	}
	
	/**
	 * Verilen işi koştur.
	 *
	 * @param delay       Erteleme süresi
	 * @param minPriority Öncelik
	 * @param runnable    İş
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
	 * Verilen işi koştur.
	 *
	 * @param runnable İş
	 * @return CompletableFuture
	 */
	@NotNull
	public static CompletableFuture<Void> complete(@NotNull Runnable runnable) {
		
		return complete(0, false, runnable);
	}
	
	/**
	 * Verilen işi koştur.
	 *
	 * @param execution İş nesnesi
	 */
	public static void run(@NotNull Execution execution) {
		
		if (execution.runnable != null) {
			
			run(execution.runnable, execution.onMain, execution.delay, execution.reason);
		}
	}
	
	@NotNull
	public static Execution.Builder builder() {
		
		return new Execution.Builder();
	}
	
	@NotNull
	public static Runner onBackgroundRunner(@NotNull final Runnable runnable) {
		
		try {
			return new Runner() {
				
				@Override
				public @NotNull Runnable getRunnable() {
					
					return runnable;
				}
				
				@Override
				public void run() {
					
					Runny.run(runnable, false);
				}
			};
		}
		catch (Exception e) {
			
			e.printStackTrace();
			throw new IllegalArgumentException("madafaka");
		}
	}
	
	/**
	 * İş
	 */
	static class Execution {
		
		private final String   reason;
		private final long     delay;
		private final Runnable runnable;
		private final boolean  onMain;
		
		private Execution(@NotNull Builder builder) {
			
			reason   = builder.reason;
			delay    = builder.delay;
			runnable = builder.runnable;
			onMain   = builder.onMain;
		}
		
		public String getReason() {
			
			return reason;
		}
		
		public long getDelay() {
			
			return delay;
		}
		
		public Runnable getRunnable() {
			
			return runnable;
		}
		
		public void run() {
			
			Runny.run(this);
		}
		
		public static final class Builder {
			
			private String   reason;
			private long     delay;
			private Runnable runnable;
			private boolean  onMain;
			
			public Builder() {}
			
			public Builder(@NotNull Execution copy) {
				
				this.reason   = copy.getReason();
				this.delay    = copy.getDelay();
				this.runnable = copy.getRunnable();
				onMain        = copy.onMain;
			}
			
			@NotNull
			public Builder onMain() {
				
				this.onMain = true;
				return this;
			}
			
			@NotNull
			public Builder reason(@NotNull String reason) {
				
				this.reason = reason;
				return this;
			}
			
			@NotNull
			public Builder delay(long delay) {
				
				this.delay = delay;
				return this;
			}
			
			@NotNull
			public Builder runnable(@NotNull Runnable runnable) {
				
				this.runnable = runnable;
				return this;
			}
			
			@NotNull
			public Execution build() {
				
				return new Execution(this);
			}
		}
	}
	
}