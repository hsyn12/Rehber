package com.tr.hsyn.telefonrehberi.main.call.data;


import android.content.Context;

import androidx.annotation.NonNull;

import com.tr.hsyn.calldata.Call;
import com.tr.hsyn.calldata.CallType;
import com.tr.hsyn.collection.CoupleMap;
import com.tr.hsyn.collection.Lister;
import com.tr.hsyn.contactdata.Contact;
import com.tr.hsyn.keep.Keep;
import com.tr.hsyn.key.Key;
import com.tr.hsyn.phone_numbers.PhoneNumbers;
import com.tr.hsyn.string.Stringx;
import com.tr.hsyn.telefonrehberi.R;
import com.tr.hsyn.telefonrehberi.main.contact.comment.CallRank;
import com.tr.hsyn.telefonrehberi.main.contact.data.ContactKey;
import com.tr.hsyn.telefonrehberi.main.contact.data.History;
import com.tr.hsyn.telefonrehberi.main.dev.Over;
import com.tr.hsyn.time.duration.DurationGroup;
import com.tr.hsyn.xbox.Blue;
import com.tr.hsyn.xlog.xlog;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;


/**
 * Collection of the call logs.
 * This class can be accessed via {@link Blue#getObject(Key)} with the key {@link Key#CALL_LOGS} after creation.
 */
@Keep
public final class CallLogs {
	
	/**
	 * The comparator used to sort the entries by quantity descending.
	 */
	public static final    Comparator<Map.Entry<String, List<Call>>> QUANTITY_COMPARATOR  = (e1, e2) -> e2.getValue().size() - e1.getValue().size();
	/**
	 * The filter for All calls.
	 */
	public static final    int                                       FILTER_ALL           = 0;
	/**
	 * The filter for Incoming calls.
	 */
	public static final    int                                       FILTER_INCOMING      = 1;
	/**
	 * The filter for Outgoing calls.
	 */
	public static final    int                                       FILTER_OUTGOING      = 2;
	/**
	 * The filter for missed calls
	 */
	public static final    int                                       FILTER_MISSED        = 3;
	/**
	 * The filter for rejected calls
	 */
	public static final    int                                       FILTER_REJECTED      = 4;
	/**
	 * The filter for no-named calls
	 */
	public static final    int                                       FILTER_NO_NAMED      = 5;
	/**
	 * The filter for random calls
	 */
	public static final    int                                       FILTER_RANDOM        = 6;
	/**
	 * The filter for most incoming
	 */
	public static final    int                                       FILTER_MOST_INCOMING = 7;
	/**
	 * The filter for most outgoing
	 */
	public static final    int                                       FILTER_MOST_OUTGOING = 8;
	/**
	 * The filter for most missed
	 */
	public static final    int                                       FILTER_MOST_MISSED   = 9;
	/**
	 * The filter for most rejected
	 */
	public static final    int                                       FILTER_MOST_REJECTED = 10;
	/**
	 * The filter for most speaking (incoming)
	 */
	public static final    int                                       FILTER_MOST_SPEAKING = 11;
	/**
	 * The filter for most talking (outgoing)
	 */
	public static final    int                                       FILTER_MOST_TALKING  = 12;
	/**
	 * All call log calls
	 */
	@NotNull private final List<Call>                                calls;
	/**
	 * Map object that has a key by phone number or by contact ID and a value as a list of its calls
	 * that belong to the phone number.
	 */
	@NotNull private final Map<String, List<Call>>                   mapIdToCalls;
	private final          CoupleMap<Long, Contact>                  mapIdToContact;
	private final          CoupleMap<String, Long>                   mapNumberToId;
	
	
	/**
	 * Creates a new call collection.
	 */
	private CallLogs() {
		
		List<Call> c = Over.CallLog.Calls.getCalls();
		this.calls   = c != null ? c : new ArrayList<>(0);
		mapIdToCalls = mapIdToCalls(this.calls);
		mergeSameCalls(mapIdToCalls);
		mapNumberToId  = new CoupleMap<>(createMapNumberToId());
		mapIdToContact = new CoupleMap<>(createMapIdToContact());
	}
	
	/**
	 * Creates a new call collection.
	 *
	 * @param calls list of calls
	 */
	private CallLogs(List<Call> calls) {
		
		this.calls     = calls != null ? calls : new ArrayList<>(0);
		mapIdToCalls   = mapIdToCalls(this.calls);
		mapNumberToId  = new CoupleMap<>(createMapNumberToId());
		mapIdToContact = new CoupleMap<>(createMapIdToContact());
	}
	
