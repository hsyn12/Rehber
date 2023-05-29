package com.tr.hsyn.telefonrehberi.main.call.data;


import android.content.ContentResolver;

import androidx.annotation.NonNull;

import com.tr.hsyn.calldata.Call;
import com.tr.hsyn.content.fetcher.ContentFetcher;
import com.tr.hsyn.content.requestor.ContentRequester;
import com.tr.hsyn.content.server.Server;

import java.util.List;


public class CallsServer implements Server<Call> {
	
	private final ContentFetcher<Call> contentFetcher;
	
	public CallsServer(@NonNull ContentResolver contentResolver) {
		
		ContentRequester<Call> contentRequester = new CallsRequester(contentResolver, null, null);
		contentFetcher = new CallsFetcher(contentRequester);
	}
	
	@NonNull
	@Override
	public List<Call> getContents() {
		
		return contentFetcher.fetch();
	}
	
	
}
