package com.tr.hsyn.telefonrehberi.main.contact.data.handler;

import com.tr.hsyn.telefonrehberi.main.contact.data.ContactKeyKt;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import tr.xyz.contact.Contact;


public class EmailHandler extends MimeTypeHandler {

	private final List<String> emails = new ArrayList<>();

	public EmailHandler(@NotNull String mimeType) {

		super(mimeType);
	}

	@Override
	public void handleMimeType(@NotNull String mimeType, String data1, String data2) {

		if (data1 != null && !emails.contains(data1)) emails.add(data1);
	}

	@Override
	public void applyResult(@NotNull Contact contact) {

		if (!emails.isEmpty())
			ContactKeyKt.setEmails(contact, emails);
	}


}
