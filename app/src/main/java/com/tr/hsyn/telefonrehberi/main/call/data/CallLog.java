package com.tr.hsyn.telefonrehberi.main.call.data;


import com.tr.hsyn.calldata.Call;
import com.tr.hsyn.calldata.CallType;
import com.tr.hsyn.calldata.Type;
import com.tr.hsyn.collection.Lister;
import com.tr.hsyn.contactdata.Contact;
import com.tr.hsyn.keep.Keep;
import com.tr.hsyn.phone_numbers.PhoneNumbers;
import com.tr.hsyn.string.Stringx;
import com.tr.hsyn.telefonrehberi.main.call.data.type.FilterMostCall;
import com.tr.hsyn.telefonrehberi.main.call.data.type.FilterType;
import com.tr.hsyn.telefonrehberi.main.contact.data.History;
import com.tr.hsyn.telefonrehberi.main.data.CallMap;
import com.tr.hsyn.telefonrehberi.main.data.MainContacts;
import com.tr.hsyn.time.duration.DurationGroup;
import com.tr.hsyn.xbox.Blue;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;


/**
 * Holds the call logs and provides methods for filtering, searching and analyzing.
 */
@Keep
public final class CallLog implements CCollection, Ranker {
	
	/**
	 * The filter for All calls.
	 */
	public static final int        FILTER_ALL           = 0;
	/**
	 * The filter for Incoming calls.
	 */
	public static final int        FILTER_INCOMING      = 1;
	/**
	 * The filter for Outgoing calls.
	 */
	public static final int        FILTER_OUTGOING      = 2;
	/**
	 * The filter for missed calls
	 */
	public static final int        FILTER_MISSED        = 3;
	/**
	 * The filter for rejected calls
	 */
	public static final int        FILTER_REJECTED      = 4;
	/**
	 * The filter for no-named calls
	 */
	public static final int        FILTER_NO_NAMED      = 5;
	/**
	 * The filter for random calls
	 */
	public static final int        FILTER_RANDOM        = 6;
	/**
	 * The filter for most incoming
	 */
	public static final int        FILTER_MOST_INCOMING = 7;
	/**
	 * The filter for most outgoing
	 */
	public static final int        FILTER_MOST_OUTGOING = 8;
	/**
	 * The filter for most missed
	 */
	public static final int        FILTER_MOST_MISSED   = 9;
	/**
	 * The filter for most rejected
	 */
	public static final int        FILTER_MOST_REJECTED = 10;
	/**
	 * The filter for most speaking (incoming)
	 */
	public static final int        FILTER_MOST_SPEAKING = 11;
	/**
	 * The filter for most talking (outgoing)
	 */
	public static final int        FILTER_MOST_TALKING  = 12;
	/**
	 * The calls get associated by a key.
	 * The key is a contact ID or a phone number or whatever else unique.
	 * In this way, it provides accessing the calls by a key practically.
	 */
	private final       CallMap    callMap;
	/**
	 * The list of {@link Call} that created this {@link RankMap} object
	 */
	private final       List<Call> calls;
	
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
	 * @return the calls that are creating this object by.
	 */
	@Override
	@NotNull
	public List<Call> getCalls() {
		
		return calls;
	}
	
	/**
	 * @return new rank map for incoming calls
	 */
	@Override
	@NotNull
	public RankMap incomingRank() {
		
		return new RankMap(rankByQuantity(incomingCalls()));
	}
	
	/**
	 * @return new rank map for outgoing calls
	 */
	@Override
	@NotNull
	public RankMap outgoingRank() {
		
		return new RankMap(rankByQuantity(outgoingCalls()));
	}
	
	/**
	 * @return new rank map for missed calls
	 */
	@Override
	@NotNull
	public RankMap missedRank() {
		
		return new RankMap(rankByQuantity(missedCalls()));
	}
	
	/**
	 * @return new rank map for rejected calls
	 */
	@Override
	@NotNull
	public RankMap rejectedRank() {
		
		return new RankMap(rankByQuantity(rejectedCalls()));
	}
	
	/**
	 * @return new rank map for incoming calls, which have speaking duration
	 */
	@Override
	@NotNull
	public RankMap incomingDurationRank() {
		
		List<Call> ins = incomingCalls().stream().filter(Call::isSpoken).collect(Collectors.toList());
		return new RankMap(rankByDuration(ins));
	}
	
	/**
	 * @return new rank map for outgoing calls, which have speaking duration
	 */
	@Override
	@NotNull
	public RankMap outgoingDurationRank() {
		
		List<Call> outs = outgoingCalls().stream().filter(Call::isSpoken).collect(Collectors.toList());
		return new RankMap(rankByDuration(outs));
	}
	
	public RankMap rankByQuantity() {
		
		return rankByQuantity(calls);
	}
	
	public RankMap rankByDuration() {
		
		return rankByDuration(calls);
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
		
		return History.of(contact, getById(contact.getContactId()));
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
			case FILTER_MOST_INCOMING: return incomingRank();
			case FILTER_MOST_OUTGOING: return outgoingRank();
			case FILTER_MOST_MISSED: return missedRank();
			case FILTER_MOST_REJECTED: return rejectedRank();
			case FILTER_MOST_SPEAKING: return incomingDurationRank();
			case FILTER_MOST_TALKING: return outgoingDurationRank();
			default: throw new IllegalArgumentException("Invalid filter: " + filter);
		}
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
	 * Returns all calls with the given contact ID.
	 *
	 * @param contactId the contact ID
	 * @return calls
	 */
	@NotNull
	public List<Call> getById(long contactId) {
		
		return callMap.get(String.valueOf(contactId));
	}
	
	/**
	 * Returns all calls with the given contact ID.
	 *
	 * @param contactId the contact ID
	 * @return calls
	 */
	public @NotNull List<Call> getById(String contactId) {
		
		if (Stringx.isNoboe(contactId) || isEmpty()) return new ArrayList<>(0);
		
		return callMap.get(contactId);
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
	 * @return the rank map that ranked by calls quantity.
	 * 		The ranking starts 1, and advances one by one.
	 * 		The most valuable rank is 1.
	 */
	@NotNull
	public RankMap rankByQuantity(@CallType int @NotNull ... callTypes) {
		
		return new RankMap(rankByQuantity(getByType(callTypes)));
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
	 * Creates a new call log.
	 *
	 * @param calls the calls
	 * @return the call log
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
	 * Returns the duration of the contact.
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
	
	/**
	 * Returns the valid call types.
	 *
	 * @return the valid call types
	 */
	public static int @NotNull [] getCallTypes() {
		
		return new int[]{
				Call.INCOMING,
				Call.OUTGOING,
				Call.MISSED,
				Call.REJECTED
		};
	}
}