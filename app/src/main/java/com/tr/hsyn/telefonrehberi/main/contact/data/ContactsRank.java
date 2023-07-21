package com.tr.hsyn.telefonrehberi.main.contact.data;


import com.tr.hsyn.collection.CoupleMap;
import com.tr.hsyn.contactdata.Contact;
import com.tr.hsyn.key.Key;
import com.tr.hsyn.phone_numbers.PhoneNumbers;
import com.tr.hsyn.telefonrehberi.main.call.data.NumberKey;
import com.tr.hsyn.tryme.Try;
import com.tr.hsyn.xbox.Blue;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;


/**
 * Holds the contacts and provides methods for filtering, searching and analyzing.
 */
public class ContactsRank {
	
	private final List<Contact>              contacts;
	private final Map<Long, Contact>         contactMap;
	private       CoupleMap<Long, NumberKey> mapContactIdToNumbers;
	
	private ContactsRank(@NotNull List<Contact> contacts) {
		
		this.contacts   = contacts;
		this.contactMap = ContactsRank.mapContacts(contacts);
	}
	
	@NotNull
	public List<Contact> getContacts() {
		
		return contacts;
	}
	
	/**
	 * Returns the contacts by the predicate.
	 *
	 * @param predicate the predicate to select
	 * @return the contacts
	 */
	public @Nullable List<Contact> selectContacts(@NotNull Predicate<Contact> predicate) {
		
		if (contacts == null) return null;
		
		return contacts.stream()
				.filter(predicate)
				.collect(Collectors.toList());
	}
	
	/**
	 * Returns the contacts that have any real phone number.
	 *
	 * @return the contacts
	 */
	@Nullable
	public List<Contact> getContactsWithNumber() {
		
		return selectContacts(ContactsRank::hasNumber);
	}
	
	/**
	 * Returns the contact with the given ID.
	 *
	 * @param contactId the contact ID
	 * @return the contact or {@code null} if not found
	 */
	@Nullable
	public Contact getContact(long contactId) {
		
		return contactMap.get(contactId);
	}
	
	/**
	 * Returns the contact with the given ID.
	 *
	 * @param contactId the contact ID
	 * @return the contact or {@code null} if not found
	 */
	@Nullable
	public Contact getContact(String contactId) {
		
		return Try.ignore(() -> contactMap.get(Long.parseLong(contactId)));
	}
	
	/**
	 * @return map of contact ID to the numbers
	 */
	@NotNull
	private CoupleMap<Long, NumberKey> getContactIdToNumbersMap() {
		
		if (mapContactIdToNumbers != null) return mapContactIdToNumbers;
		
		List<Contact> contacts = getContactsWithNumber();
		
		if (contacts != null)
			mapContactIdToNumbers = new CoupleMap<>(contacts.stream().collect(Collectors.toMap(Contact::getContactId, c -> new NumberKey(Objects.requireNonNull(ContactKey.getNumbers(c))))));
		
		return mapContactIdToNumbers;
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
	
	/**
	 * Creates the map object that has a key by contact ID, and a contact as value.
	 */
	private static Map<Long, Contact> mapContacts(@NotNull List<Contact> contacts) {
		
		return contacts.stream().collect(Collectors.toMap(Contact::getContactId, c -> c));
	}
	
	/**
	 * Creates a {@link ContactsRank} object.
	 *
	 * @return the contacts
	 */
	@NotNull
	public static ContactsRank create() {
		
		List<Contact> contacts = Blue.getObject(Key.CONTACTS);
		
		return new ContactsRank(contacts != null ? contacts : new ArrayList<>(0));
	}
	
	/**
	 * Creates a {@link ContactsRank} object.
	 *
	 * @param contacts the contacts
	 * @return the contacts
	 */
	@NotNull
	public static ContactsRank create(@NotNull List<Contact> contacts) {
		
		return new ContactsRank(contacts);
	}
	
	/**
	 * Creates a {@link ContactsRank} object.
	 * After this, can be accessed via {@link Blue#getObject(Key)} with the key {@link Key#CONTACTS_RANK}.
	 *
	 * @return the contacts
	 */
	@NotNull
	public static ContactsRank createGlobal() {
		
		List<Contact> contacts = Blue.getObject(Key.CONTACTS);
		ContactsRank  c        = new ContactsRank(contacts != null ? contacts : new ArrayList<>(0));
		Blue.box(Key.CONTACTS_RANK, c);
		
		return c;
	}
	
	/**
	 * Creates a {@link ContactsRank} object.
	 * After this, can be accessed via {@link Blue#getObject(Key)} with the key {@link Key#CONTACTS_RANK}.
	 *
	 * @param contacts the contacts
	 * @return the contacts
	 */
	@NotNull
	public static ContactsRank createGlobal(@NotNull List<Contact> contacts) {
		
		ContactsRank c = new ContactsRank(contacts);
		Blue.box(Key.CONTACTS_RANK, c);
		
		return c;
	}
}
