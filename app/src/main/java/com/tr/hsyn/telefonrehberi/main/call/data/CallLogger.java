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
		
		List<Call> list = getById(contact.getId());
		
		if (callTypes.length == 0) return list;
		
		return list.stream().filter(c -> Lister.contains(callTypes, c.getCallType())).collect(Collectors.toList());
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
	
	/**
	 * This method merges the calls belonging to the same contact
	 * that have more than one phone number.
	 *
	 * @param callMap call map
	 */
	private static void mergeSameCalls(@NotNull CallMap callMap) {
		
		// phone numbers
		List<String> keys = Lister.listOf(callMap.keySet());
		
		// loop on numbers
		for (int i = 0; i < keys.size(); i++) {
			
			// Get contact ID.
			// Needs to find the same ID in the list and make it one list.
			String firstKey = keys.get(i);
			// aggregated calls
			List<Call> calls = callMap.remove(firstKey);
			
			if (calls == null) continue;
			
			long contactId = Key.getContactId(calls.get(0));
			
			if (contactId != 0L) {//+ Contact ID found
				
				// loop on the other numbers and find the same ID 
				for (int j = i + 1; j < keys.size(); j++) {
					
					String     secondKey  = keys.get(j);
					List<Call> otherCalls = callMap.remove(secondKey);
					
					if (otherCalls == null) continue;
					
					long otherContactId = Key.getContactId(otherCalls.get(0));
					
					// contactId cannot be zero but otherContactId maybe
					if (contactId == otherContactId) {
						
						// that is the same ID, put it together
						calls.addAll(otherCalls);
						continue;
					}
					
					// put it back in
					callMap.put(secondKey, otherCalls);
				}
			}
			
			// put it back in
			callMap.put(firstKey, calls);
		}
	}
}
