package com.tr.hsyn.integer;


import org.jetbrains.annotations.NotNull;


/**
 * Integer
 */
public interface Int {
	
	int getInt();
	
	@NotNull
	static Int of(int val) {
		
		return new Integer(val);
	}
	
}
