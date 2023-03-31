package com.tr.hsyn.telefonrehberi.main.activity.contact.detail.comment;


import com.tr.hsyn.calldata.Call;
import com.tr.hsyn.contactdata.Contact;

import java.util.List;


public class OrderedContacts {
	
	private final List<Call>    calls;
	private final List<Contact> contacts;
	
	public OrderedContacts(List<Call> calls, List<Contact> contacts) {
		
		this.calls    = calls;
		this.contacts = contacts;
	}
}
