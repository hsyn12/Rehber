package com.tr.hsyn.telefonrehberi.code.registery.blue;


import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;

import com.tr.hsyn.db.DBBase;
import com.tr.hsyn.db.actor.SqliteBridge;
import com.tr.hsyn.key.Key;
import com.tr.hsyn.registery.SimpleDatabase;
import com.tr.hsyn.registery.Values;
import com.tr.hsyn.registery.cast.DB;
import com.tr.hsyn.registery.cast.DBColumn;
import com.tr.hsyn.xbox.Visitor;

import org.jetbrains.annotations.NotNull;


public class BlueRegister extends DBBase<Visitor> {
	
	private static final String         TIME_ENTER  = "time_enter";
	private static final String         TIME_EXIT   = "time_exit";
	private static final String         NAME        = "name";
	private static final String         ID          = "id";
	private static final String         INTERACTION = "interaction";
	private static final DB             dbInterface = new DBInterface();
	private final        SimpleDatabase simpleDatabase;
	
	public BlueRegister(@NotNull Context context) {
		
		super(context, dbInterface);
		simpleDatabase = new SqliteBridge(getWritableDatabase());
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
		
		var v = new Values();
		v.put(TIME_ENTER, item.getTimeEnter());
		v.put(TIME_EXIT, item.getTimeExit());
		v.put(NAME, item.getKey().getName());
		v.put(ID, item.getKey().getId());
		
		return v;
	}
	
	@Override
	public @NotNull DB getDBInterface() {
		
		return dbInterface;
	}
	
	@Override
	public @NotNull SimpleDatabase getSimpleDatabase() {
		
		return simpleDatabase;
	}
	
	@Override
	public boolean update(@NotNull Visitor item) {
		
		return update(item, item.getTimeEnter());
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
		public @NotNull String getTableName() {
			
			return "visitors";
		}
		
		@Override
		public @NotNull DBColumn[] getColumns() {
			
			return columns;
		}
		
		@Override
		public @NotNull String getPrimaryKey() {
			
			return TIME_ENTER;
		}
	}
	
}
