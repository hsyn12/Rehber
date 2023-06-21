package com.tr.hsyn.scaler;


import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;


public interface Generator<T extends Comparable<T>> {
	
	Generation<T> getGeneration(T start, T end, T step);
	
	default List<T> generate(@NotNull Generation<T> generation) {
		
		List<T> list = new ArrayList<>();
		
		for (T i = generation.getStart(); i.compareTo(generation.getEnd()) <= 0; i = generation.getNext()) {
			
			list.add(i);
		}
		
		return list;
	}
	
}
