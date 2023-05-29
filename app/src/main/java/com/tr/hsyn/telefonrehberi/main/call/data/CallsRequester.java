package com.tr.hsyn.telefonrehberi.main.call.data;


import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CallLog;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.tr.hsyn.calldata.Call;
import com.tr.hsyn.content.handler.ContentHandler;
import com.tr.hsyn.content.requestor.ContentRequester;

import org.jetbrains.annotations.NotNull;


public class CallsRequester implements ContentRequester<Call> {
	
	private final ContentResolver contentResolver;
	private final String          selection;
	private final String[]        selectionArgs;
	private final String[]        PROJECTION = {
			CallLog.Calls.CACHED_NAME,
			CallLog.Calls.NUMBER,
			CallLog.Calls.DATE,
			CallLog.Calls.TYPE,
			CallLog.Calls.DURATION,
			CallLog.Calls.PHONE_ACCOUNT_ID
	};
	
	private final ContentHandler<Call> contentHandler = new ContentHandler<>() {
		
		private int nameCol;
		private int numberCol;
		private int dateCol;
		private int typeCol;
		private int durationCol;
		private int extraCol;
		
		@NotNull
		@Override
		public Call handle(@NonNull Cursor cursor) {
			
			return new Call(
					cursor.getString(nameCol),
					cursor.getString(numberCol),
					cursor.getInt(typeCol),
					cursor.getLong(dateCol),
					cursor.getInt(durationCol),
					cursor.getString(extraCol)
			);
		}
		
		@Override
		public void onCreateCursor(@NotNull ContentResolver resolver, @NotNull Cursor cursor) {
			
			nameCol     = cursor.getColumnIndex(PROJECTION[0]);
			numberCol   = cursor.getColumnIndex(PROJECTION[1]);
			dateCol     = cursor.getColumnIndex(PROJECTION[2]);
			typeCol     = cursor.getColumnIndex(PROJECTION[3]);
			durationCol = cursor.getColumnIndex(PROJECTION[4]);
			extraCol    = cursor.getColumnIndex(PROJECTION[5]);
		}
		
		@Override
		public String[] getProjection() {
			
			return PROJECTION;
		}
		
		@NotNull
		@Override
		public String getSortOrder() {
			
			return PROJECTION[2] + " desc";
		}
	};
	
	public CallsRequester(ContentResolver contentResolver, String selection, String[] selectionArgs) {
		
		this.contentResolver = contentResolver;
		this.selection       = selection;
		this.selectionArgs   = selectionArgs;
		
	}
	
	@NotNull
	@Override
	public ContentResolver getContentResolver() {
		
		return contentResolver;
	}
	
	@NotNull
	@Override
	public Uri getContentUri() {
		
		return CallLog.Calls.CONTENT_URI;
	}
	
	@NotNull
	@Override
	public ContentHandler<Call> getContentHandler() {
		
		return contentHandler;
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
}
