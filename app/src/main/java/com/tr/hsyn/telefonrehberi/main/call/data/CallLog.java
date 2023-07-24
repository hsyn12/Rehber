package com.tr.hsyn.telefonrehberi.main.call.data;


import android.content.Context;

import androidx.annotation.NonNull;

import com.tr.hsyn.calldata.Call;
import com.tr.hsyn.calldata.CallType;
import com.tr.hsyn.calldata.Type;
import com.tr.hsyn.collection.Lister;
import com.tr.hsyn.contactdata.Contact;
import com.tr.hsyn.keep.Keep;
import com.tr.hsyn.phone_numbers.PhoneNumbers;
import com.tr.hsyn.string.Stringx;
import com.tr.hsyn.telefonrehberi.R;
import com.tr.hsyn.telefonrehberi.main.call.data.type.FilterMostCall;
import com.tr.hsyn.telefonrehberi.main.call.data.type.FilterType;
import com.tr.hsyn.telefonrehberi.main.contact.comment.CallRank;
import com.tr.hsyn.telefonrehberi.main.contact.data.History;
import com.tr.hsyn.telefonrehberi.main.data.CallMap;
import com.tr.hsyn.telefonrehberi.main.data.MainContacts;
import com.tr.hsyn.telefonrehberi.main.dev.Over;
import com.tr.hsyn.time.duration.DurationGroup;
import com.tr.hsyn.xbox.Blue;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;


/**
 * Holds the call logs and provides methods for filtering, searching and analyzing.
 */
@Keep
public final class CallLog {
	
	/**
	 * The comparator used to sort the entries by quantity descending.
	 */
	public static final Comparator<Map.Entry<String, List<Call>>> COMPARATOR_BY_QUANTITY = (e1, e2) -> e2.getValue().size() - e1.getValue().size();
	/**
	 * The filter for All calls.
	 */
	public static final int                                       FILTER_ALL             = 0;
	/**
	 * The filter for Incoming calls.
	 */
	public static final int                                       FILTER_INCOMING        = 1;
	/**
	 * The filter for Outgoing calls.
	 */
	public static final int                                       FILTER_OUTGOING        = 2;
	/**
	 * The filter for missed calls
	 */
	public static final int                                       FILTER_MISSED          = 3;
	/**
	 * The filter for rejected calls
	 */
	public static final int                                       FILTER_REJECTED        = 4;
	/**
	 * The filter for no-named calls
	 */
	public static final int                                       FILTER_NO_NAMED        = 5;
	/**
	 * The filter for random calls
	 */
	public static final int                                       FILTER_RANDOM          = 6;
	/**
	 * The filter for most incoming
	 */
	public static final int                                       FILTER_MOST_INCOMING   = 7;
	/**
	 * The filter for most outgoing
	 */
	public static final int                                       FILTER_MOST_OUTGOING   = 8;
	/**
	 * The filter for most missed
	 */
	public static final int                                       FILTER_MOST_MISSED     = 9;
	/**
	 * The filter for most rejected
	 */
	public static final int                                       FILTER_MOST_REJECTED   = 10;
	/**
	 * The filter for most speaking (incoming)
	 */
	public static final int                                       FILTER_MOST_SPEAKING   = 11;
	/**
	 * The filter for most talking (outgoing)
	 */
	public static final int                                       FILTER_MOST_TALKING    = 12;
	/**
	 * The calls get associated by a key.
	 * The key is a contact ID or a phone number or whatever else unique.
	 * In this way, it provides accessing the calls by a key practically.
	 */
	private final       CallMap                                   callMap;
	/**
	 * The list of {@link Call} that created this {@link RankMap} object
	 */
	private final       List<Call>                                calls;
	
	/**
	 * Creates a new call log.
	 * It uses all call log calls.
	 */
	private CallLog() {
		
		this(Over.CallLog.Calls.getCalls());
	}
	
	/**
	 * Creates a new call log.
	 *
	 * @param calls list of calls to create the call log
	 */
	private CallLog(List<Call> calls) {
		
		this.calls = calls != null ? calls : new ArrayList<>(0);
		callMap    = new CallMap(this.calls, CallLog::getKey);
		mergeSameCalls(callMap);
	}
	
	/**
	 * Returns the size of the call logs.
	 *
	 * @return the size of the call logs
	 */
	public int size() {
		
		return calls.size();
	}
	
