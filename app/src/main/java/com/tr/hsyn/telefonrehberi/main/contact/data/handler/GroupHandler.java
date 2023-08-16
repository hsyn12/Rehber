package com.tr.hsyn.telefonrehberi.main.contact.data.handler;

import com.tr.hsyn.telefonrehberi.main.contact.data.ContactKeyKt;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import tr.xyz.contact.Contact;


public class GroupHandler extends MimeTypeHandler {

	private final List<String> groups = new ArrayList<>();

	public GroupHandler(@NotNull String mimeType) {

		super(mimeType);
	}

	@Override
	public void handleMimeType(@NotNull String mimeType, String data1, String data2) {

		if (data1 != null && !groups.contains(data1)) groups.add(data1);
	}

	@Override
	public void applyResult(@NotNull Contact contact) {

		if (!groups.isEmpty())
			ContactKeyKt.setGroups(contact, groups);
	}


}
