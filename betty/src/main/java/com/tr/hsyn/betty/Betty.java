package com.tr.hsyn.betty;


import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.Callable;
import java.util.function.Consumer;


/**
 * Provides practical handling of code that is likely to produce errors.<br><br>
 *
 * <pre>
 * Runnable runnable1 = () -> {throw new IllegalArgumentException("runnable1");};
 * Runnable runnable2 = () -> System.out.println("runnable2");
 * String   name      = "Betty";
 *
 * var betty = Betty.bet(!name.isBlank())// control point. This is optional
 * 		.care(runnable1) // error part. This is the work
 * 		.call(runnable2) // direct call. This is optional
 * 		.onSuccess(() -> System.out.println("success1"))
 * 		.onError((e) -> System.out.println("Error1 :" + e));
 *
 * System.out.println(betty);
 * // runnable2
 * // Error1 :java.lang.IllegalArgumentException: runnable1
 * // Betty{exception=java.lang.IllegalArgumentException: runnable1, returnValue=null, checkOut=true}
 * </pre>
 */
public class Betty<R> {
	
	/**
	 * The error if there is one.
	 */
	@Nullable
	protected final Exception exception;
	/**
	 * Object to be returned from the job.
	 */
	@Nullable
	protected final R         returnValue;
	/**
	 * Indicates whether the job will run or not.
	 * If this variable is {@code false}, the given job will not run.
	 */
	@Nullable
	protected final Boolean   checkOut;
	
	/**
	 * Creates a new {@code Betty} instance.
	 */
	protected Betty() {
		
		this(null, null, null);
	}
	
	/**
	 * Creates a new {@code Betty} instance.
	 *
	 * @param checkOut control variable. If this variable is {@code false}, the job will not run.
	 */
	protected Betty(boolean checkOut) {
		
		this(null, null, checkOut);
	}
	
	/**
	 * Creates a new {@code Betty} instance.
	 *
	 * @param exception exception
	 */
	protected Betty(@Nullable final Exception exception) {
		
		this(exception, null, null);
	}
	
	/**
	 * Creates a new {@code Betty} instance.
	 *
	 * @param exception exception
	 * @param checkOut  control variable. If this variable is {@code false}, the job will not run.
	 */
	protected Betty(@Nullable final Exception exception, boolean checkOut) {
		
		this(exception, null, checkOut);
	}
	
	/**
	 * Creates a new {@code Betty} instance.
	 *
	 * @param returnValue return value
	 */
	protected Betty(@Nullable R returnValue) {
		
		this(null, returnValue, null);
	}
	
	/**
	 * Creates a new {@code Betty} instance.
	 *
	 * @param exception   exception
	 * @param returnValue return value
	 */
	protected Betty(@Nullable final Exception exception, @Nullable R returnValue) {
		
		this(exception, returnValue, null);
	}
	
	/**
	 * Creates a new {@code Betty} instance.
	 *
	 * @param exception   exception
	 * @param returnValue return value
	 * @param checkOut    control variable
	 */
	protected Betty(@Nullable final Exception exception, @Nullable R returnValue, @Nullable Boolean checkOut) {
		
		this.exception   = exception;
		this.returnValue = returnValue;
		this.checkOut    = checkOut;
	}
	
	@NotNull
	@Override
	public String toString() {
		
		return "Betty{" +
		       "exception=" + exception +
		       ", returnValue=" + returnValue +
		       ", checkOut=" + checkOut +
		       '}';
	}
	
	/**
	 * Executes the runnable.
	 * If there is a checkOut flag,
	 * only if this variable has {@code true} value, the runnable will be executed.
	 *
	 * @param runnable runnable to be executed
	 * @return if the runnable executed then returns a new {@code Bet} object
	 * 		that holds the result for the further processing chain.
	 * 		After the runnable is executed,
	 * 		if execute a new job again right from the returned new Bet object,
	 * 		the former result will be lost.
	 * 		Only the last job result is held.<br>
	 * 		If the runnable does not execute, then returns this Bet object.
	 */
	@NotNull
	public Betty<R> care(@NotNull final Runnable runnable) {
		
		if (checkOut == null || checkOut) {
			
			Exception e = null;
			
			try {runnable.run();}
			catch (Exception ex) {e = ex;}
			
			return new Betty<>(e, null, checkOut);
		}
		
		return this;
	}
	
