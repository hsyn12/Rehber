package com.tr.hsyn.telefonrehberi.main.activity.contact.detail.data;


import android.annotation.SuppressLint;

import com.tr.hsyn.calldata.Call;
import com.tr.hsyn.contactdata.Contact;

import org.jetbrains.annotations.NotNull;

import java.util.List;


/**
 * @inheritDoc
 */
public class ContactCallHistory implements History {
	
	private final Contact    contact;
	private final List<Call> calls;
	
	/**
	 * Creates a new history for the given contact with the given calls.
	 */
	public ContactCallHistory(@NotNull Contact contact, @NotNull List<Call> calls) {
		
		this.contact = contact;
		this.calls   = calls;
	}
	
	/**
	 * @inheritDoc
	 */
	@Override
	@NotNull
	public List<Call> getCalls() {
		
		return calls;
	}
	
	/**
	 * @inheritDoc
	 */
	@Override
	public @NotNull Contact getContact() {
		
		return contact;
	}
	
	@SuppressLint("DefaultLocale")
	@NotNull
	@Override
	public String toString() {
		
		return String.format("History{calls=%d}", calls.size());
	}
}
