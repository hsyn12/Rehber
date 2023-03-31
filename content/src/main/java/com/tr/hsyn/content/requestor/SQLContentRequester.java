package com.tr.hsyn.content.requestor;


import android.database.sqlite.SQLiteDatabase;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.tr.hsyn.content.handler.SQLContentHandler;


public interface SQLContentRequester<T> {
	
	@NonNull
	String getTableName();
	
	@NonNull
	SQLiteDatabase getReadableDatabase();
	
	/**
	 * @return Veri tabanındaki satırları işleyecek olan nesne
	 */
	@NonNull
	SQLContentHandler<T> getContentHandler();
	
	@Nullable
	default String getSelection() {return null;}
	
	@Nullable
	default String[] getSelectionArgs() {return null;}
}
