package com.tr.hsyn.telefonrehberi.main.activity.contact.detail.data;


import com.tr.hsyn.calldata.Call;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


public class RankList {
	
	private final Map<String, List<Call>>             entries;
	private final Map<Integer, List<CallRank>>        rankMap = new HashMap<>();
	private       List<Map.Entry<String, List<Call>>> rankList;
	
	public RankList(@NotNull Map<String, List<Call>> entries) {
		
		this.entries = entries;
	}
	
	public Map<Integer, List<CallRank>> getRankMap() {
		
		return rankMap;
	}
	
	public void makeRanks() {
		
		rankMap.clear();
		
		int rank = 1;
		int size = entries.size();
		int last = size - 1;
		
		if (rankList == null) makeRankList();
		
		for (int i = 0; i < size; i++) {
			
			//RankList sıfırdan başlayacak
			var ranked = rankList.get(i);
			var calls  = rankMap.computeIfAbsent(rank, r -> new ArrayList<>());
			
			calls.add(new CallRank(rank, ranked.getKey(), ranked.getValue()));
			
			if (i == last) break;
			
			var next = rankList.get(i + 1);
			
			if (ranked.getValue().size() > next.getValue().size()) rank++;
		}
	}
	
	private void makeRankList() {
		
		rankList = entries.entrySet().stream().sorted((e1, e2) -> e2.getValue().size() - e1.getValue().size()).collect(Collectors.toList());
	}
	
}
