package com.tr.hsyn.collection;


import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;


/**
 * A {@link Map} provides access to the value only with the key.
 * This class makes this access bidirectional.
 * That is, it provides access to the value with the key and provides access to the key with the value.
 *
 * @param <K> Key
 * @param <V> Value
 */
public class CoupleMap<K, V> extends HashMap<K, V> {
	
	public CoupleMap() {}
	
	public CoupleMap(Map<? extends K, ? extends V> m) {
		
		super(m);
	}
	
	/**
	 * Returns the key for the given value.
	 *
	 * @param value the value
	 * @return the key
	 */
	@Nullable
	public K getKey(@NotNull V value) {
		
		for (Map.Entry<K, V> entry : this.entrySet()) {
			
			if (entry.getValue().equals(value)) {
				
				return entry.getKey();
			}
		}
		
		return null;
	}
}
