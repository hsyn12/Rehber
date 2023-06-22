package com.tr.hsyn.scaler;


import org.jetbrains.annotations.NotNull;


/**
 * Defines a generatable object.
 *
 * @param <T> generatable object
 */
public interface Generatable<T extends Comparable<T>> {
	
	/**
	 * Returns the generation.
	 *
	 * @param start start point of the generation
	 * @param end   end point of the generation
	 * @param step  step of the generation
	 * @return {@link Generation}
	 */
	@NotNull
	Generation<T> getGeneration(@NotNull T start, @NotNull T end, @NotNull T step);
	
	
}
