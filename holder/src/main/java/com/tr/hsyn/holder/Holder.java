package com.tr.hsyn.holder;


import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


/**
 * Object holder.
 *
 * @param <T> the type of the object
 */
public interface Holder<T> {
	
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
}
