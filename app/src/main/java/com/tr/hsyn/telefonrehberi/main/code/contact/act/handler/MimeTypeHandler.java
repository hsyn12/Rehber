package com.tr.hsyn.telefonrehberi.main.code.contact.act.handler;


import androidx.annotation.Nullable;

import com.tr.hsyn.contactdata.Contact;

import org.jetbrains.annotations.NotNull;


public abstract class MimeTypeHandler {
	
	private final String mimeType;
	
	public MimeTypeHandler(@NotNull String mimeType) {
		
		this.mimeType = mimeType;
	}
	
	public abstract void handleMimeType(@NotNull String mimeType, String data1, String data2);
	
	public abstract void applyResult(@NotNull Contact contact);
	
	public final
	@NotNull
	String getMimeType() {
		
		return mimeType;
	}
	
	@Override
	public boolean equals(@Nullable Object obj) {
		
		return obj instanceof MimeTypeHandler && mimeType.equals(((MimeTypeHandler) obj).mimeType);
	}
	
	@Override
	public int hashCode() {
		
		return mimeType.hashCode();
	}
}
