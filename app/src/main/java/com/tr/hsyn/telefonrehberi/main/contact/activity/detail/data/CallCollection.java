package com.tr.hsyn.telefonrehberi.main.contact.activity.detail.data;


import androidx.annotation.NonNull;

import com.tr.hsyn.calldata.Call;
import com.tr.hsyn.collection.Lister;
import com.tr.hsyn.key.Key;
import com.tr.hsyn.phone_numbers.PhoneNumbers;
import com.tr.hsyn.string.Stringx;
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
public final class CallCollection {
	
	private final List<Call>              calls;
	/**
	 * Map object that has a key by phone number and a value as a list of its calls
	 * that belong to the phone number.
	 */
	private final Map<String, List<Call>> numberedCalls;
	private final List<Call>              incomingCalls;
	private final List<Call>              outgoingCalls;
	private final List<Call>              missedCalls;
	private final List<Call>              rejectedCalls;
	private final int                     incomingDuration;
	private final int                     outgoingDuration;
	
	/**
	 * Creates a new call collection.
	 * Also, it is stored on the blue cloud.
	 *
	 * @param calls the calls
	 * @return the call collection
	 * @see Key#CALL_COLLECTION
	 */
	@NotNull
	public static CallCollection create(@NotNull List<Call> calls) {
		
		var collection = new CallCollection(calls);
		
		Blue.box(Key.CALL_COLLECTION, collection);
		
		return collection;
	}
	
	@NotNull
	public static CallCollection createEmpty() {
		
		var collection = new CallCollection();
		
		Blue.box(Key.CALL_COLLECTION, collection);
		
		return collection;
	}
	
	/**
	 * Creates a new empty call collection.
	 */
	private CallCollection() {
		
		this.calls       = null;
		numberedCalls    = null;
		incomingCalls    = null;
		outgoingCalls    = null;
		missedCalls      = null;
		rejectedCalls    = null;
		incomingDuration = 0;
		outgoingDuration = 0;
	}
	
	private CallCollection(@NotNull List<Call> calls) {
		
		this.calls    = calls;
		numberedCalls = calls.stream().collect(Collectors.groupingBy(this::getKey));
		incomingCalls = getCallsByTypes(Call.INCOMING, Call.INCOMING_WIFI);
		outgoingCalls = getCallsByTypes(Call.OUTGOING, Call.OUTGOING_WIFI);
		missedCalls   = getCalls(Call.MISSED);
		rejectedCalls = getCalls(Call.REJECTED);
		
		incomingDuration = getIncomingCalls().stream()
				.map(Call::getDuration)
				.reduce(Integer::sum)
				.orElse(0);
		
		outgoingDuration = getOutgoingCalls().stream()
				.map(Call::getDuration)
				.reduce(Integer::sum)
				.orElse(0);
	}
	
	/**
	 * Return the key for the given call.
	 *
	 * @param call the call
	 * @return the key
	 */
	@NotNull
	private String getKey(@NotNull Call call) {
		
		/* var id = CallKey.getContactId(call);
		
		if (id != 0L) return String.valueOf(id); */
		
		return PhoneNumbers.formatNumber(call.getNumber(), PhoneNumbers.N_MIN);
	}
	
	/**
	 * Returns all calls with the given call types.
	 *
	 * @param callTypes call types
	 * @return calls
	 */
	@NonNull
	public List<Call> getCallsByTypes(int... callTypes) {
		
		List<Call> _calls = new ArrayList<>();
		
		Lister.loop(callTypes, type -> _calls.addAll(getCalls(type)));
		
		return _calls;
	}
	
	/**
	 * Returns the object that mapped phone number against its calls.
	 *
	 * @return the object that mapped phone number against its calls
	 */
	public Map<String, List<Call>> getNumberedCalls() {
		
		return numberedCalls;
	}
	
	public List<Call> getCalls() {
		
		return calls;
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
	 * @return total speaking duration of all incoming calls (seconds)
	 */
	public int getIncomingDuration() {
		
		return incomingDuration;
	}
	
	/**
	 * @return total speaking duration of all outgoing calls (seconds)
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
			case Call.INCOMING_WIFI:
				return incomingCalls.size();
			case Call.OUTGOING:
			case Call.OUTGOING_WIFI:
				return outgoingCalls.size();
			case Call.MISSED:
				return missedCalls.size();
			case Call.REJECTED:
				return rejectedCalls.size();
			default: return getCalls(callType).size();
		}
	}
	
	/**
	 * Returns all calls with the given call type.
	 *
	 * @param callType call type
	 * @return calls
	 */
	@NotNull
	private List<Call> getCalls(int callType) {
		
		Predicate<Call> typePredicate = call -> call.getCallType() == callType;
		return getCalls(typePredicate);
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
		
		for (var number : numbers) calls.addAll(getCallsByNumber(number));
		
		return calls;
		
		//return numberedCalls.getOrDefault(PhoneNumbers.formatNumber(number, 10), new ArrayList<>(0));
	}
	
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
}
