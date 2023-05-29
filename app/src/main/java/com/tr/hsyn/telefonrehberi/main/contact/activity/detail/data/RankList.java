package com.tr.hsyn.telefonrehberi.main.contact.activity.detail.data;


import com.tr.hsyn.calldata.Call;
import com.tr.hsyn.telefonrehberi.main.call.data.CallKey;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


public class RankList {
	
	/**
	 * The map object that has a key as identifier and a value as a list of its calls.
	 */
	private final Map<String, List<Call>>             entries;
	/**
	 * The object that has key as a rank and value as a list of {@link CallRank}.
	 * The first rank is 1,
	 * and it is having the highest number of calls.
	 * But maybe there are more than one rank with the same number of calls.
	 */
	private final Map<Integer, List<CallRank>>        rankMap = new HashMap<>();
	/**
	 * The list of entry that identifier and its calls with sorted by calls size descending
	 */
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
			
			CallRank callRank = new CallRank(rank, ranked.getKey(), ranked.getValue());
			
			long contactId = CallKey.getContactId(ranked.getValue().get(0));
			
			if (contactId != 0L) {
				
				int index = calls.indexOf(callRank);
				
				if (index != -1) {
					
					calls.get(index).getCalls().addAll(ranked.getValue());
				}
				else {
					
					calls.add(callRank);
				}
			}
			else calls.add(callRank);
			
			
			if (i == last) break;
			
			var next = rankList.get(i + 1);
			
			if (ranked.getValue().size() > next.getValue().size()) rank++;
		}
	}
	
	private void makeRankList() {
		
		List<String> keys = new ArrayList<>(entries.keySet());
		
		for (int i = 0; i < keys.size(); i++) {
			
			var id    = keys.get(i);
			var calls = entries.get(id);
			
			assert calls != null;
			var contactId = CallKey.getContactId(calls.get(0));
			
			if (contactId != 0L) {
				
				
			}
			
		}
		
		
		rankList = entries.entrySet().stream().sorted((e1, e2) -> e2.getValue().size() - e1.getValue().size()).collect(Collectors.toList());
	}
	
}
