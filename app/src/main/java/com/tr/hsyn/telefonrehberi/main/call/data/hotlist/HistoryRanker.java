package com.tr.hsyn.telefonrehberi.main.call.data.hotlist;


import com.tr.hsyn.calldata.Call;
import com.tr.hsyn.contactdata.Contact;
import com.tr.hsyn.phone_numbers.PhoneNumbers;
import com.tr.hsyn.telefonrehberi.main.call.data.CallCollection;
import com.tr.hsyn.telefonrehberi.main.contact.data.ContactKey;
import com.tr.hsyn.telefonrehberi.main.contact.data.History;
import com.tr.hsyn.time.DurationGroup;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


public class HistoryRanker {
	
	public static Map<String, DurationGroup> createRankMap(@NotNull CallCollection callCollection) {
		
		Map<String, DurationGroup> durations = new HashMap<>();
		Map<String, List<Call>>    map       = callCollection.getMapNumberToCalls();
		
		for (Map.Entry<String, List<Call>> entry : map.entrySet()) {
			
			var calls = callCollection.getCallsByNumber(entry.getKey());
			
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
	public static DurationGroup getDuration(@NotNull Map<String, DurationGroup> durations, Contact contact) {
		
		if (contact == null) return null;
		
		List<String> phoneNumber = ContactKey.getNumbers(contact);
		
		if (phoneNumber == null || phoneNumber.isEmpty()) return null;
		
		DurationGroup duration = null;
		
		for (String number : phoneNumber) {
			
			duration = durations.get(PhoneNumbers.formatNumber(number, PhoneNumbers.N_MIN));
			
			if (duration != null) break;
		}
		
		return duration;
	}
	
}
