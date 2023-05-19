package com.tr.hsyn.telefonrehberi.main.activity.contact.detail.data;


import androidx.annotation.NonNull;

import com.tr.hsyn.calldata.Call;
import com.tr.hsyn.collection.Lister;
import com.tr.hsyn.phone_numbers.PhoneNumbers;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;


/**
 * Manages the call logs.
 */
public final class CallCollection {
	
	private final List<Call>              calls;
	private final Map<String, List<Call>> numberedCalls;
	private final List<Call>              incomingCalls;
	private final List<Call>              outgoingCalls;
	private final List<Call>              missedCalls;
	private final List<Call>              rejectedCalls;
	private final int                     incomingDuration;
	private final int                     outgoingDuration;
	
	/**
	 * Creates a new call collection.
	 *
	 * @param calls the calls
	 * @return the call collection
	 */
	@NotNull
	public static CallCollection create(@NotNull List<Call> calls) {
		
		return new CallCollection(calls);
	}
	
	/**
	 * Creates a new call collection.
	 *
	 * @param calls the calls
	 */
	private CallCollection(@NotNull List<Call> calls) {
		
		this.calls    = calls;
		numberedCalls = calls.stream().collect(Collectors.groupingBy(call -> PhoneNumbers.formatNumber(call.getNumber(), 10)));
		incomingCalls = getCalls(Call.INCOMING, Call.INCOMING_WIFI);
		outgoingCalls = getCalls(Call.OUTGOING, Call.OUTGOING_WIFI);
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
	 * Returns all calls with the given call types.
	 *
	 * @param callTypes call types
	 * @return calls
	 */
	@NonNull
	public List<Call> getCalls(int... callTypes) {
		
		List<Call> _calls = new ArrayList<>();
		
		Lister.loop(callTypes, type -> _calls.addAll(getCalls(type)));
		
		return _calls;
	}
	
	/**
	 * Returns all calls with the given number.
	 *
	 * @param phoneNumber the number
	 * @return calls
	 */
	public @NotNull List<Call> getCallsByNumber(@NotNull String phoneNumber) {
		
		phoneNumber = PhoneNumbers.formatNumber(phoneNumber, 10);
		//noinspection DataFlowIssue
		return numberedCalls.getOrDefault(phoneNumber, new ArrayList<>(0));
		
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
	
}
