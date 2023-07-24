package com.tr.hsyn.telefonrehberi.main.data;


import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * Provides a group structure that maps a key with a list (group).
 * This class extends {@link HashMap} and overrides the {@link #get(Object)} method.
 * This method does not return {@code null} if the key does not exist,
 * instead it returns an empty list.
 *
 * @param <K> type of the key
 * @param <V> type of the list of items
 */
public class Groups<K, V> extends HashMap<K, List<V>> {
	
	/**
	 * Creates a new instance.
	 *
	 * @param groups the group map
	 */
	public Groups(@NotNull Map<K, List<V>> groups) {
		
		super(groups);
	}
	
	/**
	 * Returns the list.
	 *
	 * @param key the key to get the list
	 * @return the list
	 */
	@Override
	public @NotNull List<V> get(final Object key) {
		
		var e = super.get(key);
		return e != null ? e : new ArrayList<>(0);
	}
	
	/**
	 * Returns the entry list that is sorted by the comparator.
	 *
	 * @param comparator the comparator to sort
	 * @return the entry list
	 */
	public List<Map.Entry<K, List<V>>> sortedEntries(@NotNull Comparator<Map.Entry<K, List<V>>> comparator) {
		
		return entrySet().stream().sorted(comparator).collect(Collectors.toList());
	}
}
