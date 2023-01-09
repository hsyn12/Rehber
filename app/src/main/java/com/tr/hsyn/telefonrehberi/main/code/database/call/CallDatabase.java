package com.tr.hsyn.telefonrehberi.main.code.database.call;


import android.content.Context;
import android.database.Cursor;

import androidx.annotation.NonNull;

import com.tr.hsyn.calldata.Call;
import com.tr.hsyn.db.DBBase;
import com.tr.hsyn.registery.Values;
import com.tr.hsyn.registery.cast.DB;
import com.tr.hsyn.registery.cast.DBColumn;

import org.jetbrains.annotations.NotNull;


/**
 * Arama kayıtları veri tabanı
 */
public class CallDatabase extends DBBase<Call> implements DBCalls {
	
	//==============================================================
	//==============================================================
	private static final DB      dbInterface     = new DBInterface();
	//==============================================================
	//==============================================================
	//======================== Columns ==============================
	private              int     nameCol;
	private              int     numberCol;
	private              int     dateCol;
	private              int     typeCol;
	private              int     durationCol;
	private              int     contactIdCol;
	private              int     ringingCol;
	private              int     deletedDateCol;
	//==============================================================
	private              int     noteCol;
	private              int     extraCol;
	/**
	 * Bilgilere daha hızlı erişim için kolonlar ilk erişimde ve sadece bir kez tanımlanmaktadır.
	 * Kolon bilgileri set edilmişse bu değişken {@code false} durumundadır.
	 */
	private              boolean isColumnsNotSet = true;
	
	public CallDatabase(@NonNull Context context) {
		
		super(context, dbInterface);
	}
	
	@NonNull
	@Override
	protected Call createObject(@NonNull Cursor cursor) {
		
		if (isColumnsNotSet) {
			
			nameCol        = cursor.getColumnIndex(NAME);
			numberCol      = cursor.getColumnIndex(NUMBER);
			dateCol        = cursor.getColumnIndex(DATE);
			typeCol        = cursor.getColumnIndex(TYPE);
			durationCol    = cursor.getColumnIndex(DURATION);
			contactIdCol   = cursor.getColumnIndex(CONTACT_ID);
			ringingCol     = cursor.getColumnIndex(RINGING_DURATION);
			deletedDateCol = cursor.getColumnIndex(DELETED_DATE);
			noteCol        = cursor.getColumnIndex(NOTE);
			extraCol       = cursor.getColumnIndex(EXTRA);
			
			isColumnsNotSet = false;
		}
		
		String name            = cursor.getString(nameCol);
		String number          = cursor.getString(numberCol);
		long   date            = cursor.getLong(dateCol);
		int    type            = cursor.getInt(typeCol);
		int    duration        = cursor.getInt(durationCol);
		long   contactId       = cursor.getLong(contactIdCol);
		long   ringingDuration = cursor.getLong(ringingCol);
		long   deletedDate     = cursor.getLong(deletedDateCol);
		String note            = cursor.getString(noteCol);
		String extra           = cursor.getString(extraCol);
		
		return Call.newCall(
				name, number, type, date, duration, extra,
				contactId, note, deletedDate, ringingDuration
		);
		
	}
	
	@Override
	@NonNull
	public Values contentValuesOf(@NonNull final Call call) {
		
		Values values = new Values();
		
		if (call.getName() != null) values.put(NAME, call.getName());
		else values.putNull(Values.TYPE_STRING, NAME);
		
		values.put(NUMBER, call.getNumber());
		values.put(DATE, call.getTime());
		values.put(TYPE, call.getType());
		values.put(DURATION, call.getDuration());
		values.put(CONTACT_ID, call.getContactId());
		values.put(DELETED_DATE, call.getDeletedDate());
		values.put(RINGING_DURATION, call.getRingingDuration());
		//values.put(TRACK_TYPE, call.getTrackType());
		values.put(EXTRA, call.getExtra());
		
		if (call.getNote() != null) values.put(NOTE, call.getNote());
		else values.putNull(Values.TYPE_STRING, NOTE);
		
		return values;
	}
	
	/**
	 * Arama kayıtları bilgileri için veri tabanında oluşturulacak kolonlarını ve bazı temel bilgileri tanımlar.
	 */
	private static final class DBInterface implements DB {
		
		// ==============================================================
		public static final  String TABLE    = "calls";
		private static final String DATABASE = "call_log_calls";
		
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
					
					DB.text(NAME),
					DB.text(NUMBER),
					DB.number(DATE).primaryKey(),
					DB.number(TYPE),
					DB.number(DURATION),
					DB.text(CONTACT_ID),
					DB.number(DELETED_DATE).defaultValue(0),
					DB.number(RINGING_DURATION).defaultValue(0),
					DB.text(NOTE),
					DB.text(EXTRA)
			};
		}
		
		@Override
		public @NotNull String getPrimaryKey() {
			
			return DATE;
		}
	}
}
