package com.tr.hsyn.telefonrehberi.main.call.data;


import androidx.annotation.NonNull;

import com.tr.hsyn.calldata.Call;
import com.tr.hsyn.contactdata.Contact;
import com.tr.hsyn.keep.Keep;
import com.tr.hsyn.key.Key;
import com.tr.hsyn.phone_numbers.PhoneNumbers;
import com.tr.hsyn.string.Stringx;
import com.tr.hsyn.telefonrehberi.main.call.data.hotlist.HotListByQuantity;
import com.tr.hsyn.telefonrehberi.main.contact.data.ContactKey;
import com.tr.hsyn.telefonrehberi.main.contact.data.History;
import com.tr.hsyn.xbox.Blue;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
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
	private final List<Call>              calls;
	/**
	 * Map object that has a key by phone number, and a value as a list of its calls
	 * that belong to the phone number.
	 */
	private final Map<String, List<Call>> numberedCalls;
	private final List<Call>              incomingCalls;
	private final List<Call>              outgoingCalls;
	private final List<Call>              missedCalls;
	private final List<Call>              rejectedCalls;
	private final int                     incomingDuration;
	private final int                     outgoingDuration;
	private final HotListByQuantity       hotListByQuantity;
	
	/**
	 * Creates a new empty call collection.
	 */
	private CallCollection() {
		
		this.calls        = null;
		numberedCalls     = null;
		incomingCalls     = null;
		outgoingCalls     = null;
		missedCalls       = null;
		rejectedCalls     = null;
		incomingDuration  = 0;
		outgoingDuration  = 0;
		hotListByQuantity = null;
	}
	
	/**
	 * Creates a new call collection.
	 *
	 * @param calls the calls
	 */
	private CallCollection(@NotNull List<Call> calls) {
		
		this.calls    = calls;
		numberedCalls = makeNumberedCalls(calls);
		incomingCalls = getCallsByType(Call.INCOMING, Call.INCOMING_WIFI);
		outgoingCalls = getCallsByType(Call.OUTGOING, Call.OUTGOING_WIFI);
		missedCalls   = getCallsByType(Call.MISSED);
		rejectedCalls = getCallsByType(Call.REJECTED);
		
		incomingDuration = getIncomingCalls().stream()
				.map(Call::getDuration)
				.reduce(Integer::sum)
				.orElse(0);
		
		outgoingDuration = getOutgoingCalls().stream()
				.map(Call::getDuration)
				.reduce(Integer::sum)
				.orElse(0);
		
		hotListByQuantity = new HotListByQuantity(this);
	}
	
	public HotListByQuantity getHotListByQuantity() {
		
		return hotListByQuantity;
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
		
		return incomingCalls;
	}
	
	/**
	 * @return outgoing calls
	 */
	@NonNull
	public List<Call> getOutgoingCalls() {
		
		return outgoingCalls;
	}
	
	/**
	 * Returns the object that mapped phone number and its calls.
	 *
	 * @return the object that mapped phone number and its calls
	 */
	public Map<String, List<Call>> getNumberedCalls() {
		
		return numberedCalls;
	}
	
	/**
	 * @return all call log calls
	 */
	public List<Call> getCalls() {
		
		return calls;
	}
	
	/**
	 * @return total speaking duration of all incoming calls in seconds
	 */
	public int getIncomingDuration() {
		
		return incomingDuration;
	}
	
	/**
	 * @return total speaking duration of all outgoing calls in seconds
	 */
	public int getOutgoingDuration() {
		
		return outgoingDuration;
	}
	
	/**
	 * @return missed calls
	 */
	@NonNull
	public List<Call> getMissedCalls() {
		
		return missedCalls;
	}
	
	/**
	 * @return rejected calls
	 */
	@NonNull
	public List<Call> getRejectedCalls() {
		
		return rejectedCalls;
	}
	
	/**
	 * @param callType call type
	 * @return call size for the given call type
	 */
	public int getCallSize(int callType) {
		
		switch (callType) {
			case Call.INCOMING:
			case Call.INCOMING_WIFI: return incomingCalls.size();
			case Call.OUTGOING:
			case Call.OUTGOING_WIFI: return outgoingCalls.size();
			case Call.MISSED: return missedCalls.size();
			case Call.REJECTED: return rejectedCalls.size();
			default: return getCallsByType(callType).size();
		}
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
	
	public List<Call> getCallsByNumbers(List<String> numbers, List<Call> callList) {
		
		List<Call> calls = new ArrayList<>();
		
		if (numbers == null || isEmpty()) return calls;
		
		for (String number : numbers) calls.addAll(getCallsByNumber(number, callList));
		
		return calls;
	}
	
	/**
	 * @return {@code true} if the collection is empty or <code>null</code>
	 */
	public boolean isEmpty() {
		
		return calls == null || calls.isEmpty();
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
		return numberedCalls.getOrDefault(phoneNumber, new ArrayList<>(0));
	}
	
	public @NotNull List<Call> getCallsByNumber(String phoneNumber, List<Call> callList) {
		
		if (Stringx.isNoboe(phoneNumber) || isEmpty()) return new ArrayList<>(0);
		
		@NotNull String _phoneNumber = PhoneNumbers.formatNumber(phoneNumber, 10);
		return callList.stream().filter(c -> c.getNumber().equals(_phoneNumber)).collect(Collectors.toList());
	}
	
	public static Map<String, List<Call>> makeNumberedCalls(@NotNull List<Call> calls) {
		
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
	 * @param calls the calls
	 * @return the call collection
	 * @see Key#CALL_COLLECTION
	 */
	@NotNull
	public static CallCollection create(@NotNull List<Call> calls) {
		
		CallCollection collection = new CallCollection(calls);
		
		Blue.box(Key.CALL_COLLECTION, collection);
		
		return collection;
	}
	
	/**
	 * Creates a new empty call collection.
	 *
	 * @return the call collection
	 */
	@NotNull
	public static CallCollection createEmpty() {
		
		CallCollection collection = new CallCollection();
		
		Blue.box(Key.CALL_COLLECTION, collection);
		
		return collection;
	}
}
