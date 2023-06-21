package com.tr.hsyn.telefonrehberi.main.call.data.hotlist;


import com.tr.hsyn.calldata.Call;
import com.tr.hsyn.contactdata.Contact;
import com.tr.hsyn.telefonrehberi.main.call.data.CallCollection;
import com.tr.hsyn.telefonrehberi.main.contact.comment.CallRank;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class DurationRanker {
	
	/**
	 * Returns a map object that ranked by calls duration by descending.
	 *
	 * @param callCollection call collection
	 * @return a map object that ranked by calls duration by descending
	 */
	@NotNull
	public static Map<Integer, List<CallRank>> createRankMap(@NotNull CallCollection callCollection) {
		
		var            entries   = callCollection.getMapIdToCalls();
		Set<String>    keys      = entries.keySet();
		List<CallRank> callRanks = new ArrayList<>();
		
		//_ create call rank objects
		for (String key : keys) {
			
			List<Call> calls = entries.get(key);
			
			if (calls == null) continue;
			
			CallRank callRank = new CallRank(key, calls);
			
			long incomingDuration = 0L;
			long outgoingDuration = 0L;
			
			for (Call call : calls) {
				
				if (call.isIncoming()) incomingDuration += call.getDuration();
				else if (call.isOutgoing()) outgoingDuration += call.getDuration();
			}
			
			callRank.setIncomingDuration(incomingDuration);
			callRank.setOutgoingDuration(outgoingDuration);
			callRank.setContact(callCollection.getContact(key));
			callRanks.add(callRank);
		}
		
		//_ sort by duration descending
		callRanks.sort((c1, c2) -> Long.compare(c2.getDuration(), c1.getDuration()));
		
		int                              rank    = 1;
		int                              size    = callRanks.size();
		int                              last    = size - 1;
		HashMap<Integer, List<CallRank>> rankMap = new HashMap<>();
		
		//_ set ranks
		for (int i = 0; i < size; i++) {
			
			CallRank       callRank = callRanks.get(i);
			List<CallRank> ranks    = rankMap.computeIfAbsent(rank, r -> new ArrayList<>());
			ranks.add(callRank);
			
			if (i == last) break;
			
			CallRank next = callRanks.get(i + 1);
			if (callRank.getDuration() > next.getDuration()) rank++;
		}
		
		return rankMap;
	}
	
	@Nullable
	public static CallRank getCallRank(@NotNull Map<Integer, List<CallRank>> rankMap, int rank, Contact contact) {
		
		if (contact == null) return null;
		
		List<CallRank> ranks = rankMap.get(rank);
		
		if (ranks == null) return null;
		
		for (CallRank callRank : ranks)
			if (callRank.getKey().equals(String.valueOf(contact.getContactId())))
				return callRank;
		
		return null;
	}
	
}