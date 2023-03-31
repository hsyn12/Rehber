package com.tr.hsyn.telefonrehberi.main.code.call.cast;


import com.tr.hsyn.calldata.Call;
import com.tr.hsyn.content.fetcher.ContentFetcher;
import com.tr.hsyn.content.requestor.ContentRequester;


public class CallsFetcher implements ContentFetcher<Call> {
	
	private final ContentRequester<Call> callContentRequester;
	
	public CallsFetcher(ContentRequester<Call> callContentRequester) {
		
		this.callContentRequester = callContentRequester;
	}
	
	@Override
	public ContentRequester<Call> getContentRequester() {
		
		return callContentRequester;
	}
}
