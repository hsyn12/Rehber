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


public class ContactMap {
	
	private final Map<Long, Contact> contacts;
	
	public ContactMap(@NotNull List<Contact> contacts) {
		
		this.contacts = mapContacts(contacts);
	}
	
	public List<Contact> getContacts() {
		
		return new ArrayList<>(contacts.values());
	}
	
	/**
	 * Returns the contact with the given ID.
	 *
	 * @param contactId the contact ID
	 * @return the contact or {@code null} if not found
	 */
	@Nullable
	public Contact getById(String contactId) {
		
		return Try.ignore(() -> contacts.get(Long.parseLong(contactId)));
	}
	
	/**
	 * Returns the contact with the given ID.
	 *
	 * @param contactId the contact ID
	 * @return the contact or {@code null} if not found
	 */
	@Nullable
	public Contact getById(long contactId) {
		
		return contacts.get(contactId);
	}
	
	/**
	 * Returns the contacts that have any real phone number.
	 *
	 * @return the contacts
	 */
	public @NotNull List<Contact> getWithNumber() {
		
		return filter(MainContacts::hasNumber);
	}
	
	/**
	 * Returns the contacts by the predicate.
	 *
	 * @param predicate the predicate to select
	 * @return the contacts
	 */
	public @NotNull List<Contact> filter(@NotNull Predicate<Contact> predicate) {
		
		return getContacts().stream()
				.filter(predicate)
				.collect(Collectors.toList());
	}
	
	/**
	 * Creates the map object that has a key by contact ID, and a contact as value.
	 */
	private static Map<Long, Contact> mapContacts(@NotNull List<Contact> contacts) {
		
		return contacts.stream().collect(Collectors.toMap(Contact::getContactId, c -> c));
	}
	
}
