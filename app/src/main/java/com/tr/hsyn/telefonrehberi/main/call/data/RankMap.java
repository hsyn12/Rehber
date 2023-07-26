package com.tr.hsyn.telefonrehberi.main.call.data;


import com.tr.hsyn.contactdata.Contact;
import com.tr.hsyn.telefonrehberi.main.contact.comment.CallRank;
import com.tr.hsyn.telefonrehberi.main.data.Groups;
import com.tr.hsyn.time.duration.DurationGroup;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * <h3>RankMap</h3>
 * <p>
 * The rank is a number that is used to determine the value of the calls by some criteria.
 * And it starts from 1, advanced by 1.
 * The most valuable rank is 1.
 * And the {@code RankMap} maps the ranks to the list of {@link CallRank}s.
 * Because, the different call lists can have the same rank.
 * So, the {@link #getRank(int)} method when called returns the list of {@link CallRank}.
 */
public class RankMap extends Groups<Integer, CallRank> {
	
	/**
	 * The comparator used to sort the entries by rank ascending.
	 */
	public static final Comparator<Map.Entry<Integer, List<CallRank>>> COMPARATOR_BY_RANK = Map.Entry.comparingByKey();
	
	/**
	 * Creates a new instance.
	 *
	 * @param rankMap the rank map
	 */
	public RankMap(@NotNull Map<Integer, List<CallRank>> rankMap) {
		
		super(rankMap);
	}
	
	/**
	 * Returns the list of {@link CallRank} by the rank.
	 *
	 * @param rank the rank
	 * @return the list of {@link CallRank} or empty list if the rank does not exist.
	 */
	@NotNull
	public List<CallRank> getRank(int rank) {
		
		return get(rank);
	}
	
	/**
	 * Returns the all {@link CallRank}s that have.
	 *
	 * @return the list of {@link CallRank}s
	 */
	@NotNull
	public List<CallRank> getCallRanks() {
		
		return values().stream()
				.flatMap(Collection::stream)
				.sorted(Comparator.comparingInt(CallRank::getRank))
				.collect(Collectors.toList());
	}
	
	/**
	 * Returns the rank of the contact.
	 *
	 * @param contact the contact
	 * @return the rank of the contact or zero if not found
	 */
	public int getRank(@NotNull Contact contact) {
		
		//noinspection DataFlowIssue
		return getCallRanks().stream()
				.filter(callRank -> callRank.getKey().equals(String.valueOf(contact.getContactId())))
				.findFirst().orElse(new CallRank(0, "", null)).getRank();
		
		/* for (Entry<Integer, List<CallRank>> entry : entries) {
			
			List<CallRank> callRanks = entry.getValue();
			
			for (CallRank callRank : callRanks) {
				
				if (callRank.getKey().equals(String.valueOf(contact.getContactId()))) {
					
					return entry.getKey();
				}
			}
		}
		
		return 0; */
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
		
		if (ranks.isEmpty()) return null;
		
		for (CallRank callRank : ranks)
			if (callRank.getKey().equals(String.valueOf(contact.getContactId())))
				return callRank;
		
		return null;
	}
	
	@Nullable
	public CallRank getCallRank(@NotNull Contact contact) {
		
		return getCallRanks().stream()
				.filter(rank -> rank.getKey().equals(String.valueOf(contact.getContactId())))
				.findFirst()
				.orElse(null);
	}
	
	/**
	 * Returns the count of all {@link CallRank}s.
	 * This is the <strong>number of persons</strong>.<br>
	 * {@link #size()} method returns the count of keys,
	 * this is <strong>the number of ranks</strong>.
	 *
	 * @return the count of {@link CallRank}s
	 */
	public int getCallRankCount() {
		
		return getCallRanks().size();
	}
	
	/**
	 * Returns the rank list in order by rank ascending.
	 *
	 * @return the rank list in order by rank ascending
	 */
	public List<Map.Entry<Integer, List<CallRank>>> sortedEntries() {
		
		return sortedEntries(COMPARATOR_BY_RANK);
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
