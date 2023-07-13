package tr.xyz.loop;


import org.jetbrains.annotations.NotNull;


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
 */
public interface Condition {
	
	/**
	 * @return the condition
	 */
	boolean getCondition();
	
	void setCondition(boolean condition);
	
	/**
	 * Creates a condition.
	 *
	 * @return new condition object
	 */
	@NotNull
	static Condition create() {
		
		return new ConditionImpl();
	}
}
