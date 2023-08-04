package com.tr.hsyn.registery;


import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Counterpart of the android content values.
 * Accepts the primitive types only.
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class Values {
	
	public static final String TYPE_STRING = "type_string";
	public static final String TYPE_INT    = "type_int";
	public static final String TYPE_LONG   = "type_long";
	public static final String TYPE_BOOL   = "type_bool";
	
	private final Map<String, List<Entry>> map = new HashMap<>();
	
	public void putString(@NotNull String key, @NotNull String value) {
		
		var entries = map.computeIfAbsent(TYPE_STRING, k -> new ArrayList<>(2));
		entries.add(Entry.ofString(key, value));
	}
	
	public void putLong(@NotNull String key, long value) {
		
		var entries = map.computeIfAbsent(TYPE_LONG, k -> new ArrayList<>(2));
		entries.add(Entry.ofLong(key, value));
	}
	
	public void putBoolean(@NotNull String key, boolean value) {
		
		var entries = map.computeIfAbsent(TYPE_BOOL, k -> new ArrayList<>(2));
		entries.add(Entry.ofBoolean(key, value));
	}
	
	public <V> void putNull(String type, String key) {
		
		var entries = getEntries(type);
		
		if (entries == null) entries = new ArrayList<>(2);
		
		entries.add(Entry.ofNull(key));
		map.put(type, entries);
	}
	
	@Nullable
	public List<Entry> getEntries(@NotNull String valueType) {
		
		return map.get(valueType);
	}
	
}
