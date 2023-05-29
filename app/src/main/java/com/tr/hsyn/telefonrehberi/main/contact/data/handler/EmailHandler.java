package com.tr.hsyn.telefonrehberi.main.contact.data.handler;


import com.tr.hsyn.contactdata.Contact;
import com.tr.hsyn.telefonrehberi.main.contact.data.ContactKey;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;


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
			contact.setData(ContactKey.EMAILS, emails);
	}
	
	
}
