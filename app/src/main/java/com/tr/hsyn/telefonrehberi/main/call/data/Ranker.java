package com.tr.hsyn.telefonrehberi.main.call.data;


import com.tr.hsyn.calldata.Call;
import com.tr.hsyn.collection.Lister;
import com.tr.hsyn.telefonrehberi.main.contact.comment.CallRank;
import com.tr.hsyn.telefonrehberi.main.data.CallMap;
import com.tr.hsyn.telefonrehberi.main.data.MainContacts;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public interface Ranker {
	
	/**
	 * The comparator used to sort the entries by quantity descending.
	 */
	Comparator<Map.Entry<String, List<Call>>> COMPARATOR_BY_QUANTITY = (e1, e2) -> e2.getValue().size() - e1.getValue().size();
	
	/**
	 * Creates a rank map from incoming calls.
	 *
	 * @return the rank map
	 */
	RankMap incomingRank();
	
	/**
	 * Creates a rank map from outgoing calls.
	 *
	 * @return the rank map
	 */
	RankMap outgoingRank();
	
	/**
	 * Creates a rank map from missed calls.
	 *
	 * @return the rank map
	 */
	RankMap missedRank();
	
	/**
	 * Creates a rank map from rejected calls.
	 *
	 * @return the rank map
	 */
	RankMap rejectedRank();
	
	/**
	 * Creates a rank map from incoming durations.
	 *
	 * @return the rank map
	 */
	RankMap incomingDurationRank();
	
	/**
	 * Creates a rank map from outgoing durations.
	 *
	 * @return the rank map
	 */
	RankMap outgoingDurationRank();
	
	/**
	 * Creates a rank map by quantity.
	 *
	 * @param calls the calls
	 * @return the rank map
	 */
	default RankMap rankByQuantity(@NotNull List<Call> calls) {
		
		return new RankMap(rankMapByQuantity(calls));
	}
	
	/**
	 * Creates a rank map from by duration.
	 *
	 * @param calls the calls
	 * @return the rank map
	 */
	default RankMap rankByDuration(@NotNull List<Call> calls) {
		
		return new RankMap(rankMapByDuration(calls));
	}
	
	/**
	 * Creates a ranked map from the entries.
	 * The ranking is done by the comparator.
	 * The ranking starts from 1.
	 * The most valuable rank is 1 and advances one by one.
	 *
	 * @return the ranked map.
	 * 		The keys are the ranks, and the values are the call rank objects.
	 */
	@NotNull
	private static Map<Integer, List<CallRank>> rankMapByQuantity(@NotNull List<Call> calls) {
		
		Map<Integer, List<CallRank>>        rankMap  = new HashMap<>();
		List<Map.Entry<String, List<Call>>> rankList = new CallMap(calls, CallLog::getKey).sortedEntries(COMPARATOR_BY_QUANTITY);
		int                                 rank     = 1;
		int                                 size     = rankList.size();
		int                                 last     = size - 1;
		
		for (int i = 0; i < size; i++) {
			
			//RankList sıfırdan başlayacak
			Map.Entry<String, List<Call>> ranked    = rankList.get(i);
			List<CallRank>                rankList1 = rankMap.computeIfAbsent(rank, r -> new ArrayList<>());
			CallRank                      callRank  = new CallRank(rank, ranked.getKey(), ranked.getValue());
			rankList1.add(callRank);
			
			callRank.setContact(MainContacts.getById(ranked.getKey()));
			
			if (i == last) break;
			
			Map.Entry<String, List<Call>> next = rankList.get(i + 1);
			
			if (ranked.getValue().size() > next.getValue().size()) rank++;
		}
		
		return rankMap;
	}
	
	/**
	 * Returns a map object that ranked by call duration by descending.
	 *
	 * @return a map object that ranked by calls duration by descending
	 */
	@NotNull
	static Map<Integer, List<CallRank>> rankMapByDuration(@NotNull List<Call> calls) {
		
		CallMap        callMap   = new CallMap(calls, CallLog::getKey);
		List<String>   keys      = Lister.listOf(callMap.keySet());
		List<CallRank> callRanks = new ArrayList<>();
		
		//_ create call rank objects
		for (String key : keys) {
			
			List<Call> _calls = callMap.get(key);
			
			if (_calls.isEmpty()) continue;
			
			CallRank callRank = new CallRank(key, _calls);
			
			long incomingDuration = 0L;
			long outgoingDuration = 0L;
			
			for (Call call : _calls) {
				
				if (call.isIncoming()) incomingDuration += call.getDuration();
				else if (call.isOutgoing()) outgoingDuration += call.getDuration();
			}
			
			callRank.setIncomingDuration(incomingDuration);
			callRank.setOutgoingDuration(outgoingDuration);
			callRank.setContact(MainContacts.getById(key));
			callRanks.add(callRank);
		}
		
		//_ sort by duration descending
		callRanks.sort((c1, c2) -> Long.compare(c2.getDuration(), c1.getDuration()));
		
		int                          rank    = 1;
		int                          size    = callRanks.size();
		int                          last    = size - 1;
		Map<Integer, List<CallRank>> rankMap = new HashMap<>();
		
		//_ set ranks
		for (int i = 0; i < size; i++) {
			
			CallRank callRank = callRanks.get(i);
			callRank.setRank(rank);
			List<CallRank> ranks = rankMap.computeIfAbsent(rank, r -> new ArrayList<>());
			ranks.add(callRank);
			
			if (i == last) break;
			
			CallRank next = callRanks.get(i + 1);
			if (callRank.getDuration() > next.getDuration()) rank++;
		}
		
		return rankMap;
	}
}
