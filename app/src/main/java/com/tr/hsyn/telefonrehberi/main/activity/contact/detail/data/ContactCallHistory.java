package com.tr.hsyn.telefonrehberi.main.activity.contact.detail.data;


import com.tr.hsyn.contactdata.Contact;

import org.jetbrains.annotations.NotNull;


public class ContactCallHistory implements History {
	
	private final Contact contact;
	
	public ContactCallHistory(@NotNull Contact contact) {this.contact = contact;}
	
	@Override
	public @NotNull Contact getContact() {
		
		return contact;
	}
}
