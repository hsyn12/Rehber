package com.tr.hsyn.registery;


import org.jetbrains.annotations.NotNull;


public interface Entry<V> {
	
	String getKey();
	
	V getValue();
	
	@NotNull
	static Entry<String> ofString(String key, String value) {
		
		return new StringEntry(key, value);
	}
	
	@NotNull
	static Entry<Long> ofLong(String key, long value) {
		
		return new LongEntry(key, value);
	}
	
	@NotNull
	static Entry<Boolean> ofBoolean(String key, boolean value) {
		
		return new BoolEntry(key, value);
	}
	
	@NotNull
	static Entry<String> ofNull(String key) {
		
		return new StringEntry(key, null);
	}
	
}
