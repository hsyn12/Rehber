package com.tr.hsyn.telefonrehberi.main.contact.data.bank.system;


import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.provider.ContactsContract;

import androidx.annotation.NonNull;

import com.tr.hsyn.string.Stringx;
import com.tr.hsyn.xlog.xlog;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;


public interface SystemContactUpdater {
	
	
	/**
	 * Sadece ve sadece telefon numarasını değiştirmek-güncellemek için.
	 * Numaranın türü veya başka bir bilgisi değiştirilmez, sadece "<u>numara</u>".
	 *
	 * @param context   app context
	 * @param number    Değişecek olan numara.
	 * @param newNumber Kaydedilecek yeni numara.
	 * @return Eğer güncelleme yapılırsa true
	 */
	static boolean updateContactNumber(final Context context, String number, @Nullable String newNumber) {
		
		if (context == null || number == null || number.trim().isEmpty()) {
			return false;
		}
		
		if (newNumber != null && newNumber.trim().isEmpty()) {
			newNumber = null;
		}
		
		if (newNumber == null) {
			
			//todo eğer numara null ise numarayı ve diğer bilgileri başka bir metotla sil
			//şimdilik false
			return false;
		}
		
		String[] PHONE_COLUMNS = {
				
				ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
				ContactsContract.CommonDataKinds.Phone.NUMBER
			
		};
		
		String[] DATA_COLUMNS = {
				
				ContactsContract.Data.MIMETYPE,
				ContactsContract.Data.CONTACT_ID
			
		};
		
		ContentValues values = new ContentValues();
		values.put(PHONE_COLUMNS[1], newNumber);
		
		String where = Stringx.format(
				"%s = '%s' AND %s = ?",
				DATA_COLUMNS[0],
				ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE,
				DATA_COLUMNS[1]);
		
		String[] args = {number};
		
		int rows = context.getContentResolver().update(ContactsContract.Data.CONTENT_URI, values, where, args);
		
		return rows != 0;
	}
	
	static boolean updateNameAndNumber(final Context context, String number, String newName, String newNumber) {
		
		if (context == null || number == null || number.trim().isEmpty()) {
			return false;
		}
		
		if (newNumber != null && newNumber.trim().isEmpty()) {
			newNumber = null;
		}
		
		if (newNumber == null) {
			return false;
		}
		
		String[] PHONE_COLUMNS = {
				
				ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
				ContactsContract.CommonDataKinds.Phone.NUMBER
			
		};
		
		String[] DATA_COLUMNS = {
				
				ContactsContract.Data.MIMETYPE,
				ContactsContract.Data.CONTACT_ID
			
		};
		
		
		long contactId = SystemContactNumbers.getContactId(context.getContentResolver(), number);
		
		if (contactId == 0) {
			return false;
		}
		
		//selection for name
		String where = String.format(
				"%s = '%s' AND %s = ?",
				DATA_COLUMNS[0], //mimetype
				ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE,
				DATA_COLUMNS[1]/*contactId*/);
		
		String[] args = {String.valueOf(contactId)};
		
		ArrayList<ContentProviderOperation> operations = new ArrayList<>();
		
		operations.add(
				ContentProviderOperation.newUpdate(ContactsContract.Data.CONTENT_URI)
						.withSelection(where, args)
						.withValue(ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME, newName)
						.build()
		);
		
		//change selection for number
		where = String.format(
				"%s = '%s' AND %s = ?",
				DATA_COLUMNS[0],//mimetype
				ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE,
				DATA_COLUMNS[1]/*number*/);
		
		//change args for number
		args[0] = number;
		
		operations.add(
				ContentProviderOperation.newUpdate(ContactsContract.Data.CONTENT_URI)
						.withSelection(where, args)
						.withValue(DATA_COLUMNS[1]/*number*/, newNumber)
						.build()
		);
		
		try {
			
			ContentProviderResult[] results = context.getContentResolver().applyBatch(ContactsContract.AUTHORITY, operations);
			
			for (ContentProviderResult result : results) {
				
				xlog.d("Update Result", result.toString());
			}
			
			return true;
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return false;
	}
	
	/**
	 * Verilen isme ait kişiyi yeni telefon numarası ile günceller.
	 *
	 * @param resolver resolver
	 * @param name     name
	 * @return Başarılı ise {@code true}
	 */
	static boolean update(@NotNull ContentResolver resolver, @NonNull String primaryKey, @NonNull String primaryValue, @NotNull String name, @NotNull List<String> numbers) {
		
		
		ArrayList<ContentProviderOperation> ops = new ArrayList<>();
		
		// ContactsContract.Data.CONTACT_ID
		String where = primaryKey + " = ? AND " +
		               ContactsContract.Data.MIMETYPE + " = ?";
		
		String[] params = {
				primaryValue,
				ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE
		};
		
		for (var number : numbers) {
			
			ops.add(ContentProviderOperation.newUpdate(ContactsContract.Data.CONTENT_URI)
					        .withSelection(where, params)
					        .withValue(ContactsContract.CommonDataKinds.Phone.DATA, number)
					        .build());
		}
		
		
		try {
			resolver.applyBatch(ContactsContract.AUTHORITY, ops);
			return true;
			
		}
		catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * {@code contactId} değerine bir kişiyi rehberden siler.
	 *
	 * @param resolver  resolver
	 * @param contactId contactId
	 * @return Silme işlemi başarılı ise {@code true}
	 */
	static int delete(@NotNull ContentResolver resolver, long contactId) {
		
		Uri contactUri = ContactsContract.RawContacts.CONTENT_URI.buildUpon().appendQueryParameter(ContactsContract.CALLER_IS_SYNCADAPTER, "true").build();
		
		return resolver.delete(contactUri, createSelectionEqual(ContactsContract.RawContacts.CONTACT_ID), createArgs(String.valueOf(contactId)));
	}
	
	@NonNull
	static String createSelectionEqual(@NonNull String key) {
		
		return String.format("%s=?", key);
	}
	
	@NonNull
	static String[] createArgs(String... values) {
		
		return values;
	}
	
	static int delete(@NotNull ContentResolver resolver, List<String> contactIds) {
		
		Uri contactUri = ContactsContract.RawContacts.CONTENT_URI.buildUpon().appendQueryParameter(ContactsContract.CALLER_IS_SYNCADAPTER, "true").build();
		
		return resolver.delete(contactUri, createSelectionIn(ContactsContract.RawContacts.CONTACT_ID, contactIds), null);
	}
	
	@NonNull
	static String createSelectionIn(@NonNull String key, @NonNull List<String> values) {
		
		return String.format("%s in (%s)", key, String.join(",", values));
	}
}
