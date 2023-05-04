package com.tr.hsyn.telefonrehberi.main.activity.contact.detail.data;


import com.tr.hsyn.calldata.Call;
import com.tr.hsyn.collection.Lister;
import com.tr.hsyn.contactdata.Contact;
import com.tr.hsyn.datakey.DataKey;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;


/**
 * Describes the history of a contact and provides some helper methods.
 */
public interface History {
	
	/**
	 * Data key for the call history of a contact
	 */
	DataKey CALL_HISTORY_KEY = DataKey.of(3434, "call_history");
	
	/**
	 * Creates a new history of a contact
	 *
	 * @param contact the contact
	 * @return the history
	 */
	@NotNull
	static History of(@NotNull Contact contact) {
		
		return new ContactCallHistory(contact);
	}
	
	/**
	 * Returns the most recent call
	 *
	 * @return the last call
	 */
	default Call getLastCall() {
		
		return getHistory().get(0);
	}
	
	/**
	 * Returns all calls of the contact
	 *
	 * @return the calls
	 */
	default List<Call> getHistory() {
		
		return getContact().getData(CALL_HISTORY_KEY);
	}
	
	/**
	 * Returns the contact
	 *
	 * @return the contact
	 */
	@NotNull Contact getContact();
	
	/**
	 * Returns the oldest call
	 *
	 * @return the first call
	 */
	default Call getFirstCall() {
		
		return getHistory().get(size() - 1);
	}
	
	/**
	 * Returns the size of the history
	 *
	 * @return the size
	 */
	default int size() {
		
		return getHistory().size();
	}
	
	/**
	 * Returns true if the history is empty
	 *
	 * @return true if the history is empty
	 */
	default boolean isEmpty() {
		
		return getHistory().isEmpty();
	}
	
	/**
	 * Returns the call at the given index
	 *
	 * @param index the index
	 * @return the call
	 */
	default Call get(int index) {
		
		return getHistory().get(index);
	}
	
	/**
	 * Sorts the history
	 *
	 * @param comparator the comparator
	 */
	default void sort(Comparator<Call> comparator) {
		
		getHistory().sort(comparator);
	}
	
	/**
	 * Returns the total call duration of the given call types.
	 *
	 * @param types the call types
	 * @return the duration
	 */
	default int getDuration(int... types) {
		
		return getCalls(types).stream().mapToInt(Call::getDuration).sum();
	}
	
	/**
	 * Returns the calls of the given call types.
	 *
	 * @param types the call types
	 * @return the calls
	 */
	default @NotNull List<Call> getCalls(int... types) {
		
		return getCalls(getHistory(), types);
	}
	
	/**
	 * Returns the calls of the given call types from the given list of calls
	 *
	 * @param calls the calls
	 * @param types the call types
	 * @return the calls
	 */
	static @NotNull List<Call> getCalls(List<Call> calls, int... types) {
		
		List<Call> _calls = new ArrayList<>();
		
		Lister.loop(types, type -> {
			
			var list = calls.stream().filter(call -> call.getType() == type).collect(Collectors.toList());
			
			_calls.addAll(list);
		});
		
		return _calls;
	}
}
