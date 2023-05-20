package com.tr.hsyn.telefonrehberi.main.activity.contact.detail.comment;


import android.app.Activity;

import com.tr.hsyn.calldata.Call;
import com.tr.hsyn.contactdata.Contact;
import com.tr.hsyn.telefonrehberi.main.code.comment.CommentEditor;
import com.tr.hsyn.telefonrehberi.main.code.comment.Commentator;
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
 * This interface extends the {@link Commentator} interface,
 * which defines the {@link Commentator#commentOn(Object)} method
 * for generating a comment on a contact.
 */
public interface ContactCommentator extends Commentator<Contact>, CommentEditor {
	
	/**
	 * Returns a list of all contacts.
	 *
	 * @return a list of all contacts
	 */
	default List<Contact> getContacts() {
		
		return Over.Contacts.getContacts();
	}
	
	/**
	 * Returns the activity by calling {@link #getCommentStore()}.
	 *
	 * @return the activity
	 */
	@Override
	default @NotNull Activity getActivity() {
		
		return getCommentStore().getActivity();
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
	 * Returns the call history of the contact.
	 *
	 * @return the call history of the contact
	 */
	default List<Call> getCallHistory() {
		
		return getContact().getData(ContactKey.SHOW_CALLS);
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
