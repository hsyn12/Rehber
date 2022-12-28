package com.tr.hsyn.telefonrehberi.main.code.call.cast;


import com.tr.hsyn.calldata.SystemCall;
import com.tr.hsyn.content.fetcher.ContentFetcher;
import com.tr.hsyn.content.requestor.ContentRequester;


public class CallsFetcher implements ContentFetcher<SystemCall> {

    private final ContentRequester<SystemCall> callContentRequester;

    public CallsFetcher(ContentRequester<SystemCall> callContentRequester) {

        this.callContentRequester = callContentRequester;
    }

    @Override
    public ContentRequester<SystemCall> getContentRequester() {

        return callContentRequester;
    }
}
