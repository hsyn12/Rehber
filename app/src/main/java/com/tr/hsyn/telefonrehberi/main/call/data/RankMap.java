package com.tr.hsyn.telefonrehberi.main.call.data;


import com.tr.hsyn.calldata.Call;
import com.tr.hsyn.collection.Lister;
import com.tr.hsyn.contactdata.Contact;
import com.tr.hsyn.phone_numbers.PhoneNumbers;
import com.tr.hsyn.telefonrehberi.main.contact.comment.CallRank;
import com.tr.hsyn.time.duration.DurationGroup;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;


/**
 * <h3>RankMap</h3>
 * <p>
 * The rank is a number that is used to determine the value of the calls by some criteria.
 * And it starts from 1, advanced by 1.
 * The most valuable rank is 1.
 * And the {@code RankMap} maps the ranks to the list of {@link CallRank}s.
 * Because, the different call lists can have the same rank.
 * This is related to the used criteria.
 * So, the {@link #getRank(int)} method when called returns the list of {@link CallRank}.
 */
public class RankMap {
	
	public static final Comparator<Map.Entry<String, List<Call>>> QUANTITY_COMPARATOR = (e1, e2) -> e2.getValue().size() - e1.getValue().size();
	
	/**
	 * The map object that maps the ranks to the list of {@link CallRank}s
	 */
	private final Map<Integer, List<CallRank>> rankMap;
	/**
	 * The list of {@link Call} that created this {@link RankMap} object
	 */
	private final List<Call>                   calls;
	/**
	 * The map object that maps the keys to the list of {@link Call}s
	 */
	private final Map<String, List<Call>>      callsMap;
	
	/**
	 * Creates an instance of {@link RankMap}.
	 *
	 * @param calls the list of {@link Call} to make rank of.
	 */
	public RankMap(@NotNull List<Call> calls) {
		
		this.calls = calls;
		callsMap   = groupByKey(calls);
		rankMap    = createRankMap(callsMap);
	}
	
	public List<Call> getCalls() {
		
		return calls;
	}
	
	/**
	 * @return the map object that maps the keys to the list of {@link Call}s
	 */
	public Map<String, List<Call>> getCallMap() {
		
		return callsMap;
	}
	
	/**
	 * Returns the rank map.
	 *
	 * @return the map object that maps the ranks to the list of {@link CallRank}s
	 */
	public Map<Integer, List<CallRank>> getRankMap() {
		
		return rankMap;
	}
	
	/**
	 * Returns the list of {@link CallRank}  by the rank.
	 *
	 * @param rank the rank
	 * @return the list of {@link CallRank} or {@code null} if the rank does not exist.
	 */
	@Nullable
	public List<CallRank> getRank(int rank) {
		
		return rankMap.get(rank);
	}
	
	/**
	 * Returns the count of the rank.
	 *
	 * @return the count of the rank
	 */
	public int size() {
		
		return rankMap.size();
	}
	
	/**
	 * @return {@code true} if the call list that created this {@link RankMap} object is empty.
	 */
	public boolean isEmpty() {
		
		return calls.isEmpty();
	}
	
	/**
	 * Returns the size of the calls that creating this {@link RankMap} object by.
	 *
	 * @return the size of the calls
	 */
	public int callSize() {
		
		return calls.size();
	}
	
	/**
	 * Returns the list of {@link CallRank}s that had.
	 *
	 * @return the list of {@link CallRank}s
	 */
	public List<CallRank> getCallRanks() {
		
		return rankMap.values().stream()
				.flatMap(Collection::stream)
				.sorted(Comparator.comparingInt(CallRank::getRank))
				.collect(Collectors.toList());
	}
	
	/**
	 * Returns the rank of the contact.
	 *
	 * @param contact the contact
	 * @return the rank of the contact or –1 if not found
	 */
	public int getRank(@NotNull Contact contact) {
		
		for (Map.Entry<Integer, List<CallRank>> entry : rankMap.entrySet()) {
			
			List<CallRank> callRanks = entry.getValue();
			
			for (CallRank callRank : callRanks) {
				
				if (callRank.getKey().equals(String.valueOf(contact.getContactId()))) {
					
					return entry.getKey();
				}
			}
		}
		
		return 0;
	}
	
	/**
	 * Return the {@link CallRank} of the contact.
	 *
	 * @param rank    the rank of the contact
	 * @param contact the contact to select
	 * @return the {@link CallRank} or {@code null} if not found
	 */
	@Nullable
	public CallRank getCallRank(int rank, Contact contact) {
		
		if (contact == null || rank < 1) return null;
		
		List<CallRank> ranks = getRank(rank);
		
		if (ranks == null) return null;
		
		for (CallRank callRank : ranks)
			if (callRank.getKey().equals(String.valueOf(contact.getContactId())))
				return callRank;
		
		return null;
	}
	
	/**
	 * Returns the rank of the contact.
	 *
	 * @param contact the contact
	 * @return the rank of the contact or –1 if not found
	 */
	public static int getRank(@NotNull Contact contact) {
		
		for (Map.Entry<Integer, List<CallRank>> entry : rankMap.entrySet()) {
			
			List<CallRank> callRanks = entry.getValue();
			
			for (CallRank callRank : callRanks) {
				
				if (callRank.getKey().equals(String.valueOf(contact.getContactId()))) {
					
					return entry.getKey();
				}
			}
		}
		
		return 0;
	}
	
