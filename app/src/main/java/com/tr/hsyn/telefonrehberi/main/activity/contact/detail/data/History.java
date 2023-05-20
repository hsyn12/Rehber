package com.tr.hsyn.telefonrehberi.main.activity.contact.detail.data;


import com.tr.hsyn.calldata.Call;
import com.tr.hsyn.collection.Lister;
import com.tr.hsyn.contactdata.Contact;
import com.tr.hsyn.telefonrehberi.code.android.Res;
import com.tr.hsyn.telefonrehberi.main.code.contact.act.ContactKey;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;


/**
 * Describes the history of a contact and provides some methods to manage it.
 * History means the call history between a contact and the user.
 * And this class makes it easy to manage it.
 */
public interface History {
	
	/**
	 * Creates a new history for the given contact.
	 *
	 * @param contact the contact to be used by this history
	 * @return the history for the given contact
	 */
	@NotNull
	static History of(@NotNull Contact contact, @NotNull List<Call> calls) {
		
		var h = new ContactCallHistory(contact, calls);
		
		contact.setData(ContactKey.HISTORY, h);
		
		return h;
	}
	
	/**
	 * Creates a new empty history for the given contact.
	 *
	 * @param contact the contact to be used by this history
	 * @return the history for the given contact
	 */
	@NotNull
	static History ofEmpty(@NotNull Contact contact) {
		
		return new ContactCallHistory(contact, new ArrayList<>(0));
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
	 * Returns all calls that between the contact and the user.
	 *
	 * @return the calls
	 */
	@NotNull List<Call> getCalls();
	
	/**
	 * Returns the contact that this history belongs to.
	 *
	 * @return the contact
	 */
	@NotNull Contact getContact();
	
	/**
	 * Returns the oldest call.
	 *
	 * @return the first call
	 */
	default Call getFirstCall() {
		
		return getCalls().get(size() - 1);
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
	 * Returns whether the call history is empty.
	 *
	 * @return {@code true} if the call history is empty
	 */
	default boolean isEmpty() {
		
		return getCalls().isEmpty();
	}
	
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
	
	/**
	 * Returns the calls of the given call types.
	 *
	 * @param types the call types
	 * @return the calls
	 */
	default @NotNull List<Call> getCallsByTypes(int... types) {
		
		return getCallsByTypes(getCalls(), types);
	}
	
	/**
	 * Returns the calls of the given call types from the given list of calls
	 *
	 * @param calls the calls
	 * @param types the call types
	 * @return the calls
	 */
	static @NotNull List<Call> getCallsByTypes(List<Call> calls, int... types) {
		
		List<Call> _calls = new ArrayList<>();
		
		Lister.loop(types, type -> {
			
			var list = calls.stream().filter(call -> call.getCallType() == type).collect(Collectors.toList());
			
			_calls.addAll(list);
		});
		
		return _calls;
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
}
