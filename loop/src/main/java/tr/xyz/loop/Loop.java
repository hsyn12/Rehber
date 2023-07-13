package tr.xyz.loop;


import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;


/**
 * The Loop.<br>
 * Provides methods to create loops.
 */
public interface Loop<T> {
	
	Condition<T> getCondition();
	
	default void loop(@NotNull Consumer<T> consumer) {
		
		while (getCondition().test()) consumer.accept(getCondition().getCondition());
	}
	
	/**
	 * Creates a loop to work while the condition is {@code true} and calls the function on each cycle.
	 *
	 * @param condition the condition to check
	 * @param consumer  the function to call on each cycle
	 */
	static <T> void on(@NotNull Condition<T> condition, @NotNull Consumer<T> consumer) {
		
		new LoopImpl<>(condition).loop(consumer);
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
		
		var list = List.of("march", "june", "april");
		
		var go        = true;
		var condition = Condition.of(go, b -> true);
		
		Function<Boolean, Boolean> predicate = b -> {
			
			var s    = list.get(condition.getCycle());
			var test = s.length() > 4;
			
			if (!test) return false;
			
			System.out.println(s);
			
			return true;
		};
		
		Loop.on(condition, predicate);
	}
	
	
}