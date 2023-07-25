package com.tr.hsyn.telefonrehberi.main.call.data;


import androidx.annotation.NonNull;

import com.tr.hsyn.calldata.Call;
import com.tr.hsyn.calldata.CallType;
import com.tr.hsyn.collection.Lister;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;


/**
 * Defines a collection of {@link Call} objects.
 */
public interface CCollection {
	
	/**
	 * Returns the calls.
	 *
	 * @return the calls
	 */
	@NotNull List<Call> getCalls();
	
	/**
	 * Returns all calls that match the given predicate.
	 *
	 * @param predicate the predicate
	 * @return calls
	 */
	default @NotNull List<Call> getCalls(@NotNull Predicate<Call> predicate) {
		
		return getCalls().stream().filter(predicate).collect(Collectors.toList());
	}
	
	/**
	 * Returns the size of the call logs.
	 *
	 * @return the size of the call logs
	 */
	default int size() {
		
		return getCalls().size();
	}
	
	/**
	 * @return {@code true} if the call log is empty.
	 */
	default boolean isEmpty() {
		
		return getCalls().isEmpty();
	}
	
	/**
	 * @return {@code true} if the call log is not empty
	 */
	default boolean isNotEmpty() {
		
		return !isEmpty();
	}
	
	/**
	 * Returns all calls with the given call types.
	 *
	 * @param type call type
	 * @return calls
	 */
	default @NotNull List<Call> getByType(@CallType int type) {
		
		return getCalls().stream().filter(c -> c.isType(type)).collect(Collectors.toList());
	}
	
	/**
	 * Returns all calls with the given call types.
	 *
	 * @param type call types
	 * @return calls
	 */
	default @NotNull List<Call> getByType(@CallType int @NotNull ... type) {
		
		if (type.length == 0) return getCalls();
		
		return getCalls().stream().filter(c -> Lister.contains(type, c.getCallType())).collect(Collectors.toList());
	}
	
	/**
	 * @return incoming calls
	 */
	@NonNull
	default List<Call> incomingCalls() {
		
		return getByType(Call.INCOMING, Call.INCOMING_WIFI);
	}
	
	/**
	 * @return outgoing calls
	 */
	@NotNull
	default List<Call> outgoingCalls() {
		
		return getByType(Call.OUTGOING, Call.OUTGOING_WIFI);
	}
	
	/**
	 * @return missed calls
	 */
	@NotNull
	default List<Call> missedCalls() {
		
		return getByType(Call.MISSED);
	}
	
	/**
	 * @return rejected calls
	 */
	@NotNull
	default List<Call> rejectedCalls() {
		
		return getByType(Call.REJECTED);
	}
	
	
}
