package com.tr.hsyn.telefonrehberi.main.data;


import com.tr.hsyn.collection.Lister;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * Provides a group structure that maps a key with a list (group).
 *
 * @param <K> type of the key
 * @param <V> type of the list of items
 */
public class Groups<K, V> {
	
	protected final Map<K, List<V>> groups;
	
	/**
	 * Creates a new instance.
	 *
	 * @param groups the group map
	 */
	public Groups(@NotNull Map<K, List<V>> groups) {
		
		this.groups = groups;
	}
	
	/**
	 * Returns the list.
	 *
	 * @param key the key to get the list
	 * @return the list
	 */
	public @NotNull List<V> get(@NotNull final K key) {
		
		//noinspection DataFlowIssue
		return groups.getOrDefault(key, new ArrayList<>());
	}
	
	public boolean isEmpty() {
		
		return groups.isEmpty();
	}
	
	/**
	 * Returns the group size.
	 *
	 * @return the group size
	 */
	public int size() {
		
		return groups.size();
	}
	
	/**
	 * Returns the all groups.
	 *
	 * @return the all groups
	 */
	public List<List<V>> getValues() {
		
		return new ArrayList<>(groups.values());
	}
	
	public List<K> getKeys() {
		
		return Lister.listOf(groups.keySet());
	}
	
	public List<Map.Entry<K, List<V>>> getEntries() {
		
		return Lister.listOf(groups.entrySet());
	}
	
	public List<V> remove(@NotNull K key) {
		
		return groups.remove(key);
	}
	
	public void put(@NotNull K key, @NotNull List<V> value) {
		
		groups.put(key, value);
	}
	
	public Map<K, List<V>> getMap() {
		
		return groups;
	}
	
	public List<Map.Entry<K, List<V>>> sortedEntries(@NotNull Comparator<Map.Entry<K, List<V>>> comparator) {
		
		return groups.entrySet().stream().sorted(comparator).collect(Collectors.toList());
	}
}
