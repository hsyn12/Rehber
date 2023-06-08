package com.tr.hsyn.telefonrehberi.main.contact.data;


import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.provider.ContactsContract;

import androidx.annotation.NonNull;

import com.tr.hsyn.collection.Lister;
import com.tr.hsyn.contactdata.Contact;
import com.tr.hsyn.contactdata.ContactDat;
import com.tr.hsyn.content.Contents;
import com.tr.hsyn.label.Label;
import com.tr.hsyn.perfectsort.PerfectSort;
import com.tr.hsyn.phone_numbers.PhoneNumbers;
import com.tr.hsyn.telefonrehberi.main.contact.data.bank.system.ContactColumns;
import com.tr.hsyn.telefonrehberi.main.contact.data.handler.MimeTypeHandler;
import com.tr.hsyn.xlog.xlog;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;


/**
 * Sistem rehberi üzerinde çalışan genel işlemleri tanımlar.<br>
 */
public interface Contacts extends ContactColumns {
	
	@NotNull
	static Contact newContact(Contact contact) {
		
		return new Contact(contact);
	}
	
	@NotNull
	static Contact newContact(long contactId, String name, String pic) {
		
		return new Contact(contactId, name, pic);
	}
	
	static
	@NotNull
	Contact newContact(long contactId, String name, String pic, String bigPic, List<String> numbers, List<String> emails, String note, List<ContactDat> events, List<String> groups, Set<Label> labels) {
		
		return new Contact(contactId, name, pic) {{
			
			setData(ContactKey.BIG_PIC, bigPic);
			setData(ContactKey.NUMBERS, numbers);
			setData(ContactKey.EMAILS, emails);
			setData(ContactKey.EVENTS, events);
			setData(ContactKey.NOTE, note);
			setData(ContactKey.GROUPS, groups);
			setData(ContactKey.LABELS, labels);
		}};
	}
	
	@SuppressLint("Range")
	static String getBigPic(@NotNull ContentResolver resolver, long contactId) {
		
		Cursor cursor = resolver.query(
				ContactsContract.Contacts.CONTENT_URI,
				Lister.arrayOf(ContactsContract.Contacts.PHOTO_URI),
				ContactsContract.Contacts._ID + "=?",
				Lister.arrayOf(String.valueOf(contactId)),
				null
		);
		
		String pic = null;
		
		if (cursor != null) {
			
			if (cursor.moveToFirst())
				pic = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.PHOTO_URI));
			
