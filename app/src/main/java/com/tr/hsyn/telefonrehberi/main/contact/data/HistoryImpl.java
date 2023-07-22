package com.tr.hsyn.telefonrehberi.main.contact.data;


import android.annotation.SuppressLint;

import com.tr.hsyn.calldata.Call;
import com.tr.hsyn.contactdata.Contact;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;


/**
 * @inheritDoc
 */
public final class HistoryImpl implements History {
	
	private final Contact    contact;
	private final List<Call> calls;
	private final String     key;
	
	/**
	 * Creates a new history for the given contact with the given calls.
	 */
	public HistoryImpl(@NotNull String key, @NotNull List<Call> calls) {
		
		this.contact = null;
		this.calls   = calls;
		this.key     = key;
	}
	
	public HistoryImpl(@NotNull Contact contact, @NotNull List<Call> calls) {
		
		this.contact = contact;
		this.calls   = calls;
		key          = String.valueOf(contact.getContactId());
	}
	
	/**
	 * @inheritDoc
	 */
	@Override
	public @NotNull Contact getContact() {
		
		return contact;
	}
	
	/**
	 * @inheritDoc
	 */
	@Override
	@NotNull
	public List<Call> getCalls() {
		
		return calls;
	}
	
	@Override
	public int hashCode() {
		
		return Objects.hash(contact, calls);
	}
	
	@Override
	public boolean equals(Object obj) {
		
		if (obj == this) return true;
		if (obj == null || obj.getClass() != this.getClass()) return false;
		var that = (HistoryImpl) obj;
		return Objects.equals(this.contact, that.contact) &&
		       Objects.equals(this.calls, that.calls);
	}
	
	@SuppressLint("DefaultLocale")
	@NotNull
	@Override
	public String toString() {
		
		return String.format("History{calls=%d}", calls.size());
	}
	
	public String getKey() {
		
		return key;
	}
	
}