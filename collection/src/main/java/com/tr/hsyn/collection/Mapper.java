package com.tr.hsyn.collection;

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
	 * @param mapper Anahtar üretecek fonksiyon
	 * @param <K> Key
	 * @param <V> Value
	 * @return Map&lt;K,V&gt;
	 */
	@NotNull
	public static <K, V> Map<K, V> toMap(@NotNull Iterable<V> iterable, @NotNull Function<V, K> mapper) {
		
		Map<K, V> map = new HashMap<>();
		
		for (var c : iterable)
			map.put(mapper.apply(c), c);
		
		return map;
	}
}
