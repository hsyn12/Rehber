package com.tr.hsyn.telefonrehberi.main.contact.comment;


import com.tr.hsyn.calldata.Call;
import com.tr.hsyn.telefonrehberi.main.call.data.CallKey;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;


public class RankList {
	
	/**
	 * The map object that has a key as identifier and a value as a list of its calls.
	 */
	private final Map<String, List<Call>>             entries;
	/**
	 * The object that has key as a rank and value as a list of {@link CallRank}.
	 * The Ranking starts from 1,
	 * and it has the highest number of calls.
	 * Maybe there are more than one rank with the same number of calls.
	 */
	private final Map<Integer, List<CallRank>>        rankMap = new HashMap<>();
	/**
	 * The list of entry that identifier and its calls.
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
	 * The first order is 1, and it has the highest number of calls.
	 * Maybe there are more than one rank with the same number of calls.
	 *
	 * @return the map object that has a key as identifier and a value as a list of {@link CallRank}.
	 */
	public Map<Integer, List<CallRank>> getRankMap() {
		
		return rankMap;
	}
	
	
	/**
	 * Starts the rank process.
	 * After this, call {@link #getRankMap()} to get the rank map.
	 */
	public RankList makeQuantityRanks() {
		
		rankMap.clear();
		mergeSameCalls(entries);
		
		if (rankList == null) makeRankListByQuantity();
		
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
		
		return this;
	}
	
	/**
	 * Makes the rank list by call duration and sets the rank map object.
	 *
	 * @return this {@link RankList} object
	 */
	public RankList makeDurationRanks() {
		
		rankMap.clear();
		mergeSameCalls(entries);
		
		Set<String>    keys      = entries.keySet();
		List<CallRank> callRanks = new ArrayList<>();
		
		//_ create call rank objects
		for (var key : keys) {
			
			var calls = entries.get(key);
			
			if (calls == null) continue;
			
			var callRank = new CallRank(key, calls);
			
			long incomingDuration = 0L;
			long outgoingDuration = 0L;
			
			for (var call : calls) {
				
				if (call.isIncoming()) incomingDuration += call.getDuration();
				else if (call.isOutgoing()) outgoingDuration += call.getDuration();
			}
			
			callRank.setIncomingDuration(incomingDuration);
			callRank.setOutgoingDuration(outgoingDuration);
			
			callRanks.add(callRank);
		}
		
		//_ sort by duration descending
		callRanks.sort((c1, c2) -> Long.compare(c2.getDuration(), c1.getDuration()));
		
		int rank = 1;
		int size = callRanks.size();
		int last = size - 1;
		//_ set ranks
		for (int i = 0; i < size; i++) {
			
			var callRank = callRanks.get(i);
			var ranks    = rankMap.computeIfAbsent(rank, r -> new ArrayList<>());
			ranks.add(callRank);
			
			if (i == last) break;
			
			var next = callRanks.get(i + 1);
			if (callRank.getDuration() > next.getDuration()) rank++;
		}
		
		return this;
	}
	
	/**
	 * Make a list that sorted by calls size descending.
	 * Puts all numbers that belong to the same contact ID into the same list.
	 */
	private void makeRankListByQuantity() {
		
		// sort
		rankList = entries.entrySet().stream().sorted((e1, e2) -> e2.getValue().size() - e1.getValue().size()).collect(Collectors.toList());
	}
	
	@NotNull
	@Override
	public String toString() {
		
		StringBuilder sb = new StringBuilder("\n");
		
		for (Map.Entry<String, List<Call>> entry : rankList) {
			
			sb.append(entry.getKey()).append(" : ").append(entry.getValue().size()).append("\n");
		}
		
		return sb.toString();
	}
	
	/**
	 * Maybe there are more than one phone number belonging to the same contact.
	 * This method merges the calls belonging to the same contact.
	 *
	 * @param entries the map object that mapped the phone number to its calls.
	 */
	public static void mergeSameCalls(@NotNull Map<String, List<Call>> entries) {
		
		// phone numbers
		List<String> keys = new ArrayList<>(entries.keySet());
		
		// loop on numbers
		for (int i = 0; i < keys.size(); i++) {
			
			// Get contact ID.
			// Needs to find the same ID in the list and make it one list.
			var firstKey = keys.get(i);
			// aggregated calls
			var calls = entries.remove(firstKey);
			
			if (calls == null) continue;
			
			var contactId = CallKey.getContactId(calls.get(0));
			
			if (contactId != 0L) {//+ Contact ID found
				
				// loop on the other numbers and find the same ID 
				for (int j = i + 1; j < keys.size(); j++) {
					
					var secondKey  = keys.get(j);
					var otherCalls = entries.remove(secondKey);
					
					if (otherCalls == null) continue;
					
					var otherContactId = CallKey.getContactId(otherCalls.get(0));
					
					// contactId cannot be zero but otherContactId maybe
					if (contactId == otherContactId) {
						
						// that is the same ID, put it together
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
	}
}
