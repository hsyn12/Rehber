package com.tr.hsyn.telefonrehberi.main.dev.content;


import android.content.ContentResolver;
import android.database.Cursor;
import android.provider.ContactsContract;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.tr.hsyn.contactdata.Contact;
import com.tr.hsyn.content.handler.ContentHandler;
import com.tr.hsyn.perfectsort.PerfectSort;

import java.util.Comparator;


/**
 * Ana ekranda gösterilecek olan kişileri nesneleştirecek.
 * Minimal bilgi sağlayacak, {@code contactId, name, pic}.<br>
 * Ana ekran için bu kadarı yeterli.
 */
public class MainContactContentHandler implements ContentHandler<Contact> {
	
	
	private final String[]        PROJECTION = {
			ContactsContract.Contacts._ID,
			ContactsContract.Contacts.DISPLAY_NAME_PRIMARY,
			ContactsContract.Contacts.PHOTO_THUMBNAIL_URI,
			ContactsContract.Contacts.PHOTO_URI
	};
	private       int             contactIdCol;
	private       int             nameCol;
	private       int             picCol;
	private       int             bigPicCol;
	private       ContentResolver contentResolver;
	
	@Override
	public void onCreateCursor(@NonNull ContentResolver contentResolver, @NonNull Cursor cursor) {
		
		this.contentResolver = contentResolver;
		contactIdCol         = cursor.getColumnIndex(PROJECTION[0]);
		nameCol              = cursor.getColumnIndex(PROJECTION[1]);
		picCol               = cursor.getColumnIndex(PROJECTION[2]);
		bigPicCol            = cursor.getColumnIndex(PROJECTION[3]);
	}
	
	@NonNull
	@Override
	public Contact handle(@NonNull final Cursor cursor) {
		
		return new Contact(
				cursor.getLong(contactIdCol),
				cursor.getString(nameCol),
				cursor.getString(picCol));
	}
	
	@Override
	public String[] getProjection() {
		
		return PROJECTION;
	}
	
	@Nullable
	@Override
	public Comparator<Contact> getComparator() {
		
		return PerfectSort.comparator(Contact::getName);
	}
}
