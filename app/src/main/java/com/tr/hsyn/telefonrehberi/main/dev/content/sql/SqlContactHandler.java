package com.tr.hsyn.telefonrehberi.main.dev.content.sql;


import android.database.Cursor;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.tr.hsyn.contactdata.Contact;
import com.tr.hsyn.content.handler.SQLContentHandler;
import com.tr.hsyn.perfectsort.PerfectSort;

import org.jetbrains.annotations.NotNull;

import java.util.Comparator;


public class SqlContactHandler implements SQLContentHandler<Contact> {
	
	private int contactIdCol;
	private int nameCol;
	
	@Override
	public void onCreateCursor(@NonNull @NotNull Cursor cursor) {
		
		contactIdCol = cursor.getColumnIndex("0");
		nameCol      = cursor.getColumnIndex("1");
	}
	
	@NonNull
	@Override
	public Contact handle(@NonNull Cursor cursor) {
		
		return new Contact(
				cursor.getLong(contactIdCol),
				cursor.getString(nameCol),
				null
		);
	}
	
	@Nullable
	@Override
	public Comparator<Contact> getComparator() {
		
		return PerfectSort.stringComparator(Contact::getName);
	}
}
