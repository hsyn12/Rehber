package com.tr.hsyn.telefonrehberi.main.call.data;


import com.tr.hsyn.calldata.Call;
import com.tr.hsyn.collection.Lister;
import com.tr.hsyn.phone_numbers.PhoneNumbers;
import com.tr.hsyn.telefonrehberi.main.contact.comment.CallRank;

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
	
	private Map<Integer, List<CallRank>> rankMap;
	private List<Call>                   calls;
	private Map<String, List<Call>>      idToCalls;
	
	public RankMap(@NotNull Map<Integer, List<CallRank>> rankMap) {
		
		this.rankMap = rankMap;
	}
	
	public RankMap(@NotNull List<Call> calls) {
		
		this.calls = calls;
		idToCalls  = groupByKey(calls, RankMap::getKey);
		makeRank();
	}
	
	public List<Call> getCalls() {
		
		return calls;
	}
	
	public Map<String, List<Call>> getIdToCalls() {
		
		return idToCalls;
	}
	
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
	 * Returns the count of same ranks.
	 *
	 * @param rank rank
	 * @return the count of same ranks. Returns zero if the rank does not exist.
	 */
	public int getRankCount(int rank) {
		
		var ranks = getRank(rank);
		
		if (ranks != null) return ranks.size();
		return 0;
	}
	
	public int size() {
		
		return rankMap.size();
	}
	
	public boolean isEmpty() {
		
		return calls.isEmpty();
	}
	
	/**
	 * Creates a rank map for calls that related to this {@link CallLog} object.
	 * Remember, a {@code CallLogs} object can be related to any list of {@link Call}.
	 *
	 * @return the rank map.
	 * 		The ranking starts 1, and advances one by one.
	 * 		The most valuable rank is 1.
	 */
	public @NotNull Map<Integer, List<CallRank>> makeRank() {
		
		return createRankMap(idToCalls, QUANTITY_COMPARATOR);
	}
	
	/**
	 * Groups the calls by the key function.
	 *
	 * @param calls       the calls to group
	 * @param callTypes   the call types. If not specified, all calls are grouped.
	 * @param keyFunction the key function to extract the key
	 * @return the group of calls by key
	 */
	public static Map<String, List<Call>> groupByKey(@NotNull List<Call> calls, @Nullable Function<Call, String> keyFunction, int @NotNull ... callTypes) {
		
		if (callTypes.length == 0)
			return calls.stream().collect(Collectors.groupingBy(keyFunction != null ? keyFunction : RankMap::getKey));
		
		return calls.stream().filter(c -> Lister.contains(callTypes, c.getCallType())).collect(Collectors.groupingBy(keyFunction != null ? keyFunction : RankMap::getKey));
	}
	
	/**
	 * Creates a ranked map from the calls.
	 *
	 * @param calls      the calls
	 * @param comparator the comparator
	 * @param callType   the call type to select
	 * @return the ranked map
	 */
	@NotNull
	public static Map<Integer, List<CallRank>> createRankMap(@NotNull List<Call> calls, Comparator<Map.Entry<String, List<Call>>> comparator, int callType) {
		
		return createRankMap(groupByKey(calls, null, callType), comparator);
	}
	
	/**
	 * Creates a ranked map from the calls.
	 *
	 * @param calls    the calls
	 * @param callType the call type to select
	 * @return the rank map
	 */
	@NotNull
	public static Map<Integer, List<CallRank>> createRankMap(@NotNull List<Call> calls, int callType) {
		
		return createRankMap(groupByKey(calls, null, callType), QUANTITY_COMPARATOR);
	}
	
	/**
	 * Creates a ranked map from the entries.
	 * The ranking is done by the comparator.
	 * The ranking starts from 1.
	 * The most valuable rank is 1 and advances one by one.
	 *
	 * @param entries    entries that mapped an ID to its calls
	 * @param comparator the comparator that the ranking is done according to
	 * @return the ranked map.
	 * 		The keys are the ranks, and the values are the call rank objects.
	 */
	@NotNull
	public static Map<Integer, List<CallRank>> createRankMap(@NotNull Map<String, List<Call>> entries, @NotNull Comparator<Map.Entry<String, List<Call>>> comparator) {
		
		Map<Integer, List<CallRank>>        rankMap  = new HashMap<>();
		List<Map.Entry<String, List<Call>>> rankList = sortedList(entries, comparator);
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
	public static Map<String, List<Call>> groupByKey(@NotNull List<Call> calls) {
		
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
	
	public static List<CallRank> createRankList(@NotNull List<Call> calls) {
		
		return new RankMap(calls).makeRank().values()
				.stream()
				.flatMap(Collection::stream)
				.sorted(Comparator.comparingInt(CallRank::getRank))
				.collect(Collectors.toList());
	}
	
	
}
