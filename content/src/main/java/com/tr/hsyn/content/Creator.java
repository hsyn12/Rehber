package com.tr.hsyn.content;


import android.database.Cursor;

import androidx.annotation.NonNull;

import com.tr.hsyn.content.fetcher.ContentFetcher;
import com.tr.hsyn.content.handler.ContentHandler;
import com.tr.hsyn.content.requestor.ContentRequester;
import com.tr.hsyn.content.server.Server;

import java.util.function.Function;


public class Creator {
	
	@NonNull
	public static <T> RequesterBuilder<T> createRequestor() {
		
		return new RequesterBuilder<>();
	}
	
	@NonNull
	public static <T> ContentFetcher<T> createFetcher(@NonNull ContentRequester<T> requestor) {
		
		class Fetcher implements ContentFetcher<T> {
			
			@Override
			public ContentRequester<T> getContentRequester() {
				
				return requestor;
			}
		}
		
		return new Fetcher();
	}
	
	@NonNull
	public static <T> Server<T> createServer(@NonNull ContentFetcher<T> fetcher) {
		
		return new ContentServer<>(fetcher);
	}
	
	@NonNull
	public static <T> ContentHandler<T> createHandler(@NonNull Function<Cursor, T> handler) {
		
		//noinspection NullableProblems
		return handler::apply;
	}
	
}