	/**
	 * Returns the map object that has a key by contact ID, and a contact as value.
	 *
	 * @return map
	 */
	public CoupleMap<Long, Contact> getMapIdToContact() {
		
		return mapIdToContact;
	}
	
	/**
	 * Returns the map object that has a key from phone number, and a contact ID as value.
	 *
	 * @return map
	 */
	public CoupleMap<String, Long> getMapNumberToId() {
		
		return mapNumberToId;
	}
	
	/**
	 * Returns the contact by phone number.
	 *
	 * @param number the phone number
	 * @return the contact
	 */
	@NotNull
	public Contact getContact(@NotNull String number) {
		
		return mapIdToContact.get(mapNumberToId.get(number));
	}
	
	/**
	 * Returns the contact by contact ID.
	 *
	 * @param id the contact ID
	 * @return the contact
	 */
	@NotNull
	public Contact getContact(long id) {
		
		return mapIdToContact.get(id);
	}
	
	/**
	 * Returns the contact ID by phone number.
	 *
	 * @param number the phone number
	 * @return the contact ID
	 */
	@Nullable
	public Long getContactId(@NotNull String number) {
		
		return mapNumberToId.get(number);
	}
	
	/**
	 * Returns the phone number by contact ID.
	 *
	 * @param id the contact ID
	 * @return the phone number
	 */
	public String getNumber(long id) {
		
		return mapNumberToId.getKey(id);
	}
	
	/**
	 * Creates the map object that has a key by contact ID, and a contact as value.
	 */
	private Map<Long, Contact> createMapIdToContact() {
		
		List<Contact> contacts = Blue.getObject(Key.CONTACTS);
		
		if (contacts != null) {
			
			xlog.d("Contacts size : %s", contacts.size());
			
			return contacts.stream().collect(Collectors.toMap(Contact::getContactId, c -> c));
		}
		
		return new HashMap<>();
	}
	
	/**
	 * Creates the map object that has a key by phone number, and a contact ID as value.
	 */
	@NotNull
	private Map<String, Long> createMapNumberToId() {
		
		Map<String, Long> mapNumberToId = new HashMap<>();
		
		for (Map.Entry<String, List<Call>> entry : mapIdToCalls.entrySet()) {
			
			String number = entry.getKey();
			long   id     = CallKey.getContactId(entry.getValue().get(0));
			mapNumberToId.put(number, id);
		}
		
		return mapNumberToId;
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
		
		return History.of(contact, getCallsById(String.valueOf(contact.getContactId())));
	}
	
	public @NotNull List<Contact> getContactsByCalls(@NotNull Predicate<@Nullable List<Call>> predicate) {
		
		List<Contact> contacts = CallLogs.getContactsWithNumber();
		
		if (contacts == null) return new ArrayList<>();
		
		return contacts.stream()
				.filter(contact -> predicate.test(getCalls(contact)))
				.collect(Collectors.toList());
	}
	
