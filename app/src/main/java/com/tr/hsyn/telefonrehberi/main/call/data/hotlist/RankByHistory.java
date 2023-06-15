package com.tr.hsyn.telefonrehberi.main.call.data.hotlist;


import com.tr.hsyn.calldata.Call;
import com.tr.hsyn.telefonrehberi.main.call.data.CallCollection;
import com.tr.hsyn.telefonrehberi.main.contact.comment.RankList;
import com.tr.hsyn.telefonrehberi.main.contact.data.History;
import com.tr.hsyn.time.DurationGroup;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


public class RankByHistory {
	
	/**
	 * Returns a list of entries
	 * that mapped the phone number to history duration by descending order of duration.
	 *
	 * @param callCollection the call collection
	 * @return the list of entries
	 */
	@NotNull
	public static List<Map.Entry<String, DurationGroup>> createRankMap(@NotNull CallCollection callCollection) {
		
		Map<String, DurationGroup> durations = new HashMap<>();
		Map<String, List<Call>>    map       = callCollection.getMapNumberToCalls();
		RankList.mergeSameCalls(map);
		
		for (Map.Entry<String, List<Call>> entry : map.entrySet()) {
			
			var calls = callCollection.getCallsByNumber(entry.getKey());
			
			if (calls.size() > 2) {
				
				var history = History.of(entry.getKey(), calls);
				durations.put(entry.getKey(), history.getHistoryDuration());
			}
		}
		
		// descending order
		return durations.entrySet().stream()
				.sorted((o1, o2) -> o2.getValue().compareTo(o1.getValue()))
				.collect(Collectors.toList());
	}
	
}
