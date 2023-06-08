package com.tr.hsyn.content.fetcher;


import android.database.Cursor;

import androidx.annotation.NonNull;

import com.tr.hsyn.content.handler.SQLContentHandler;
import com.tr.hsyn.content.requestor.SQLContentRequester;

import java.util.ArrayList;
import java.util.List;


/**
 * SQLite veri tabanından bilgi alacak sınıflar için arayüz.
 *
 * @param <T> Veri tabanından okunan bir satırın dönüştürüleceği nesne türü.
 */
public interface SQLContentFetcher<T> extends Fetcher<T> {
	
	@NonNull
	SQLContentRequester<T> getContentRequester();
	
	@NonNull
	@Override
	default List<T> fetch() {
		
		SQLContentRequester<T> requester = getContentRequester();
		
		Cursor cursor = requester.getReadableDatabase().query(
				requester.getTableName(),
				requester.getContentHandler().getProjection(),
				requester.getSelection(),
				requester.getSelectionArgs(),
				null,
				null,
				requester.getContentHandler().getSortOrder());
		
		if (cursor == null) return new ArrayList<>(0);
		
		List<T> contents = new ArrayList<>(cursor.getCount());
		
		SQLContentHandler<T> handler = requester.getContentHandler();
		
		handler.onCreateCursor(cursor);
		
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
