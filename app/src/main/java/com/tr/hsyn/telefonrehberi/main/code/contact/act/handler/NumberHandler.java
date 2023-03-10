package com.tr.hsyn.telefonrehberi.main.code.contact.act.handler;


import android.provider.ContactsContract;

import com.tr.hsyn.contactdata.Contact;
import com.tr.hsyn.phone_numbers.PhoneNumbers;
import com.tr.hsyn.telefonrehberi.main.code.contact.act.ContactKey;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;


public class NumberHandler extends MimeTypeHandler {
	
	private final List<String> numbers = new ArrayList<>();
	
	public NumberHandler() {
		
		super(ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE);
	}
	
	@Override
	public void handleMimeType(@NotNull String mimeType, String data1, String data2) {
		
		final int numberLength = 13;
		var       number       = PhoneNumbers.formatNumber(data1, numberLength);
		
		var notExist = numbers.stream()
				.noneMatch(num -> PhoneNumbers.equals(number, num));
		
		if (notExist) numbers.add(number);
		
		
	}
	
	@Override
	public void applyResult(@NotNull Contact contact) {
		
		if (!numbers.isEmpty())
			contact.setData(ContactKey.NUMBERS, numbers);
	}
	
	
}
