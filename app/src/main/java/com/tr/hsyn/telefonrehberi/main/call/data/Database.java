package com.tr.hsyn.telefonrehberi.main.call.data;


import android.content.Context;
import android.database.Cursor;

import androidx.annotation.NonNull;

import com.tr.hsyn.calldata.Call;
import com.tr.hsyn.db.DBBase;
import com.tr.hsyn.label.Label;
import com.tr.hsyn.registery.Values;
import com.tr.hsyn.registery.cast.DB;
import com.tr.hsyn.registery.cast.DBColumn;
import com.tr.hsyn.xlog.xlog;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;


/**
 * Arama kayıtları veri tabanı
 */
public class Database extends DBBase<Call> implements DBCalls {
	
	//==============================================================
	//==============================================================
	private final String  SEPARATOR     = ";";
	//==============================================================
	//==============================================================
	//======================== Columns ==============================
	private       int     nameCol;
	private       int     numberCol;
	private       int     dateCol;
	private       int     typeCol;
	private       int     durationCol;
	/////
	private       int     contactIdCol;
	private       int     deletedDateCol;
	private       int     ringingCol;
	//==============================================================
	private       int     noteCol;
	private       int     extraCol;
	private       int     labelCol;
	/**
	 * Bilgilere daha hızlı erişim için kolonlar ilk erişimde ve sadece bir kez tanımlanmaktadır.
	 * Kolon bilgileri set edilmişse bu değişken {@code false} durumundadır.
	 */
	private       boolean columnsNotSet = true;
	
	public Database(@NonNull Context context) {
		
		super(context, new DBInterface());
	}
	
	@NonNull
	@Override
	protected Call createObject(@NonNull Cursor cursor) {
		
		if (columnsNotSet) {
			
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
			labelCol       = cursor.getColumnIndex(LABELS);
			columnsNotSet  = false;
		}
		
		String     name            = cursor.getString(nameCol);
		String     number          = cursor.getString(numberCol);
		long       date            = cursor.getLong(dateCol);
		int        type            = cursor.getInt(typeCol);
		int        duration        = cursor.getInt(durationCol);
		long       contactId       = cursor.getLong(contactIdCol);
		long       ringingDuration = cursor.getLong(ringingCol);
		long       deletedDate     = cursor.getLong(deletedDateCol);
		String     note            = cursor.getString(noteCol);
		String     extra           = cursor.getString(extraCol);
		Set<Label> labels          = getLabels(cursor.getString(labelCol));
		
		Call call = new Call(name, number, type, date, duration, extra);
		
		if (contactId != 0L) call.setData(Key.CONTACT_ID, contactId);
		if (note != null) call.setData(Key.NOTE, note);
		if (deletedDate != 0L) call.setData(Key.DELETED_DATE, deletedDate);
		if (ringingDuration != 0L) call.setData(Key.RINGING_DURATION, ringingDuration);
		if (!labels.isEmpty()) call.setData(Key.LABELS, getLabels(labels));
		
		call.setData(Key.TRACK_TYPE, getTrackType(call));
		call.setData(Key.RANDOM, call.isRandom());
		
		return call;
	}
	
	@Override
	@NonNull
	public Values contentValuesOf(@NonNull final Call call) {
		
		Values values = new Values();
		
		if (call.getName() != null) values.put(NAME, call.getName());
		else values.putNull(Values.TYPE_STRING, NAME);
		
		values.put(NUMBER, call.getNumber());
		values.put(DATE, call.getTime());
		values.put(TYPE, call.getCallType());
		values.put(DURATION, call.getDuration());
		values.put(CONTACT_ID, call.getLong(Key.CONTACT_ID, 0L));
		values.put(DELETED_DATE, call.getLong(Key.DELETED_DATE, 0L));
		values.put(RINGING_DURATION, call.getLong(Key.RINGING_DURATION, 0L));
		values.put(EXTRA, call.getExtra());
		
		if (call.exist(Key.NOTE)) //noinspection ConstantConditions
			values.put(NOTE, call.getData(Key.NOTE));
		else values.putNull(Values.TYPE_STRING, NOTE);
		
		Set<Label> labels = call.getData(Key.LABELS);
		
		if (labels == null || labels.isEmpty()) values.putNull(Values.TYPE_STRING, LABELS);
		else values.put(LABELS, getLabels(labels));
		
		return values;
	}
	
	private @NotNull Set<Label> getLabels(String labels) {
		
		Set<Label> labelSet = new HashSet<>();
		
		if (labels == null) return labelSet;
		
		String[] parts = labels.split(SEPARATOR);
		
		for (String part : parts) {
			
			Label label = Label.of(part);
			
			if (label.isValid())
				labelSet.add(label);
		}
		
		return labelSet;
	}
	
	/**
	 * Etiket listesi boş olmamalı.
	 *
	 * @param labels Etiket listesi
	 * @return Etiketlerin string karşılığı
	 */
	private @NotNull String getLabels(@NotNull Set<Label> labels) {
		
		StringBuilder sb = new StringBuilder();
		
		for (Label label : labels) sb.append(label).append(SEPARATOR);
		
		return sb.subSequence(0, sb.lastIndexOf(SEPARATOR)).toString();
	}
	
	private int getTrackType(@NotNull Call call) {
		
		String extra = call.getExtra();
		
		if (extra != null && extra.startsWith(Calls.ACCOUNT_ID)) {
			
			String[] parts = extra.split(SEPARATOR);
			
			try {return Integer.parseInt(parts[2]);}
			catch (Exception e) {xlog.e(e);}
		}
		
		return 0;
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
		public String getDatabaseName() {
			
			return DATABASE;
		}
		
		@Override
		public @NotNull
		String getPrimaryKey() {
			
			return DATE;
		}
		
		@NonNull
		@Override
		public String getTableName() {
			
			return TABLE;
		}
		
		@NonNull
		@Override
		public DBColumn[] getColumns() {
			
			return new DBColumn[]{
					
					DB.text(NAME),
					DB.text(NUMBER),
					DB.number(DATE).primaryKey(false),
					DB.number(TYPE),
					DB.number(DURATION),
					DB.text(CONTACT_ID),
					DB.number(DELETED_DATE).defaultValue(0),
					DB.number(RINGING_DURATION).defaultValue(0),
					DB.text(NOTE),
					DB.text(EXTRA),
					DB.text(LABELS)
			};
		}
		
		@NotNull
		@Override
		public String toString() {
			
			return String.format("DB {%s}", Arrays.toString(getColumns()));
		}
	}
}
