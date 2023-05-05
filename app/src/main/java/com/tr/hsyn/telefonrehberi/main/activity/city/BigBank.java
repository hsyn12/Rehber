package com.tr.hsyn.telefonrehberi.main.activity.city;


import com.tr.hsyn.calldata.Call;
import com.tr.hsyn.contactdata.Contact;
import com.tr.hsyn.key.Key;
import com.tr.hsyn.telefonrehberi.main.code.contact.act.Contacts;
import com.tr.hsyn.telefonrehberi.main.code.database.call.CallDatabase;
import com.tr.hsyn.telefonrehberi.main.code.story.call.CallStory;
import com.tr.hsyn.telefonrehberi.main.dev.Loader;
import com.tr.hsyn.telefonrehberi.main.dev.Story;
import com.tr.hsyn.xbox.Blue;

import org.jetbrains.annotations.NotNull;

import java.util.List;


public abstract class BigBank extends NorthBridge {
	
	/**
	 * Arama kayıtları yöneticisi
	 */
	private Story<Call> callStory;
	
	/**
	 * @return Arama kayıtları yöneticisi
	 */
	protected final Story<Call> getCallStory() {
		
		return callStory;
	}
	
	@Override
	protected void onCreate() {
		
		super.onCreate();
		
		var callDatabase = new CallDatabase(this);
		callStory = new CallStory(callDatabase, getContentResolver());
		
		Blue.box(Key.CALL_STORY, callStory);
		
	}
	
	@NotNull
	protected final Loader<Call> getCallLogLoader() {
		
		return callStory::load;
	}
	
	@NotNull
	protected final Loader<Contact> getContactsLoader() {
		
		return this::loadContacts;
	}
	
	private @NotNull
	List<Contact> loadContacts() {
		
		return Contacts.getContacts(getContentResolver());
	}
}
