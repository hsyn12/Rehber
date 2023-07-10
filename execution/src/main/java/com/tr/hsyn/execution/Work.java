package com.tr.hsyn.execution;


import org.jetbrains.annotations.NotNull;

import java.util.concurrent.Callable;
import java.util.function.BiConsumer;
import java.util.function.Consumer;


/**
 * Defines a work to be executed on a background thread,
 * and take the result on the main thread.
 * The result and the exception are on the main thread.<br><br>
 *
 * <pre>
 * Runnable r = () -> System.out.println("work");
 * Work.on(r)
 *    .onError(e -> System.out.println("Error : " + e))
 *    .onSuccess(n -> System.out.println("Success : " + n))
 *    .execute();
 * </pre><br>
 *
 * @param <T> return type
 */
public interface Work<T> {
	
	/**
	 * Instead of taking the result separately as success and failure,
	 * it allows getting both of them together.
	 * It is invoked on the main thread.
	 * This method disables the other two methods.
	 * This means that if this method is defined,
	 * the {@link #onError(Consumer)} and {@link #onSuccess(Consumer)} methods are not called.
	 *
	 * @param onResult consumer to take the result
	 * @return Work
	 */
	Work<T> onResult(BiConsumer<T, Throwable> onResult);
	
	/**
	 * It takes the work that will run in the foreground in case of the success of the operation.
	 * On the main thread.
	 *
	 * @param onSuccess consumer to take the result
	 * @return Work
	 */
	Work<T> onSuccess(Consumer<T> onSuccess);
	
	/**
	 * Sets the error handler.
	 *
	 * @param onError consumer to take the error
	 * @return Work
	 */
	Work<T> onError(Consumer<Throwable> onError);
	
	/**
	 * Sets the work to run whether it is success or failure after the operation.
	 * On the main thread.
	 *
	 * @param onLast runnable
	 * @return Work
	 */
	Work<T> onLast(@NotNull Runnable onLast);
	
	/**
	 * It takes the work that will run in the foreground in case of the success of the operation.
	 * On the main thread.
	 *
	 * @param onLast runnable
	 * @param delay  delay in milliseconds
	 * @return Work
	 */
	Work<T> onLast(@NotNull Runnable onLast, long delay);
	
	/**
	 * Executes the work.
	 */
	void execute();
	
	/**
	 * Executes the work.
	 *
	 * @param delay delay in milliseconds
	 */
	void execute(long delay);
	
	/**
	 * Sets the priority.
	 *
	 * @param minPriority whether to execute the works on low-priority threads
	 * @return Work
	 */
	Work<T> priority(boolean minPriority);
	
	/**
	 * Sets the delay.
	 *
	 * @param delay delay in milliseconds
	 * @return Work
	 */
	Work<T> delay(long delay);
	
	/**
	 * Creates a new {@code Work} instance with the given {@code callable}.
	 *
	 * @param callable the callable
	 * @param <T>      the return type
	 * @return a new {@code Work} instance
	 */
	@NotNull
	static <T> Work<T> on(@NotNull Callable<T> callable) {
		
		return Worker.on(callable);
	}
	
	/**
	 * Creates a new {@code Work} instance with the given {@code runnable}.
	 *
	 * @param runnable the runnable
	 * @param <T>      the return type
	 * @return a new {@code Work} instance
	 */
	@NotNull
	static <T> Work<T> on(@NotNull Runnable runnable) {
		
		return Worker.on(runnable);
	}
	
	
}
