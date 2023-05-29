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
	
	public List<Map.Entry<String, List<Call>>> getRankList() {
		
		return rankList;
	}
	
	/**
	 * Rank map that mapped identifier to a list of {@link CallRank}.
	 * The first order is 1, and it is having the highest number of calls.
	 * Maybe there are more than one rank with the same number of calls.
	 *
	 * @return the map object that has a key as identifier and a value as a list of {@link CallRank}.
	 */
	public Map<Integer, List<CallRank>> getRankMap() {
		
		return rankMap;
	}
	
	/**
	 * Starts the rank process.
	 * After this, can be called {@link #getRankMap()} to get the rank map.
	 */
	public void makeRanks() {
		
		rankMap.clear();
		
		if (rankList == null) makeRankList();
		
		int rank = 1;
		int size = rankList.size();
		int last = size - 1;
		
		for (int i = 0; i < size; i++) {
			
			//RankList sıfırdan başlayacak
			var ranked = rankList.get(i);
			var calls  = rankMap.computeIfAbsent(rank, r -> new ArrayList<>());
			
			CallRank callRank = new CallRank(rank, ranked.getKey(), ranked.getValue());
			calls.add(callRank);
			
			if (i == last) break;
			
			var next = rankList.get(i + 1);
			
			if (ranked.getValue().size() > next.getValue().size()) rank++;
		}
	}
	
	/**
	 * Make a list that sorted by calls size descending.
	 * All numbers that belong to the same contact id are put into the same list.
	 */
	private void makeRankList() {
		
		// phone numbers
		List<String> keys = new ArrayList<>(entries.keySet());
		
		// loop on numbers
		for (int i = 0; i < keys.size(); i++) {
			
			// get contact id
			// we need to find the same id in the list and make it one list
			var firstKey = keys.get(i);
			// aggregate calls
			var calls = entries.remove(firstKey);
			
			if (calls == null) continue;
			
			var contactId = CallKey.getContactId(calls.get(0));
			
			if (contactId != 0L) {//- Contact id found
				
				// loop on the other numbers and find the same id 
				for (int j = i + 1; j < keys.size(); j++) {
					
					var secondKey  = keys.get(j);
					var otherCalls = entries.remove(secondKey);
					
					if (otherCalls == null) continue;
					
					var otherContactId = CallKey.getContactId(otherCalls.get(0));
					
					// contactId can not be zero but otherContactId maybe
					if (contactId == otherContactId) {
						
						// that is the same id, put it together
						calls.addAll(otherCalls);
						continue;
					}
					
					// put it back in
					entries.put(secondKey, otherCalls);
				}
			}
			
			// put it back in
			entries.put(firstKey, calls);
		}
		
		rankList = entries.entrySet().stream().sorted((e1, e2) -> e2.getValue().size() - e1.getValue().size()).collect(Collectors.toList());
	}
	
	public int getRankCount(int rank) {
		
		var r = rankMap.get(rank);
		
		if (r != null) return r.size();
		
		return 0;
	}
	
	@NotNull
	@Override
	public String toString() {
		
		StringBuilder sb = new StringBuilder("\n");
		
		for (Map.Entry<String, List<Call>> entry : rankList) {
			
			sb.append(entry.getKey()).append(": ").append(entry.getValue().size()).append("\n");
		}
		
		return sb.toString();
	}
}