	/**
	 * Groups the calls by the key function.
	 *
	 * @param calls       the calls to group
	 * @param callTypes   the call types. If not specified, all calls are grouped.
	 * @param keyFunction the key function to extract the key
	 * @return the group of calls by key
	 */
	private static Map<String, List<Call>> groupByKey(@NotNull List<Call> calls, @Nullable Function<Call, String> keyFunction, int @NotNull ... callTypes) {
		
		if (callTypes.length == 0)
			return calls.stream().collect(Collectors.groupingBy(keyFunction != null ? keyFunction : RankMap::getKey));
		
		return calls.stream().filter(c -> Lister.contains(callTypes, c.getCallType())).collect(Collectors.groupingBy(keyFunction != null ? keyFunction : RankMap::getKey));
	}
	
	/**
	 * Creates a ranked map from the calls.
	 *
	 * @param calls    the calls
	 * @param callType the call type to select
	 * @return the rank map
	 */
	@NotNull
	public static RankMap of(@NotNull List<Call> calls, int callType) {
		
		return of(calls.stream().filter(c -> c.isType(callType)).collect(Collectors.toList()));
	}
	
	/**
	 * Creates a ranked map from the entries.
	 * The ranking is done by the comparator.
	 * The ranking starts from 1.
	 * The most valuable rank is 1 and advances one by one.
	 *
	 * @param entries entries that mapped an ID to its calls
	 * @return the ranked map.
	 * 		The keys are the ranks, and the values are the call rank objects.
	 */
	@NotNull
	private static Map<Integer, List<CallRank>> createRankMap(@NotNull Map<String, List<Call>> entries) {
		
		Map<Integer, List<CallRank>>        rankMap  = new HashMap<>();
		List<Map.Entry<String, List<Call>>> rankList = sortedList(entries, RankMap.QUANTITY_COMPARATOR);
		int                                 rank     = 1;
		int                                 size     = rankList.size();
		int                                 last     = size - 1;
		
		for (int i = 0; i < size; i++) {
			
			//RankList sıfırdan başlayacak
			Map.Entry<String, List<Call>> ranked   = rankList.get(i);
			List<CallRank>                calls    = rankMap.computeIfAbsent(rank, r -> new ArrayList<>());
			CallRank                      callRank = new CallRank(rank, ranked.getKey(), ranked.getValue());
			calls.add(callRank);
			
			if (i == last) break;
			
			Map.Entry<String, List<Call>> next = rankList.get(i + 1);
			
			if (ranked.getValue().size() > next.getValue().size()) rank++;
		}
		
		return rankMap;
	}
	
	/**
	 * Groups the calls by {@link #getKey(Call)}.
	 *
	 * @param calls the calls
	 * @return the map of calls by key
	 */
	private static Map<String, List<Call>> groupByKey(@NotNull List<Call> calls) {
		
		return calls.stream().collect(Collectors.groupingBy(RankMap::getKey));
	}
	
	/**
	 * Returns a unique key for the given call.
	 *
	 * @param call the call
	 * @return the key
	 */
	@NotNull
	public static String getKey(@NotNull Call call) {
		
		long id = call.getLong(CallKey.CONTACT_ID, 0L);
		
		if (id != 0L) return id + "";
		
		return PhoneNumbers.formatNumber(call.getNumber(), PhoneNumbers.MINIMUM_NUMBER_LENGTH);
	}
	
	/**
	 * Creates a sorted list from the entries.
	 * The sorting is done by the comparator.
	 *
	 * @param entries    the entries
	 * @param comparator the comparator
	 * @return the ranked list that ordered by comparator
	 */
	@NotNull
	private static List<Map.Entry<String, List<Call>>> sortedList(@NotNull Map<String, List<Call>> entries, @NotNull Comparator<Map.Entry<String, List<Call>>> comparator) {
		// sort
		return entries.entrySet().stream().sorted(comparator).collect(Collectors.toList());
	}
	
	/**
	 * Creates a rank list from the calls.
	 *
	 * @param calls the calls
	 * @return the rank list that ordered by rank number ascending
	 */
	public static List<CallRank> rankListOf(@NotNull List<Call> calls) {
		
		return RankMap.of(calls).getCallRanks();
	}
	
	/**
	 * Creates a new rank map.
	 *
	 * @param calls the calls
	 * @return new rank map
	 */
	@NotNull
	public static RankMap of(@NotNull List<Call> calls) {
		
		return new RankMap(calls);
	}
	
	/**
	 * Creates a rank map from the calls.
	 *
	 * @param calls the calls
	 * @return the rank map
	 */
	@NotNull
	public static Map<Integer, List<CallRank>> rankOf(@NotNull List<Call> calls) {
		
		return RankMap.of(calls).getRankMap();
	}
	
	/**
	 * Returns the rank of the contact.
	 *
	 * @param durationList the list of duration
	 * @param contact      the contact to get the rank
	 * @return the rank or zero
	 */
	public static int getRank(@NotNull List<Map.Entry<Contact, DurationGroup>> durationList, @NotNull Contact contact) {
		
		for (int i = 0; i < durationList.size(); i++)
			if (durationList.get(i).getKey().getContactId() == contact.getContactId())
				return i;
		
		return 0;
	}
	
}
