package com.tr.hsyn.telefonrehberi.main.activity.contact.detail.comment;


import com.tr.hsyn.calldata.Call;
import com.tr.hsyn.contactdata.Contact;
import com.tr.hsyn.telefonrehberi.main.code.comment.Commentator;
import com.tr.hsyn.telefonrehberi.main.code.comment.ContactCommentStore;
import com.tr.hsyn.telefonrehberi.main.code.contact.act.ContactKey;
import com.tr.hsyn.telefonrehberi.main.dev.Over;

import org.jetbrains.annotations.NotNull;

import java.util.List;


/**
 * The <code>ContactCommentator</code> interface defines the contract
 * for commenting on a phone contact.
 * <p>
 * Implementations of this interface should provide specific functionality
 * for commenting on different contacts.
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
	 * Returns the color associated with the given resource ID.
	 *
	 * @param id the resource ID of the color
	 * @return the color associated with the given resource ID
	 */
	default int getColor(int id) {
		
		return getCommentStore().getActivity().getColor(id);
	}
	
	/**
	 * Returns the {@link ContactCommentStore} instance associated with this {@link ContactCommentator}.
	 *
	 * @return the {@link ContactCommentStore} instance associated with this {@link ContactCommentator}
	 */
	ContactCommentStore getCommentStore();
	
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
	
	/**
	 * Returns the history of the contact.
	 *
	 * @return the history of the contact
	 */
	default List<Call> getCallHistory() {
		
		return getContact().getData(ContactKey.CALL_HISTORY);
	}
	
	/**
	 * Returns the contact for which the comment is being generated.
	 *
	 * @return current selected contact
	 */
	Contact getContact();
	
	/**
	 * Return the comment about the last call.
	 * The last call comment is about the last call between the contact and the user.
	 *
	 * @return the comment about the last call
	 */
	@NotNull CharSequence commentOnTheLastCall();
	
}
