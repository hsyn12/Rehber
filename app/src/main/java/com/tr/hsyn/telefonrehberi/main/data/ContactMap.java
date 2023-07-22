package com.tr.hsyn.telefonrehberi.main.data;


import com.tr.hsyn.contactdata.Contact;
import com.tr.hsyn.tryme.Try;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
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
	 * @return all contacts that creating this object by.
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
	public Contact getContact(String contactId) {
		
		return Try.ignore(() -> contacts.get(Long.parseLong(contactId)));
	}
	
	/**
	 * Returns the contact with the ID.
	 *
	 * @param contactId the contact ID
	 * @return the contact or {@code null} if not found
	 */
	@Nullable
	public Contact getContact(long contactId) {
		
		return contacts.get(contactId);
	}
	
	/**
	 * Returns the contacts that match the predicate.
	 *
	 * @param predicate the predicate to select.
	 * @return the selected contacts or all contacts if predicate is {@code null}.
	 */
	public @NotNull List<Contact> getContacts(@Nullable Predicate<Contact> predicate) {
		
		if (predicate == null) return getContacts();
		
		return getContacts().stream()
				.filter(predicate)
				.collect(Collectors.toList());
	}
	
	/**
	 * Returns the contacts that have any real phone number.
	 *
	 * @return the contacts with number
	 */
	public @NotNull List<Contact> getContactsWithNumber() {
		
		return getContacts(MainContacts::hasNumber);
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
