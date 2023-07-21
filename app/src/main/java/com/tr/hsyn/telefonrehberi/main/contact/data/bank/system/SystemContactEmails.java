package com.tr.hsyn.telefonrehberi.main.contact.data.bank.system;


import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.provider.ContactsContract;

import com.tr.hsyn.telefonrehberi.main.contact.data.ContactsReader;

import org.jetbrains.annotations.NotNull;

import java.util.List;


public interface SystemContactEmails {
	
	@NotNull
	static List<String> getMailAddresses(@NotNull final ContentResolver contentResolver, final long contactId) {
		
		return ContactsReader.getByMimeType(contentResolver, contactId, ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE);
	}
	
	/**
	 * Rehberdeki bir kişi için email adresi ekler.
	 *
	 * @param context context
	 * @param rawId   rawId
	 * @param email   email
	 * @return eklenen yeni satıra {@link Uri}, ekleme başarısız olursa {@code null}
	 */
	static Uri addEmail(Context context, String rawId, String email) {
		
		//raw contact id zorunlu
		
		if (context == null || rawId == null || rawId.trim().isEmpty() || email == null || email.trim().isEmpty())
			return null;
		
		ContentValues values = new ContentValues();
		
		values.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE);
		values.put(ContactsContract.CommonDataKinds.Email.RAW_CONTACT_ID, rawId);
		values.put(ContactsContract.CommonDataKinds.Email.ADDRESS, email);
		values.put(ContactsContract.CommonDataKinds.Email.TYPE, ContactsContract.CommonDataKinds.Email.TYPE_MOBILE);
		
		return context.getContentResolver().insert(ContactsContract.Data.CONTENT_URI, values);
	}
}
