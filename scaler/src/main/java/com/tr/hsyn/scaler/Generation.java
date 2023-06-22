package com.tr.hsyn.scaler;


import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


/**
 * Defines the generation of objects.
 *
 * @param <T> generatable object
 */
public interface Generation<T extends Comparable<T>> extends Iterator<T>, Iterable<T> {
	
	/**
	 * @return the step for the next generation
	 */
	T getStep();
	
	/**
	 * @return start point of the generation
	 */
	T getStart();
	
	/**
	 * @return end point of the generation
	 */
	T getEnd();
	
	@NotNull
	@Override
	default Iterator<T> iterator() {
		
		return this;
	}
	
	/**
	 * @return {@code true} if the generation has next
	 */
	@Override
	boolean hasNext();
	
	/**
	 * @param i object
	 * @return {@code true} if the given object is contained
	 */
	default boolean contains(T i) {
		
		return getStart().compareTo(i) <= 0 && i.compareTo(getEnd()) <= 0;
	}
	
	/**
	 * Generates the list of objects by the given {@link Generation}.
	 *
	 * @return {@link List} of objects
	 */
	default List<T> generate() {
		
		List<T> list = new ArrayList<>();
		
		for (var i : this) list.add(i);
		
		return list;
	}
}
