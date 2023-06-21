package com.tr.hsyn.scaler;


public interface Generatable<T extends Comparable<T>> {
	
	Generator<T> getGenerator();
}
