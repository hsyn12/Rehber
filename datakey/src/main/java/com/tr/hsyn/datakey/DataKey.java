package com.tr.hsyn.datakey;


import com.tr.hsyn.integer.Int;

import org.jetbrains.annotations.NotNull;


/**
 * Veri anahtarı
 */
public interface DataKey extends DataAccessStatus, Int {
	
	String getName();
	
	@NotNull
	static DataKey of(int key, @NotNull String name) {
		
		return new IntKey(key, name, true, true);
	}
	
	@NotNull
	static DataKey of(int key, @NotNull String name, boolean readable) {
		
		return new IntKey(key, name, readable, true);
	}
	
	@NotNull
	static DataKey of(int key, @NotNull String name, boolean readable, boolean writable) {
		
		return new IntKey(key, name, readable, writable);
	}
	
}
