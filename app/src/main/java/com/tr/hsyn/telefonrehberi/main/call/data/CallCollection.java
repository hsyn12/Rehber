package com.tr.hsyn.telefonrehberi.main.call.data;


import androidx.annotation.NonNull;

import com.tr.hsyn.calldata.Call;
import com.tr.hsyn.collection.CoupleMap;
import com.tr.hsyn.contactdata.Contact;
import com.tr.hsyn.keep.Keep;
import com.tr.hsyn.key.Key;
import com.tr.hsyn.phone_numbers.PhoneNumbers;
import com.tr.hsyn.string.Stringx;
import com.tr.hsyn.telefonrehberi.main.contact.data.ContactKey;
import com.tr.hsyn.telefonrehberi.main.contact.data.History;
import com.tr.hsyn.telefonrehberi.main.dev.Over;
import com.tr.hsyn.xbox.Blue;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;


/**
 * Collection of the call logs.
 */
@Keep
public final class CallCollection {
	
	/**
	 * All call log calls
	 */
	@NotNull private final List<Call>               calls;
	/**
	 * Map object that has a key by phone number, and a value as a list of its calls
	 * that belong to the phone number.
	 */
	@NotNull private final Map<String, List<Call>>  mapNumberToCalls;
	private                CoupleMap<Long, Contact> mapIdToContact;
	private                CoupleMap<String, Long>  mapNumberToId;
	
	/**
	 * Creates a new empty call collection.
	 */
	private CallCollection() {
		
		List<Call> c = Over.CallLog.Calls.getCalls();
		this.calls       = c != null ? c : new ArrayList<>(0);
		mapNumberToCalls = mapNumberToCalls(this.calls);
		mapNumberToId    = new CoupleMap<>(createMapNumberToId());
		mapIdToContact   = new CoupleMap<>(createMapIdToContact());
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
	 * Returns the map object that has a key by phone number, and a contact ID as value.
	 *
	 * @return map
	 */
	public CoupleMap<String, Long> getMapNumberToId() {
		
		return mapNumberToId;
	}
	
	public Contact getContact(@NotNull String number) {
		
		return mapIdToContact.getFromKey(mapNumberToId.getFromKey(number));
	}
	
	@Nullable
	public Long getContactId(@NotNull String number) {
		
		return mapNumberToId.getFromKey(number);
	}
	
	public String getNumber(long id) {
		
		return mapNumberToId.getFromValue(id);
	}
	
	/**
	 * Creates the map object that has a key by contact ID, and a contact as value.
	 */
	private Map<Long, Contact> createMapIdToContact() {
		
		List<Contact> contacts = Blue.getObject(Key.CONTACTS);
		
		if (contacts != null) {
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
		
		for (var entry : mapNumberToCalls.entrySet()) {
			
			String number = entry.getKey();
			long   id     = CallKey.getContactId(entry.getValue().get(0));
			mapNumberToId.put(number, id);
		}
		
		return mapNumberToId;
	}
	
	@NotNull
	public History getHistoryOf(@NotNull Contact contact) {
		
		return History.of(contact, getCallsByNumbers(ContactKey.getNumbers(contact)));
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
	@NonNull
	public List<Call> getOutgoingCalls() {
		
		return getCallsByType(Call.OUTGOING, Call.OUTGOING_WIFI);
	}
	
	/**
	 * Returns the object that mapped phone number and its calls.
	 *
	 * @return the object that mapped phone number and its calls
	 */
	@NotNull
	public Map<String, List<Call>> getMapNumberToCalls() {
		
		return mapNumberToCalls;
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
		return mapNumberToCalls.getOrDefault(phoneNumber, new ArrayList<>(0));
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
	 * Maps the phone number to the calls.
	 *
	 * @param calls the calls
	 * @return the map
	 */
	public static Map<String, List<Call>> mapNumberToCalls(@NotNull List<Call> calls) {
		
		return calls.stream().collect(Collectors.groupingBy(CallCollection::getKey));
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
	
	/**
	 * Creates a new call collection.
	 * Also, stored on the blue cloud.
	 *
	 * @return the call collection
	 * @see Key#CALL_COLLECTION
	 */
	@NotNull
	public static CallCollection create() {
		
		CallCollection collection = new CallCollection();
		
		Blue.box(Key.CALL_COLLECTION, collection);
		
		return collection;
	}
}
