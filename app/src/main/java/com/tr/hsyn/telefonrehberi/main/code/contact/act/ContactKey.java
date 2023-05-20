package com.tr.hsyn.telefonrehberi.main.code.contact.act;


import com.tr.hsyn.contactdata.Account;
import com.tr.hsyn.contactdata.Contact;
import com.tr.hsyn.contactdata.ContactDat;
import com.tr.hsyn.datakey.DataKey;
import com.tr.hsyn.label.Label;
import com.tr.hsyn.telefonrehberi.main.activity.contact.detail.data.History;
import com.tr.hsyn.time.Time;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Set;


public interface ContactKey {
	
	/**
	 * Büyük resim (string)
	 */
	DataKey BIG_PIC      = DataKey.of(3, "bigPic");
	/**
	 * Telefon numaraları (string list)
	 */
	DataKey NUMBERS      = DataKey.of(4, "numbers");
	/**
	 * Email adresleri (string list)
	 */
	DataKey EMAILS       = DataKey.of(5, "emails");
	/**
	 * Not (string)
	 */
	DataKey NOTE         = DataKey.of(6, "note");
	/**
	 * Olaylar ({@link ContactDat}) list
	 */
	DataKey EVENTS       = DataKey.of(7, "events");
	/**
	 * Gruplar (string list)
	 */
	DataKey GROUPS       = DataKey.of(8, "groups");
	/**
	 * Etiketler (label list)
	 */
	DataKey LABELS       = DataKey.of(9, "labels");
	/**
	 * Hesaplar (string list)
	 */
	DataKey ACCOUNTS     = DataKey.of(10, "accounts");
	/**
	 * Silinme zamanı (long)
	 */
	DataKey DELETED_DATE = DataKey.of(11, "deletedDate");
	/**
	 * Detayların uygulandığını bildirir (boolean)
	 */
	@Deprecated(forRemoval = true)
	DataKey DETAILS_APPLIED = DataKey.of(12, "detailsApplied");
	
	/**
	 * Data key for the call history of a contact.<br>
	 * <code>List of Calls</code>
	 */
	DataKey CALL_HISTORY = DataKey.of(13, "call_history");
	
	/**
	 * Data key for the history object.<br>
	 * {@link History} object
	 */
	DataKey HISTORY = DataKey.of(14, "history");
	
	@Nullable
	static List<String> getNumbers(@NotNull Contact contact) {
		
		return contact.getData(NUMBERS);
	}
	
	@Nullable
	static List<String> getEmails(@NotNull Contact contact) {
		
		return contact.getData(EMAILS);
	}
	
	@Nullable
	static List<ContactDat> getEvents(@NotNull Contact contact) {
		
		return contact.getData(EVENTS);
	}
	
	@Nullable
	static String getNote(@NotNull Contact contact) {
		
		return contact.getData(NOTE);
	}
	
	@Nullable
	static List<String> getGroups(@NotNull Contact contact) {
		
		return contact.getData(GROUPS);
	}
	
	@Nullable
	static List<String> getAccounts(@NotNull Contact contact) {
		
		return contact.getData(ACCOUNTS);
	}
	
	@Nullable
	static Set<Label> getLabels(@NotNull Contact contact) {
		
		return contact.getData(LABELS);
	}
	
	static boolean isDeleted(@NotNull Contact contact) {
		
		return contact.getLong(DELETED_DATE, 0L) != 0L;
	}
	
	static void setNumbers(@NotNull Contact contact, List<String> numbers) {
		
		contact.setData(NUMBERS, numbers);
	}
	
	static void setBigPic(@NotNull Contact contact, String pic) {
		
		contact.setData(BIG_PIC, pic);
	}
	
	static void setEmails(@NotNull Contact contact, List<String> emails) {
		
		contact.setData(EMAILS, emails);
	}
	
	static void setEvents(@NotNull Contact contact, List<ContactDat> events) {
		
		contact.setData(EVENTS, events);
	}
	
	static void setLabels(@NotNull Contact contact, Set<Label> labels) {
		
		contact.setData(LABELS, labels);
	}
	
	static void setGroups(@NotNull Contact contact, List<String> groups) {
		
		contact.setData(GROUPS, groups);
	}
	
	static void setAccounts(@NotNull Contact contact, List<Account> accounts) {
		
		contact.setData(ACCOUNTS, accounts);
	}
	
	static void setNote(@NotNull Contact contact, String note) {
		
		contact.setData(NOTE, note);
	}
	
	static void setDeleted(@NotNull Contact contact) {
		
		contact.setData(DELETED_DATE, Time.now());
	}
	
	
}
