package tr.xyz.pair;


import org.jetbrains.annotations.NotNull;


/**
 * Represents a pair of elements.
 *
 * @param <T> type of element
 */
public interface Pair<T, R> {
	
	/**
	 * Gets the first element.
	 *
	 * @return the first element
	 */
	T getFirst();
	
	/**
	 * Gets the second element.
	 *
	 * @return the second element
	 */
	R getSecond();
	
	@NotNull
	static <T, R> Pair<T, R> of(T first, R second) {
		
		return new PairImpl<>(first, second);
	}
}