package tr.xyz.loop;


import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;


/**
 * The Loop.<br>
 * Provides methods to create loops.
 */
public interface Loop {
	
	/**
	 * Creates a loop to work until the condition is {@code true} and calls the runnable on each cycle.
	 *
	 * @param condition the condition to check
	 * @param runnable  the runnable to call on each cycle
	 */
	static <T> void on(@NotNull Condition<T> condition, @NotNull Runnable runnable) {
		
		while (condition.isTrue()) runnable.run();
	}
	
	/**
	 * Creates a loop on the iterable and calls the consumer with the element on each cycle.<br><br>
	 *
	 * <pre>
	 * var list = List.of(1, 2, 3, 4);
	 * Loop.on(list, i -> System.out.print(i + " ")); // 0 1 2 3 4
	 * </pre>
	 *
	 * @param iterable the iterable to loop
	 * @param consumer the consumer to call on each element
	 * @param <T>      the type of the iterable element
	 */
	static <T> void on(@NotNull Iterable<T> iterable, @NotNull Consumer<T> consumer) {
		
		for (T t : iterable) consumer.accept(t);
	}
	
	static void main(String[] args) {
		
		int val = 1;
		
		var condition = Condition.of(val, i -> i < 5, i -> i + 1);
		Runnable runnable = () -> {
			
			System.out.print(condition.getCondition() + " ");
			condition.modify();
		};
		
		Loop.on(condition, runnable);
	}
	
	
}