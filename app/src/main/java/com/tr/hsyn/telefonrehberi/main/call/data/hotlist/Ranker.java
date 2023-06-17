package com.tr.hsyn.telefonrehberi.main.call.data.hotlist;


import com.tr.hsyn.calldata.Call;
import com.tr.hsyn.contactdata.Contact;
import com.tr.hsyn.phone_numbers.PhoneNumbers;
import com.tr.hsyn.telefonrehberi.main.contact.comment.CallRank;
import com.tr.hsyn.telefonrehberi.main.contact.data.ContactKey;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


public class Ranker {
	
	public static final Comparator<Map.Entry<String, List<Call>>> QUANTITY_COMPARATOR = (e1, e2) -> e2.getValue().size() - e1.getValue().size();
	
	public static Map<Integer, List<CallRank>> createRankMap(Map<String, List<Call>> entries, Comparator<Map.Entry<String, List<Call>>> comparator) {
		
		Map<Integer, List<CallRank>>        rankMap  = new HashMap<>();
		List<Map.Entry<String, List<Call>>> rankList = makeRankList(entries, comparator);
		int                                 rank     = 1;
		int                                 size     = rankList.size();
		int                                 last     = size - 1;
		
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
		
		return rankMap;
	}
	
	private static List<Map.Entry<String, List<Call>>> makeRankList(Map<String, List<Call>> entries, Comparator<Map.Entry<String, List<Call>>> comparator) {
		// sort
		return entries.entrySet().stream().sorted(comparator).collect(Collectors.toList());
	}
	
	/**
	 * Returns the rank of the contact.
	 *
	 * @param contact the contact
	 * @param rankMap the rank map
	 * @return the rank of the contact or –1 if not found
	 */
	public static int getRank(Contact contact, Map<Integer, List<CallRank>> rankMap) {
		
		if (contact != null) {
			
			var numbers = ContactKey.getNumbers(contact);
			int rank    = -1;
			
			if (numbers != null && !numbers.isEmpty()) {
				
				for (Map.Entry<Integer, List<CallRank>> entry : rankMap.entrySet()) {
					
					for (String number : numbers) {
						
						var _number = PhoneNumbers.formatNumber(number, PhoneNumbers.N_MIN);
						
						if (entry.getValue().stream().anyMatch(p -> p.getKey().equals(_number))) {
							
							return entry.getKey();
						}
					}
				}
			}
		}
		
		return -1;
	}
	
	
	public static Map<Integer, List<CallRank>> createRankMap(@NotNull List<Call> calls, Comparator<Map.Entry<String, List<Call>>> comparator, int callType) {
		
		var entries = mapNumberToCalls(calls, callType);
		
		return createRankMap(entries, comparator);
	}
	
	public static Map<String, List<Call>> mapNumberToCalls(@NotNull List<Call> calls) {
		
		return calls.stream().collect(Collectors.groupingBy(Ranker::getKey));
	}
	
	public static Map<String, List<Call>> mapNumberToCalls(@NotNull List<Call> calls, int callType) {
		
		return calls.stream().filter(c -> c.getCallType() == callType).collect(Collectors.groupingBy(Ranker::getKey));
	}
	
	/**
	 * Returns a key for the given call.
	 * Used as an identifier.
	 *
	 * @param call the call
	 * @return the key
	 */
	@NotNull
	private static String getKey(@NotNull Call call) {
		
		return PhoneNumbers.formatNumber(call.getNumber(), PhoneNumbers.N_MIN);
	}
}
