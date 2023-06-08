package com.tr.hsyn.content;


import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.tr.hsyn.content.handler.ContentHandler;

import java.util.ArrayList;
import java.util.List;


public class Contents {
	
	@NonNull
	public static <T> List<T> getContacts(
			@NonNull ContentResolver resolver,
			@Nullable String selection,
			@Nullable String[] args,
			@NonNull ContentHandler<T> contentHandler) {
		
		
		return getContents(resolver, ContactsContract.Contacts.CONTENT_URI, selection, args, contentHandler);
	}
	
	@NonNull
	public static <T> List<T> getPhoneContacts(
			@NonNull ContentResolver resolver,
			@Nullable String selection,
			@Nullable String[] args,
			@NonNull ContentHandler<T> contentHandler) {
		
		
		return getContents(resolver, ContactsContract.CommonDataKinds.Phone.CONTENT_URI, selection, args, contentHandler);
	}
	
	/**
	 * Android veri tabanından bilgi almak.
	 *
	 * @param cursor         Cursor
	 * @param contentHandler Bilgiyi işleyecek olan nesne
	 * @param <T>            Bilgilerin temsil edileceği nesne türü
	 * @return Nesne listesi
	 */
	@NonNull
	public static <T> List<T> getContents(
			@NonNull ContentResolver resolver,
			@NonNull Cursor cursor,
			@NonNull ContentHandler<T> contentHandler) {
		
		List<T> list = new ArrayList<>(cursor.getCount());
		
		contentHandler.onCreateCursor(resolver, cursor);
		
		while (cursor.moveToNext()) {
			
			T content = contentHandler.handle(cursor);
			
			if (contentHandler.predicate(content, list))
				list.add(content);
		}
		
		if (contentHandler.getComparator() != null) list.sort(contentHandler.getComparator());
		
		cursor.close();
		return list;
	}
	
	/**
	 * Android veri tabanından bilgi almak.
	 *
	 * @param resolver       ContentResolver
	 * @param uri            Uri
	 * @param selection      Özel seçim
	 * @param selectionArgs  Seçim argümanları
	 * @param contentHandler Bilgiyi işleyecek nesne
	 * @param <T>            Bilginin çevrileceği tür
	 * @return Liste
	 */
	@NonNull
	public static <T> List<T> getContents(
			@NonNull ContentResolver resolver,
			@NonNull Uri uri,
			@Nullable String selection,
			@Nullable String[] selectionArgs,
			@NonNull ContentHandler<T> contentHandler) {
		
		Cursor cursor = resolver.query(uri, contentHandler.getProjection(), selection, selectionArgs, contentHandler.getSortOrder());
		
		if (cursor == null) return new ArrayList<>(0);
		
		return getContents(resolver, cursor, contentHandler);
	}
	
	/**
	 * Kişinin ekstra bilgilerini sunan adresi döndürür.
	 * Dönen adress, kişiyle ilgili hemen hemen tüm bilgileri barındırır.
	 *
	 * @param contactId contactId
	 * @return Uri
	 */
	public static Uri getContactEntityUri(final long contactId) {
		
		return ContactsContract.Contacts.CONTENT_URI.buildUpon()
				.appendEncodedPath(String.valueOf(contactId))
				.appendPath(ContactsContract.Contacts.Entity.CONTENT_DIRECTORY)
				.build();
	}
	
	@SuppressWarnings("SameParameterValue")
	public static Cursor createCursor(@NonNull final ContentResolver contentResolver,
	                                  @NonNull final Uri uri,
	                                  @Nullable final String[] projection,
	                                  @Nullable final String selection,
	                                  @Nullable final String[] selectionArgs,
	                                  @Nullable final String sortOrder) {
		
		return contentResolver.query(uri, projection, selection, selectionArgs, sortOrder);
	}
	
	
	public static Cursor createCursor(@NonNull final ContentResolver contentResolver, @NonNull final Uri uri) {
		
		return createCursor(contentResolver, uri, null, null, null, null);
	}
	
	public static Cursor createCursor(@NonNull final ContentResolver contentResolver, @NonNull final Uri uri, @Nullable final String[] projection) {
		
		return createCursor(contentResolver, uri, projection, null, null, null);
	}
	
}
