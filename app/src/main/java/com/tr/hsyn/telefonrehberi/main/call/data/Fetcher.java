package com.tr.hsyn.telefonrehberi.main.call.data;


import com.tr.hsyn.calldata.Call;
import com.tr.hsyn.content.fetcher.ContentFetcher;
import com.tr.hsyn.content.requestor.ContentRequester;


public class Fetcher implements ContentFetcher<Call> {
	
	private final ContentRequester<Call> callContentRequester;
	
	public Fetcher(ContentRequester<Call> callContentRequester) {
		
		this.callContentRequester = callContentRequester;
	}
	
	@Override
	public ContentRequester<Call> getContentRequester() {
		
		return callContentRequester;
	}
}
