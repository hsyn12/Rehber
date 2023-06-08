package com.tr.hsyn.telefonrehberi.main.code.data;


import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;

import com.tr.hsyn.db.DBBase;
import com.tr.hsyn.key.Key;
import com.tr.hsyn.registery.Values;
import com.tr.hsyn.registery.cast.DB;
import com.tr.hsyn.registery.cast.DBColumn;
import com.tr.hsyn.xbox.Blue;
import com.tr.hsyn.xbox.definition.Visitor;

import org.jetbrains.annotations.NotNull;


/**
 * {@link Blue} sınıfının işleme aldığı nesnelerin kayda girecek olan bilgilerini sağlar ve
 * bu bilgilerin tutulacağı veri tabanı rolünü oynar.
 */
public class BlueRegister extends DBBase<Visitor> {
	
	/**
	 * Nesnenin kayda girdiği zaman
	 */
	private static final String TIME_ENTER  = "time_enter";
	/**
	 * Nesnenin kayıttan çıkarıldı zaman
	 */
	private static final String TIME_EXIT   = "time_exit";
	/**
	 * Kayıt ismi
	 */
	private static final String NAME        = "name";
	/**
	 * Kayıt kimliği
	 */
	private static final String ID          = "id";
	/**
	 * Erişim sayısı
	 */
	private static final String INTERACTION = "interaction";
	
	public BlueRegister(@NotNull Context context) {
		
		super(context, new DBInterface());
	}
	
	@SuppressLint("Range")
	@NotNull
	@Override
	protected Visitor createObject(@NotNull Cursor cursor) {
		
		long   enter       = cursor.getLong(cursor.getColumnIndex(TIME_ENTER));
		long   exit        = cursor.getLong(cursor.getColumnIndex(TIME_EXIT));
		long   interaction = cursor.getLong(cursor.getColumnIndex(INTERACTION));
		String name        = cursor.getString(cursor.getColumnIndex(NAME));
		int    id          = cursor.getInt(cursor.getColumnIndex(ID));
		
		return new Visitor(Key.of(id, name), enter, exit, interaction);
	}
	
	@Override
	public @NotNull Values contentValuesOf(@NotNull Visitor item) {
		
		Values v = new Values();
		v.put(TIME_ENTER, item.getTimeEnter());
		v.put(TIME_EXIT, item.getTimeExit());
		v.put(NAME, item.getKey().getName());
		v.put(ID, item.getKey().getId());
		v.put(INTERACTION, item.getInteraction());
		
		return v;
	}
	
	private static class DBInterface implements DB {
		
		private final DBColumn[] columns = {
				
				DB.number(TIME_ENTER).primaryKey(),
				DB.text(NAME).notNull(),
				DB.number(ID).notNull(),
				DB.number(TIME_EXIT).defaultValue(0L),
				DB.number(INTERACTION).defaultValue(0L)
		};
		
		@Override
		public @NotNull String getDatabaseName() {
			
			return "OtelRosa";
		}
		
		@Override
		public @NotNull String getPrimaryKey() {
			
			return TIME_ENTER;
		}
		
		@Override
		public @NotNull String getTableName() {
			
			return "visitors";
		}
		
		@Override
		public @NotNull DBColumn[] getColumns() {
			
			return columns;
		}
	}
	
}
