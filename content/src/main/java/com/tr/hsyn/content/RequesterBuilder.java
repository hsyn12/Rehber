package com.tr.hsyn.content;


import android.content.ContentResolver;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.tr.hsyn.content.handler.ContentHandler;
import com.tr.hsyn.content.requestor.ContentRequester;

import java.util.Objects;


public class RequesterBuilder<T> implements RequestBuilder<T> {
	
	private ContentResolver   contentResolver;
	private Uri               uri;
	private String            selection;
	private String[]          selectionArgs;
	private ContentHandler<T> contentHandler;
	
	public RequesterBuilder() {}
	
	@NonNull
	@Override
	public RequestBuilder<T> contentResolver(@NonNull ContentResolver contentResolver) {
		
		this.contentResolver = contentResolver;
		return this;
	}
	
	@NonNull
	@Override
	public RequestBuilder<T> uri(@NonNull Uri uri) {
		
		this.uri = uri;
		return this;
	}
	
	@NonNull
	@Override
	public RequestBuilder<T> selection(String selection) {
		
		this.selection = selection;
		return this;
	}
	
	@NonNull
	@Override
	public RequestBuilder<T> selectionArgs(String[] selectionArgs) {
		
		this.selectionArgs = selectionArgs;
		return this;
	}
	
	@NonNull
	@Override
	public RequestBuilder<T> contentHandler(@NonNull ContentHandler<T> contentHandler) {
		
		this.contentHandler = contentHandler;
		return this;
	}
	
	@NonNull
	@Override
	public ContentRequester<T> build() {
		
		Objects.requireNonNull(contentResolver);
		Objects.requireNonNull(uri);
		Objects.requireNonNull(contentHandler);
		
		class Requester implements ContentRequester<T> {
			
			@NonNull
			@Override
			public ContentResolver getContentResolver() {
				
				return contentResolver;
			}
			
			@NonNull
			@Override
			public Uri getContentUri() {
				
				return uri;
			}
			
			@Nullable
			@Override
			public String getSelection() {
				
				return selection;
			}
			
			@Nullable
			@Override
			public String[] getSelectionArgs() {
				
				return selectionArgs;
			}
			
			@NonNull
			@Override
			public ContentHandler<T> getContentHandler() {
				
				return contentHandler;
			}
		}
		
		return new Requester();
	}
	
}
