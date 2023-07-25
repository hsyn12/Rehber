package com.tr.hsyn.telefonrehberi.main.call.data;


import com.tr.hsyn.calldata.Call;
import com.tr.hsyn.collection.Lister;
import com.tr.hsyn.contactdata.Contact;
import com.tr.hsyn.keep.Keep;
import com.tr.hsyn.string.Stringx;
import com.tr.hsyn.telefonrehberi.main.data.CallMap;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Keep
public class CallLogger implements CallLog {
	
	private final CallMap    callMap;
	private final List<Call> calls;
	
	public CallLogger(List<Call> calls) {
		
		this.calls = calls != null ? calls : new ArrayList<>(0);
		callMap    = new CallMap(this.calls, CallLog::getKey);
	}
	
	@Override
	public @NotNull List<Call> getCalls(@NotNull Contact contact, int @NotNull ... callTypes) {
		
		return getById(contact.getId()).stream().filter(c -> Lister.contains(callTypes, c.getCallType())).collect(Collectors.toList());
	}
	
	@Override
	@NotNull
	public List<Call> getById(long contactId) {
		
		return callMap.get(String.valueOf(contactId));
	}
	
	@Override
	public @NotNull List<Call> getById(String contactId) {
		
		if (Stringx.isNoboe(contactId) || isEmpty()) return new ArrayList<>(0);
		
		return callMap.get(contactId);
	}
	
	@Override
	public @NotNull List<Call> getCalls() {
		
		return calls;
	}
}
