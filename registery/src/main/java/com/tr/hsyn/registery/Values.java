package com.tr.hsyn.registery;


import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;


@SuppressWarnings({"rawtypes", "unchecked"})
public class Values {
	
	public static final String TYPE_STRING = "type_string";
	public static final String TYPE_INT    = "type_int";
	public static final String TYPE_LONG   = "type_long";
	public static final String TYPE_BOOL   = "type_bool";
	
	private final Map<String, Value> map = new HashMap<>();
	
	public <V> void put(@NotNull String key, @NotNull V value) {
		
		Value<V> _value;
		String   type;
		
		if (value instanceof String) _value = getValue(type = TYPE_STRING);
		else if (value instanceof Integer) _value = getValue(type = TYPE_INT);
		else if (value instanceof Long) _value = getValue(type = TYPE_LONG);
		else if (value instanceof Boolean) _value = getValue(type = TYPE_BOOL);
		else throw new IllegalArgumentException("Unsupported type " + value.getClass());
		
		if (_value != null) _value.put(key, value);
		else {
			
			_value = new Value<>(type);
			_value.put(key, value);
			map.put(type, _value);
		}
	}
	
	public <V> void putNull(String type, String key) {
		
		Value<V> _value = getValue(type);
		
		if (_value != null) _value.put(key, null);
		else {
			
			_value = new Value<>(type);
			_value.put(key, null);
			map.put(type, _value);
		}
		
	}
	
	public Set<String> keySet() {
		
		return map.keySet();
	}
	
	@Nullable
	@SuppressWarnings("unchecked")
	public <V> Value<V> getValue(@NotNull String valueType) {
		
		return (Value<V>) map.get(valueType);
	}
	
}
