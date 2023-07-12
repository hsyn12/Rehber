package com.tr.hsyn.counter;


import java.util.function.IntConsumer;


/**
 * The Counter.<br>
 * Provides methods to count.
 */
public class Count {
	
	/**
	 * Counts from zero to 'to' value one by one.<br><br>
	 *
	 * <pre>
	 * Count.until(5, i -> System.out.print(i + " "));
	 * // 0 1 2 3 4
	 * </pre>
	 *
	 * @param to       the end value that excluded
	 * @param consumer the consumer to call on each count
	 */
	public static void until(int to, IntConsumer consumer) {
		
		for (int i = 0; i < to; i++) {
			consumer.accept(i);
		}
	}
	
	public static void main(String[] args) {
		
		Count.until(5, i -> System.out.print(i + " "));
	}
	
}
