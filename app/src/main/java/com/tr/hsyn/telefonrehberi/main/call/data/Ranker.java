package com.tr.hsyn.telefonrehberi.main.call.data;


import com.tr.hsyn.calldata.Call;
import com.tr.hsyn.contactdata.Contact;
import com.tr.hsyn.telefonrehberi.main.contact.comment.CallRank;
import com.tr.hsyn.telefonrehberi.main.data.CallLog;
import com.tr.hsyn.telefonrehberi.main.data.Groups;
import com.tr.hsyn.telefonrehberi.main.data.MainContacts;
import com.tr.hsyn.time.duration.DurationGroup;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;


/**
 * <h3>Ranker</h3>
 * <p>
 * The rank is a number that is used to determine the value of the calls by some criteria.
 * And it starts from 1, advanced by 1.
 * The most valuable rank is 1.
 * And the {@code Ranker} maps the ranks to the list of {@link CallRank}s.
 * Because, the different call lists can have the same rank.
 * So, the {@link #getRank(int)} method when called returns the list of {@link CallRank}.
 */
public class Ranker {
	
	public static final Comparator<Map.Entry<String, List<Call>>> QUANTITY_COMPARATOR = (e1, e2) -> e2.getValue().size() - e1.getValue().size();
	private final       Groups<Integer, CallRank>                 rankGroup;
	
	/**
	 * Creates an instance of {@link Ranker}.
	 *
	 * @param calls the map of {@link Call} to make rank of.
	 */
	private Ranker(@NotNull Groups<String, Call> calls) {
		
		this(calls, false);
	}
	
	/**
	 * Creates an instance of {@link Ranker}.
	 *
	 * @param calls the call map to make rank of.
	 */
	private Ranker(@NotNull Groups<String, Call> calls, boolean forDuration) {
		
		rankGroup = forDuration ?
				Groups.from(calls, Ranker::rankByDuration) :
				Groups.from(calls, Ranker::rankByQuantity);
	}
	
	/**
	 * Returns the list of {@link CallRank}  by the rank.
	 *
	 * @param rank the rank
	 * @return the list of {@link CallRank} or {@code null} if the rank does not exist.
	 */
	@Nullable
	public List<CallRank> getRank(int rank) {
		
		return rankGroup.get(rank);
	}
	
	/**
	 * @return the rank size
	 */
	public int getRankSize() {
		
		return rankGroup.size();
	}
	
	/**
	 * Returns the list of {@link CallRank}s that had.
	 *
	 * @return the list of {@link CallRank}s
	 */
	public List<CallRank> getCallRanks() {
		
		return rankGroup.values().stream()
				.flatMap(Collection::stream)
				.sorted(Comparator.comparingInt(CallRank::getRank))
				.collect(Collectors.toList());
	}
	
	public Collection<List<CallRank>> getCallRankList() {
		
		return rankGroup.values();
	}
	
	/**
	 * Returns the rank of the contact.
	 *
	 * @param contact the contact
	 * @return the rank of the contact or –1 if not found
	 */
	public int getRank(@NotNull Contact contact) {
		
		for (Map.Entry<Integer, List<CallRank>> entry : rankGroup.entrySet()) {
			
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
	
	public boolean isEmpty() {
		
		return rankGroup.isEmpty();
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
	public static Groups<Integer, CallRank> rankByQuantity(@NotNull Groups<String, Call> callMap) {
		
		Map<Integer, List<CallRank>>        rankMap  = new HashMap<>();
		List<Map.Entry<String, List<Call>>> rankList = sortedList(callMap.getMap(), Ranker.QUANTITY_COMPARATOR);
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
		
		return new Groups<>(rankMap);
	}
	
	/**
	 * Returns a map object that ranked by call duration by descending.
	 *
	 * @return a map object that ranked by calls duration by descending
	 */
	@NotNull
	public static Groups<Integer, CallRank> rankByDuration(@NotNull Groups<String, Call> groups) {
		
		Set<String>    keys      = groups.keySet();
		List<CallRank> callRanks = new ArrayList<>();
		
		//_ create call rank objects
		for (String key : keys) {
			
			List<Call> calls = groups.get(key);
			
			if (calls.isEmpty()) continue;
			
			CallRank callRank = new CallRank(key, calls);
			
			long incomingDuration = 0L;
			long outgoingDuration = 0L;
			
			for (Call call : calls) {
				
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
		
		return new Groups<>(rankMap);
	}
	
	@NotNull
	public static Ranker create(@NotNull Groups<String, Call> callGroup) {
		
		return new Ranker(callGroup, false);
	}
	
	@NotNull
	public static Ranker create(@NotNull List<Call> calls) {
		
		return new Ranker(Groups.from(calls, CallLog::getKey), false);
	}
	
	@NotNull
	public static Ranker createForDuration(@NotNull List<Call> calls) {
		
		return new Ranker(Groups.from(calls, CallLog::getKey), true);
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
		
		return Ranker.create(calls).getCallRanks();
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
