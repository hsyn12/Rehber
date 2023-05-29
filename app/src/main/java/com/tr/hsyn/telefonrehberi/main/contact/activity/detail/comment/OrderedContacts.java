package com.tr.hsyn.telefonrehberi.main.contact.activity.detail.comment;


import com.tr.hsyn.contactdata.Contact;

import java.util.List;


public class OrderedContacts {
	
	private final List<com.tr.hsyn.calldata.Call> calls;
	private final List<Contact>                   contacts;
	
	public OrderedContacts(List<com.tr.hsyn.calldata.Call> calls, List<Contact> contacts) {
		
		this.calls    = calls;
		this.contacts = contacts;
	}
}
