package tr.xyz.loop;


import org.jetbrains.annotations.NotNull;

import java.util.function.Function;


/**
 * Defines the condition contract.<br>
 * A condition is a thing that has a boolean value.
 * So, <strong>all of the {@link Boolean} variables are condition</strong>,
 * you should think about that.<br>
 * But standalone {@link Boolean} variables are not changeable.
 * And yes, you can change the boolean variable true to false or vice versa.
 * But mostly the situations are more complicated than this.<br><br>
 *
 * <pre>
 * int val = 1;
 *
 * var condition = Condition.of(val, i -> i < 5, i -> i + 1);
 * Runnable runnable = () -> {
 *     System.out.print(condition.getCondition() + " ");
 *     condition.modify();
 * };
 *
 * Loop.on(condition, runnable);
 * // 1 2 3 4
 * </pre>
 * <br>
 * <p>
 * You cannot change either the condition value or the integer value without the condition object.
 * You can try.
 *
 * @param <T> the type of the condition
 */
public interface Condition<T> {
	
	/**
	 * @return {@code true} if the condition is {@code true}
	 */
	boolean test();
	
	/**
	 * Sets the condition to test the truth.
	 *
	 * @param condition the condition
	 * @return this condition object
	 */
	@NotNull
	Condition<T> condition(T condition);
	
	/**
	 * Sets the predicate of the condition to test.
	 *
	 * @param predicate the predicate to test the condition
	 * @return this condition object
	 */
	@NotNull
	Condition<T> predicate(@NotNull Function<T, Boolean> predicate);
	
	/**
	 * @return the condition
	 */
	T getCondition();
	
	void setCondition(T condition);
	
	/**
	 * Creates a condition.
	 *
	 * @param condition the condition object to test the truth
	 * @param predicate the predicate of the condition to test
	 * @param <T>       the type of the condition
	 * @return new condition object
	 */
	@NotNull
	static <T> Condition<T> of(T condition, Function<T, Boolean> predicate) {
		
		return new ConditionImpl<T>(condition, predicate);
	}
	
	/**
	 * Creates a condition.
	 *
	 * @param <T> the type of the condition
	 * @return new condition object
	 */
	@NotNull
	static <T> Condition<T> create() {
		
		return new ConditionImpl<>();
	}
}
