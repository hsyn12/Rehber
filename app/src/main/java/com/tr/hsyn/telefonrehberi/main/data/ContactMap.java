package com.tr.hsyn.telefonrehberi.main.data;


import com.tr.hsyn.contactdata.Contact;
import com.tr.hsyn.tryme.Try;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * Holds the contact in a map that has a key by contact ID, and a contact as value.
 */
public class ContactMap {
	
	/**
	 * The map object that has a key by contact ID and a contact as value.
	 */
	private final Map<Long, Contact> contacts;
	
	/**
	 * Creates a new instance.
	 *
	 * @param contacts the contacts to hold
	 */
	public ContactMap(@NotNull List<Contact> contacts) {
		
		this.contacts = mapContacts(contacts);
	}
	
	/**
	 * @return a new list consists of the all contacts
	 */
	public List<Contact> getContacts() {
		
		return new ArrayList<>(contacts.values());
	}
	
	/**
	 * Returns the contact with the ID.
	 *
	 * @param contactId the contact ID
	 * @return the contact or {@code null} if not found
	 */
	@Nullable
	public Contact get(String contactId) {
		
		return Try.ignore(() -> contacts.get(Long.parseLong(contactId)));
	}
	
	/**
	 * Returns the contact with the ID.
	 *
	 * @param contactId the contact ID
	 * @return the contact or {@code null} if not found
	 */
	@Nullable
	public Contact get(long contactId) {
		
		return contacts.get(contactId);
	}
	
	/**
	 * Creates the map object that has a key by contact ID, and a contact as value.
	 *
	 * @param contacts the contacts to map
	 * @return the map object
	 */
	private static Map<Long, Contact> mapContacts(@NotNull List<Contact> contacts) {
		
		return contacts.stream().collect(Collectors.toMap(Contact::getContactId, c -> c));
	}
	
}
