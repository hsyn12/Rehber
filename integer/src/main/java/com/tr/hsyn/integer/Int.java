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
	
	default boolean isZero() {
		
		return getInt() == 0;
	}
	
	default boolean isPositive() {
		
		return getInt() > 0;
	}
	
	default boolean isNegative() {
		
		return getInt() < 0;
	}
	
	default boolean equals(Int other) {
		
		if (other == null) return false;
		
		return getInt() == other.getInt();
	}
	
	default boolean greaterThan(@NotNull Int other) {
		
		return getInt() > other.getInt();
	}
	
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
