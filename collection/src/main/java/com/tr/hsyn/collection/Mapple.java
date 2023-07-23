package com.tr.hsyn.collection;


import com.tr.hsyn.xlog.xlog;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;


/**
 * Provides helper methods for maps.
 */
public class Mapple {
	
	/**
	 * Creates a new {@link Map} from the given list by the given function.
	 *
	 * @param iterable list of elements
	 * @param mapper   function to map each element
	 * @param <K>      Key
	 * @param <V>      Value
	 * @return {@link Map}
	 */
	@NotNull
	public static <K, V> Map<K, V> toMap(@NotNull Iterable<V> iterable, @NotNull Function<V, K> mapper) {
		
		Map<K, V> map = new HashMap<>();
		
		for (V c : iterable)
			map.put(mapper.apply(c), c);
		
		return map;
	}
	
	public static <K, V> Map<K, List<V>> groupsFrom(@NotNull Iterable<V> iterable, @NotNull Function<V, K> mapper) {
		
		return Lister.listOf(iterable).stream().collect(Collectors.groupingBy(mapper));
	}
	
	public static <K, V> void toStr(@NotNull Map<K, V> map) {
		
		java.util.Set<K> keys = map.keySet();
		
		for (K key : keys) {
			
			xlog.d("%s : %s", key, map.get(key));
		}
		
	}
	
	public static <K, V> void toStr(@NotNull Map<K, V> map, Function<K, String> mapper, Function<V, String> valueMapper) {
		
		java.util.Set<K> keys = map.keySet();
		
		for (K key : keys) {
			
			xlog.d("%s : %s", mapper.apply(key), valueMapper.apply(map.get(key)));
		}
	}
	
	public static <K, V> void toStr(@NotNull Map<K, V> map, Function<V, String> valueMapper) {
		
		java.util.Set<K> keys = map.keySet();
		
		for (K key : keys) {
			
			xlog.d("%s : %s", key, valueMapper.apply(map.get(key)));
		}
	}
}
