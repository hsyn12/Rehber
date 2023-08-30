package com.tr.hsyn.telefonrehberi.main.contact.comment.commentator;

import android.app.Activity;

import com.tr.hsyn.key.Key;
import com.tr.hsyn.telefonrehberi.main.call.data.CallLog;
import com.tr.hsyn.telefonrehberi.main.code.comment.CommentEditor;
import com.tr.hsyn.telefonrehberi.main.code.comment.Commentator;
import com.tr.hsyn.telefonrehberi.main.dev.Over;
import com.tr.hsyn.xbox.Blue;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import tr.xyz.contact.Contact;

/**
 * The <code>ContactCommentator</code> interface defines the contract
 * for commenting on a phone contact.
 * <p>
 * Implementations of this interface should give specific capability
 * for commenting on different contacts.
 * <p>
 * This interface extends the {@link Commentator} interface,
 * which defines the {@link Commentator#commentOn(Object)} method
 * for generating a comment on a contact.
 */
public interface ContactCommentator extends Commentator<Contact>, CommentEditor {
	
	/**
	 * Returns the {@link ContactCommentStore} instance associated with this {@link ContactCommentator}.
	 *
	 * @return the {@link ContactCommentStore} instance associated with this {@link ContactCommentator}
	 */
	ContactCommentStore getCommentStore();
	
	/**
	 * Returns the contact for which the comment is being generated.
	 *
	 * @return current selected contact
	 */
	Contact getContact();
	
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
	 * This method is responsible for commenting on a contact.
	 * <p>
	 * Implementations of this method should give specific capability
	 * for commenting on different contacts.
	 *
	 * @param contact the contact to be commented on
	 *
	 * @return a CharSequence representing the comment on the contact
	 */
	@Override
	default @NotNull CharSequence commentOn(@NotNull Contact contact) {
		
		throw new UnsupportedOperationException("Use commentOn(Contact, Consumer) instead");
	}
	
	/**
	 * Returns a list of all contacts.
	 *
	 * @return a list of all contacts
	 */
	@Nullable
	default List<Contact> getContacts() {
		
		return Over.Content.Contacts.getContacts();
	}
	
	/**
	 * Returns the {@link CallLog} object.
	 *
	 * @return {@link CallLog} object
	 */
	default @Nullable CallLog getCallCollection() {
		
		return Blue.getObject(Key.CALL_LOG);
	}
	
}
