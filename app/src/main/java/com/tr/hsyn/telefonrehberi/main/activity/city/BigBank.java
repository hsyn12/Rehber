package com.tr.hsyn.telefonrehberi.main.activity.city;


import com.tr.hsyn.calldata.Call;
import com.tr.hsyn.contactdata.Contact;
import com.tr.hsyn.key.Key;
import com.tr.hsyn.telefonrehberi.main.call.data.Database;
import com.tr.hsyn.telefonrehberi.main.call.story.CallStory;
import com.tr.hsyn.telefonrehberi.main.contact.data.ContactsReader;
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
	
	@Override
	protected void onCreate() {
		
		super.onCreate();
		
		Database database = new Database(this);
		callStory = new CallStory(database, getContentResolver());
		
		Blue.box(Key.CALL_STORY, callStory);
		
	}
	
	/**
	 * @return Arama kayıtları yöneticisi
	 */
	protected final Story<Call> getCallStory() {
		
		return callStory;
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
		
		return ContactsReader.getContacts(getContentResolver());
	}
}
