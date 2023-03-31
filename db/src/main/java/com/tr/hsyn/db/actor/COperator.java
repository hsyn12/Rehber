package com.tr.hsyn.db.actor;


import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import androidx.annotation.NonNull;

import java.util.function.Function;


public final class COperator<T> {
	
	private final SQLiteDatabase                database;
	private       String[]                      projection;
	private       String                        selection;
	private       String[]                      args;
	private       String                        sortOrder;
	private       ContentValues                 values;
	private       Function<Cursor, ? extends T> function;
	
	private COperator(SQLiteDatabase database) {
		
		this.database = database;
	}
	
	public static <T> COperator<T> on(SQLiteDatabase database) {
		
		return new COperator<>(database);
	}
	
	public COperator<T> projection(String[] projection) {
		
		this.projection = projection;
		return this;
	}
	
	public COperator<T> selection(String selection) {
		
		this.selection = selection;
		return this;
	}
	
	public COperator<T> selectionArgs(String[] args) {
		
		this.args = args;
		return this;
	}
	
	public COperator<T> sortOrder(String sortOrder) {
		
		this.sortOrder = sortOrder;
		return this;
	}
	
	public COperator<T> contentValues(ContentValues values) {
		
		this.values = values;
		return this;
	}
	
	public COperator<T> objectFunction(@NonNull Function<Cursor, ? extends T> function) {
		
		this.function = function;
		return this;
	}
	
	public OCreator<T> performQuery(String tableName) {
		
		Cursor cursor = database.query(tableName, projection, selection, args, null, null, sortOrder);
		
		return new Operator<>(cursor, function);
	}
	
	/**
	 * Verilen tablodan kayıt silinir.
	 * {@linkplain #selection} ve {@linkplain #args} değerlerini kullanır.
	 *
	 * @param tableName tableName
	 * @return Başarı durumu
	 */
	public boolean performDelete(String tableName) {
		
		return database.delete(tableName, selection, args) > 0;
	}
	
	public boolean performAdd(String tableName) {
		
		return database.insert(tableName, null, values) != -1;
	}
	
	public boolean performUpdate(String tableName) {
		
		return database.update(tableName, values, selection, args) > 0;
	}
	
	
}
