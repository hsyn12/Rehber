package com.tr.hsyn.telefonrehberi.main.data;


import com.tr.hsyn.contactdata.Contact;
import com.tr.hsyn.key.Key;
import com.tr.hsyn.phone_numbers.PhoneNumbers;
import com.tr.hsyn.telefonrehberi.main.contact.data.ContactKey;
import com.tr.hsyn.tryme.Try;
import com.tr.hsyn.xbox.Blue;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;


/**
 * Holds the contacts and provides methods for filtering, searching and analyzing.
 */
public final class Contacts {
	
	/**
	 * The filter for all contacts
	 */
	public static final int FILTER_ALL                    = 0;
	/**
	 * The filter for the contacts that have the most incoming calls
	 */
	public static final int FILTER_MOST_INCOMING          = 1;
	/**
	 * The filter for the contacts that have the most outgoing calls
	 */
	public static final int FILTER_MOST_OUTGOING          = 2;
	/**
	 * The filter for the contacts that have the most missed calls
	 */
	public static final int FILTER_MOST_MISSED            = 3;
	/**
	 * The filter for the contacts that have the most rejected calls
	 */
	public static final int FILTER_MOST_REJECTED          = 4;
	/**
	 * The filter for the contacts that have the most incoming duration
	 */
	public static final int FILTER_MOST_INCOMING_DURATION = 5;
	/**
	 * The filter for the contacts that have the most outgoing duration
	 */
	public static final int FILTER_MOST_OUTGOING_DURATION = 6;
	/**
	 * The filter for the contacts that have the most total duration
	 */
	public static final int FILTER_MOST_TOTAL_DURATION    = 7;
	
	private static ContactMap contactsMap;
	
	/**
	 * Returns the contact with the given ID.
	 *
	 * @param contactId the contact ID
	 * @return the contact or {@code null} if not found
	 */
	@Nullable
	public static Contact getById(String contactId) {
		
		return Try.ignore(() -> getById(Long.parseLong(contactId)));
	}
	
	/**
	 * Returns the contact with the given ID.
	 *
	 * @param contactId the contact ID
	 * @return the contact or {@code null} if not found
	 */
	@Nullable
	public static Contact getById(long contactId) {
		
		if (contactsMap != null) return contactsMap.get(contactId);
		
		return getContacts().stream().filter(c -> c.getId() == contactId).findFirst().orElse(null);
	}
	
	/**
	 * Returns the contacts.
	 *
	 * @return the contacts
	 */
	public static @NotNull List<Contact> getContacts() {
		
		List<Contact> contacts = Blue.getObject(Key.CONTACTS);
		
		if (contactsMap == null && contacts != null) {
			
			contactsMap = new ContactMap(contacts);
			return contacts;
		}
		
		return contacts != null ? contacts : new ArrayList<>(0);
	}
	
	/**
	 * @return {@code true} if the contacts are loaded
	 */
	public static boolean isContactsLoaded() {
		
		return Blue.getObject(Key.CONTACTS) != null;
	}
	
	/**
	 * Returns the contacts that have any real phone number.
	 *
	 * @return the contacts
	 */
	public static @NotNull List<Contact> getWithNumber() {
		
		return filter(Contacts::hasNumber);
	}
	
	/**
	 * Returns the contacts by the predicate.
	 *
	 * @param predicate the predicate to select
	 * @return the contacts
	 */
	public static @NotNull List<Contact> filter(@NotNull Predicate<Contact> predicate) {
		
		return getContacts().stream()
				.filter(predicate)
				.collect(Collectors.toList());
	}
	
	/**
	 * Checks if the contact has any real phone number.
	 *
	 * @param contact the contact
	 * @return {@code true} if the contact has a number
	 */
	public static boolean hasNumber(@NotNull Contact contact) {
		
		List<String> numbers = ContactKey.getNumbers(contact);
		
		if (numbers == null || numbers.isEmpty()) return false;
		
		for (String number : numbers)
			if (PhoneNumbers.isPhoneNumber(number))
				return true;
		
		return false;
	}
}
