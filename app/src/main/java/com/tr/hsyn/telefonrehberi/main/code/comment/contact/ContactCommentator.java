package com.tr.hsyn.telefonrehberi.main.code.comment.contact;


import android.app.Activity;

import com.tr.hsyn.calldata.Call;
import com.tr.hsyn.contactdata.Contact;
import com.tr.hsyn.phone_numbers.PhoneNumbers;
import com.tr.hsyn.telefonrehberi.main.code.comment.Commentator;
import com.tr.hsyn.telefonrehberi.main.code.comment.ContactCommentStore;
import com.tr.hsyn.telefonrehberi.main.code.comment.Moody;
import com.tr.hsyn.telefonrehberi.main.code.comment.contact.defaults.DefaultContactCommentator;
import com.tr.hsyn.telefonrehberi.main.code.contact.act.ContactKey;
import com.tr.hsyn.telefonrehberi.main.dev.Over;
import com.tr.hsyn.text.Spanner;
import com.tr.hsyn.xlog.xlog;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


/**
 * Kişi yorumlayıcısı.<br>
 */
public abstract class ContactCommentator implements Commentator<Contact> {
	
	protected final List<Call>          calls;
	protected       Contact             contact;
	protected       List<Call>          history;
	protected       List<Contact>       contacts;
	protected final ContactCommentStore store;
	protected       Spanner             comment = new Spanner();
	protected       ContactName         contactName;
	
	public ContactCommentator(ContactCommentStore store) {
		
		this.store = store;
		calls      = Over.CallLog.Calls.getCalls();
		contacts   = Over.Contacts.getContacts();
	}
	
	protected final int getColor(int id) {
		
		return store.getActivity().getColor(id);
	}
	
	abstract protected void commentContact();
	
	@Override
	public final @NotNull
	CharSequence commentate(@NotNull Contact subject) {
		
		contact      = subject;
		contactName  = new ContactName(contact.getName());
		this.history = getContactHistory();
		
		commentContact();
		
		return comment;
	}
	
	@NotNull
	private List<Call> getContactHistory() {
		
		if (calls != null)
			return calls
					.stream()
					.filter(call -> PhoneNumbers.containsNumber(contact.getData(ContactKey.NUMBERS), call.getNumber()))
					.collect(Collectors.toList());
		else return new ArrayList<>(0);
	}
	
	public static @NotNull
	ContactCommentator createCommentator(@NotNull Activity activity) {
		
		Moody moody = Moody.getMood();
		
		var store = ContactCommentStore.createCommentStore(activity, moody);
		
		switch (moody) {
			
			case DEFAULT:
				
				var commentator = new DefaultContactCommentator(store);
				xlog.d("Default Commentator");
				return commentator;
			case HAPPY:
				xlog.d("not yet happy");
		}
		
		xlog.d("Wrong moody : %d", moody.ordinal());
		return new DefaultContactCommentator(store);
	}
	
	
}
