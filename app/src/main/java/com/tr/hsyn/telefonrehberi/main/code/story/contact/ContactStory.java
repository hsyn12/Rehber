package com.tr.hsyn.telefonrehberi.main.code.story.contact;


import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.ContentResolver;
import android.content.OperationApplicationException;
import android.net.Uri;
import android.os.RemoteException;
import android.provider.ContactsContract;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.tr.hsyn.contactdata.Account;
import com.tr.hsyn.keep.Keep;
import com.tr.hsyn.string.Stringx;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;


/**
 * Rehber Yöneticisi.<br>
 * Rehber üzerinde yapılacak tüm işlemler bu sınıf üzerinden yürütülür.
 */
@Keep
@Deprecated(forRemoval = true)
public class ContactStory {
	
	/**
	 * Sistem rehberine kişi ekler.
	 *
	 * @param resolver resolver
	 * @param name     name
	 * @param numbers  numbers
	 * @return ContentProviderResult dizisi
	 */
	@Nullable
	private static ContentProviderResult[] addContact(@NonNull ContentResolver resolver, @NotNull String name, @NotNull Iterable<String> numbers) {
		
		ArrayList<ContentProviderOperation> ops                   = new ArrayList<>();
		int                                 rawContactInsertIndex = 0;
		
		ops.add(ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI)
				        .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
				        .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null)
				        .build());
		
		
		ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
				        .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, rawContactInsertIndex)
				        .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
				        .withValue(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, name)
				        .build());
		
		for (var number : numbers)
			ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
					        .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, rawContactInsertIndex)
					        .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
					        .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, number)
					        .withValue(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE)
					        .build());
		
		
		try {
			return resolver.applyBatch(ContactsContract.AUTHORITY, ops);
		}
		catch (RemoteException | OperationApplicationException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@Nullable
	private static ContentProviderResult[] addContact(@NonNull ContentResolver resolver, Account account, @NonNull String name, @NotNull Iterable<String> numbers) {
		
		ArrayList<ContentProviderOperation> ops                   = new ArrayList<>();
		int                                 rawContactInsertIndex = 0;
		
		ops.add(ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI)
				        .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, account == null ? null : account.getType())
				        .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, account == null ? null : account.getName())
				        .build());
		
		
		ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
				        .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, rawContactInsertIndex)
				        .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
				        .withValue(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, name)
				        .build());
		
		for (var number : numbers)
			ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
					        .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, rawContactInsertIndex)
					        .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
					        .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, number)
					        .withValue(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE)
					        .build());
		
		
		try {
			return resolver.applyBatch(ContactsContract.AUTHORITY, ops);
		}
		catch (RemoteException | OperationApplicationException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Sistem kayıtlarından kişiyi sil.
	 *
	 * @param contentResolver ContentResolver
	 * @param contactId       contact id
	 * @return Silme başarılı olursa {@code true}.
	 */
	private static boolean deleteContact(@NotNull final ContentResolver contentResolver, @NotNull final String contactId) {
		
		Uri      contactUri  = ContactsContract.RawContacts.CONTENT_URI.buildUpon().appendQueryParameter(ContactsContract.CALLER_IS_SYNCADAPTER, "true").build();
		String   whereClause = ContactsContract.RawContacts.CONTACT_ID + " = ?";
		String[] args        = {contactId};
		
		return contentResolver.delete(contactUri, whereClause, args) > 0;
	}
	
	/**
	 * Kişileri sistem kayıtlarından siler.
	 *
	 * @param contentResolver contentResolver
	 * @param contactIds      Silinecek kişilere ait contact id listesi
	 * @return Silinen kişi sayısı
	 */
	private static int deleteContacts(@NotNull final ContentResolver contentResolver, @NotNull final List<String> contactIds) {
		
		Uri    contactUri  = ContactsContract.RawContacts.CONTENT_URI.buildUpon().appendQueryParameter(ContactsContract.CALLER_IS_SYNCADAPTER, "true").build();
		String whereClause = Stringx.format("%s in (%s)", ContactsContract.RawContacts.CONTACT_ID, Stringx.joinToString(contactIds));
		
		return contentResolver.delete(contactUri, whereClause, null);
	}
	
	
}
