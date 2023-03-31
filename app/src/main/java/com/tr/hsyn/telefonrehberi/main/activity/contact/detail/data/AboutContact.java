package com.tr.hsyn.telefonrehberi.main.activity.contact.detail.data;


import com.tr.hsyn.calldata.Call;
import com.tr.hsyn.calldata.CallType;
import com.tr.hsyn.contactdata.Contact;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.stream.Collectors;


public class AboutContact {
	
	private final Contact    contact;
	private final List<Call> history;
	
	public AboutContact(@NotNull Contact contact, @NotNull List<Call> history) {
		
		this.contact = contact;
		this.history = history;
	}
	
	public List<Call> getHistory() {
		
		return history;
	}
	
	public Contact getContact() {
		
		return contact;
	}
	
	@NotNull
	public List<Call> getCallsByType(int callType) {
		
		return history.stream().filter(c -> c.getType() == callType).collect(Collectors.toList());
	}
	
	@NotNull
	public List<Call> getIncommingCalls() {
		
		return getCallsByType(CallType.INCOMING);
	}
	
	@NotNull
	public List<Call> getOutgoingCalls() {
		
		return getCallsByType(CallType.OUTGOING);
	}
	
	@NotNull
	public List<Call> getMissedCalls() {
		
		return getCallsByType(CallType.MISSED);
	}
	
	@NotNull
	public List<Call> getRejectedCalls() {
		
		return getCallsByType(CallType.REJECTED);
	}
	
	
}
