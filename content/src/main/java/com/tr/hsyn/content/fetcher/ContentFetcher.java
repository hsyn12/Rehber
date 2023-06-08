package com.tr.hsyn.content.fetcher;


import android.database.Cursor;

import androidx.annotation.NonNull;

import com.tr.hsyn.content.handler.ContentHandler;
import com.tr.hsyn.content.requestor.ContentRequester;

import java.util.ArrayList;
import java.util.List;


public interface ContentFetcher<T> extends Fetcher<T> {
	
	ContentRequester<T> getContentRequester();
	
	@NonNull
	@Override
	default List<T> fetch() {
		
		ContentRequester<T> requester = getContentRequester();
		
		Cursor cursor = requester.getContentResolver().query(
				requester.getContentUri(),
				requester.getContentHandler().getProjection(),
				requester.getSelection(),
				requester.getSelectionArgs(),
				requester.getContentHandler().getSortOrder());
		
		if (cursor == null) return new ArrayList<>(0);
		
		List<T> contents = new ArrayList<>(cursor.getCount());
		
		ContentHandler<T> handler = requester.getContentHandler();
		
		handler.onCreateCursor(requester.getContentResolver(), cursor);
		
		while (cursor.moveToNext()) {
			
			T content = handler.handle(cursor);
			
			if (handler.predicate(content, contents))
				contents.add(content);
		}
		
		if (requester.getContentHandler().getComparator() != null) {
			
			contents.sort(requester.getContentHandler().getComparator());
		}
		
		cursor.close();
		return contents;
	}
}
