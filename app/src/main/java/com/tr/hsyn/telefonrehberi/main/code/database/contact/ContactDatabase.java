package com.tr.hsyn.telefonrehberi.main.code.database.contact;


import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;

import androidx.annotation.NonNull;

import com.tr.hsyn.db.DBBase;
import com.tr.hsyn.db.actor.SqliteBridge;
import com.tr.hsyn.label.Label;
import com.tr.hsyn.registery.SimpleDatabase;
import com.tr.hsyn.registery.Values;
import com.tr.hsyn.registery.cast.DB;
import com.tr.hsyn.registery.cast.DBColumn;
import com.tr.hsyn.string.Stringx;
import com.tr.hsyn.telefonrehberi.main.code.contact.act.Contacts;
import com.tr.hsyn.telefonrehberi.main.code.contact.act.Dates;
import com.tr.hsyn.telefonrehberi.main.code.contact.cast.Contact;
import com.tr.hsyn.telefonrehberi.main.code.contact.cast.ContactLabel;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;


/**
 * Kişiler veri tabanı
 */
public class ContactDatabase extends DBBase<Contact> implements DBContact {
	
	private static final DB             db = new DbInterface();
	private final        SimpleDatabase simpleDatabase;
	
	public ContactDatabase(@NonNull Context context) {
		
		super(context, db);
		simpleDatabase = new SqliteBridge(getWritableDatabase());
	}
	
	@SuppressLint("Range")
	@NonNull
	@Override
	protected Contact createObject(@NonNull Cursor cursor) {
		
		Dates dates = Dates.newDates(cursor.getLong(cursor.getColumnIndex(SAVED_DATE)));
		dates.setUpdatedDate(cursor.getLong(cursor.getColumnIndex(UPDATED_DATE)));
		dates.setDeletedDate(cursor.getLong(cursor.getColumnIndex(DELETED_DATE)));
		dates.setLastLookDate(cursor.getLong(cursor.getColumnIndex(LAST_LOOK_DATE)));
		
		var labels = getLabels(cursor.getString(cursor.getColumnIndex(LABELS)));
		
		return Contacts.newContact(
				cursor.getLong(cursor.getColumnIndex(ContactDatabase.CONTACT_ID)),
				cursor.getString(cursor.getColumnIndex(ContactDatabase.NAME)),
				null,
				null,
				DBContact.getList(cursor.getString(cursor.getColumnIndex(ContactDatabase.NUMBERS))),
				DBContact.getList(cursor.getString(cursor.getColumnIndex(ContactDatabase.EMAILS))),
				null,
				null,
				null,
				cursor.getInt(cursor.getColumnIndex(LOOK_COUNT)),
				dates,
				labels
		);
	}
	
	@Override
	public @NotNull DB getDBInterface() {
		
		return db;
	}
	
	@Override
	public @NotNull SimpleDatabase getSimpleDatabase() {
		
		return simpleDatabase;
	}
	
	@Override
	public @NotNull Values contentValuesOf(@NonNull Contact contact) {
		
		Values values = new Values();
		
		values.put(ContactDatabase.CONTACT_ID, contact.getContactId());
		
		if (contact.getName() != null) values.put(ContactDatabase.NAME, contact.getName());
		else values.putNull(Values.TYPE_STRING, NAME);
		
		if (contact.getEmails() != null) values.put(EMAILS, DBContact.getList(contact.getEmails()));
		else values.putNull(Values.TYPE_STRING, EMAILS);
		
		if (contact.getNumbers() != null) values.put(NUMBERS, DBContact.getList(contact.getNumbers()));
		else values.putNull(Values.TYPE_STRING, NUMBERS);
		
		values.put(SAVED_DATE, contact.getDates().getSavedDate());
		values.put(UPDATED_DATE, contact.getDates().getUpdatedDate());
		values.put(DELETED_DATE, contact.getDates().getDeletedDate());
		values.put(LAST_LOOK_DATE, contact.getDates().getLastLookDate());
		values.put(LOOK_COUNT, contact.getLookCount());
		
		var labels = contact.getLabels();
		
		if (labels != null) {
			
			if (labels.isEmpty()) {
				
				values.putNull(Values.TYPE_STRING, LABELS);
			}
			else {
				
				var ids = Stringx.joinToString(contact.getLabelIds());
				values.put(LABELS, ids);
			}
		}
		
		return values;
	}
	
