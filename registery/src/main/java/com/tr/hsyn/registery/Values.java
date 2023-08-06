package com.tr.hsyn.registery;


import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;


/**
 * Counterpart of the android content values.
 * Accepts the primitive types only.
 */
public class Values {
	
	private final Map<String, Object> values = new HashMap<>();
	
	public Set<Map.Entry<String, Object>> getEntries() {
		
		return values.entrySet();
	}
	
	public void put(@NotNull String key, @Nullable Object value) {
		
		values.put(key, value);
	}
	
	
}
