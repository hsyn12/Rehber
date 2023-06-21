package com.tr.hsyn.scaler;


public interface Generation<T> {
	
	T getStep();
	
	T getStart();
	
	T getEnd();
	
	T getNext();
	
	
}