	/**
	 * @return {@code true} if the call log is empty.
	 */
	public boolean isEmpty() {
		
		return calls.isEmpty();
	}
	
	/**
	 * @return {@code true} if the call log is not empty
	 */
	public boolean isNotEmpty() {
		
		return !isEmpty();
	}
	
	/**
	 * Returns the history of the contact.
	 *
	 * @param contact the contact
	 * @return the history
	 * @see History
	 */
	@NotNull
	public History getHistoryOf(@NotNull Contact contact) {
		
		return History.of(contact, getCallsById(contact.getContactId()));
	}
	
	/**
	 * Returns all calls with the given call types.
	 *
	 * @param callTypes call types
	 * @return calls
	 */
	@NonNull
	public List<Call> getCallsByType(int @NotNull ... callTypes) {
		
		return getCalls(call -> Lister.contains(callTypes, call.getCallType()));
	}
	
	/**
	 * @return the calls that are creating this object by.
	 */
	@NotNull
	public List<Call> getCalls() {
		
		return calls;
	}
	
	/**
	 * Returns the calls by the filter.
	 *
	 * @param filter the filter
	 * @return the calls
	 * @see FilterType
	 */
	@NotNull
	public List<Call> getCalls(@FilterType int filter) {
		
		//@off
		switch (filter) {
			case FILTER_ALL:      return calls;
			case FILTER_INCOMING: return incomingCalls();
			case FILTER_OUTGOING: return outgoingCalls();
			case FILTER_MISSED:   return missedCalls();
			case FILTER_REJECTED: return rejectedCalls();
			case FILTER_NO_NAMED: return getCalls(Call::isNoNamed);
			case FILTER_RANDOM:   return getCalls(Call::isRandom);
			default:              throw new IllegalArgumentException("Invalid filter: " + filter);
		}//@on
	}
	
	/**
	 * Returns a rank map for the filter.
	 *
	 * @param filter the filter
	 * @return the rank map
	 */
	@NotNull
	public RankMap getMostCalls(@FilterMostCall int filter) {
		
		switch (filter) {
			case FILTER_MOST_INCOMING: return mostIncoming();
			case FILTER_MOST_OUTGOING: return mostOutgoing();
			case FILTER_MOST_MISSED: return mostMissed();
			case FILTER_MOST_REJECTED: return mostRejected();
			case FILTER_MOST_SPEAKING: return mostSpeaking();
			case FILTER_MOST_TALKING: return mostTalking();
			default: throw new IllegalArgumentException("Invalid filter: " + filter);
		}
	}
	
	/**
	 * @return new rank map for incoming calls
	 */
	@NotNull
	public RankMap mostIncoming() {
		
		return new RankMap(rankByQuantity(incomingCalls()));
	}
	
	/**
	 * @return new rank map for outgoing calls
	 */
	@NotNull
	public RankMap mostOutgoing() {
		
		return new RankMap(rankByQuantity(outgoingCalls()));
	}
	
	/**
	 * @return new rank map for missed calls
	 */
	@NotNull
	public RankMap mostMissed() {
		
		return new RankMap(rankByQuantity(missedCalls()));
	}
	
	/**
	 * @return new rank map for rejected calls
	 */
	@NotNull
	public RankMap mostRejected() {
		
		return new RankMap(rankByQuantity(rejectedCalls()));
	}
	
	/**
	 * @return new rank map for incoming calls, which have speaking duration
	 */
	@NotNull
	public RankMap mostSpeaking() {
		
		List<Call> ins = incomingCalls().stream().filter(Call::isSpoken).collect(Collectors.toList());
		return new RankMap(rankByDuration(ins));
	}
	
	/**
	 * @return new rank map for outgoing calls, which have speaking duration
	 */
	@NotNull
	public RankMap mostTalking() {
		
		List<Call> outs = outgoingCalls().stream().filter(Call::isSpoken).collect(Collectors.toList());
		return new RankMap(rankByDuration(outs));
	}
	
	/**
	 * Returns the calls of the contact.
	 *
	 * @param contact   the contact
	 * @param callTypes the call types to select
	 * @return the calls
	 */
	@NotNull
	public List<Call> getCalls(@NotNull Contact contact, int @NotNull ... callTypes) {
		
		List<Call> calls = callMap.get(String.valueOf(contact.getContactId()));
		
		if (callTypes.length == 0) return calls;
		
		return calls.stream().filter(call -> Lister.contains(callTypes, call.getCallType())).collect(Collectors.toList());
	}
	
