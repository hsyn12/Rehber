package com.tr.hsyn.telefonrehberi.code.mislister;


import com.tr.hsyn.collection.Lister;
import com.tr.hsyn.telefonrehberi.main.code.call.cast.Group;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;


public interface MisterLister {
	
	/**
	 * Bir listeyi, liste elemanlarının belirli bir özelliğine göre gruplar.
	 * Bu metodun {@link Lister#group(List, Function)} metodundan farkı,
	 * grupları ayrı bir nesne içine alması.
	 *
	 * @param list       liste
	 * @param groupMaker gruplama kriteri
	 * @param <X>        verilen listenin eleman türü
	 * @param <Y>        gruplama için uygulanan fonksiyonun döndürdüğü nesne türü
	 * @return Grup listesi
	 * @see Group
	 */
	@NotNull
	static <X, Y> List<Group<X>> group(@NotNull List<X> list, @NotNull Function<X, Y> groupMaker) {
		
		Map<Y, List<X>> groups     = Lister.group(list, groupMaker);
		List<Group<X>>  callGroups = new ArrayList<>();
		
		Lister.loop(groups.keySet(), key -> callGroups.add(new Group<>(groups.get(key))));
		
		return callGroups;
	}
	
	static <X> void makeRanks(@NotNull List<Group<X>> groups, int rankType) {
		
		int rank = 1;
		
		for (int i = 0; i < groups.size(); i++) {
			
			var g = groups.get(i);
			
			g.setRank(rank);
			
			if (i == groups.size() - 1) break;
			
			if (rankType == 0) {
				
				if (g.size() > groups.get(i + 1).size()) rank++;
			}
			else {
				
				if (g.getExtra() > groups.get(i + 1).getExtra()) rank++;
			}
		}
	}
	
	@NotNull
	private static <X, Y> List<Map.Entry<X, List<X>>> _group(@NotNull List<X> list, @NotNull Function<X, Y> groupMaker) {
		
		var                         groups     = Lister.group(list, groupMaker);
		List<Map.Entry<X, List<X>>> callGroups = new ArrayList<>();
		
		Lister.loop(groups.keySet(), key -> {
			
			var g = groups.get(key);
			callGroups.add(Map.entry(g.get(0), g));
		});
		
		callGroups.sort((x, y) -> Integer.compare(y.getValue().size(), x.getValue().size()));
		return callGroups;
	}
}
