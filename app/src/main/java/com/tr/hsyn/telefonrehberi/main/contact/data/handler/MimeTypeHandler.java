package com.tr.hsyn.telefonrehberi.main.contact.data.handler;

import androidx.annotation.Nullable;

import org.jetbrains.annotations.NotNull;

import tr.xyz.contact.Contact;


public abstract class MimeTypeHandler {

	private final String mimeType;

	public MimeTypeHandler(@NotNull String mimeType) {

		this.mimeType = mimeType;
	}

	public abstract void handleMimeType(@NotNull String mimeType, String data1, String data2);

	public abstract void applyResult(@NotNull Contact contact);

	@Override
	public int hashCode() {

		return mimeType.hashCode();
	}

	@Override
	public boolean equals(@Nullable Object obj) {

		return obj instanceof MimeTypeHandler && mimeType.equals(((MimeTypeHandler) obj).mimeType);
	}

	public final
	@NotNull
	String getMimeType() {

		return mimeType;
	}
}
