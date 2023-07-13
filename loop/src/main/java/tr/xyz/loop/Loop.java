package tr.xyz.loop;


import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Consumer;


/**
 * The Loop.<br>
 * Provides methods to create loops.
 */
public interface Loop {
	
	int cycle();
	
	int getCycle();
	
	Runnable getRunnable();
	
	default void test(@NotNull Condition condition) {
		
		while (condition.getCondition()) {
			
			if (getRunnable() != null) getRunnable().run();
			cycle();
		}
	}
	
	@NotNull
	static Loop whileTrue(Runnable runnable) {
		
		return new LoopImpl();
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
		
		While loop = While.create();
		
		loop.with(i -> {
			
			var item = list.get(i);
			
			if (item.length() < 5) return false;
			
			System.out.println(item);
			
			return true;
		});
		
	}
	
	
}