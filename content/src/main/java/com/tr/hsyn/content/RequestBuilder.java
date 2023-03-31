package com.tr.hsyn.content;


import android.content.ContentResolver;
import android.net.Uri;

import androidx.annotation.NonNull;

import com.tr.hsyn.content.handler.ContentHandler;
import com.tr.hsyn.content.requestor.ContentRequester;


/**
 * Veri tabanından bilgi almak isteyen bir kodun sağlaması gereken bilgileri tanımlar.
 *
 * @param <T> Veri tabanında saklanan bilgilerin dönüştürüleceği nesne türü
 */
public interface RequestBuilder<T> {
	
	@NonNull
	ContentRequester<T> build();
	
	@NonNull
	RequestBuilder<T> contentResolver(@NonNull ContentResolver contentResolver);
	
	@NonNull
	RequestBuilder<T> uri(@NonNull Uri uri);
	
	@NonNull
	RequestBuilder<T> selection(String selection);
	
	@NonNull
	RequestBuilder<T> selectionArgs(String[] selectionArgs);
	
	@NonNull
	RequestBuilder<T> contentHandler(@NonNull ContentHandler<T> contentHandler);
	
	@NonNull
	static <T> RequestBuilder<T> createBuilder() {
		
		return new RequesterBuilder<>();
	}
	
}
