package com.tr.hsyn.registery;


import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;


public class Value<V> {
	
	private final String         type;
	private final Map<String, V> map = new HashMap<>();
	
	public Value(String type) {
		
		this.type = type;
	}
	
	public String getValueType() {
		
		return type;
	}
	
	public void put(@NotNull String key, @Nullable V value) {
		
		map.put(key, value);
	}
	
	@NotNull
	public Set<String> keySet() {
		
		return map.keySet();
	}
	
	@Nullable
	public V get(@NotNull String key) {
		
		return map.get(key);
	}
	
}
