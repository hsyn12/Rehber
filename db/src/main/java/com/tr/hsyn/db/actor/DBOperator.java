package com.tr.hsyn.db.actor;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.tr.hsyn.db.cast.DB;
import com.tr.hsyn.db.cast.Database;

import java.util.List;
import java.util.function.Function;


/**
 * Veri tabanını ve üzerinde yapılacak işlemleri tanımlar.<br>
 * En temel sınıf durumdadır ve veri tabanını oluşturmak ve en temel 4 işlemi
 * sunmakla yetinir. Bu işlemler, sorgulama, ekleme, silme, güncelleme.
 *
 * @param <T> Veri tabanına yazılacak nesne türü
 * @author hsyn 02 Nisan 2021 Cuma 12:21:54
 */
public abstract class DBOperator<T> extends SQLiteOpenHelper implements Database<T> {
	
	protected final DB databaseInterface;
	
	public DBOperator(@Nullable Context context, @NonNull DB dbInterface) {
		
		super(context, dbInterface.getDatabaseName(), null, dbInterface.getVersion());
		this.databaseInterface = dbInterface;
	}
	
	@Override
	public void onCreate(SQLiteDatabase sqLiteDatabase) {
		
		sqLiteDatabase.execSQL(databaseInterface.getCreateTableQuery());
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
		
	}
	
	@Override
	@NonNull
	public List<T> query(@NonNull Function<Cursor, ? extends T> function, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
		
		return COperator.<T>on(getReadableDatabase())
				.selection(selection)
				.selectionArgs(selectionArgs)
				.sortOrder(sortOrder)
				.objectFunction(function)
				.performQuery(databaseInterface.getTableName())
				.execute();
	}
	
	@Override
	public boolean add(ContentValues values) {
		
		return SimpleDBOperator.add(getWritableDatabase(), databaseInterface.getTableName(), values);
	}
	
	@Override
	public boolean update(@NonNull ContentValues values, @NonNull String selection, String[] selectionArgs) {
		
		return SimpleDBOperator.update(getWritableDatabase(), databaseInterface.getTableName(), selection, selectionArgs, values);
	}
	
	@Override
	public int delete(@NonNull String selection, @Nullable String[] selectionArgs) {
		
		return SimpleDBOperator.delete(getWritableDatabase(), databaseInterface.getTableName(), selection, selectionArgs);
	}
}
