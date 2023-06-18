package com.tr.hsyn.telefonrehberi.main.call.data.hotlist;


import com.tr.hsyn.calldata.Call;
import com.tr.hsyn.contactdata.Contact;
import com.tr.hsyn.phone_numbers.PhoneNumbers;
import com.tr.hsyn.telefonrehberi.main.contact.comment.CallRank;
import com.tr.hsyn.telefonrehberi.main.contact.data.ContactKey;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


public class Ranker {
	
	/**
	 * The comparator used to sort the entries by quantity descending.
	 */
	public static final Comparator<Map.Entry<String, List<Call>>> QUANTITY_COMPARATOR = (e1, e2) -> e2.getValue().size() - e1.getValue().size();
	
	/**
	 * Creates a ranked map from the entries.
	 * The ranking is done by the comparator.
	 *
	 * @param entries    entries
	 * @param comparator the comparator
	 * @return the ranked map
	 */
	@NotNull
	public static Map<Integer, List<CallRank>> createRankMap(@NotNull Map<String, List<Call>> entries, @NotNull Comparator<Map.Entry<String, List<Call>>> comparator) {
		
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
	
	/**
	 * Creates a ranked list from the entries.
	 * The ranking is done by the comparator.
	 *
	 * @param entries    the entries
	 * @param comparator the comparator
	 * @return the ranked list that ordered by comparator
	 */
	@NotNull
	private static List<Map.Entry<String, List<Call>>> makeRankList(@NotNull Map<String, List<Call>> entries, @NotNull Comparator<Map.Entry<String, List<Call>>> comparator) {
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
	public static int getRank(Contact contact, @NotNull Map<Integer, List<CallRank>> rankMap) {
		
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
	
	/**
	 * Creates a ranked map from the entries.
	 *
	 * @param entries    the entries
	 * @param comparator the comparator
	 * @param callType   the call type to select
	 * @return the ranked map
	 */
	@NotNull
	public static Map<Integer, List<CallRank>> createRankMap(@NotNull Map<String, List<Call>> entries, Comparator<Map.Entry<String, List<Call>>> comparator, int callType) {
		
		Map<String, List<Call>> filtered = new HashMap<>();
		
		for (var entry : entries.entrySet()) {
			
			filtered.put(entry.getKey(), entry.getValue().stream().filter(c -> c.getCallType() == callType).collect(Collectors.toList()));
		}
		
		return createRankMap(filtered, comparator);
	}
	
	@Nullable
	public static List<Call> getCalls(@NotNull Contact contact, List<CallRank> ranks) {
		
		if (ranks == null) return null;
		
		var numbers = ContactKey.getNumbers(contact);
		
		if (numbers != null && !numbers.isEmpty()) {
			
			for (var rank : ranks) {
				
				for (String number : numbers) {
					
					var _number = PhoneNumbers.formatNumber(number, PhoneNumbers.N_MIN);
					
					if (rank.getKey().equals(_number)) {
						
						return rank.getCalls();
					}
				}
			}
		}
		
		return null;
	}
	
}