			cursor.close();
		}
		
		return pic;
	}
	
	static
	@NotNull
	List<Contact> getSimpleContactList(@NotNull final ContentResolver resolver) {
		
		//region var cursor = resolver.query(...)
		Cursor cursor = resolver.query(
				ContactsContract.Contacts.CONTENT_URI,
				PROJECTION,
				null,
				null,
				null
		);
		//endregion
		
		if (cursor != null) {
			
			// region Setup indexes of contact column
			int contactIdCol = cursor.getColumnIndex(PROJECTION[0]);
			int nameCol      = cursor.getColumnIndex(PROJECTION[1]);
			int picCol       = cursor.getColumnIndex(PROJECTION[2]);
			// endregion
			
			// region Taking data in while loop and creating a contact object
			
			List<Contact> contacts = new ArrayList<>(cursor.getCount());
			
			while (cursor.moveToNext()) {
				
				Contact contact = new Contact(cursor.getLong(contactIdCol),
				                              cursor.getString(nameCol),
				                              cursor.getString(picCol));
				
				
				contacts.add(contact);
			}
			// endregion
			
			cursor.close();
			
			contacts.sort(PerfectSort.stringComparator(Contact::getName));
			return contacts;
		}
		
		//- return me
		return new ArrayList<>(0);
	}
	
	/**
	 * Kişi listesini verir.<br>
	 * Bu çağrıdan önce rehber okuma izni alınmış olmalı.<br>
	 * Bu metot kişileri almak için kullanılan ana metot.
	 *
	 * @param resolver ContentResolver
	 * @return Kişi listesi
	 */
	@NotNull
	static List<Contact> getContacts(@NotNull final ContentResolver resolver) {
		
		//region var cursor = resolver.query(...)
		Cursor cursor = resolver.query(
				ContactsContract.Contacts.CONTENT_URI,
				PROJECTION,
				null,
				null,
				null
		);
		//endregion
		
		if (cursor != null) {
			
			// region Setup indexes of contact column
			int contactIdCol = cursor.getColumnIndex(PROJECTION[0]);
			int nameCol      = cursor.getColumnIndex(PROJECTION[1]);
			int picCol       = cursor.getColumnIndex(PROJECTION[2]);
			int bigPicCol    = cursor.getColumnIndex(PROJECTION[3]);
			// endregion
			
			// region Taking data in while loop and creating a contact object
			
			List<Contact> contacts = new ArrayList<>(cursor.getCount());
			
			while (cursor.moveToNext()) {
				
				Contact contact = new Contact(cursor.getLong(contactIdCol),
				                              cursor.getString(nameCol),
				                              cursor.getString(picCol));
				
				String bigPic = cursor.getString(bigPicCol);
				contact.setData(ContactKey.BIG_PIC, bigPic);
				setContact(resolver, contact);
				contacts.add(contact);
			}
			// endregion
			
			cursor.close();
			
			//xlog.d("Found %d contacts", contacts.size());
			
			contacts.sort(PerfectSort.stringComparator(Contact::getName));
			
			return contacts;
		}
		xlog.d("No contact");
		//- return me
		return new ArrayList<>(0);
	}
	
	static void setContact(@NotNull final ContentResolver contentResolver, @NotNull Contact contact) {
		
		android.net.Uri uri    = Contents.getContactEntityUri(contact.getContactId());
		Cursor          cursor = contentResolver.query(uri, null, null, null, null);
		
		if (cursor == null) {return;}
		
		if (!cursor.moveToFirst()) {
			
			cursor.close();
			return;
		}
		
		_setContactDetails(cursor, contact);
	}
	
	/**
	 * Sets the contact information for the given contact.
	 *
	 * @param cursor  The cursor to read the contact information from.
	 * @param contact The contact to set the information for.
	 */
	@SuppressLint("Range")
	static void _setContactDetails(@NotNull Cursor cursor, @NotNull Contact contact) {
		
		int              data1Col    = cursor.getColumnIndex(DATA_COLUMNS[0]);
		int              mimeTypeCol = cursor.getColumnIndex(DATA_COLUMNS[1]);
		List<String>     emails      = new ArrayList<>(2);
		List<String>     numbers     = new ArrayList<>(2);
		List<String>     groups      = new ArrayList<>(2);
		List<ContactDat> events      = new ArrayList<>(2);
		
		do {
			
			String data1 = cursor.getString(data1Col);
			
			if (data1 == null) continue;
			
			String mimeType = cursor.getString(mimeTypeCol);
			//var data2    = cursor.getString(data2Col);
			
			switch (mimeType) {
				
				case ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE:
					emails.add(data1);
					break;
				case ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE:
					addNumbers(cursor, data1Col, numbers);
					break;
				case ContactsContract.CommonDataKinds.GroupMembership.CONTENT_ITEM_TYPE:
					groups.add(data1);
					break;
				case ContactsContract.CommonDataKinds.Event.CONTENT_ITEM_TYPE:
					addEvents(cursor, data1, events);
					break;
				case ContactsContract.CommonDataKinds.Note.CONTENT_ITEM_TYPE:
					contact.setData(ContactKey.NOTE, data1);
					break;
			}
			
		}
		while ((cursor.moveToNext()));
		
		cursor.close();
		
		if (!numbers.isEmpty()) contact.setData(ContactKey.NUMBERS, numbers);
		if (!emails.isEmpty()) contact.setData(ContactKey.EMAILS, emails);
		if (!groups.isEmpty()) contact.setData(ContactKey.GROUPS, groups);
		if (!events.isEmpty()) contact.setData(ContactKey.EVENTS, events);
	}
	
	static void addNumbers(@NotNull Cursor cursor, int data1Column, @NotNull List<String> numbers) {
		
		final int       numberLength = 13;
		@NotNull String number       = PhoneNumbers.formatNumber(cursor.getString(data1Column), numberLength);
		
		boolean notExist = numbers.stream()
				.noneMatch(num -> PhoneNumbers.equalsOrContains(number, num));
		
		if (notExist) numbers.add(number);
	}
	
	@SuppressLint("Range")
	static void addEvents(@NotNull Cursor cursor, String data1, @NotNull List<? super ContactDat> events) {
		
		int type = cursor.getInt(cursor.getColumnIndex(DATA_COLUMNS[2]));
		
		events.add(ContactDat.newData(data1, type));
	}
	
	/**
	 * Creates an Intent to launch the Android Contacts app with a new contact form.
	 * Takes in a phone number as a parameter and adds it to the Intent as an extra, so that it will be pre-filled in the contact form when the user launches it.
	 *
	 * @param number Phone number
	 * @return New {@code Intent}
	 */
	@NonNull
	static Intent createNewContactIntent(@NotNull String number) {
		
		Intent intent = createNewContactIntent();
		intent.putExtra(ContactsContract.Intents.Insert.PHONE, number);
		return intent;
	}
	
	/**
	 * Creates a new contact intent.
	 *
	 * @return the new contact intent
	 */
	@NonNull
	static Intent createNewContactIntent() {
		
		Intent intent = new Intent(ContactsContract.Intents.Insert.ACTION);
		intent.setType(ContactsContract.RawContacts.CONTENT_TYPE);
		intent.putExtra("finishActivityOnSaveCompleted", true);
		
		return intent;
	}
	
	/**
	 * Creates an Intent to add a new contact to the system contact book.
	 *
	 * @param name   The name of the new contact
	 * @param number The phone number of the new contact.
	 * @return An Intent to add a new contact to the system contact book.
	 */
	@NonNull
	static Intent createNewContactIntent(@NotNull String name, @NotNull String number) {
		
		Intent intent = createNewContactIntent();
		intent.putExtra(ContactsContract.Intents.Insert.NAME, name);
		intent.putExtra(ContactsContract.Intents.Insert.PHONE, number);
		return intent;
	}
	
	public static void setContactDetails(@NotNull final ContentResolver contentResolver, @NotNull Contact contact, @NotNull MimeTypeHandler handler) {
		
		android.net.Uri uri    = Contents.getContactEntityUri(contact.getContactId());
		Cursor          cursor = contentResolver.query(uri, null, null, null, null);
		
		if (cursor == null) {
			return;
		}
		
		if (!cursor.moveToFirst()) {
			
			cursor.close();
			return;
		}
		
		
		int data1Col    = cursor.getColumnIndex(DATA_COLUMNS[0]);
		int mimeTypeCol = cursor.getColumnIndex(DATA_COLUMNS[1]);
		int data2Col    = cursor.getColumnIndex(DATA_COLUMNS[2]);
		
		do {
			
			String data1 = cursor.getString(data1Col);
			
			if (data1 == null) continue;
			
			String mimeType = cursor.getString(mimeTypeCol);
			String data2    = cursor.getString(data2Col);
			
			handler.handleMimeType(mimeType, data1, data2);
			
			/* switch (mimeType) {
				
				case ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE:
					emails.add(data1);
					break;
				case ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE:
					addNumbers(cursor, data1Col, numbers);
					break;
				case ContactsContract.CommonDataKinds.GroupMembership.CONTENT_ITEM_TYPE:
					groups.add(data1);
					break;
				case ContactsContract.CommonDataKinds.Event.CONTENT_ITEM_TYPE:
					addEvents(cursor, data1, events);
					break;
				case ContactsContract.CommonDataKinds.Note.CONTENT_ITEM_TYPE:
					contact.setData(ContactKey.NOTE, data1);
					break;
			} */
			
		}
		while ((cursor.moveToNext()));
		
		cursor.close();
		
		handler.applyResult(contact);
	}
	
	/**
	 * Verilen contact id değerine ait kişi için belirli bir mimetype ile kaydedilmiş bilgileri toplar.<br>
	 * Bununla bir kişinin telefon numaraları veya email adresleri alınabilir.<br>
	 *
	 * <pre>{@code
	 * List<String> getMailAddresses(@NotNull ContentResolver contentResolver, long contactId) {
	 *
	 *    return getByMimeType(contentResolver, contactId, ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE);
	 * }
	 * }</pre>
	 *
	 * @param contentResolver contentResolver
	 * @param contactId       contactId
	 * @param mimeType        mimeType
	 * @return list of data
	 */
	@NotNull
	static List<String> getByMimeType(@NotNull final ContentResolver contentResolver, final long contactId, @NotNull final String mimeType) {
		
		android.net.Uri uri    = Contents.getContactEntityUri(contactId);
		Cursor          cursor = contentResolver.query(uri, DATA_COLUMNS, DATA_COLUMNS[1] + "=?", new String[]{mimeType}, null);
		
		if (cursor != null) {
			
			int          data1Col    = cursor.getColumnIndex(DATA_COLUMNS[0]);
			int          mimeTypeCol = cursor.getColumnIndex(DATA_COLUMNS[1]);
			List<String> data        = new ArrayList<>(cursor.getCount());
			
			while (cursor.moveToNext()) {
				
				String _mimeType = cursor.getString(mimeTypeCol);
				
				if (mimeType.equals(_mimeType)) {
					
					String data1 = cursor.getString(data1Col);
					
					if (data1 != null) data.add(data1);
				}
			}
			
			cursor.close();
			return data;
		}
		
		return new ArrayList<>(0);
	}
	
	static long getContactId(@NotNull Context context, @NotNull String phoneNumber) {
		
		if (phoneNumber.trim().isEmpty()) return 0;
		
		ContentResolver resolver = context.getContentResolver();
		
		Cursor cursor = resolver.query(
				ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
				Lister.arrayOf(ContactsContract.CommonDataKinds.Phone.NUMBER, ContactsContract.CommonDataKinds.Phone.CONTACT_ID),
				null,
				null,
				null,
				null
		);
		
		if (cursor == null) return 0;
		
		if (cursor.getCount() == 0) {
			
			cursor.close();
			return 0;
		}
		
		long id        = 0;
		int  idCol     = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID);
		int  numberCol = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
		
		while (cursor.moveToNext()) {
			
			String number = cursor.getString(numberCol);
			
			if (PhoneNumbers.equalsOrContains(phoneNumber, number)) {
				
				id = cursor.getLong(idCol);
				break;
			}
		}
		
		cursor.close();
		return id;
	}
	
}