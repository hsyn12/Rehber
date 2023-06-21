package com.tr.hsyn.telefonrehberi.main.call.data.hotlist;


import com.tr.hsyn.calldata.Call;
import com.tr.hsyn.contactdata.Contact;
import com.tr.hsyn.telefonrehberi.main.contact.data.History;
import com.tr.hsyn.time.duration.DurationGroup;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


public class HistoryRanker {
	
	/**
	 * Creates a map of phone number to a duration group.
	 *
	 * @param entries the map of phone number to calls
	 * @return the map of phone number to a duration group
	 */
	@NotNull
	public static Map<String, DurationGroup> createRankMap(@NotNull Map<String, List<Call>> entries) {
		
		Map<String, DurationGroup> durations = new HashMap<>();
		
		for (Map.Entry<String, List<Call>> entry : entries.entrySet()) {
			
			var calls = entries.get(entry.getKey());
			
			assert calls != null;
			if (calls.size() > 2) {
				
				var history = History.of(entry.getKey(), calls);
				durations.put(entry.getKey(), history.getHistoryDuration());
			}
		}
		
		return durations;
	}
	
	/**
	 * Returns a list of entries
	 * that mapped the phone number to history duration by descending order of duration.
	 *
	 * @param durations the map of phone number to duration
	 * @return the list of entries
	 */
	@NotNull
	public static List<Map.Entry<String, DurationGroup>> createRankList(@NotNull Map<String, DurationGroup> durations) {
		// descending order
		return durations.entrySet().stream()
				.sorted((o1, o2) -> o2.getValue().compareTo(o1.getValue()))
				.collect(Collectors.toList());
	}
	
	/**
	 * Finds the duration of the contact.
	 *
	 * @param durations the map of phone number to duration
	 * @param contact   the contact
	 * @return the duration
	 */
	@Nullable
	public static DurationGroup getDuration(@NotNull List<Map.Entry<Contact, DurationGroup>> durations, Contact contact) {
		
		if (contact == null) return null;
		
		return durations.stream()
				.filter(e -> e.getKey().getContactId() == contact.getContactId())
				.findFirst().map(Map.Entry::getValue)
				.orElse(null);
	}
	
	
	public static int getRank(List<Map.Entry<Contact, DurationGroup>> durationList, Contact contact) {
		
		for (int i = 0; i < durationList.size(); i++)
			if (durationList.get(i).getKey().getContactId() == contact.getContactId())
				return i;
		
		return 0;
		
	}
}
