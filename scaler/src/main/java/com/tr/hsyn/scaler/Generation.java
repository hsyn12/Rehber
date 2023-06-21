package com.tr.hsyn.scaler;


import org.jetbrains.annotations.NotNull;

import java.util.Iterator;


public interface Generation<T extends Comparable<T>> extends Iterable<T> {
	
	@NotNull
	@Override
	default Iterator<T> iterator() {
		
		return getGenerator().generate(this).iterator();
	}
	
	T getStep();
	
	T getStart();
	
	T getEnd();
	
	boolean hasNext();
	
	T getNext();
	
	default boolean contains(T i) {
		
		return getStart().compareTo(i) <= 0 && i.compareTo(getEnd()) <= 0;
	}
	
	default Generator<T> getGenerator() {
		
		return (start, end, step) -> Generation.this;
	}
	
}
