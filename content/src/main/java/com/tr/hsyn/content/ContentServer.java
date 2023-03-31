package com.tr.hsyn.content;


import androidx.annotation.NonNull;

import com.tr.hsyn.content.fetcher.ContentFetcher;
import com.tr.hsyn.content.server.Server;

import java.util.List;


public class ContentServer<T> implements Server<T> {
	
	public final ContentFetcher<T> contentFetcher;
	
	public ContentServer(@NonNull final ContentFetcher<T> contentFetcher) {
		
		this.contentFetcher = contentFetcher;
	}
	
	@NonNull
	@Override
	public List<T> getContents() {
		
		return contentFetcher.fetch();
	}
}
