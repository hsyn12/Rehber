package com.tr.hsyn.telefonrehberi.main.activity.contact.detail.comment;


import com.tr.hsyn.calldata.Call;
import com.tr.hsyn.contactdata.Contact;
import com.tr.hsyn.key.Key;
import com.tr.hsyn.telefonrehberi.main.code.comment.Commentator;
import com.tr.hsyn.telefonrehberi.main.code.comment.ContactCommentStore;
import com.tr.hsyn.telefonrehberi.main.dev.Over;
import com.tr.hsyn.xbox.Blue;

import org.jetbrains.annotations.NotNull;

import java.util.List;


/**
 * The ContactCommentator interface defines the contract for commenting on a phone contact.
 * <p>
 * Implementations of this interface should provide specific functionality for commenting on different contacts.
 * <p>
 * This interface extends the Commentator<Contact> interface,
 * which defines the {@link Commentator#commentOn(Object)} method
 * for generating a comment on a contact.
 */
public interface ContactCommentator extends Commentator<Contact> {
	
	/**
	 * Returns a list of all contacts.
	 *
	 * @return a list of all contacts
	 */
	default List<Contact> getContacts() {
		
		return Over.Contacts.getContacts();
	}
	
	/**
	 * Returns a list of all calls.
	 *
	 * @return a list of all calls
	 */
	default List<Call> getCalls() {
		
		return Over.CallLog.Calls.getCalls();
	}
	
	/**
	 * Returns a list of all calls for the selected contact.
	 *
	 * @return a list of all calls the selected contact
	 */
	default List<Call> getHistory() {
		
		return Blue.getObject(Key.CALL_HISTORY);
	}
	
	/**
	 * Returns the contact for which the comment is being generated.
	 *
	 * @return current selected contact
	 */
	Contact getContact();
	
	/**
	 * Returns the {@link ContactCommentStore} instance associated with this {@link ContactCommentator}.
	 *
	 * @return the {@link ContactCommentStore} instance associated with this {@link ContactCommentator}
	 */
	ContactCommentStore getCommentStore();
	
	/**
	 * Returns the color associated with the given resource ID.
	 *
	 * @param id the resource ID of the color
	 * @return the color associated with the given resource ID
	 */
	default int getColor(int id) {
		
		return getCommentStore().getActivity().getColor(id);
	}
	
	/**
	 * This method is responsible for commenting on a contact.
	 * <p>
	 * Implementations of this method should provide specific functionality
	 * for commenting on different contacts.
	 *
	 * @param contact the contact to be commented on
	 * @return a CharSequence representing the comment on the contact
	 */
	@Override
	@NotNull CharSequence commentOn(@NotNull Contact contact);
	
	//write java documentation for this method : 
}
