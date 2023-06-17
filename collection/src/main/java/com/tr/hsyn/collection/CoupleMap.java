package com.tr.hsyn.collection;


import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Set;


/**
 * A {@link Map} provides access to the value only with the key.
 * This class makes this access bidirectional.
 * That is, it provides access to the value with the key and provides access to the key with the value.
 *
 * @param <K>
 * @param <V>
 */
public class CoupleMap<K, V> {
	
	/** The map */
	private final Map<K, V> map;
	
	/**
	 * Creates a new couple map.
	 *
	 * @param map the map to create the couple map from
	 */
	public CoupleMap(@NotNull Map<K, V> map) {
		
		this.map = map;
	}
	
	/**
	 * Returns the value for the given key.
	 *
	 * @param key the key
	 * @return the value
	 */
	public V getFromKey(@NotNull K key) {
		
		return map.get(key);
	}
	
	/**
	 * Returns the key for the given value.
	 *
	 * @param value the value
	 * @return the key
	 */
	public K getFromValue(@NotNull V value) {
		
		for (Map.Entry<K, V> entry : map.entrySet()) {
			
			if (entry.getValue().equals(value)) {
				
				return entry.getKey();
			}
		}
		
		return null;
	}
	
	public Set<Map.Entry<K, V>> getEntries() {
		
		return map.entrySet();
		
	}
	
	public Map<K, V> getMap() {
		
		return map;
	}
}
