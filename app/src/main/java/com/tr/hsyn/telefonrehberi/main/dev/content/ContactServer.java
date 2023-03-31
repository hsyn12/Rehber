package com.tr.hsyn.telefonrehberi.main.dev.content;


import android.content.ContentResolver;

import androidx.annotation.NonNull;

import com.tr.hsyn.contactdata.Contact;
import com.tr.hsyn.content.fetcher.ContentFetcher;
import com.tr.hsyn.content.requestor.ContentRequester;
import com.tr.hsyn.content.server.Server;

import java.util.List;


public class ContactServer implements Server<Contact> {
	
	private final ContentFetcher<Contact> contentFetcher;
	
	public ContactServer(@NonNull ContentResolver contentResolver) {
		
		ContentRequester<Contact> requester = new ContactRequester(contentResolver);
		contentFetcher = new ContactFetcher(requester);
	}
	
	public ContactServer(@NonNull ContentRequester<Contact> requester) {
		
		contentFetcher = new ContactFetcher(requester);
	}
	
	@NonNull
	@Override
	public List<Contact> getContents() {
		
		return contentFetcher.fetch();
	}
}