	/**
	 * Returns all calls with the given number.
	 *
	 * @param phoneNumber the number
	 * @return calls
	 */
	public @NotNull List<Call> getCallsByNumber(String phoneNumber) {
		
		if (Stringx.isNoboe(phoneNumber) || isEmpty()) return new ArrayList<>(0);
		
		phoneNumber = PhoneNumbers.formatNumber(phoneNumber, 10);
		
		return callMap.get(phoneNumber);
	}
	
	/**
	 * Returns all calls that match the given predicate.
	 *
	 * @param predicate the predicate
	 * @return calls
	 */
	@NotNull
	public List<Call> getCalls(@NotNull Predicate<Call> predicate) {
		
		return calls.stream().filter(predicate).collect(Collectors.toList());
	}
	
	/**
	 * Returns all calls with the given contact ID.
	 *
	 * @param contactId the contact ID
	 * @return calls
	 */
	@NotNull
	public List<Call> getCallsById(long contactId) {
		
		return callMap.get(String.valueOf(contactId));
	}
	
	/**
	 * Returns all calls with the given contact ID.
	 *
	 * @param contactId the contact ID
	 * @return calls
	 */
	public @NotNull List<Call> getCallsById(String contactId) {
		
		if (Stringx.isNoboe(contactId) || isEmpty()) return new ArrayList<>(0);
		
		return callMap.get(contactId);
	}
	
	/**
	 * @return incoming calls
	 */
	@NonNull
	public List<Call> incomingCalls() {
		
		return getCallsByType(Call.INCOMING, Call.INCOMING_WIFI);
	}
	
	/**
	 * @return outgoing calls
	 */
	@NotNull
	public List<Call> outgoingCalls() {
		
		return getCallsByType(Call.OUTGOING, Call.OUTGOING_WIFI);
	}
	
	/**
	 * @return missed calls
	 */
	@NotNull
	public List<Call> missedCalls() {
		
		return getCallsByType(Call.MISSED);
	}
	
	/**
	 * @return rejected calls
	 */
	@NotNull
	public List<Call> rejectedCalls() {
		
		return getCallsByType(Call.REJECTED);
	}
	
	/**
	 * @return total speaking duration of all incoming calls in seconds
	 */
	public int getIncomingDuration(@NotNull List<Call> calls) {
		
		return calls.stream()
				.map(Call::getDuration)
				.reduce(Integer::sum)
				.orElse(0);
	}
	
	/**
	 * @return total speaking duration of all outgoing calls in seconds
	 */
	public int getOutgoingDuration(@NotNull List<Call> calls) {
		
		return calls.stream()
				.map(Call::getDuration)
				.reduce(Integer::sum)
				.orElse(0);
	}
	
	/**
	 * Creates a rank map for the given call types.
	 *
	 * @param callTypes the call types to select
	 * @return the rank map that ranked by calls duration.
	 * 		The ranking starts 1, and advances one by one.
	 * 		The most valuable rank is 1.
	 */
	@NotNull
	public RankMap rankByDuration(@CallType int @NotNull ... callTypes) {
		
		return new RankMap(rankByDuration(getCallsByType(callTypes)));
	}
	
	/**
	 * Creates a rank map for the given call types.
	 *
	 * @param callTypes the call types to select
	 * @return the rank map that ranked by calls quantity.
	 * 		The ranking starts 1, and advances one by one.
	 * 		The most valuable rank is 1.
	 */
	@NotNull
	public RankMap rankByQuantity(@CallType int @NotNull ... callTypes) {
		
		return new RankMap(rankByQuantity(getCallsByType(callTypes)));
	}
	
	/**
	 * Returns the most incoming calls that ranked by quantity descending.
	 * The ranking is based on the number of incoming calls.
	 * And it starts 1, and advances one by one.
	 * The most valuable rank is 1.
	 *
	 * @return the most incoming calls
	 */
	@NotNull
	public RankMap makeIncomingRank() {
		
		return rankByQuantity(Call.INCOMING, Call.INCOMING_WIFI);
	}
	
	/**
	 * Returns the most outgoing calls that ranked by quantity descending.
	 * The ranking is based on the number of outgoing calls.
	 * And it starts 1, and advances one by one.
	 * The most valuable rank is 1.
	 *
	 * @return the most outgoing calls
	 */
	@NotNull
	public RankMap makeOutgoingRank() {
		
		return rankByQuantity(Call.OUTGOING, Call.OUTGOING_WIFI);
	}
	
