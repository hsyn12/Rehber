package com.tr.hsyn.telefonrehberi.main.contact.data.bank;


import android.content.Context;

import androidx.annotation.NonNull;

import com.tr.hsyn.contactdata.Contact;
import com.tr.hsyn.telefonrehberi.main.dev.Over;


public class ContactDataClient implements DataBankClient<Contact> {
	
	@Override
	public int getId() {
		
		return 1;
	}
	
	@NonNull
	@Override
	public String getName() {
		
		return "contacts_client";
	}
	
	@NonNull
	@Override
	public Context getContext() {
		
		return Over.App.getContext();
	}
	
	@NonNull
	@Override
	public Class<? extends Contact> getClazz() {
		
		return Contact.class;
	}
}
