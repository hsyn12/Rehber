package tr.xyz.loop;


import org.jetbrains.annotations.NotNull;

import java.util.function.Predicate;
import java.util.function.UnaryOperator;


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
	boolean isTrue();
	
	/**
	 * Executes the modifier on the condition.
	 */
	void modify();
	
	/**
	 * Executes the modifier on the condition.
	 *
	 * @param modifier the modifier
	 */
	void modify(@NotNull UnaryOperator<T> modifier);
	
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
	Condition<T> predicate(@NotNull Predicate<T> predicate);
	
	/**
	 * Sets the modifier of the condition to change the condition.
	 *
	 * @param modifier the modifier
	 * @return this condition object
	 */
	@NotNull
	Condition<T> modifier(@NotNull UnaryOperator<T> modifier);
	
	/**
	 * @return the condition
	 */
	T getCondition();
	
	/**
	 * Creates a condition.
	 *
	 * @param condition the condition object to test the truth
	 * @param predicate the predicate of the condition to test
	 * @param modifier  the modifier of the condition to change
	 * @param <T>       the type of the condition
	 * @return new condition object
	 */
	@NotNull
	static <T> Condition<T> of(T condition, Predicate<T> predicate, UnaryOperator<T> modifier) {
		
		return new ConditionImpl<T>(condition, predicate, modifier);
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
