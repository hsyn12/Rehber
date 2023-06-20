package com.tr.hsyn.telefonrehberi.main.contact.data;


import com.tr.hsyn.calldata.Call;
import com.tr.hsyn.collection.Lister;
import com.tr.hsyn.contactdata.Contact;
import com.tr.hsyn.key.Key;
import com.tr.hsyn.telefonrehberi.main.call.data.CallCollection;
import com.tr.hsyn.telefonrehberi.main.call.data.Res;
import com.tr.hsyn.time.DurationGroup;
import com.tr.hsyn.time.Time;
import com.tr.hsyn.xbox.Blue;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;


/**
 * Describes the history of a contact and provides some methods to manage it.
 * History means the call history between a contact and the user.
 */
public interface History {
	
	/**
	 * Returns the contact that this history belongs to.
	 *
	 * @return the contact
	 */
	@NotNull Contact getContact();
	
	/**
	 * Returns the call at the given index
	 *
	 * @param index the index
	 * @return the call
	 */
	default Call get(int index) {
		
		return getCalls().get(index);
	}
	
	/**
	 * Returns all calls that between the contact and the user.
	 *
	 * @return the calls
	 */
	@NotNull List<Call> getCalls();
	
	/**
	 * Returns the duration of the call history.
	 *
	 * @return the {@link DurationGroup}
	 */
	default DurationGroup getHistoryDuration() {
		
		if (size() > 1) {
			
			Call firstCall     = getFirstCall();
			Call lastCall      = getLastCall();
			long estimatedTime = lastCall.getTime() - firstCall.getTime();
			return Time.toDuration(estimatedTime);
		}
		
		return DurationGroup.EMPTY;
	}
	
	/**
	 * Returns the size of the call history of the contact.
	 *
	 * @return the size of the call history
	 */
	default int size() {
		
		return getCalls().size();
	}
	
	/**
	 * Returns the oldest call.
	 *
	 * @return the first call
	 */
	default Call getFirstCall() {
		
		return getCalls().get(size() - 1);
	}
	
	/**
	 * Returns the most recent call.
	 *
	 * @return the most recent call
	 */
	default Call getLastCall() {
		
		return getCalls().get(0);
	}
	
	/**
	 * Sorts the history.
	 *
	 * @param comparator the comparator
	 */
	default void sort(Comparator<Call> comparator) {
		
		getCalls().sort(comparator);
	}
	
	/**
	 * Returns the total call duration of the given call types.
	 *
	 * @param types the call types
	 * @return the duration
	 */
	default int getDuration(int... types) {
		
		return getCallsByTypes(types).stream().mapToInt(Call::getDuration).sum();
	}
	
	default List<Call> getIncomingCalls() {
		
		return getCallsByTypes(Call.INCOMING, Call.INCOMING_WIFI);
	}
	
	default List<Call> getOutgoingCalls() {
		
		return getCallsByTypes(Call.OUTGOING, Call.OUTGOING_WIFI);
	}
	
	default List<Call> getMediaCalls() {
		
		return getCallsByTypes(Call.MISSED);
	}
	
	default List<Call> getRejectCalls() {
		
		return getCallsByTypes(Call.REJECTED);
	}
	
	/**
	 * Returns the calls of the given call types.
	 *
	 * @param types the call types
	 * @return the calls
	 */
	default @NotNull List<Call> getCallsByTypes(int @NotNull ... types) {
		
		return getCalls().stream().filter(c -> Lister.IntArray.contains(types, c.getCallType())).collect(Collectors.toList());
	}
	
	/**
	 * Returns all calls that match the given predicate.
	 *
	 * @param predicate the predicate
	 * @return the calls
	 */
	default @NotNull List<Call> getCalls(@NotNull Predicate<Call> predicate) {
		
		return getCalls().stream().filter(predicate).collect(Collectors.toList());
	}
	
	/**
	 * Returns the size of the given call type.
	 *
	 * @param callType the call type
	 * @return the size
	 */
	default int size(int callType) {
		
		var types = Res.getCallTypes(callType);
		
		if (types.length == 1) {
			
			return (int) getCalls().stream().filter(call -> call.getCallType() == callType).count();
		}
		
		return (int) getCalls().stream().filter(call -> call.getCallType() == callType || call.getCallType() == types[1]).count();
	}
	
	/**
	 * Returns whether the call history is empty.
	 *
	 * @return {@code true} if the call history is empty
	 */
	default boolean isEmpty() {
		
		return getCalls().isEmpty();
	}
	
	/**
	 * Returns the history of the given contact.
	 *
	 * @param contact the contact
	 * @return the history
	 * @deprecated use {@link CallCollection#getHistoryOf(Contact)}
	 */
	@Deprecated
	static History getHistory(@NotNull Contact contact) {
		
		CallCollection collection = Blue.getObject(Key.CALL_COLLECTION);
		
		if (collection == null) return ofEmpty(contact);
		
		var numbers = ContactKey.getNumbers(contact);
		
		if (numbers == null || numbers.isEmpty()) return ofEmpty(contact);
		
		var calls = collection.getCallsByNumbers(numbers);
		
		return of(contact, calls);
	}
	
	/**
	 * Creates a new empty history for the given contact.
	 *
	 * @param contact the contact used by this history
	 * @return the history for the given contact
	 */
	@NotNull
	static History ofEmpty(@NotNull Contact contact) {
		
		return new ContactCallHistory(contact, new ArrayList<>(0));
	}
	
	/**
	 * Creates a new history for the given key.
	 *
	 * @param key the key used by this history
	 * @return the history for the given contact
	 */
	@NotNull
	static History of(@NotNull String key, @NotNull List<Call> calls) {
		
		return new ContactCallHistory(key, calls);
	}
	
	/**
	 * Creates a new history for the given contact.
	 *
	 * @param contact the contact used by this history
	 * @return the history for the given contact
	 */
	static @NotNull History of(@NotNull Contact contact, @NotNull List<Call> calls) {
		
		return new ContactCallHistory(contact, calls);
	}
	
	/**
	 * Returns the calls of the given call types from the given list of calls.
	 *
	 * @param calls the calls
	 * @param types the call types
	 * @return the calls
	 */
	static List<Call> getCallsByTypes(@NotNull List<Call> calls, int @NotNull ... types) {
		
		List<Call> _calls = new ArrayList<>();
		
		for (int type : types) {
			
			for (Call call : calls) {
				
				if (type == call.getCallType() && !_calls.contains(call)) {
					
					_calls.add(call);
				}
			}
		}
		
		return _calls;
	}
}