	@Nullable
	public List<Call> getCalls(@NotNull Contact contact) {
		
		return mapIdToCalls.get(String.valueOf(contact.getContactId()));
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
	public Map<Integer, List<CallRank>> getMostIncoming() {
		
		return getMost(Call.INCOMING, Call.INCOMING_WIFI);
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
	public Map<Integer, List<CallRank>> getMostOutgoing() {
		
		return getMost(Call.OUTGOING, Call.OUTGOING_WIFI);
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
	public Map<Integer, List<CallRank>> getMostMissed() {
		
		return getMost(Call.MISSED);
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
	public Map<Integer, List<CallRank>> getMostRejected() {
		
		return getMost(Call.REJECTED);
	}
	
	/**
	 * Returns the object that mapped a key and its calls.
	 *
	 * @return the object that mapped phone number and its calls
	 */
	@NotNull
	public Map<String, List<Call>> getMapIdToCalls() {
		
		return mapIdToCalls;
	}
	
	/**
	 * @return all call log calls
	 */
	@NotNull
	public List<Call> getCalls() {
		
		return calls;
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
	
	/**
	 * @return {@code true} if the collection is empty
	 */
	public boolean isEmpty() {
		
		return calls.isEmpty();
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
		//noinspection DataFlowIssue
		return mapIdToCalls.getOrDefault(phoneNumber, new ArrayList<>(0));
	}
	
	/**
	 * Returns all calls with the given contact ID.
	 *
	 * @param id the contact ID
	 * @return calls
	 */
	public @NotNull List<Call> getCallsById(String id) {
		
		if (Stringx.isNoboe(id) || isEmpty()) return new ArrayList<>(0);
		//noinspection DataFlowIssue
		return mapIdToCalls.getOrDefault(id, new ArrayList<>(0));
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
	public Map<Integer, List<CallRank>> getMost(int @NotNull ... callTypes) {
		
		List<Call> calls = getCallsByType(callTypes);
		return createRankMap(CallLogs.mapIdToCalls(calls), QUANTITY_COMPARATOR);
	}
	
	/**
	 * Creates a new {@link CallLogs} object based on the given call type.
	 *
	 * @param callType the call type
	 * @return the new {@link CallLogs} object
	 */
	public @NotNull CallLogs createByCallType(int callType) {
		
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
	
	public @NotNull List<Contact> getContacts(@Nullable Boolean incoming, @Nullable Boolean outgoing, @Nullable Boolean missed, @Nullable Boolean rejected, int minSize) {
		
		List<Contact> contacts = getContactsWithNumber();
		
		if (contacts == null) return new ArrayList<>();
		
		List<Contact> _contacts = new ArrayList<>();
		
		for (Contact contact : contacts) {
			
			Predicate<List<Call>> ip = null;
			CallLogs              il = null;
			Predicate<List<Call>> op = null;
			CallLogs              ol = null;
			Predicate<List<Call>> mp = null;
			CallLogs              ml = null;
			Predicate<List<Call>> rp = null;
			CallLogs              rl = null;
			Boolean               ir = null;
			Boolean               or = null;
			Boolean               mr = null;
			Boolean               rr = null;
			
			if (incoming != null) {
				ip = incoming ? Objects::nonNull : Objects::isNull;
				ip = ip.and(c -> c.size() >= minSize);
				il = createFromType(Call.INCOMING);
			}
			if (outgoing != null) {
				op = outgoing ? Objects::nonNull : Objects::isNull;
				op = op.and(c -> c.size() >= minSize);
				ol = createFromType(Call.OUTGOING);
			}
			if (missed != null) {
				mp = missed ? Objects::nonNull : Objects::isNull;
				mp = mp.and(c -> c.size() >= minSize);
				ml = createFromType(Call.MISSED);
			}
			if (rejected != null) {
				rp = rejected ? Objects::nonNull : Objects::isNull;
				rp = rp.and(c -> c.size() >= minSize);
				rl = createFromType(Call.REJECTED);
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
	
	@NotNull
	public CallLogs createFrom(@NotNull Predicate<Call> predicate) {
		
		return create(getCalls(predicate));
	}
	
	@NotNull
	public CallLogs createFromType(int @NotNull ... callTypes) {
		
		List<Call> calls = new ArrayList<>();
		
		if (callTypes.length > 0)
			for (Call call : this.calls)
				if (Lister.contains(callTypes, call.getCallType())) calls.add(call);
		
		return create(calls);
	}
	
	public @NotNull CallLogs createFrom(List<Contact> contacts) {
		
		return null;
	}
	
	/**
	 * Creates a rank map for calls that related to this {@link CallLogs} object.
	 * Remember, a {@code CallLogs} object can be related to any list of {@link Call}.
	 *
	 * @return the rank map.
	 * 		The ranking starts 1, and advances one by one.
	 * 		The most valuable rank is 1.
	 */
	public @NotNull Map<Integer, List<CallRank>> makeRank() {
		
		return createRankMap(mapIdToCalls, QUANTITY_COMPARATOR);
	}
	
	public static @NotNull Map<Integer, List<CallRank>> makeRank(@NotNull CallLogs callLogs) {
		
		return callLogs.makeRank();
	}
	
	/**
	 * @return all contacts
	 */
	@Nullable
	public static List<Contact> getContacts() {
		
		return Blue.getObject(Key.CONTACTS);
	}
	
	/**
	 * Returns the contacts that have any real phone number.
	 *
	 * @return the contacts
	 */
	@Nullable
	public static List<Contact> getContactsWithNumber() {
		
		return getContacts(CallLogs::hasNumber);
	}
	
	public static @Nullable List<Contact> getContacts(@NotNull Predicate<Contact> predicate) {
		
		List<Contact> contacts = getContacts();
		
		if (contacts == null) return null;
		
		return contacts.stream()
				.filter(predicate)
				.collect(Collectors.toList());
	}
	
	/**
	 * Checks if the contact has any real phone number.
	 *
	 * @param contact the contact
	 * @return {@code true} if the contact has a number
	 */
	public static boolean hasNumber(@NotNull Contact contact) {
		
		List<String> numbers = ContactKey.getNumbers(contact);
		
		if (numbers == null || numbers.isEmpty()) return false;
		
		boolean hasNumber = false;
		for (String number : numbers) {
			
			if (PhoneNumbers.isPhoneNumber(number)) {
				
				hasNumber = true;
				break;
			}
		}
		
		return hasNumber;
	}
	
	/**
	 * Creates a new call collection.
	 * Also, stored on the blue cloud.
	 *
	 * @return the call collection
	 * @see Key#CALL_LOGS
	 */
	@NotNull
	public static CallLogs createOnTheCloud() {
		
		CallLogs collection = new CallLogs();
		
		Blue.box(Key.CALL_LOGS, collection);
		
		return collection;
	}
	
	/**
	 * Creates a new call collection.
	 *
	 * @return the call logs
	 */
	@NotNull
	public static CallLogs create() {
		
		return new CallLogs();
	}
	
	/**
	 * Creates a new call collection.
	 *
	 * @param calls the calls
	 * @return the call collection
	 */
	@NotNull
	public static CallLogs createOnTheCloud(List<Call> calls) {
		
		CallLogs collection = new CallLogs(calls);
		
		Blue.box(Key.CALL_LOGS, collection);
		
		return collection;
	}
	
	/**
	 * Creates a new call collection.
	 *
	 * @param calls the calls
	 * @return the call collection
	 */
	@NotNull
	public static CallLogs create(List<Call> calls) {
		
		return new CallLogs(calls);
	}
	
	/**
	 * Maybe there are more than one phone number belonging to the same contact.
	 * This method merges the calls belonging to the same contact.
	 *
	 * @param entries the map object that mapped the phone number to its calls.
	 */
	public static void mergeSameCalls(@NotNull Map<String, List<Call>> entries) {
		
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
	 * Groups the calls by {@link #getKey(Call)}.
	 *
	 * @param calls the calls
	 * @return the map of calls by key
	 */
	public static Map<String, List<Call>> mapIdToCalls(@NotNull List<Call> calls) {
		
		return calls.stream().collect(Collectors.groupingBy(CallLogs::getKey));
	}
	
	/**
	 * Groups the calls of the given call type by {@link #getKey(Call)}.
	 *
	 * @param calls    the calls
	 * @param callType the call type to group
	 * @return the groups of calls by key
	 */
	public static Map<String, List<Call>> mapIdToCalls(@NotNull List<Call> calls, int callType) {
		
		if (CallType.UNKNOWN == callType) return mapIdToCalls(calls);
		
		return calls.stream().filter(c -> c.getCallType() == callType).collect(Collectors.groupingBy(CallLogs::getKey));
	}
	
	/**
	 * Returns a key for the given call.
	 * Used as an identifier.
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
		List<Map.Entry<String, List<Call>>> rankList = makeRankList(entries, comparator);
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
	 * @param rankMap the rank map
	 * @param contact the contact
	 * @return the rank of the contact or –1 if not found
	 */
	public static int getRank(@NotNull Map<Integer, List<CallRank>> rankMap, @NotNull Contact contact) {
		
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
	 * Creates a ranked map from the calls.
	 *
	 * @param calls      the calls
	 * @param comparator the comparator
	 * @param callType   the call type to select
	 * @return the ranked map
	 */
	@NotNull
	public static Map<Integer, List<CallRank>> createRankMap(@NotNull List<Call> calls, Comparator<Map.Entry<String, List<Call>>> comparator, int callType) {
		
		Map<String, List<Call>> entries = mapIdToCalls(calls);
		
		Map<String, List<Call>> filtered = new HashMap<>();
		
		for (Map.Entry<String, List<Call>> entry : entries.entrySet()) {
			
			filtered.put(entry.getKey(), entry.getValue().stream().filter(c -> c.getCallType() == callType).collect(Collectors.toList()));
		}
		
		return createRankMap(filtered, comparator);
	}
	
	/**
	 * Creates a ranked map from the calls.
	 *
	 * @param calls    the calls
	 * @param callType the call type to select
	 * @return the ranked map
	 */
	@NotNull
	public static Map<Integer, List<CallRank>> createRankMap(@NotNull List<Call> calls, int callType) {
		
		Map<String, List<Call>> entries = mapIdToCalls(calls);
		
		Map<String, List<Call>> filtered = new HashMap<>();
		
		for (Map.Entry<String, List<Call>> entry : entries.entrySet()) {
			
			filtered.put(entry.getKey(), entry.getValue().stream().filter(c -> c.getCallType() == callType).collect(Collectors.toList()));
		}
		
		return createRankMap(filtered, QUANTITY_COMPARATOR);
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
	
	/**
	 * Finds the duration of the contact.
	 *
	 * @param durations the map of phone number to duration
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
	 * Returns a map object that ranked by call duration by descending.
	 *
	 * @param callLogs call collection
	 * @return a map object that ranked by calls duration by descending
	 */
	@NotNull
	public static Map<Integer, List<CallRank>> createRankMapByCallDuration(@NotNull CallLogs callLogs) {
		
		Map<String, List<Call>> entries   = callLogs.getMapIdToCalls();
		Set<String>             keys      = entries.keySet();
		List<CallRank>          callRanks = new ArrayList<>();
		
		//_ create call rank objects
		for (String key : keys) {
			
			List<Call> calls = entries.get(key);
			
			if (calls == null) continue;
			
			CallRank callRank = new CallRank(key, calls);
			
			long incomingDuration = 0L;
			long outgoingDuration = 0L;
			
			for (Call call : calls) {
				
				if (call.isIncoming()) incomingDuration += call.getDuration();
				else if (call.isOutgoing()) outgoingDuration += call.getDuration();
			}
			
			callRank.setIncomingDuration(incomingDuration);
			callRank.setOutgoingDuration(outgoingDuration);
			callRank.setContact(callLogs.getContact(key));
			callRanks.add(callRank);
		}
		
		//_ sort by duration descending
		callRanks.sort((c1, c2) -> Long.compare(c2.getDuration(), c1.getDuration()));
		
		int                              rank    = 1;
		int                              size    = callRanks.size();
		int                              last    = size - 1;
		HashMap<Integer, List<CallRank>> rankMap = new HashMap<>();
		
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
	 * Returns a map object that ranked by call duration by descending.
	 *
	 * @param calls the calls
	 * @return a map object that ranked by calls duration by descending
	 */
	@NotNull
	public static Map<Integer, List<CallRank>> createRankMapByCallDuration(@NotNull List<Call> calls) {
		
		return createRankMapByCallDuration(create(calls));
	}
	
	@NotNull
	public static Map<Integer, List<CallRank>> createRankMap(@NotNull List<Call> calls) {
		
		return create(calls).makeRank();
	}
	
	/**
	 * Return the {@link CallRank} of the contact.
	 *
	 * @param rankMap the rank map
	 * @param rank    the rank of the contact
	 * @param contact the contact
	 * @return the {@link CallRank} or {@code null} if not found
	 */
	@Nullable
	public static CallRank getCallRank(@NotNull Map<Integer, List<CallRank>> rankMap, int rank, Contact contact) {
		
		if (contact == null || rank < 1) return null;
		
		List<CallRank> ranks = rankMap.get(rank);
		
		if (ranks == null) return null;
		
		for (CallRank callRank : ranks)
			if (callRank.getKey().equals(String.valueOf(contact.getContactId())))
				return callRank;
		
		return null;
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
	 * Creates a ranked list by quantity.
	 *
	 * @param calls the calls
	 * @return the ranked list
	 */
	public static List<CallRank> createRankList(@NotNull List<Call> calls) {
		
		return CallLogs.createRankMap(calls).values()
				.stream()
				.flatMap(Collection::stream)
				.sorted(Comparator.comparingInt(CallRank::getRank))
				.collect(Collectors.toList());
	}
	
	/**
	 * Creates a ranked list by duration.
	 *
	 * @param calls the calls
	 * @return the ranked list
	 */
	public static List<CallRank> createRankListByDuration(@NotNull List<Call> calls) {
		
		return CallLogs.createRankMapByCallDuration(calls).values()
				.stream().
				flatMap(Collection::stream)
				.collect(Collectors.toList());
	}
}
