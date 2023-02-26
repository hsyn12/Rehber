package com.tr.hsyn.telefonrehberi.main.dev.content;


import android.content.ContentResolver;
import android.net.Uri;
import android.provider.ContactsContract;

import com.tr.hsyn.contactdata.Contact;
import com.tr.hsyn.content.handler.ContentHandler;
import com.tr.hsyn.content.requestor.ContentRequester;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


public final class ContactRequester implements ContentRequester<Contact> {
	
	private final ContentResolver         contentResolver;
	private final String                  selection;
	private final String[]                selectionArgs;
	private final ContentHandler<Contact> contentHandler = new MainContactContentHandler();
	
	public ContactRequester(@NotNull ContentResolver contentResolver) {
		
		this(contentResolver, null, null);
	}
	
	public ContactRequester(ContentResolver contentResolver, String selection, String[] selectionArgs) {
		
		this.contentResolver = contentResolver;
		this.selection       = selection;
		this.selectionArgs   = selectionArgs;
	}
	
	@NotNull
	@Override
	public ContentResolver getContentResolver() {
		
		return contentResolver;
	}
	
	@NotNull
	@Override
	public Uri getContentUri() {
		
		return ContactsContract.Contacts.CONTENT_URI;
	}
	
	@Nullable
	@Override
	public String getSelection() {
		
		return selection;
	}
	
	@Nullable
	@Override
	public String[] getSelectionArgs() {
		
		return selectionArgs;
	}
	
	@NotNull
	@Override
	public ContentHandler<Contact> getContentHandler() {
		
		return contentHandler;
	}
}
