package com.tr.hsyn.collection;


import com.tr.hsyn.xlog.xlog;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;


public class Mapper {
	
	/**
	 * Verilen listeyi ({@code iterable}) verilen fonksiyonu
	 * ({@code mapper}) kullanarak anahtar-değer
	 * çiftine çevirir. Fonksiyon anahtar üretir,
	 * anahtarın üretildiği nesne üretilen anahtarın değeri olur.
	 *
	 * @param iterable Values
	 * @param mapper   Anahtar üretecek fonksiyon
	 * @param <K>      Key
	 * @param <V>      Value
	 * @return Map&lt;K,V&gt;
	 */
	@NotNull
	public static <K, V> Map<K, V> toMap(@NotNull Iterable<V> iterable, @NotNull Function<V, K> mapper) {
		
		Map<K, V> map = new HashMap<>();
		
		for (var c : iterable)
			map.put(mapper.apply(c), c);
		
		return map;
	}
	
	public static <K, V> void toStr(@NotNull Map<K, V> map) {
		
		var keys = map.keySet();
		
		for (var key : keys) {
			
			xlog.d("%s : %s", key, map.get(key));
		}
		
	}
	
	public static <K, V> void toStr(@NotNull Map<K, V> map, Function<K, String> mapper, Function<V, String> valueMapper) {
		
		var keys = map.keySet();
		
		for (var key : keys) {
			
			xlog.d("%s : %s", mapper.apply(key), valueMapper.apply(map.get(key)));
		}
	}
	
	public static <K, V> void toStr(@NotNull Map<K, V> map, Function<V, String> valueMapper) {
		
		var keys = map.keySet();
		
		for (var key : keys) {
			
			xlog.d("%s : %s", key, valueMapper.apply(map.get(key)));
		}
	}
}
