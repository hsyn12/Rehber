package com.tr.hsyn.telefonrehberi.main.data;


import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Collectors;


/**
 * Provides a group structure that maps a key with a list (group).
 *
 * @param <K> type of the key
 * @param <V> type of the list of items
 */
public class Groups<K, V> {
	
	private final Map<K, List<V>> groups;
	
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
	public List<List<V>> values() {
		
		return new ArrayList<>(groups.values());
	}
	
	public Set<Map.Entry<K, List<V>>> entrySet() {
		
		return groups.entrySet();
	}
	
	public Set<K> keySet() {
		
		return groups.keySet();
	}
	
	public List<V> remove(@NotNull K key) {
		
		return groups.remove(key);
	}
	
	public void put(@NotNull K key, @NotNull List<V> value) {
		
		groups.put(key, value);
	}
	
	public void forEachEntry(@NotNull BiConsumer<K, List<V>> action) {
		
		groups.forEach(action);
	}
	
	public Map<K, List<V>> getMap() {
		
		return groups;
	}
	
	/**
	 * Groups the list of items.
	 *
	 * @param list      the list to group
	 * @param keyMapper the key mapper to map the list
	 * @param <K>       the key type
	 * @param <V>       the type of the list items
	 * @return a new {@code Groups} object
	 */
	@NotNull
	public static <K, V> Groups<K, V> from(@NotNull List<V> list, @NotNull Function<V, K> keyMapper) {
		
		return new Groups<>(list.stream().collect(Collectors.groupingBy(keyMapper)));
	}
	
	@NotNull
	public static <K, V, T, R> Groups<T, R> from(@NotNull Groups<K, V> groups, @NotNull Function<Groups<K, V>, Groups<T, R>> keyMapper) {
		
		return keyMapper.apply(groups);
	}
}