	/**
	 * Returns the most missed calls that ranked by quantity descending.
	 * The ranking is based on the number of missed calls.
	 * And it starts 1, and advances one by one.
	 * The most valuable rank is 1.
	 *
	 * @return the most missed calls
	 */
	@NotNull
	public RankMap makeMissedRank() {
		
		return rankByQuantity(Call.MISSED);
	}
	
	/**
	 * Returns the most rejected calls that ranked by quantity descending.
	 * The ranking is based on the number of rejected calls.
	 * And it starts 1, and advances one by one.
	 * The most valuable rank is 1.
	 *
	 * @return the most rejected calls
	 */
	@NotNull
	public RankMap makeRejectedRank() {
		
		return rankByQuantity(Call.REJECTED);
	}
	
	/**
	 * Creates a new {@link CallLog} object based on the given call type.
	 *
	 * @param callType the call type
	 * @return the new {@link CallLog} object
	 */
	public @NotNull CallLog createByCallType(@CallType int callType) {
		
		switch (callType) {
			
			case Call.INCOMING:
			case Call.INCOMING_WIFI: return create(incomingCalls());
			case Call.OUTGOING:
			case Call.OUTGOING_WIFI: return create(outgoingCalls());
			case Call.MISSED: return create(missedCalls());
			case Call.REJECTED: return create(rejectedCalls());
			
			default: throw new IllegalArgumentException("Unknown call type : " + callType);
		}
	}
	
	/**
	 * Checks if the list is not empty.
	 *
	 * @param list the list
	 * @param <T>  the type of the list
	 * @return {@code true} if the list is not empty
	 */
	public <T> boolean isNotEmpty(@NotNull List<T> list) {
		
		return !list.isEmpty();
	}
	
	/**
	 * Returns the contacts that have or have no calls.<br><br>
	 * Get all the contacts that have incoming calls,
	 * but have no outgoing and rejected calls,<br>
	 * <pre>getContacts(true, false, null, false, 1);</pre>
	 * <br>
	 * <p>
	 * Get all the contacts that having only rejected calls,<br>
	 * <pre>getContacts(false, false, false, true, 1);</pre>
	 * <br>
	 * <p>
	 * Get all the contacts that have incoming calls,<br>
	 * <pre>getContacts(true, null, null, null, 1);</pre>
	 * <br>
	 * <p>
	 * Get all the contacts that have only incoming calls,<br>
	 * <pre>getContacts(true, false, false, false, 1);</pre>
	 * <br>
	 *
	 * @param incoming have calls or not. {@code null} if not specified.
	 * @param outgoing have calls or not. {@code null} if not specified.
	 * @param missed   have calls or not. {@code null} if not specified.
	 * @param rejected have calls or not. {@code null} if not specified.
	 * @param minSize  the minimum number of calls. The number greater than zero means does not select empty calls.
	 * @return the contacts that match the all criteria together.
	 */
	public @NotNull List<Contact> selectContacts(@Nullable Boolean incoming, @Nullable Boolean outgoing, @Nullable Boolean missed, @Nullable Boolean rejected, int minSize) {
		
		List<Contact> contacts = MainContacts.getWithNumber();
		
		if (contacts.isEmpty()) return new ArrayList<>();
		
		List<Contact> _contacts = new ArrayList<>();
		
		for (Contact contact : contacts) {
			
			Predicate<List<Call>> ip = null;
			CallLog               il = null;
			Predicate<List<Call>> op = null;
			CallLog               ol = null;
			Predicate<List<Call>> mp = null;
			CallLog               ml = null;
			Predicate<List<Call>> rp = null;
			CallLog               rl = null;
			Boolean               ir = null;
			Boolean               or = null;
			Boolean               mr = null;
			Boolean               rr = null;
			
			if (incoming != null) {
				ip = incoming ? this::isNotEmpty : List::isEmpty;
				ip = ip.and(c -> c.size() >= minSize);
				il = create(incomingCalls());
			}
			if (outgoing != null) {
				op = outgoing ? this::isNotEmpty : List::isEmpty;
				op = op.and(c -> c.size() >= minSize);
				ol = create(outgoingCalls());
			}
			if (missed != null) {
				mp = missed ? this::isNotEmpty : List::isEmpty;
				mp = mp.and(c -> c.size() >= minSize);
				ml = create(missedCalls());
			}
			if (rejected != null) {
				rp = rejected ? this::isNotEmpty : List::isEmpty;
				rp = rp.and(c -> c.size() >= minSize);
				rl = create(rejectedCalls());
			}
			
			if (ip != null) ir = ip.test(il.getCalls(contact));
			if (op != null) or = op.test(ol.getCalls(contact));
			if (mp != null) mr = mp.test(ml.getCalls(contact));
			if (rp != null) rr = rp.test(rl.getCalls(contact));
			
			if ((ir != null ? ir : true) &&
			    (or != null ? or : true) &&
			    (mr != null ? mr : true) &&
			    (rr != null ? rr : true))
				_contacts.add(contact);
		}
		
		return _contacts;
		
	}
	
