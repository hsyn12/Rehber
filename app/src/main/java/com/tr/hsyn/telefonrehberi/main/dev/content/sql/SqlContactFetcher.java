package com.tr.hsyn.telefonrehberi.main.dev.content.sql;


import androidx.annotation.NonNull;

import com.tr.hsyn.contactdata.Contact;
import com.tr.hsyn.content.fetcher.SQLContentFetcher;
import com.tr.hsyn.content.requestor.SQLContentRequester;


public class SqlContactFetcher implements SQLContentFetcher<Contact> {
	
	private final SQLContentRequester<Contact> contentRequester;
	
	public SqlContactFetcher(@NonNull SQLContentRequester<Contact> contentRequester) {
		
		this.contentRequester = contentRequester;
	}
	
	@NonNull
	@Override
	public SQLContentRequester<Contact> getContentRequester() {
		
		return contentRequester;
	}
}
