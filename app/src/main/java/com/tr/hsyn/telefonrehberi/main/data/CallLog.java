package com.tr.hsyn.telefonrehberi.main.data;


import android.content.Context;

import androidx.annotation.NonNull;

import com.tr.hsyn.calldata.Call;
import com.tr.hsyn.collection.Lister;
import com.tr.hsyn.contactdata.Contact;
import com.tr.hsyn.keep.Keep;
import com.tr.hsyn.key.Key;
import com.tr.hsyn.phone_numbers.PhoneNumbers;
import com.tr.hsyn.string.Stringx;
import com.tr.hsyn.telefonrehberi.R;
import com.tr.hsyn.telefonrehberi.main.call.data.CallKey;
import com.tr.hsyn.telefonrehberi.main.call.data.Ranker;
import com.tr.hsyn.telefonrehberi.main.contact.comment.CallRank;
import com.tr.hsyn.telefonrehberi.main.contact.data.History;
import com.tr.hsyn.telefonrehberi.main.dev.Over;
import com.tr.hsyn.time.duration.DurationGroup;
import com.tr.hsyn.xbox.Blue;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
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
	public static final Comparator<Map.Entry<String, List<Call>>> QUANTITY_COMPARATOR  = (e1, e2) -> e2.getValue().size() - e1.getValue().size();
	/**
	 * The filter for All calls.
	 */
	public static final int                                       FILTER_ALL           = 0;
	/**
	 * The filter for Incoming calls.
	 */
	public static final int                                       FILTER_INCOMING      = 1;
	/**
	 * The filter for Outgoing calls.
	 */
	public static final int                                       FILTER_OUTGOING      = 2;
	/**
	 * The filter for missed calls
	 */
	public static final int                                       FILTER_MISSED        = 3;
	/**
	 * The filter for rejected calls
	 */
	public static final int                                       FILTER_REJECTED      = 4;
	/**
	 * The filter for no-named calls
	 */
	public static final int                                       FILTER_NO_NAMED      = 5;
	/**
	 * The filter for random calls
	 */
	public static final int                                       FILTER_RANDOM        = 6;
	/**
	 * The filter for most incoming
	 */
	public static final int                                       FILTER_MOST_INCOMING = 7;
	/**
	 * The filter for most outgoing
	 */
	public static final int                                       FILTER_MOST_OUTGOING = 8;
	/**
	 * The filter for most missed
	 */
	public static final int                                       FILTER_MOST_MISSED   = 9;
	/**
	 * The filter for most rejected
	 */
	public static final int                                       FILTER_MOST_REJECTED = 10;
	/**
	 * The filter for most speaking (incoming)
	 */
	public static final int                                       FILTER_MOST_SPEAKING = 11;
	/**
	 * The filter for most talking (outgoing)
	 */
	public static final int                                       FILTER_MOST_TALKING  = 12;
	/**
	 * The calls get associated by a key.
	 * The key is a contact ID or a phone number or whatever else unique.
	 * In this way, it provides accessing the calls by a key practically.
	 */
	private final       Groups<String, Call>                      callGroups;
	
	/**
	 * The list of {@link Call} that created this {@link Ranker} object
	 */
	private final List<Call> calls;
	private final Ranker     ranker;
	
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
		callGroups = Groups.from(this.calls, CallLog::getKey);
		mergeSameCalls(callGroups);
		ranker = Ranker.create(callGroups);
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
	 * @return {@code true} if the call list that created this {@link Ranker} object is empty.
	 */
	public boolean isEmpty() {
		
		return calls.isEmpty();
	}
	
	/**
	 * Returns the history of the given contact.
	 *
	 * @param contact the contact
	 * @return the history
	 * @see History
	 */
	@NotNull
	public History getHistoryOf(@NotNull Contact contact) {
		
		return History.of(contact, getCalls(String.valueOf(contact.getContactId())));
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
		
		var calls = callGroups.get(String.valueOf(contact.getContactId()));
		
		if (callTypes.length == 0) return calls;
		
		return calls.stream().filter(call -> Lister.contains(callTypes, call.getCallType())).collect(Collectors.toList());
	}
	
	/**
	 * Returns all calls with the given call types.
	 *
	 * @param callTypes call types
	 * @return calls
	 */
	@NonNull
	public List<Call> getCallsByType(int @NotNull ... callTypes) {
		
		List<Call> _calls = new ArrayList<>();
		
		for (int callType : callTypes)
			for (Call call : calls)
				if (callType == call.getCallType() && !_calls.contains(call)) _calls.add(call);
		
		return _calls;
	}
	
	/**
	 * @return incoming calls
	 */
	@NonNull
	public List<Call> getIncomingCalls() {
		
		return getCallsByType(Call.INCOMING, Call.INCOMING_WIFI);
	}
	
	/**
	 * @return outgoing calls
	 */
	@NotNull
	public List<Call> getOutgoingCalls() {
		
		return getCallsByType(Call.OUTGOING, Call.OUTGOING_WIFI);
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
	public Ranker makeIncomingRank() {
		
		return makeRank(Call.INCOMING, Call.INCOMING_WIFI);
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
	public Ranker makeOutgoingRank() {
		
		return makeRank(Call.OUTGOING, Call.OUTGOING_WIFI);
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
	public Ranker makeMissedRank() {
		
		return makeRank(Call.MISSED);
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
	public Ranker makeRejectedRank() {
		
		return makeRank(Call.REJECTED);
	}
	
	/**
	 * @return the calls that are creating this object by.
	 */
	@NotNull
	public List<Call> getCalls() {
		
		return calls;
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
		
		return callGroups.get(phoneNumber);
	}
	
	/**
	 * Returns all calls for the given numbers in the given list.
	 *
	 * @param numbers  the numbers
	 * @param callList the list of calls to search
	 * @return calls
	 */
	@NotNull
	public List<Call> getCallsByNumbers(List<String> numbers, @NotNull List<Call> callList) {
		
		List<Call> calls = new ArrayList<>();
		
		if (numbers == null || isEmpty()) return calls;
		
		for (String number : numbers) calls.addAll(getCallsByNumber(number, callList));
		
		return calls;
	}
	
	@NotNull
	public List<Call> getCalls(long contactId) {
		
		return callGroups.get(String.valueOf(contactId));
		
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
	 * @return missed calls
	 */
	@NotNull
	public List<Call> getMissedCalls() {
		
		return getCallsByType(Call.MISSED);
	}
	
	/**
	 * @return rejected calls
	 */
	@NotNull
	public List<Call> getRejectedCalls() {
		
		return getCallsByType(Call.REJECTED);
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
	 * Returns all calls for the given numbers.
	 *
	 * @param numbers the numbers
	 * @return calls
	 */
	@NotNull
	public List<Call> getCallsByNumbers(List<String> numbers) {
		
		List<Call> calls = new ArrayList<>();
		
		if (numbers == null || isEmpty()) return calls;
		
		for (String number : numbers) calls.addAll(getCallsByNumber(number));
		
		return calls;
	}
	
	/**
	 * @return {@code true} if the collection is not empty
	 */
	public boolean isNotEmpty() {
		
		return !isEmpty();
	}
	
	/**
	 * Returns all calls with the given contact ID.
	 *
	 * @param id the contact ID
	 * @return calls
	 */
	public @NotNull List<Call> getCalls(String id) {
		
		if (Stringx.isNoboe(id) || isEmpty()) return new ArrayList<>(0);
		
		return callGroups.get(id);
	}
	
	/**
	 * Returns all calls with the given number in the given list.
	 *
	 * @param phoneNumber the number
	 * @param callList    the list of calls to search
	 * @return calls
	 */
	public @NotNull List<Call> getCallsByNumber(String phoneNumber, @NotNull List<Call> callList) {
		
		if (Stringx.isNoboe(phoneNumber) || isEmpty()) return new ArrayList<>(0);
		
		@NotNull String _phoneNumber = PhoneNumbers.formatNumber(phoneNumber, 10);
		return callList.stream().filter(c -> c.getNumber().equals(_phoneNumber)).collect(Collectors.toList());
	}
	
	/**
	 * Creates a rank map for the given call types.
	 *
	 * @param callTypes the call types
	 * @return the rank map that ranked by calls quantity.
	 * 		The ranking starts 1, and advances one by one.
	 * 		The most valuable rank is 1.
	 */
	@NotNull
	public Ranker makeRank(int @NotNull ... callTypes) {
		
		return Ranker.create(Groups.from(getCallsByType(callTypes), CallLog::getKey));
	}
	
	/**
	 * Creates a new {@link CallLog} object based on the given call type.
	 *
	 * @param callType the call type
	 * @return the new {@link CallLog} object
	 */
	public @NotNull CallLog createByCallType(int callType) {
		
		switch (callType) {
			
			case Call.INCOMING:
			case Call.INCOMING_WIFI: return create(getIncomingCalls());
			case Call.OUTGOING:
			case Call.OUTGOING_WIFI: return create(getOutgoingCalls());
			case Call.MISSED: return create(getMissedCalls());
			case Call.REJECTED: return create(getRejectedCalls());
			
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
				il = create(getIncomingCalls());
			}
			if (outgoing != null) {
				op = outgoing ? this::isNotEmpty : List::isEmpty;
				op = op.and(c -> c.size() >= minSize);
				ol = create(getOutgoingCalls());
			}
			if (missed != null) {
				mp = missed ? this::isNotEmpty : List::isEmpty;
				mp = mp.and(c -> c.size() >= minSize);
				ml = create(getMissedCalls());
			}
			if (rejected != null) {
				rp = rejected ? this::isNotEmpty : List::isEmpty;
				rp = rp.and(c -> c.size() >= minSize);
				rl = create(getRejectedCalls());
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
	 * @param entries the map object that mapped the key to its calls.
	 */
	private static void mergeSameCalls(@NotNull Groups<String, Call> entries) {
		
		// phone numbers
		List<String> keys = new ArrayList<>(entries.keySet());
		
		// loop on numbers
		for (int i = 0; i < keys.size(); i++) {
			
			// Get contact ID.
			// Needs to find the same ID in the list and make it one list.
			String firstKey = keys.get(i);
			// aggregated calls
			List<Call> calls = entries.remove(firstKey);
			
			if (calls == null) continue;
			
			long contactId = CallKey.getContactId(calls.get(0));
			
			if (contactId != 0L) {//+ Contact ID found
				
				// loop on the other numbers and find the same ID 
				for (int j = i + 1; j < keys.size(); j++) {
					
					String     secondKey  = keys.get(j);
					List<Call> otherCalls = entries.remove(secondKey);
					
					if (otherCalls == null) continue;
					
					long otherContactId = CallKey.getContactId(otherCalls.get(0));
					
					// contactId cannot be zero but otherContactId maybe
					if (contactId == otherContactId) {
						
						// that is the same ID, put it together
						calls.addAll(otherCalls);
						continue;
					}
					
					// put it back in
					entries.put(secondKey, otherCalls);
				}
			}
			
			// put it back in
			entries.put(firstKey, calls);
		}
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
			return calls.stream().collect(Collectors.groupingBy(keyFunction != null ? keyFunction : CallLog::getKey));
		
		return calls.stream().filter(c -> Lister.contains(callTypes, c.getCallType())).collect(Collectors.groupingBy(keyFunction != null ? keyFunction : CallLog::getKey));
	}
	
	/**
	 * Creates a ranked map from the calls.
	 *
	 * @param calls    the calls
	 * @param callType the call type to select
	 * @return the rank map
	 */
	@NotNull
	public static Ranker by(@NotNull List<Call> calls, int callType) {
		
		calls = calls.stream().filter(c -> c.isType(callType)).collect(Collectors.toList());
		return Ranker.create(Groups.from(calls, CallLog::getKey));
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
	 * Groups the calls by {@link #getKey(Call)}.
	 *
	 * @param calls the calls
	 * @return the map of calls by key
	 */
	private static Map<String, List<Call>> groupByKey(@NotNull List<Call> calls) {
		
		return calls.stream().collect(Collectors.groupingBy(CallLog::getKey));
	}
	
	/**
	 * Creates a new call collection.
	 * Also, stored on the blue cloud.
	 *
	 * @return the call collection
	 * @see Key#CALL_LOG
	 */
	@NotNull
	public static CallLog createGlobal() {
		
		CallLog collection = new CallLog();
		
		Blue.box(Key.CALL_LOG, collection);
		
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
		
		Blue.box(Key.CALL_LOG, collection);
		
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
		
		return Ranker.createForDuration(calls).getCallRanks();
	}
}