	/**
	 * Returns the contacts by the predicate.
	 *
	 * @param predicate the predicate to select
	 * @return the contacts
	 */
	public @NotNull List<Contact> getContactsByCalls(@NotNull Predicate<@NotNull List<Call>> predicate) {
		
		return MainContacts.getWithNumber().stream()
				.filter(contact -> predicate.test(getCalls(contact)))
				.collect(Collectors.toList());
	}
	
	/**
	 * Maybe there are more than one phone number belonging to the same contact.
	 * This method merges the calls belonging to the same contact.
	 *
	 * @param callMap the map object that mapped the key to its calls.
	 */
	private static void mergeSameCalls(@NotNull CallMap callMap) {
		
		// phone numbers
		List<String> keys = Lister.listOf(callMap.keySet());
		
		// loop on numbers
		for (int i = 0; i < keys.size(); i++) {
			
			// Get contact ID.
			// Needs to find the same ID in the list and make it one list.
			String firstKey = keys.get(i);
			// aggregated calls
			List<Call> calls = callMap.remove(firstKey);
			
			if (calls == null) continue;
			
			long contactId = Key.getContactId(calls.get(0));
			
			if (contactId != 0L) {//+ Contact ID found
				
				// loop on the other numbers and find the same ID 
				for (int j = i + 1; j < keys.size(); j++) {
					
					String     secondKey  = keys.get(j);
					List<Call> otherCalls = callMap.remove(secondKey);
					
					if (otherCalls == null) continue;
					
					long otherContactId = Key.getContactId(otherCalls.get(0));
					
					// contactId cannot be zero but otherContactId maybe
					if (contactId == otherContactId) {
						
						// that is the same ID, put it together
						calls.addAll(otherCalls);
						continue;
					}
					
					// put it back in
					callMap.put(secondKey, otherCalls);
				}
			}
			
			// put it back in
			callMap.put(firstKey, calls);
		}
	}
	
	/**
	 * Creates a ranked map from the calls.
	 *
	 * @param calls    the calls
	 * @param callType the call type to select
	 * @return the rank map
	 */
	@NotNull
	public static RankMap rankByDuration(@NotNull List<Call> calls, @CallType int callType) {
		
		calls = calls.stream().filter(c -> c.isType(callType)).collect(Collectors.toList());
		return new RankMap(rankByDuration(calls));
	}
	
	/**
	 * Creates a new call collection.
	 * Also, stored on the blue cloud.
	 *
	 * @return the call collection
	 * @see com.tr.hsyn.key.Key#CALL_LOG
	 */
	@NotNull
	public static CallLog createGlobal() {
		
		CallLog collection = new CallLog();
		
		Blue.box(com.tr.hsyn.key.Key.CALL_LOG, collection);
		
		return collection;
	}
	
	/**
	 * Creates a new call collection.
	 *
	 * @return the call logs
	 */
	@NotNull
	public static CallLog create() {
		
		return new CallLog();
	}
	
	/**
	 * Creates a new call collection.
	 *
	 * @param calls the calls
	 * @return the call collection
	 */
	@NotNull
	public static CallLog createGlobal(List<Call> calls) {
		
		CallLog collection = new CallLog(calls);
		
		Blue.box(com.tr.hsyn.key.Key.CALL_LOG, collection);
		
		return collection;
	}
	
	/**
	 * Creates a new call log.
	 *
	 * @param calls the calls
	 * @return new call log
	 */
	@NotNull
	public static CallLog create(List<Call> calls) {
		
		return new CallLog(calls);
	}
	