	/**
	 * Executes the callable.
	 * If there is a checkOut flag,
	 * only if this variable has {@code true} value, the runnable will be executed.
	 *
	 * @param callable callable
	 * @return if the callable executed then returns a new {@code Bet} object
	 * 		that holds the result for the further processing chain.
	 * 		After the callable is executed,
	 * 		if execute a new job again right from the returned new Bet object,
	 * 		the former result will be lost.
	 * 		Only the last job result is held.<br>
	 * 		If the callable does not execute, return this Bet object.
	 */
	public Betty<R> care(@NotNull final Callable<R> callable) {
		
		Exception e = null;
		R         v = null;
		
		if (checkOut == null || checkOut) {
			try {v = callable.call();}
			catch (Exception ex) {e = ex;}
			
			return new Betty<>(e, v, checkOut);
		}
		
		return this;
	}
	
	/**
	 * Tests whether the checkOut flag is set.
	 * If the checkOut flag is not null, then the consumer will be called.
	 *
	 * @param consumer consumer
	 * @return this Bet object
	 */
	public Betty<R> checkIn(@NotNull Consumer<Boolean> consumer) {
		
		if (checkOut != null) {
			
			consumer.accept(checkOut);
		}
		
		return this;
	}
	
	/**
	 * Defines the control flag.
	 * If this variable is {@code false}, the job will not run.
	 *
	 * @param checkout control variable
	 * @return new {@code Bet} object copy with stored the flag
	 */
	public Betty<R> checkOut(boolean checkout) {
		
		return new Betty<>(exception, returnValue, checkout);
	}
	
	/**
	 * Executes the runnable directly.
	 *
	 * @param runnable runnable
	 * @return this Bet object
	 */
	@NotNull//Finally
	public Betty<R> call(@NotNull final Runnable runnable) {
		
		runnable.run();
		return this;
	}
	
	/**
	 * If the given work has been completed without generating an error,
	 * the given work is called to this method.
	 * <p>
	 * If a control variable is given ({@link #checkOut(boolean)}),
	 * it is called if and only if {@code true}.
	 *
	 * @param action action
	 * @return this Bet object
	 */
	public Betty<R> onSuccess(@NotNull final Runnable action) {
		
		if (exception == null && (checkOut == null || checkOut))
			action.run();
		
		return this;
	}
	
	/**
	 * If the given work was completed without generating an error,
	 * the object given to this method is called with the result.
	 * <p>
	 * If a control variable is given ({@link #checkOut(boolean)}),
	 * it is called if and only if {@code true}.
	 *
	 * @param consumer consumer
	 * @return this Bet object
	 */
	public Betty<R> onSuccess(@NotNull final Consumer<R> consumer) {
		//- Eğer kontrol edilecek bir şey varsa ve bu false ise çağrılmayacak
		//- Eğer işlem hata üretmiş ise çağrılmayacak
		if (exception == null && (checkOut == null || checkOut))
			consumer.accept(returnValue);
		
		return this;
	}
	
	/**
	 * Sets the error consumer.
	 * Called if there is an error.
	 *
	 * @param consumer error consumer
	 * @return this Bet object
	 */
	@NotNull//Exceptional
	public Betty<R> onError(@NotNull final Consumer<@NotNull Throwable> consumer) {
		
		if (exception != null)
			consumer.accept(exception);
		
		return this;
	}
	
	/**
	 * Creates a new {@code Betty} instance.
	 *
	 * @param runnable runnable
	 * @param <R>      return type
	 * @return this Bet object
	 */
	@NotNull
	public static <R> Betty<R> bet(@NotNull final Runnable runnable) {
		
		return new Betty<R>().care(runnable);
	}
	
	/**
	 * Creates a new {@code Betty} instance.
	 *
	 * @param out control variable
	 * @param <R> return type
	 * @return this Bet object
	 */
	public static <R> Betty<R> bet(boolean out) {
		
		return new Betty<R>().checkOut(out);
	}
	
	/**
	 * Creates a new {@code Betty} instance.
	 *
	 * @param callable callable
	 * @param <R>      return type
	 * @return new Bet object
	 */
	@NotNull
	public static <R> Betty<R> bet(@NotNull final Callable<R> callable) {
		
		return new Betty<R>().care(callable);
	}
	
	public static void main(String... args) {
		
		Runnable runnable1 = () -> {throw new IllegalArgumentException("runnable1");};
		Runnable runnable2 = () -> System.out.println("runnable2");
		String   name      = "Betty";
		
		var betty = Betty.bet(!name.isBlank())// control point
				.care(runnable1) // error part
				.call(runnable2) // direct call
				.onSuccess(() -> System.out.println("success1"))
				.onError((e) -> System.out.println("Error1 :" + e));
		
		System.out.println(betty);
		
	}
}
