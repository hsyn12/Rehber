package com.tr.hsyn.telefonrehberi.main.dev.content;


import com.tr.hsyn.contactdata.Contact;
import com.tr.hsyn.content.fetcher.ContentFetcher;
import com.tr.hsyn.content.requestor.ContentRequester;


public class ContactFetcher implements ContentFetcher<Contact> {
	
	private final ContentRequester<Contact> requester;
	
	public ContactFetcher(ContentRequester<Contact> requester) {
		
		this.requester = requester;
	}
	
	@Override
	public ContentRequester<Contact> getContentRequester() {
		
		return requester;
	}
}