	/**
	 * Finds the duration of the contact.
	 *
	 * @param durations the list of entry of contact to duration
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
	
	/**
	 * Returns the name equivalent of the filtering options used in the call logs.
	 *
	 * @param context context
	 * @param filter  the filter
	 * @return the name of the filter
	 */
	public static String getCallFilterName(@NotNull Context context, int filter) {
		
		String[] filters = context.getResources().getStringArray(R.array.call_filter_items);
		
		return filters[filter];
	}
	
	/**
	 * Creates a ranked list by duration.
	 *
	 * @param calls the calls
	 * @return the ranked list in order by rank ascending
	 */
	public static List<CallRank> createRankListByDuration(@NotNull List<Call> calls) {
		
		return new RankMap(rankByDuration(calls)).getCallRanks();
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
	public static Map<Integer, List<CallRank>> rankByQuantity(@NotNull List<Call> calls) {
		
		Map<Integer, List<CallRank>>        rankMap  = new HashMap<>();
		List<Map.Entry<String, List<Call>>> rankList = new CallMap(calls, CallLog::getKey).sortedEntries(COMPARATOR_BY_QUANTITY);
		int                                 rank     = 1;
		int                                 size     = rankList.size();
		int                                 last     = size - 1;
		
		for (int i = 0; i < size; i++) {
			
			//RankList sıfırdan başlayacak
			Map.Entry<String, List<Call>> ranked    = rankList.get(i);
			List<CallRank>                rankList1 = rankMap.computeIfAbsent(rank, r -> new ArrayList<>());
			CallRank                      callRank  = new CallRank(rank, ranked.getKey(), ranked.getValue());
			rankList1.add(callRank);
			
			if (i == last) break;
			
			Map.Entry<String, List<Call>> next = rankList.get(i + 1);
			
			if (ranked.getValue().size() > next.getValue().size()) rank++;
		}
		
		return rankMap;
	}
	
	/**
	 * Returns a map object that ranked by call duration by descending.
	 *
	 * @return a map object that ranked by calls duration by descending
	 */
	@NotNull
	public static Map<Integer, List<CallRank>> rankByDuration(@NotNull List<Call> calls) {
		
		CallMap        callMap   = new CallMap(calls, CallLog::getKey);
		List<String>   keys      = Lister.listOf(callMap.keySet());
		List<CallRank> callRanks = new ArrayList<>();
		
		//_ create call rank objects
		for (String key : keys) {
			
			List<Call> _calls = callMap.get(key);
			
			if (_calls.isEmpty()) continue;
			
			CallRank callRank = new CallRank(key, _calls);
			
			long incomingDuration = 0L;
			long outgoingDuration = 0L;
			
			for (Call call : _calls) {
				
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
		
		return rankMap;
	}
	
	/**
	 * Returns a unique key for the given call.
	 *
	 * @param call the call
	 * @return the key
	 */
	@NotNull
	public static String getKey(@NotNull Call call) {
		
		long id = call.getLong(Key.CONTACT_ID, 0L);
		
		if (id != 0L) return id + "";
		
		return PhoneNumbers.formatNumber(call.getNumber(), PhoneNumbers.MINIMUM_NUMBER_LENGTH);
	}
	
	/**
	 * Returns the call types for the given call type.
	 * {@link Type#INCOMING} and {@link Type#INCOMING_WIFI} are same types.
	 * {@link Type#OUTGOING} and {@link Type#OUTGOING_WIFI} are same types.
	 * So, if the given call type is {@link Type#MISSED} or {@link Type#REJECTED},
	 * this method returns {@link Type#MISSED} or {@link Type#REJECTED}.
	 *
	 * @param callType the call type
	 * @return the call types
	 * @see Type
	 */
	@CallType
	public static int @NotNull [] getCallTypes(@CallType int callType) {
		
		if (callType == Call.MISSED || callType == Call.REJECTED) return new int[]{callType};
		
		switch (callType) {
			case Call.OUTGOING:
			case Call.OUTGOING_WIFI: return new int[]{Call.OUTGOING, Call.OUTGOING_WIFI};
			case Call.INCOMING:
			case Call.INCOMING_WIFI: return new int[]{Call.INCOMING, Call.INCOMING_WIFI};
			default: throw new IllegalArgumentException("Unknown call type: " + callType);
		}
	}
}
