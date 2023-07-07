package com.tr.hsyn.integer;


import org.jetbrains.annotations.NotNull;


/**
 * Holds an integer value like {@link java.lang.Integer}.
 */
public interface Int {
	
	/**
	 * Gets the value.
	 *
	 * @return integer
	 */
	int getInt();
	
	/**
	 * Tests if the value is zero.
	 *
	 * @return {@code true} if the value is zero
	 */
	default boolean isZero() {
		
		return getInt() == 0;
	}
	
	/**
	 * Tests if the value is positive.
	 *
	 * @return {@code true} if the value is positive
	 */
	default boolean isPositive() {
		
		return getInt() > 0;
	}
	
	/**
	 * Tests if the value is negative.
	 *
	 * @return {@code true} if the value is negative
	 */
	default boolean isNegative() {
		
		return getInt() < 0;
	}
	
	/**
	 * Tests if the value equals the other.
	 *
	 * @param other the other
	 * @return {@code true} if the value equals the other.
	 */
	default boolean equals(Int other) {
		
		if (other == null) return false;
		
		return getInt() == other.getInt();
	}
	
	/**
	 * Tests if the value is greater than the other.
	 *
	 * @param other the other
	 * @return {@code true} if the value is greater than the other.
	 */
	default boolean greaterThan(@NotNull Int other) {
		
		return getInt() > other.getInt();
	}
	
	/**
	 * Tests if the value is less than the other.
	 *
	 * @param other the other
	 * @return {@code true} if the value is less than the other
	 */
	default boolean lessThan(@NotNull Int other) {
		
		return getInt() < other.getInt();
	}
	
	/**
	 * Create a new {@link Int} instance.
	 *
	 * @param val the value
	 * @return {@link Int} instance
	 */
	@NotNull
	static Int of(int val) {
		
		return new Integer(val);
	}
	
}
