package com.tr.hsyn.telefonrehberi.main.dev.atom;


import com.tr.hsyn.collection.Lister;

import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.function.Function;


/**
 * Bir listedeki elemanları çokluğa göre puanlar.
 *
 * @param <T> liste eleman türü
 */
public class Ranker<T> {
	
	private final List<T> list;
	
	public Ranker(@NotNull List<T> items) {
		
		this.list = items;
	}
	
	/**
	 * Liste elemanlarını çokluğa göre gruplar
	 *
	 * @return dönen nesnede <code>Map&lt;Long, T&gt;</code> anahtar çokluk, değer ise o çokluktaki eleman
	 */
	public Map<Long, T> makeRank() {
		
		Map<Long, T> ranks = new HashMap<>();
		
		for (int i = 0; i < list.size(); i++) {
			
			T    e    = list.get(i);
			long rank = Lister.count(list, e);
			
			ranks.putIfAbsent(rank, e);
		}
		
		return ranks;
	}
	
	/**
	 * Liste elemanlarını çokluğa göre gruplar.
	 *
	 * @param mapper sayılacak bilgiyi belirtir.
	 * @param <X>    Sayılacak bilginin türü
	 * @return anahtar çokluk, değer ise o çokluğu sahip elemanlar
	 */
	public <X> Map<Long, List<T>> makeRank(@NotNull Function<T, X> mapper) {
		
		Map<Long, List<T>> ranks = new HashMap<>();
		
		for (int i = 0; i < list.size(); i++) {
			
			var e     = list.get(i);
			var rank  = Lister.count(list, e, mapper);
			var items = ranks.get(rank);
			
			if (items != null) items.add(e);
			else ranks.put(rank, Lister.listOf(e));
		}
		
		return ranks;
	}
	
	/**
	 * @param map liste
	 * @return listenin en fazla çokluğa sahip olduğu nesne.
	 * 		Eğer liste boş ise {@code Map.entry(-1L, (X)new Object())} döner
	 */
	@NotNull
	public static <X> Map.Entry<Long, X> getMostRank(@NotNull Map<Long, X> map) {
		
		var most = Collections.max(map.keySet());
		var keys = map.get(most);
		
		if (keys != null) return Map.entry(most, keys);
		else  //noinspection unchecked
			return Map.entry(-1L, (X) new Object());
	}
	
	/**
	 * @param map liste
	 * @return listenin en az çokluğa sahip olduğu nesne.
	 * 		Eğer liste boş ise {@code Map.entry(-1L, (X)new Object())} döner
	 */
	@NotNull
	public static <X> Map.Entry<Long, X> getMinRank(@NotNull Map<Long, X> map) {
		
		try {
			
			var most = Collections.min(map.keySet());
			
			//noinspection ConstantConditions
			return Map.entry(most, map.get(most));
		}
		catch (NoSuchElementException e) {
			
			//noinspection unchecked
			return Map.entry(-1L, (X) new Object());
		}
	}
	
}
