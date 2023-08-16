package com.tr.hsyn.telefonrehberi.main.data;

import com.tr.hsyn.keep.Keep;
import com.tr.hsyn.key.Key;
import com.tr.hsyn.phone_numbers.PhoneNumbers;
import com.tr.hsyn.telefonrehberi.main.contact.data.ContactKeyKt;
import com.tr.hsyn.tryme.Try;
import com.tr.hsyn.xbox.Blue;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import tr.xyz.contact.Contact;


/**
 * Holds the contacts and provides methods for filtering, searching and analyzing.
 */
@Keep
public final class ContactLog {

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

	/**
	 * The map object that has a key by contact ID and a contact as value.
	 */
	private final Map<Long, Contact> contactMap;
	private final List<Contact>      contacts;

	public ContactLog(@NotNull List<Contact> contacts) {

		this.contacts   = contacts;
		this.contactMap = mapContacts(contacts);
	}

	/**
	 * Returns the contact with the given ID.
	 *
	 * @param contactId the contact ID
	 *
	 * @return the contact or {@code null} if not found
	 */
	@Nullable
	public Contact getById(String contactId) {

		return Try.ignore(() -> getById(Long.parseLong(contactId)));
	}

	/**
	 * Returns the contact with the given ID.
	 *
	 * @param contactId the contact ID
	 *
	 * @return the contact or {@code null} if not found
	 */
	@Nullable
	public Contact getById(long contactId) {

		return contactMap.get(contactId);
	}

	/**
	 * Returns the contacts.
	 *
	 * @return the contacts
	 */
	public @NotNull List<Contact> getContacts() {

		return contacts;
	}

	/**
	 * Returns the contacts that have any real phone number.
	 *
	 * @return the contacts
	 */
	public @NotNull List<Contact> getWithNumber() {

		return filter(ContactLog::hasNumber);
	}

	/**
	 * Returns the contacts by the predicate.
	 *
	 * @param predicate the predicate to select
	 *
	 * @return the contacts
	 */
	public @NotNull List<Contact> filter(@NotNull Predicate<Contact> predicate) {

		return contacts.stream()
			.filter(predicate)
			.collect(Collectors.toList());
	}

	/**
	 * Creates the map object that has a key by contact ID, and a contact as value.
	 *
	 * @param contacts the contacts to map
	 *
	 * @return the map object
	 */
	private static Map<Long, Contact> mapContacts(@NotNull List<Contact> contacts) {

		return contacts.stream().collect(Collectors.toMap(Contact::getContactId, c -> c));
	}

	/**
	 * @return {@code true} if the contacts are loaded
	 */
	public static boolean isLoaded() {

		return Blue.getObject(Key.CONTACT_LOG) != null;
	}

	/**
	 * Checks if the contact has any real phone number.
	 *
	 * @param contact the contact
	 *
	 * @return {@code true} if the contact has a number
	 */
	public static boolean hasNumber(@NotNull Contact contact) {

		List<String> numbers = ContactKeyKt.getNumbers(contact);

		if (numbers == null || numbers.isEmpty()) return false;

		for (String number : numbers)
			if (PhoneNumbers.isPhoneNumber(number))
				return true;

		return false;
	}

	public static @Nullable ContactLog getLog() {

		return Blue.getObject(Key.CONTACT_LOG);
	}

	public static @NotNull ContactLog getLogOrEmpty() {

		var log = getLog();

		return log != null ? log : new ContactLog(new ArrayList<>(0));
	}

	public static void createGlobal(@NotNull List<Contact> contacts) {

		var log = new ContactLog(contacts);
		Blue.box(Key.CONTACT_LOG, log);
	}
}
