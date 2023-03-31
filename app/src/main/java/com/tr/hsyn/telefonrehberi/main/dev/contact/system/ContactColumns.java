package com.tr.hsyn.telefonrehberi.main.dev.contact.system;


import android.provider.ContactsContract;


public interface ContactColumns {
	
	/**
	 * Minimal projection
	 */
	String[] PROJECTION   = {
			ContactsContract.Contacts._ID,
			ContactsContract.Contacts.DISPLAY_NAME_PRIMARY,
			ContactsContract.Contacts.PHOTO_THUMBNAIL_URI
	};
	/**
	 * Data projection
	 */
	String[] DATA_COLUMNS = {
			
			ContactsContract.Data.DATA1,
			ContactsContract.Data.MIMETYPE,
			ContactsContract.Data.DATA2,
			ContactsContract.RawContacts.ACCOUNT_NAME,
			ContactsContract.RawContacts.ACCOUNT_TYPE
	};
}
