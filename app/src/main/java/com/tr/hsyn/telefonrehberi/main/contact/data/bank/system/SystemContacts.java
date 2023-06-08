package com.tr.hsyn.telefonrehberi.main.contact.data.bank.system;


import android.annotation.SuppressLint;
import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.Context;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.net.Uri;
import android.os.RemoteException;
import android.provider.ContactsContract;

import com.tr.hsyn.contactdata.Contact;
import com.tr.hsyn.phone_numbers.PhoneNumbers;
import com.tr.hsyn.string.Stringx;
import com.tr.hsyn.xlog.xlog;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;


public interface SystemContacts {
	
	static boolean addContact(@NotNull ContentResolver resolver, @NotNull String name, @NotNull String number) {
		
		ArrayList<ContentProviderOperation> ops                   = new ArrayList<>();
		int                                 rawContactInsertIndex = 0;
		//ContentProviderResult[]             results               = null;
		
		ops.add(ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI)
				        .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
				        .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null)
				        .build());
		
		
		ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
				        .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, rawContactInsertIndex)
				        .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
				        .withValue(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, name)
				        .build());
		
		
		ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
				        .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, rawContactInsertIndex)
				        .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
				        .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, number)
				        .withValue(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE)
				        .build());
		
		
		try {
			resolver.applyBatch(ContactsContract.AUTHORITY, ops);
			return true;
		}
		catch (RemoteException | OperationApplicationException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * Verilen raw contact uri ile contact id değerini bul.
	 *
	 * @param contentResolver cr
	 * @param rawContactUri   Raw Contact Uri
	 * @return Contact id
	 */
	@SuppressLint("Range")
	static String getContactIdFromRawId(@NotNull final ContentResolver contentResolver, @NotNull final Uri rawContactUri) {
		
		final Cursor cursor = contentResolver.query(
				
				rawContactUri,
				new String[]{ContactsContract.RawContacts.CONTACT_ID},
				null,
				null,
				null
		);
		
		if (cursor == null || cursor.getCount() == 0) {
			
			if (cursor != null) {
				
				cursor.close();
			}
			
			return null;
		}
		
		String id = null;
		
		while (cursor.moveToNext()) {
			
			id = cursor.getString(cursor.getColumnIndex(ContactsContract.RawContacts.CONTACT_ID));
			
			xlog.d("Bulunan id : %s", id);
		}
		
		
		cursor.close();
		return id;
	}
	
	@SuppressLint("Range")
	static void testUri(Cursor cursor, Context context) {
		
		String[] columnNames = cursor.getColumnNames();
		
		StringBuilder s = new StringBuilder();
		
		try {
			
			while (cursor.moveToNext()) {
				
				for (String column : columnNames) {
					
					String value = cursor.getString(cursor.getColumnIndex(column));
					
					s.append(Stringx.format("%40s : %s%n", column, value));
					
				}
				s.append("================================================================\n");
			}
			
			cursor.close();
		}
		catch (Exception e) {
			
			xlog.e(e);
		}
		
		//xlog.d(s);
		
		
		File file = new File(context.getFilesDir(), "data.txt");
		
		try {
			//noinspection ResultOfMethodCallIgnored
			file.createNewFile();
			
			FileOutputStream out = new FileOutputStream(file, true);
			
			out.write("****************************************************\n".getBytes());
			out.write(s.toString().getBytes());
			
			out.close();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	@SuppressLint("Range")
	static void testUri(Cursor cursor, Context context, String fileName) {
		
		String[] columnNames = cursor.getColumnNames();
		
		StringBuilder s = new StringBuilder();
		
		try {
			
			while (cursor.moveToNext()) {
				
				for (String column : columnNames) {
					
					String value = cursor.getString(cursor.getColumnIndex(column));
					
					s.append(Stringx.format("%40s : %s%n", column, value));
					
				}
				s.append("================================================================\n");
			}
			
			cursor.close();
		}
		catch (Exception e) {
			
			xlog.e(e);
		}
		
		//xlog.d(s);
		
		
		File file = new File(context.getFilesDir(), fileName);
		
		try {
			//noinspection ResultOfMethodCallIgnored
			file.createNewFile();
			
			FileOutputStream out = new FileOutputStream(file, true);
			
			out.write("****************************************************\n".getBytes());
			out.write(s.toString().getBytes());
			
			out.close();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	@SuppressLint("Range")
	static void writeUri(Cursor cursor, Context context) {
		
		String[] columnNames = cursor.getColumnNames();
		
		StringBuilder s = new StringBuilder();
		
		try {
			
			while (cursor.moveToNext()) {
				
				for (String column : columnNames) {
					
					String value = cursor.getString(cursor.getColumnIndex(column));
					
					s.append(Stringx.format("%40s : %s%n", column, value));
					
				}
				s.append("================================================================\n");
			}
			
			cursor.close();
		}
		catch (Exception e) {
			
			xlog.e(e);
		}
		
		xlog.d(s);


			/*File file = new File(context.getFilesDir(), "data.txt");

			try {
				//noinspection ResultOfMethodCallIgnored
				file.createNewFile();

				FileOutputStream out = new FileOutputStream(file, true);

				out.write("****************************************************\n".getBytes());
				out.write(s.toString().getBytes());

				out.close();
			}
			catch (IOException e) {
				e.printStackTrace();
			}*/
		
	}
	
	@SuppressLint("Range")
	static void lookUri(Context context) {
		
		Uri uri = ContactsContract.Contacts.CONTENT_URI;
		
		Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
		
		if (cursor == null) return;
		
		while (cursor.moveToNext()) {
			
			String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
			
			Uri data = ContactsContract.Contacts.CONTENT_URI.buildUpon()
					.appendEncodedPath(id)
					.appendPath(ContactsContract.Contacts.Data.CONTENT_DIRECTORY)
					.build();
			
			
			//var p = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, id);
			
			//var data = Uri.withAppendedPath(p, ContactsContract.Contacts.Entity.CONTENT_DIRECTORY);
			
			try {
				
				//Thread.sleep(15000);
				
				xlog.w("================================================");
				xlog.w("Current : %s", String.valueOf(data));
				testUri(data, context);
				xlog.w("================================================");
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		
		cursor.close();
	}
	
	@SuppressLint("Range")
	static void testUri(Uri uri, Context context) {
		
		StringBuilder s = new StringBuilder();
		
		try {
			
			Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
			
			String[] columnNames = cursor.getColumnNames();
			
			xlog.w("Cursor count : %d", cursor.getCount());
			
			while (cursor.moveToNext()) {
				
				for (String column : columnNames) {
					
					try {
						
						String value = cursor.getString(cursor.getColumnIndex(column));
						
						s.append(Stringx.format("%40s : %s%n", column, value));
					}
					catch (Exception e) {
						
						e.printStackTrace();
					}
				}
				s.append("================================================================\n");
			}
			
			cursor.close();
		}
		catch (Exception e) {
			
			xlog.e(e);
		}

		/*xlog.d("==================================");
		xlog.d(s);
		xlog.d("==================================");*/
		
		//noinspection ConstantConditions
		if (false) return;
		
		File file = new File(context.getFilesDir(), String.join(".", uri.getPathSegments()));
		
		try {
			
			if (file.createNewFile()) {
				
				FileOutputStream out = new FileOutputStream(file, true);
				
				out.write("****************************************************\n".getBytes());
				out.write(s.toString().getBytes());
				
				out.close();
			}
			else {
				
				xlog.w("Dosya oluşturulamadı : %s", file);
			}
			
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	@SuppressLint("Range")
	@Nullable
	static String getLookupKey(@NotNull ContentResolver resolver, String contactId) {
		
		Cursor cursor = resolver.query(
				
				ContactsContract.Contacts.CONTENT_URI,
				new String[]{ContactsContract.Contacts.LOOKUP_KEY},
				ContactsContract.Contacts._ID + " = ?",
				new String[]{contactId},
				null
		);
		
		if (cursor == null) return null;
		
		if (cursor.getCount() == 0) {
			
			cursor.close();
			return null;
		}
		
		String key = null;
		
		if (cursor.moveToFirst()) {
			
			key = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.LOOKUP_KEY));
		}
		
		cursor.close();
		return key;
	}
	
	@SuppressLint("Range")
	static @Nullable Contact getContact(@NotNull ContentResolver resolver, @NotNull String number) {
		
		String[] columns = {
				ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
				ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME_PRIMARY,
				ContactsContract.CommonDataKinds.Phone.NUMBER
		};
		
		Cursor cursor = resolver.query(
				ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
				columns,
				null,
				null,
				null);
		
		
		if (cursor != null) {
			
			while (cursor.moveToNext()) {
				
				String _number = cursor.getString(cursor.getColumnIndex(columns[2]));
				
				if (PhoneNumbers.equals(_number, number)) {
					
					Contact c = new Contact(
							cursor.getLong(cursor.getColumnIndex(columns[0])),
							cursor.getString(cursor.getColumnIndex(columns[1])),
							cursor.getString(cursor.getColumnIndex(columns[2]))
					);
					
					cursor.close();
					return c;
				}
			}
			
			cursor.close();
		}
		
		return null;
	}
	
}
