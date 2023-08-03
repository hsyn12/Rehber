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
public interface Contacts {
	
	int FILTER_ALL                    = 0;
	int FILTER_MOST_INCOMING          = 1;
	int FILTER_MOST_OUTGOING          = 2;
	int FILTER_MOST_MISSED            = 3;
	int FILTER_MOST_REJECTED          = 4;
	int FILTER_MOST_INCOMING_DURATION = 5;
	int FILTER_MOST_OUTGOING_DURATION = 6;
	int FILTER_MOST_TOTAL_DURATION    = 7;
	
	
	/**
	 * Returns the contact with the given ID.
	 *
	 * @param contactId the contact ID
	 * @return the contact or {@code null} if not found
	 */
	@Nullable
	static Contact getById(String contactId) {
		
		return Try.ignore(() -> getById(Long.parseLong(contactId)));
	}
	
	/**
	 * Returns the contact with the given ID.
	 *
	 * @param contactId the contact ID
	 * @return the contact or {@code null} if not found
	 */
	@Nullable
	static Contact getById(long contactId) {
		
		return getContacts().stream().filter(c -> c.getId() == contactId).findFirst().orElse(null);
	}
	
	@NotNull
	static List<Contact> getContacts() {
		
		List<Contact> contacts = Blue.getObject(Key.CONTACTS);
		
		return contacts != null ? contacts : new ArrayList<>(0);
	}
	
	/**
	 * Returns the contacts that have any real phone number.
	 *
	 * @return the contacts
	 */
	static @NotNull List<Contact> getWithNumber() {
		
		return filter(Contacts::hasNumber);
	}
	
	/**
	 * Returns the contacts by the predicate.
	 *
	 * @param predicate the predicate to select
	 * @return the contacts
	 */
	static @NotNull List<Contact> filter(@NotNull Predicate<Contact> predicate) {
		
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
	static boolean hasNumber(@NotNull Contact contact) {
		
		List<String> numbers = ContactKey.getNumbers(contact);
		
		if (numbers == null || numbers.isEmpty()) return false;
		
		for (String number : numbers)
			if (PhoneNumbers.isPhoneNumber(number))
				return true;
		
		return false;
	}
	
	
}