	@NotNull
	private Set<Label> getLabels(@Nullable String labels) {
		
		if (labels == null) return new HashSet<>(0);
		
		var        ids     = Stringx.split(labels);
		Set<Label> _labels = new HashSet<>();
		
		for (var id : ids) {
			
			_labels.add(ContactLabel.getLabel(Integer.parseInt(id)));
		}
		
		return _labels;
	}
	
	@Override
	@Nullable
	public List<String> getEmails(long id) {
		
		var value = getString(id, EMAILS);
		
		if (value != null) return DBContact.getList(value);
		
		return null;
	}
	
	private boolean setName(long contactId, String newName) {
		
		var values = createValues(NAME, newName);
		
		return update(values, String.valueOf(contactId));
	}
	
	@Override
	public boolean setEmails(long id, List<String> emails) {
		
		var value = DBContact.getList(emails);
		
		return update(createValues(EMAILS, value), String.valueOf(id));
	}
	
	@Override
	@Nullable
	public Dates getDates(long id) {
		
		var contact = find(id);
		
		if (contact == null) return null;
		
		return contact.getDates();
        /*long[] longs = getLongs(id, new String[]{SAVED_DATE, UPDATED_DATE, DELETED_DATE, LAST_LOOK_DATE});

        if (longs != null)
            return Dates.newDates(longs[0], longs[1], longs[2], longs[3]);

        return null;*/
	}
	
	@Override
	public boolean setDates(long id, @NotNull Dates dates) {
		
		var values = createValues(SAVED_DATE, dates.getSavedDate());
		values.put(UPDATED_DATE, dates.getUpdatedDate());
		values.put(DELETED_DATE, dates.getDeletedDate());
		values.put(LAST_LOOK_DATE, dates.getLastLookDate());
		
		return update(values, String.valueOf(id));
	}
	
	@Override
	public boolean update(@NonNull Contact contact) {
		
		return update(contact, contact.getContactId());
	}
	
	@Override
	public int add(@NotNull List<? extends Contact> items, @NotNull Function<? super Contact, Values> valuesFunction) {
		
		var db    = getWritableDatabase();
		int count = 0;
		
		try {
			db.beginTransaction();
			
			for (var item : items) {
				
				var i = db.insert(getDatabaseInterface().getTableName(), null, convertFrom(valuesFunction.apply(item)));
				
				if (i != -1) count++;
			}
			
			db.setTransactionSuccessful();
		}
		finally {
			
			db.endTransaction();
		}
		
		return count;
	}
	
	@NotNull
	private static Values createValues(String key, String value) {
		
		var v = new Values();
		
		if (value != null) v.put(key, value);
		else v.putNull(Values.TYPE_STRING, key);
		return v;
	}
	
	@NotNull
	private static Values createValues(String key, long value) {
		
		var v = new Values();
		
		v.put(key, value);
		
		return v;
	}
	
	private static final class DbInterface implements DB {
		
		public static final  String TABLE    = "contacts";
		private static final String DATABASE = "contact_database";
		
		@NonNull
		@Override
		public String getTableName() {
			
			return TABLE;
		}
		
		@NonNull
		@Override
		public String getDatabaseName() {
			
			return DATABASE;
		}
		
		@NonNull
		@Override
		public DBColumn[] getColumns() {
			
			return new DBColumn[]{
					
					DB.text(CONTACT_ID).primaryKey(),
					DB.text(NAME),
					DB.text(NUMBERS),
					DB.number(LOOK_COUNT).defaultValue(0),
					DB.number(SAVED_DATE).defaultValue(0),
					DB.number(UPDATED_DATE).defaultValue(0),
					DB.number(DELETED_DATE).defaultValue(0),
					DB.number(LAST_LOOK_DATE).defaultValue(0),
					DB.text(EMAILS),
					DB.text(LABELS)
			};
		}
		
		@Override
		public @NotNull String getPrimaryKey() {
			
			return CONTACT_ID;
		}
	}
}
