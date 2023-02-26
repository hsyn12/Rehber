package com.tr.hsyn.telefonrehberi.main.dev.contact.system;


import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;

import com.tr.hsyn.contactdata.Account;
import com.tr.hsyn.string.Stringx;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;


public interface SystemContactAccounts {
	
	/**
	 * Verilen id değerine sahip kişinin bağlı olduğu tüm hesapları döndür.
	 *
	 * @param context   Context
	 * @param contactId id
	 * @return Varsa hesaplar, yoksa {@code null}
	 */
	@NotNull
	@SuppressLint("Range")
	static List<Account> getAccounts(@NotNull final Context context, @NotNull final String contactId) {
		
		final Cursor cursor = context.getContentResolver().query(
				ContactsContract.RawContacts.CONTENT_URI,
				new String[]{ContactsContract.RawContacts.CONTACT_ID, ContactsContract.RawContacts.ACCOUNT_NAME, ContactsContract.RawContacts.ACCOUNT_TYPE},
				Stringx.format("%s = ?", ContactsContract.RawContacts.CONTACT_ID),
				new String[]{contactId},
				null
		);
		
		if (cursor != null) {
			
			final List<Account> accounts = new ArrayList<>();
			
			while (cursor.moveToNext()) {
				
				final String account     = cursor.getString(cursor.getColumnIndex(ContactsContract.RawContacts.ACCOUNT_NAME));
				final String accountType = cursor.getString(cursor.getColumnIndex(ContactsContract.RawContacts.ACCOUNT_TYPE));
				
				if (account != null) accounts.add(Account.newAccount(account, accountType));
			}
			
			cursor.close();
			return accounts;
		}
		
		return new ArrayList<>(0);
	}
}
