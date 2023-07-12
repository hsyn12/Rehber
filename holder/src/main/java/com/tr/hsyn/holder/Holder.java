package com.tr.hsyn.holder;


import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


/**
 * Object holder.<br>
 * Provides to hold a value of any type T.
 * Maybe the best way to use it is to use in an interface.
 * Unchangeable interface variables become changeable.
 * Or whatever else.
 *
 * @param <T> the type of the object
 */
public interface Holder<T> {
	
	/**
	 * Gets the value.
	 *
	 * @return the value
	 */
	@Nullable T getValue();
	
	/**
	 * Sets the value.
	 *
	 * @param t the value
	 */
	void setValue(@Nullable T t);
	
	/**
	 * @return {@code true} if the object is null
	 */
	default boolean isNull() {
		
		return getValue() == null;
	}
	
	/**
	 * Creates a new {@link Holder} object.
	 *
	 * @param <T> the type of the held object
	 * @return a new {@link Holder} object
	 */
	@NotNull
	static <T> Holder<T> newHolder() {
		
		return new Holo<>();
	}
	
	/**
	 * Creates a new {@link Holder} object.
	 *
	 * @param obj the object
	 * @param <T> the type of the held object
	 * @return a new {@link Holder} object
	 */
	@NotNull
	static <T> Holder<T> newHolder(T obj) {
		
		return new Holo<>(obj);
	}
}
