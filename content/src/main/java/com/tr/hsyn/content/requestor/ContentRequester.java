package com.tr.hsyn.content.requestor;


import android.content.ContentResolver;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.tr.hsyn.content.handler.ContentHandler;


/**
 * Hangi veri tabanından hangi bilgilerin nasıl alınıp kim tarafından işleneceğini bildirir.
 *
 * @param <T> Veri tabanındaki satırları işleyecek olan nesnenin türü
 */
public interface ContentRequester<T> {
	
	@NonNull
	ContentResolver getContentResolver();
	
	/**
	 * @return Veri tabanı adresi
	 */
	@NonNull
	Uri getContentUri();
	
	/**
	 * @return Veri tabanındaki satırları işleyecek olan nesne
	 */
	@NonNull
	ContentHandler<T> getContentHandler();
	
	@Nullable
	default String getSelection() {return null;}
	
	@Nullable
	default String[] getSelectionArgs() {return null;}
}
