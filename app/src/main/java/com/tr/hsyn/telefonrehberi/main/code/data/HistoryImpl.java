package com.tr.hsyn.telefonrehberi.main.code.data;


import android.annotation.SuppressLint;

import com.tr.hsyn.calldata.Call;
import com.tr.hsyn.contactdata.Contact;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


public final class HistoryImpl implements History {
	
	private final Contact    contact;
	private final List<Call> calls;
	private final List<Call> incoming;
	private final List<Call> outgoing;
	private final List<Call> missed;
	private final List<Call> rejected;
	private final int        incomingDuration;
	private final int        outgoingDuration;
	
	public HistoryImpl(@NotNull Contact contact, @NotNull List<Call> calls) {
		
		this.contact  = contact;
		this.calls    = calls;
		this.incoming = calls.stream().filter(c -> c.getCallType() == Call.INCOMING || c.getCallType() == Call.INCOMING_WIFI).collect(Collectors.toList());
		this.outgoing = calls.stream().filter(c -> c.getCallType() == Call.OUTGOING || c.getCallType() == Call.OUTGOING_WIFI).collect(Collectors.toList());
		this.missed   = calls.stream().filter(c -> c.getCallType() == Call.MISSED).collect(Collectors.toList());
		this.rejected = calls.stream().filter(c -> c.getCallType() == Call.REJECTED).collect(Collectors.toList());
		
		this.incomingDuration = (int) incoming.stream().mapToLong(Call::getDuration).sum();
		this.outgoingDuration = (int) outgoing.stream().filter(Call::isSpoken).mapToLong(Call::getDuration).sum();
	}
	
	@Override
	public @NotNull Contact getContact() {
		
		return contact;
	}
	
	@Override
	@NotNull
	public List<Call> getCalls() {
		
		return calls;
	}
	
	@Override
	public List<Call> getIncomingCalls() {
		
		return incoming;
	}
	
	@Override
	public List<Call> getOutgoingCalls() {
		
		return outgoing;
	}
	
	@Override
	public List<Call> getMissedCalls() {
		
		return missed;
	}
	
	@Override
	public List<Call> getRejectedCalls() {
		
		return rejected;
	}
	
	@Override
	public int getIncomingDuration() {
		
		return incomingDuration;
	}
	
	@Override
	public int getOutgoingDuration() {
		
		return outgoingDuration;
	}
	
	@Override
	public int getTotalDuration() {
		
		return incomingDuration + outgoingDuration;
	}
	
	@Override
	public int hashCode() {
		
		return Objects.hash(contact.getContactId());
	}
	
	@Override
	public boolean equals(Object obj) {
		
		return obj instanceof Contact o && contact.getContactId() == o.getContactId();
	}
	
	@SuppressLint("DefaultLocale")
	@Override
	public @NotNull String toString() {
		
		return String.format("History{contact=%s, calls=%d}", contact, calls.size());
	}
}
