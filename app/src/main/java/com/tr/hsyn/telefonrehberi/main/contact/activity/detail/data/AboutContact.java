package com.tr.hsyn.telefonrehberi.main.contact.activity.detail.data;


import com.tr.hsyn.calldata.CallType;
import com.tr.hsyn.contactdata.Contact;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.stream.Collectors;


public class AboutContact {
	
	private final Contact                         contact;
	private final List<com.tr.hsyn.calldata.Call> history;
	
	public AboutContact(@NotNull Contact contact, @NotNull List<com.tr.hsyn.calldata.Call> history) {
		
		this.contact = contact;
		this.history = history;
	}
	
	public List<com.tr.hsyn.calldata.Call> getHistory() {
		
		return history;
	}
	
	public Contact getContact() {
		
		return contact;
	}
	
	@NotNull
	public List<com.tr.hsyn.calldata.Call> getIncommingCalls() {
		
		return getCallsByType(CallType.INCOMING);
	}
	
	@NotNull
	public List<com.tr.hsyn.calldata.Call> getCallsByType(int callType) {
		
		return history.stream().filter(c -> c.getCallType() == callType).collect(Collectors.toList());
	}
	
	@NotNull
	public List<com.tr.hsyn.calldata.Call> getOutgoingCalls() {
		
		return getCallsByType(CallType.OUTGOING);
	}
	
	@NotNull
	public List<com.tr.hsyn.calldata.Call> getMissedCalls() {
		
		return getCallsByType(CallType.MISSED);
	}
	
	@NotNull
	public List<com.tr.hsyn.calldata.Call> getRejectedCalls() {
		
		return getCallsByType(CallType.REJECTED);
	}
	
	
}
