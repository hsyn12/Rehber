package com.tr.hsyn.db.actor;


import android.database.sqlite.SQLiteDatabase;

import com.tr.hsyn.registery.SimpleDatabase;
import com.tr.hsyn.registery.Values;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


public class SqliteBridge implements SimpleDatabase {
	
	@NotNull
	private final SQLiteDatabase db;
	
	public SqliteBridge(@NotNull SQLiteDatabase db) {
		
		this.db = db;
	}
	
	@Override
	public long insert(@NotNull String table, @Nullable String nullColumnHack, @NotNull Values values) {
		
		return db.insert(table, null, DBOperator.convertFrom(values));
	}
	
	@Override
	public int update(@NotNull String table, @NotNull Values values, @NotNull String whereClause, @Nullable String[] whereArgs) {
		
		return db.update(table, DBOperator.convertFrom(values), whereClause, whereArgs);
	}
	
	@Override
	public int delete(@NotNull String table, @NotNull String whereClause, @Nullable String[] whereArgs) {
		
		return db.delete(table, whereClause, whereArgs);
	}
	
	@Override
	public void beginTransaction() {
		
		db.beginTransaction();
	}
	
	@Override
	public void endTransaction() {
		
		db.endTransaction();
	}
	
	@Override
	public void setTransactionSuccessful() {
		
		db.setTransactionSuccessful();
	}
}
