package com.tr.hsyn.telefonrehberi.main.code.call.cast;


import android.content.ContentResolver;

import androidx.annotation.NonNull;

import com.tr.hsyn.calldata.SystemCall;
import com.tr.hsyn.content.fetcher.ContentFetcher;
import com.tr.hsyn.content.requestor.ContentRequester;
import com.tr.hsyn.content.server.Server;

import java.util.List;


public class CallsServer implements Server<SystemCall> {

    private final ContentFetcher<SystemCall> contentFetcher;

    public CallsServer(@NonNull ContentResolver contentResolver) {

        ContentRequester<SystemCall> contentRequester = new CallsRequester(contentResolver, null, null);
        contentFetcher = new CallsFetcher(contentRequester);
    }

    @NonNull
    @Override
    public List<SystemCall> getContents() {

        return contentFetcher.fetch();
    }


}
